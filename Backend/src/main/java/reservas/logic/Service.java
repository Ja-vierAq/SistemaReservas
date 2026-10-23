package reservas.logic;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import reservas.data.*;
import reservas.logic.ai.ReservaExtraccion;
import reservas.logic.ai.ReservaExtractorService;
import reservas.logic.ai.ReservaIA;

import java.sql.Connection;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Service implements IService {

    private static Service theInstance;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private final UsuarioDao usuarioDao;
    private final FuncionarioDao funcionarioDao;
    private final CategoriaDao categoriaDao;
    private final RecursoDao recursoDao;
    private final ReservaDao reservaDao;

    private Service() {
        usuarioDao = new UsuarioDao();
        funcionarioDao = new FuncionarioDao();
        categoriaDao = new CategoriaDao();
        recursoDao = new RecursoDao();
        reservaDao = new ReservaDao();
    }

    // USUARIOS

    public Usuario login(String id, String clave) throws Exception {
        Usuario usuario = usuarioDao.read(id);
        if (!usuario.getClave().equals(clave)) throw new Exception("Usuario o clave incorrectos");
        return usuario;
    }

    public Usuario read(Usuario e) throws Exception {
        return usuarioDao.read(e.getId());
    }

    public void cambiarClave(Usuario e, String claveActual, String claveNueva) throws Exception {
        Usuario usuario = usuarioDao.read(e.getId());
        if (!usuario.getClave().equals(claveActual)) throw new Exception("La clave actual es incorrecta");
        if (claveNueva == null || claveNueva.isBlank()) throw new Exception("La clave nueva es requerida");
        usuarioDao.cambiarClave(e.getId(), claveNueva);
        e.setClave(claveNueva);
    }

    // FUNCIONARIOS

    public void create(Funcionario e) throws Exception { funcionarioDao.createConUsuario(e); }
    public Funcionario read(Funcionario e) throws Exception { return funcionarioDao.read(e.getId()); }
    public void update(Funcionario e) throws Exception { funcionarioDao.update(e); }
    public void delete(Funcionario e) throws Exception { funcionarioDao.deleteConUsuario(e.getId()); }

    public List<Funcionario> search(Funcionario e) {
        try {
            return funcionarioDao.search(e.getId(), e.getNombre());
        } catch (Exception ex) {
            throw new IllegalStateException("Error buscando funcionarios", ex);
        }
    }

    public List<Funcionario> findAllFuncionarios() { return search(new Funcionario()); }

    // CATEGORIAS

    public void create(Categoria e) throws Exception { categoriaDao.create(e); }
    public Categoria read(Categoria e) throws Exception { return categoriaDao.read(e.getId()); }
    public void update(Categoria e) throws Exception { categoriaDao.update(e); }
    public void delete(Categoria e) throws Exception { categoriaDao.delete(e.getId()); }

    public List<Categoria> search(Categoria e) {
        try {
            return categoriaDao.search(e.getDescripcion());
        } catch (Exception ex) {
            throw new IllegalStateException("Error buscando categorías", ex);
        }
    }

    public List<Categoria> findAllCategorias() { return search(new Categoria()); }

    // RECURSOS

    public void create(Recurso e) throws Exception { recursoDao.create(e); }
    public Recurso read(Recurso e) throws Exception { return recursoDao.read(e.getId()); }
    public void update(Recurso e) throws Exception { recursoDao.update(e); }
    public void delete(Recurso e) throws Exception { recursoDao.delete(e.getId()); }

    public List<Recurso> search(Recurso e) {
        try {
            String categoriaId = e.getCategoria() == null ? "" : e.getCategoria().getId();
            return recursoDao.search(e.getId(), e.getDescripcion(), categoriaId);
        } catch (Exception ex) {
            throw new IllegalStateException("Error buscando recursos", ex);
        }
    }

    public List<Recurso> findAllRecursos() { return search(new Recurso()); }

    // RESERVAS

    public void create(Reserva e) throws Exception {
        validarReserva(e);
        try (Connection cnx = Database.instance().getConnection()) {
            cnx.setAutoCommit(false);
            try {
                List<Categoria> categorias = e.getCategorias().stream()
                        .sorted(Comparator.comparing(Categoria::getId)).toList();
                Set<String> ids = new HashSet<>();
                List<Recurso> asignados = new ArrayList<>();
                List<Categoria> seleccionadas = new ArrayList<>();
                List<String> faltantes = new ArrayList<>();

                for (Categoria categoria : categorias) {
                    if (!ids.add(categoria.getId())) continue;
                    seleccionadas.add(categoria);
                    Recurso recurso = reservaDao.seleccionarRecursoDisponible(
                            categoria.getId(), e.getFecha(), e.getHoraInicio(), e.getHoraFin(), cnx);
                    if (recurso == null) faltantes.add(categoria.getDescripcion());
                    else asignados.add(recurso);
                }
                if (!faltantes.isEmpty()) {
                    throw new Exception("No hay disponibilidad para: " + String.join(", ", faltantes));
                }

                // Asignar los recursos solamente después de comprobar todas las categorías.
                e.setCategorias(seleccionadas);
                e.setRecursos(asignados);
                e.setEstado("ACTIVA");
                reservaDao.create(e, cnx);
                cnx.commit();
            } catch (Exception ex) {
                cnx.rollback();
                throw ex;
            }
        }
    }

    public Reserva read(Reserva e) throws Exception { return reservaDao.read(e.getId()); }

    public void cancel(Reserva e) throws Exception {
        Reserva actual = reservaDao.read(e.getId());
        if (!"ACTIVA".equals(actual.getEstado())) throw new Exception("La reserva ya está cancelada");
        if (!actual.getFecha().isAfter(LocalDate.now()) &&
                !(actual.getFecha().isEqual(LocalDate.now()) &&
                  actual.getHoraInicio().isAfter(LocalTime.now()))) {
            throw new Exception("Solo se pueden cancelar reservas futuras");
        }
        reservaDao.cancel(e.getId());
        e.setEstado("CANCELADA");
    }

    public List<Reserva> findReservas(Funcionario funcionario) throws Exception {
        return reservaDao.findByFuncionario(funcionario.getId());
    }

    public List<Reserva> findAllReservas() {
        try {
            return reservaDao.findAll();
        } catch (Exception ex) {
            throw new IllegalStateException("Error consultando reservas", ex);
        }
    }

    // CALENDARIZACION Y ACTIVIDADES

    public List<Reserva> calendarizacionRecursos(LocalDate fecha, Categoria categoria) throws Exception {
        return reservaDao.findActivasEnPeriodo(fecha, fecha).stream()
                .filter(r -> r.getRecursos().stream()
                        .anyMatch(rec -> rec.getCategoria().getId().equals(categoria.getId())))
                .toList();
    }

    public Reserva reservaEn(Recurso recurso, LocalDate fecha, LocalTime hora) throws Exception {
        return reservaDao.findActivasEnPeriodo(fecha, fecha).stream()
                .filter(r -> r.getRecursos().stream().anyMatch(rec -> rec.getId().equals(recurso.getId())))
                .filter(r -> seTraslapan(hora, hora.plusHours(1), r.getHoraInicio(), r.getHoraFin()))
                .findFirst().orElse(null);
    }

    public LocalDate inicioSemana(LocalDate fecha) {
        return fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public List<Reserva> actividadesSemana(LocalDate fecha) throws Exception {
        LocalDate lunes = inicioSemana(fecha);
        return reservaDao.findActivasEnPeriodo(lunes, lunes.plusDays(6));
    }

    public List<Reserva> actividadesEn(LocalDate fecha, LocalTime hora) throws Exception {
        return reservaDao.findActivasEnPeriodo(fecha, fecha).stream()
                .filter(r -> seTraslapan(hora, hora.plusHours(1), r.getHoraInicio(), r.getHoraFin()))
                .sorted(Comparator.comparing(Reserva::getActividad)).toList();
    }

    static boolean seTraslapan(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return inicio1.isBefore(fin2) && fin1.isAfter(inicio2);
    }

    // ESTADISTICAS

    public Map<String, Integer> estadisticasRecursos(LocalDate desde, LocalDate hasta) throws Exception {
        validarPeriodo(desde, hasta);
        Map<String, Integer> resultado = new LinkedHashMap<>();
        for (Reserva reserva : reservaDao.findActivasEnPeriodo(desde, hasta)) {
            for (Recurso recurso : reserva.getRecursos()) {
                String categoria = recurso.getCategoria().getDescripcion();
                resultado.merge(categoria, 1, Integer::sum);
            }
        }
        return resultado;
    }

    public Map<LocalDate, Integer> estadisticasActividades(LocalDate desde, LocalDate hasta) throws Exception {
        validarPeriodo(desde, hasta);
        Map<LocalDate, Integer> resultado = new LinkedHashMap<>();
        for (LocalDate semana = inicioSemana(desde); !semana.isAfter(hasta); semana = semana.plusWeeks(1)) {
            resultado.put(semana, 0);
        }
        for (Reserva reserva : reservaDao.findActivasEnPeriodo(desde, hasta)) {
            LocalDate semana = inicioSemana(reserva.getFecha());
            resultado.merge(semana, 1, Integer::sum);
        }
        return resultado;
    }

    private void validarPeriodo(LocalDate desde, LocalDate hasta) throws Exception {
        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            throw new Exception("Periodo de fechas inválido");
        }
    }

    private void validarReserva(Reserva e) throws Exception {
        if (e == null || e.getFuncionario() == null || e.getFuncionario().getId() == null ||
                e.getActividad() == null || e.getActividad().isBlank() ||
                e.getFecha() == null || e.getHoraInicio() == null || e.getHoraFin() == null ||
                !e.getHoraInicio().isBefore(e.getHoraFin()) ||
                e.getCategorias() == null || e.getCategorias().isEmpty()) {
            throw new Exception("Datos de reserva incompletos o incorrectos");
        }
    }

    // EXTRACCION CON IA

    public ReservaIA extractIA(String frase) throws Exception {
        if (frase == null || frase.isBlank()) throw new Exception("Debe escribir una descripción de la reserva");

        OpenAiChatModel modelo = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo").modelName("gpt-4o-mini").build();
        ReservaExtractorService extractor = AiServices.create(ReservaExtractorService.class, modelo);
        String categorias = findAllCategorias().stream()
                .map(Categoria::getDescripcion).collect(Collectors.joining("\n"));
        ReservaExtraccion respuesta = extractor.extraer(frase, categorias, LocalDate.now().toString());

        return new ReservaIA(
                respuesta.getActividad(),
                respuesta.getFecha() == null ? null : LocalDate.parse(respuesta.getFecha()),
                respuesta.getHoraInicio() == null ? null : LocalTime.parse(respuesta.getHoraInicio()),
                respuesta.getHoraFinal() == null ? null : LocalTime.parse(respuesta.getHoraFinal()),
                respuesta.getCategoriasRecurso() == null ? new ArrayList<>() : respuesta.getCategoriasRecurso());
    }

    public Categoria findCategoriaByDescripcion(String descripcion) {
        return findAllCategorias().stream()
                .filter(c -> c.getDescripcion().equalsIgnoreCase(descripcion))
                .findFirst().orElse(null);
    }
}
