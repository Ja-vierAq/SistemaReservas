package reservas.logic;

import reservas.data.Data;
import reservas.data.XmlPersister;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Service {

    private static Service theInstance;
    private Data data;

    // Contructor e instanciador
    public static Service instance() {
        if (theInstance == null) {
            theInstance = new Service();
        }
        return theInstance;
    }
    private Service() {
        try {
            data = XmlPersister.instance().load();
        } catch (Exception e) {
            data = new Data();
        }
    }
    // Stop para cuando termine
    public void stop() {
        try {
            XmlPersister.instance().store(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //=========================================================
    //USUARIOS / LOGIN / CAMBIO DE CLAVE
    //==========================================================

    public Usuario login(String id, String clave) throws Exception {
        Usuario buscado = new Usuario();
        buscado.setId(id);
        Usuario usuario = read(buscado);
        if (!usuario.getClave().equals(clave)) {
            throw new Exception("usuario o clave incorrectos");
        }
        return usuario;
    }

    public Usuario read(Usuario e) throws Exception {
        validarTexto(e == null ? null : e.getId(), "Id de usuario");
        Usuario result = data.getUsuarios().stream().filter(i -> i.getId().equals(e.getId())).findFirst().orElse(null);
        if (result != null) {
            return result;
        }
        throw new Exception("Usuario no existe");
    }

    public void cambiarClave(Usuario e, String claveActual, String claveNueva) throws Exception {
        Usuario usuario = read(e);
        validarTexto(claveActual, "Clave actual");
        validarTexto(claveNueva, "Clave nueva");
        if (!usuario.getClave().equals(claveActual)) {
            throw new Exception("La clave actual es incorrecta");
        }
        usuario.setClave(claveNueva);
    }

    // =========================================================
    // FUNCIONARIOS
    // =========================================================

    public void create(Funcionario e) throws Exception {
        validarFuncionario(e);
        if (existeFuncionario(e.getId())) {
            throw new Exception("Funcionario ya existe");
        }
        if (existeUsuario(e.getId())) {
            throw new Exception("Ya existe un usuario con ese id");
        }
        Usuario usuario = new Usuario(e.getId(), e.getId(), "FUNCIONARIO");
        e.setUsuario(usuario);
        data.getUsuarios().add(usuario);
        data.getFuncionarios().add(e);
    }

    public Funcionario read(Funcionario e) throws Exception {
        validarTexto(e == null ? null : e.getId(), "Id de funcionario");
        Funcionario result = data.getFuncionarios().stream().filter(i -> i.getId().equals(e.getId())).findFirst().orElse(null);
        if (result != null) {
            return result;
        }
        throw new Exception("Funcionario no existe");
    }

    public void update(Funcionario e) throws Exception {
        validarFuncionario(e);
        Funcionario actual = read(e);
        actual.setNombre(e.getNombre());
        actual.setTelefono(e.getTelefono());
    }

    public void delete(Funcionario e) throws Exception {
        Funcionario actual = read(e);
        boolean tieneReservas = data.getReservas().stream()
                .anyMatch(r -> mismoFuncionario(r.getFuncionario(), actual));
        if (tieneReservas) {
            throw new Exception("No se puede borrar el funcionario porque tiene reservas asociadas");
        }
        data.getFuncionarios().remove(actual);
        if (actual.getUsuario() != null) {
            data.getUsuarios().remove(actual.getUsuario());
        }
    }

    public List<Funcionario> search(Funcionario filtro) {
        String id = textoFiltro(filtro == null ? null : filtro.getId());
        String nombre = textoFiltro(filtro == null ? null : filtro.getNombre());
        return data.getFuncionarios().stream().filter(i -> id.isEmpty() || i.getId().toLowerCase().contains(id)).filter(i -> nombre.isEmpty() || i.getNombre().toLowerCase().contains(nombre)).sorted(Comparator.comparing(Funcionario::getId)).collect(Collectors.toList());
    }

    public List<Funcionario> findAllFuncionarios() {
        return search(new Funcionario());
    }

    // =========================================================
    // CATEGORIAS
    // =========================================================

    public void create(Categoria e) throws Exception {
        validarCategoria(e, false);
        if (e.getId() == null || e.getId().trim().isEmpty()) {
            e.setId(siguienteIdCategoria());
        }
        if (existeCategoria(e.getId())) {
            throw new Exception("Categoría ya existe");
        }
        data.getCategorias().add(e);
    }

    public Categoria read(Categoria e) throws Exception {
        validarTexto(e == null ? null : e.getId(), "Id de categoría");
        Categoria result = data.getCategorias().stream().filter(i -> i.getId().equals(e.getId())).findFirst().orElse(null);
        if (result != null) {
            return result;
        }
        throw new Exception("Categoría no existe");
    }

    public void update(Categoria e) throws Exception {
        validarCategoria(e, true);
        Categoria actual = read(e);
        actual.setDescripcion(e.getDescripcion());
    }

    public void delete(Categoria e) throws Exception {
        Categoria actual = read(e);

        boolean tieneRecursos = data.getRecursos().stream().anyMatch(r -> mismaCategoria(r.getCategoria(), actual));
        if (tieneRecursos) {
            throw new Exception("No se puede borrar la categoría porque tiene recursos asociados");
        }

        boolean estaEnReservas = data.getReservas().stream().anyMatch(r -> contieneCategoria(r.getCategorias(), actual));
        if (estaEnReservas) {
            throw new Exception("No se puede borrar la categoría porque aparece en reservas");
        }

        data.getCategorias().remove(actual);
    }

    public List<Categoria> search(Categoria filtro) {
        String descripcion = textoFiltro(filtro == null ? null : filtro.getDescripcion());
        return data.getCategorias().stream().filter(i -> descripcion.isEmpty() || i.getDescripcion().toLowerCase().contains(descripcion)).sorted(Comparator.comparing(Categoria::getDescripcion)).collect(Collectors.toList());
    }

    public List<Categoria> findAllCategorias() {
        return search(new Categoria());
    }

    // =========================================================
    // RECURSOS
    // =========================================================

    public void create(Recurso e) throws Exception {
        validarRecurso(e);
        if (existeRecurso(e.getId())) {
            throw new Exception("Recurso ya existe");
        }
        e.setCategoria(read(e.getCategoria()));
        data.getRecursos().add(e);
    }

    public Recurso read(Recurso e) throws Exception {
        validarTexto(e == null ? null : e.getId(), "Id de recurso");
        Recurso result = data.getRecursos().stream().filter(i -> i.getId().equals(e.getId())).findFirst().orElse(null);
        if (result != null) {
            return result;
        }
        throw new Exception("Recurso no existe");
    }

    public void update(Recurso e) throws Exception {
        validarRecurso(e);
        Recurso actual = read(e);
        actual.setCategoria(read(e.getCategoria()));
        actual.setDescripcion(e.getDescripcion());
    }

    public void delete(Recurso e) throws Exception {
        Recurso actual = read(e);
        boolean estaEnReservas = data.getReservas().stream()
                .anyMatch(r -> contieneRecurso(r.getRecursos(), actual));
        if (estaEnReservas) {
            throw new Exception("No se puede borrar el recurso porque aparece en reservas");
        }
        data.getRecursos().remove(actual);
    }

    public List<Recurso> search(Recurso filtro) {
        String id = textoFiltro(filtro == null ? null : filtro.getId());
        String descripcion = textoFiltro(filtro == null ? null : filtro.getDescripcion());
        String categoriaId = filtro != null && filtro.getCategoria() != null ? textoFiltro(filtro.getCategoria().getId()) : "";

        return data.getRecursos().stream()
                .filter(i -> id.isEmpty() || i.getId().toLowerCase().contains(id))
                .filter(i -> descripcion.isEmpty() || i.getDescripcion().toLowerCase().contains(descripcion))
                .filter(i -> categoriaId.isEmpty() ||
                        (i.getCategoria() != null && i.getCategoria().getId().toLowerCase().equals(categoriaId)))
                .sorted(Comparator.comparing(Recurso::getId))
                .collect(Collectors.toList());
    }

    public List<Recurso> findAllRecursos() {
        return search(new Recurso());
    }

    // =========================================================
    // RESERVAS
    // =========================================================

    public void create(Reserva e) throws Exception {
        validarReserva(e);
        if (e.getId() != null && !e.getId().trim().isEmpty() && existeReserva(e.getId())) {
            throw new Exception("Reserva ya existe");
        }
        Funcionario funcionario = read(e.getFuncionario());
        List<Categoria> categorias = categoriasCanonicas(e.getCategorias());
        List<Recurso> asignados = new ArrayList<>();
        List<String> noDisponibles = new ArrayList<>();
        for (Categoria categoria : categorias) {
            Recurso disponible = primerRecursoDisponible(categoria, e.getFecha(), e.getHoraInicio(), e.getHoraFin());

            if (disponible == null) {
                noDisponibles.add(categoria.getDescripcion());
            } else {
                asignados.add(disponible);
            }
        }

        if (!noDisponibles.isEmpty()) {
            throw new Exception("No hay disponibilidad para: " + String.join(", ", noDisponibles));
        }

        if (e.getId() == null || e.getId().trim().isEmpty()) {
            e.setId(siguienteIdReserva());
        }
        e.setFuncionario(funcionario);
        e.setCategorias(categorias);
        e.setRecursos(asignados);
        e.setEstado("ACTIVA");
        data.getReservas().add(e);
    }

    public Reserva read(Reserva e) throws Exception {
        validarTexto(e == null ? null : e.getId(), "Id de reserva");
        Reserva result = data.getReservas().stream().filter(i -> i.getId().equals(e.getId())).findFirst().orElse(null);
        if (result != null) {
            return result;
        }
        throw new Exception("Reserva no existe");
    }

    public void cancel(Reserva e) throws Exception {
        Reserva actual = read(e);
        if (!esActiva(actual)) {
            throw new Exception("La reserva ya está cancelada");
        }
        if (!esFutura(actual)) {
            throw new Exception("Solo se pueden cancelar reservas futuras");
        }
        actual.setEstado("CANCELADA");
        actual.getRecursos().clear();
    }

    public List<Reserva> findReservas(Funcionario funcionario) throws Exception {
        Funcionario actual = read(funcionario);
        return data.getReservas().stream()
                .filter(r -> mismoFuncionario(r.getFuncionario(), actual))
                .sorted(Comparator.comparing(Reserva::getFecha)
                        .thenComparing(Reserva::getHoraInicio))
                .collect(Collectors.toList());
    }

    public List<Reserva> findAllReservas() {
        return data.getReservas().stream()
                .sorted(Comparator.comparing(Reserva::getFecha)
                        .thenComparing(Reserva::getHoraInicio))
                .collect(Collectors.toList());
    }

    // =========================================================
    // CALENDARIZACION DE RECURSOS
    // =========================================================

    public List<Reserva> calendarizacionRecursos(LocalDate fecha, Categoria categoria) throws Exception {
        if (fecha == null) {
            throw new Exception("Fecha requerida");
        }
        Categoria actual = read(categoria);
        return data.getReservas().stream()
                .filter(this::esActiva)
                .filter(r -> fecha.equals(r.getFecha()))
                .filter(r -> r.getRecursos().stream()
                        .anyMatch(rec -> mismaCategoria(rec.getCategoria(), actual)))
                .sorted(Comparator.comparing(Reserva::getHoraInicio))
                .collect(Collectors.toList());
    }

    public Reserva reservaEn(Recurso recurso, LocalDate fecha, LocalTime hora) throws Exception {
        Recurso actual = read(recurso);
        if (fecha == null || hora == null) {
            throw new Exception("Fecha y hora requeridas");
        }
        LocalTime horaFinBloque = hora.plusHours(1);
        return data.getReservas().stream()
                .filter(this::esActiva)
                .filter(r -> fecha.equals(r.getFecha()))
                .filter(r -> contieneRecurso(r.getRecursos(), actual))
                .filter(r -> seTraslapan(hora, horaFinBloque, r.getHoraInicio(), r.getHoraFin()))
                .findFirst()
                .orElse(null);
    }

    // =========================================================
    // PROGRAMACION DE ACTIVIDADES
    // =========================================================

    public LocalDate inicioSemana(LocalDate fechaReferencia) throws Exception {
        if (fechaReferencia == null) {
            throw new Exception("Fecha requerida");
        }
        return fechaReferencia.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public List<Reserva> actividadesSemana(LocalDate fechaReferencia) throws Exception {
        LocalDate desde = inicioSemana(fechaReferencia);
        LocalDate hasta = desde.plusDays(6);
        return reservasActivasEnPeriodo(desde, hasta);
    }

    public List<Reserva> actividadesEn(LocalDate fecha, LocalTime hora) throws Exception {
        if (fecha == null || hora == null) {
            throw new Exception("Fecha y hora requeridas");
        }
        LocalTime horaFinBloque = hora.plusHours(1);
        return data.getReservas().stream()
                .filter(this::esActiva)
                .filter(r -> fecha.equals(r.getFecha()))
                .filter(r -> seTraslapan(hora, horaFinBloque, r.getHoraInicio(), r.getHoraFin()))
                .sorted(Comparator.comparing(Reserva::getActividad))
                .collect(Collectors.toList());
    }

    // =========================================================
    // ESTADISTICAS
    // =========================================================

    public Map<String, Integer> estadisticasRecursos(LocalDate desde, LocalDate hasta) throws Exception {
        validarPeriodo(desde, hasta);
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Reserva reserva : reservasActivasEnPeriodo(desde, hasta)) {
            for (Recurso recurso : reserva.getRecursos()) {
                if (recurso.getCategoria() != null) {
                    String categoria = recurso.getCategoria().getDescripcion();
                    result.put(categoria, result.getOrDefault(categoria, 0) + 1);
                }
            }
        }
        return result;
    }

    public Map<LocalDate, Integer> estadisticasActividades(LocalDate desde, LocalDate hasta) throws Exception {
        validarPeriodo(desde, hasta);
        Map<LocalDate, Integer> result = new LinkedHashMap<>();
        LocalDate primeraSemana = inicioSemana(desde);
        LocalDate ultimaSemana = inicioSemana(hasta);
        for (LocalDate semana = primeraSemana; !semana.isAfter(ultimaSemana); semana = semana.plusWeeks(1)) {
            result.put(semana, 0);
        }
        for (Reserva reserva : reservasActivasEnPeriodo(desde, hasta)) {
            LocalDate semana = inicioSemana(reserva.getFecha());
            result.put(semana, result.getOrDefault(semana, 0) + 1);
        }
        return result;
    }

    // =========================================================
    // AUXILIARES PRIVADOS
    // =========================================================

    private List<Reserva> reservasActivasEnPeriodo(LocalDate desde, LocalDate hasta) throws Exception {
        validarPeriodo(desde, hasta);
        return data.getReservas().stream()
                .filter(this::esActiva)
                .filter(r -> !r.getFecha().isBefore(desde) && !r.getFecha().isAfter(hasta))
                .sorted(Comparator.comparing(Reserva::getFecha)
                        .thenComparing(Reserva::getHoraInicio))
                .collect(Collectors.toList());
    }

    private Recurso primerRecursoDisponible(Categoria categoria, LocalDate fecha,
                                            LocalTime inicio, LocalTime fin) {
        return data.getRecursos().stream()
                .filter(r -> mismaCategoria(r.getCategoria(), categoria))
                .sorted(Comparator.comparing(Recurso::getId))
                .filter(r -> recursoDisponible(r, fecha, inicio, fin))
                .findFirst()
                .orElse(null);
    }

    private boolean recursoDisponible(Recurso recurso, LocalDate fecha,
                                      LocalTime inicio, LocalTime fin) {
        return data.getReservas().stream()
                .filter(this::esActiva)
                .filter(r -> fecha.equals(r.getFecha()))
                .filter(r -> contieneRecurso(r.getRecursos(), recurso))
                .noneMatch(r -> seTraslapan(inicio, fin, r.getHoraInicio(), r.getHoraFin()));
    }

    private boolean seTraslapan(LocalTime inicio1, LocalTime fin1,
                                LocalTime inicio2, LocalTime fin2) {
        return inicio1.isBefore(fin2) && fin1.isAfter(inicio2);
    }

    private boolean esActiva(Reserva reserva) {
        return reserva != null && "ACTIVA".equalsIgnoreCase(reserva.getEstado());
    }

    private boolean esFutura(Reserva reserva) {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        return reserva.getFecha().isAfter(hoy)
                || (reserva.getFecha().equals(hoy) && reserva.getHoraInicio().isAfter(ahora));
    }

    private List<Categoria> categoriasCanonicas(List<Categoria> categorias) throws Exception {
        List<Categoria> result = new ArrayList<>();
        for (Categoria categoria : categorias) {
            Categoria actual = read(categoria);
            if (!contieneCategoria(result, actual)) {
                result.add(actual);
            }
        }
        return result;
    }

    private boolean existeUsuario(String id) {
        return data.getUsuarios().stream().anyMatch(i -> i.getId().equals(id));
    }

    private boolean existeFuncionario(String id) {
        return data.getFuncionarios().stream().anyMatch(i -> i.getId().equals(id));
    }

    private boolean existeCategoria(String id) {
        return data.getCategorias().stream().anyMatch(i -> i.getId().equals(id));
    }

    private boolean existeRecurso(String id) {
        return data.getRecursos().stream().anyMatch(i -> i.getId().equals(id));
    }

    private boolean existeReserva(String id) {
        return data.getReservas().stream().anyMatch(i -> i.getId().equals(id));
    }

    private boolean mismoFuncionario(Funcionario a, Funcionario b) {
        return a != null && b != null && a.getId() != null && a.getId().equals(b.getId());
    }

    private boolean mismaCategoria(Categoria a, Categoria b) {
        return a != null && b != null && a.getId() != null && a.getId().equals(b.getId());
    }

    private boolean mismoRecurso(Recurso a, Recurso b) {
        return a != null && b != null && a.getId() != null && a.getId().equals(b.getId());
    }

    private boolean contieneCategoria(List<Categoria> categorias, Categoria buscada) {
        return categorias != null && categorias.stream().anyMatch(c -> mismaCategoria(c, buscada));
    }

    private boolean contieneRecurso(List<Recurso> recursos, Recurso buscado) {
        return recursos != null && recursos.stream().anyMatch(r -> mismoRecurso(r, buscado));
    }

    private String siguienteIdCategoria() {
        int max = data.getCategorias().stream()
                .mapToInt(c -> numeroId(c.getId()))
                .max()
                .orElse(0);
        return String.format("CAT-%07d", max + 1);
    }

    private String siguienteIdReserva() {
        int max = data.getReservas().stream()
                .mapToInt(r -> numeroId(r.getId()))
                .max()
                .orElse(0);
        return String.format("RES-%07d", max + 1);
    }

    private int numeroId(String id) {
        if (id == null) return 0;
        String numeros = id.replaceAll("\\D", "");
        if (numeros.isEmpty()) return 0;
        try {
            return Integer.parseInt(numeros);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void validarFuncionario(Funcionario e) throws Exception {
        if (e == null) throw new Exception("Funcionario requerido");
        validarTexto(e.getId(), "Id");
        validarTexto(e.getNombre(), "Nombre");
        validarTexto(e.getTelefono(), "Teléfono");
    }

    private void validarCategoria(Categoria e, boolean requiereId) throws Exception {
        if (e == null) throw new Exception("Categoría requerida");
        if (requiereId) validarTexto(e.getId(), "Id");
        validarTexto(e.getDescripcion(), "Descripción");
    }

    private void validarRecurso(Recurso e) throws Exception {
        if (e == null) throw new Exception("Recurso requerido");
        validarTexto(e.getId(), "Id de recurso");
        validarTexto(e.getDescripcion(), "Descripción");
        if (e.getCategoria() == null) throw new Exception("Categoría requerida");
        validarTexto(e.getCategoria().getId(), "Categoría");
    }

    private void validarReserva(Reserva e) throws Exception {
        if (e == null) throw new Exception("Reserva requerida");
        if (e.getFuncionario() == null) throw new Exception("Funcionario requerido");
        validarTexto(e.getFuncionario().getId(), "Funcionario");
        validarTexto(e.getActividad(), "Actividad");
        if (e.getFecha() == null) throw new Exception("Fecha requerida");
        if (e.getHoraInicio() == null) throw new Exception("Hora de inicio requerida");
        if (e.getHoraFin() == null) throw new Exception("Hora de finalización requerida");
        if (!e.getHoraInicio().isBefore(e.getHoraFin())) {
            throw new Exception("La hora de inicio debe ser anterior a la hora de finalización");
        }
        if (e.getCategorias() == null || e.getCategorias().isEmpty()) {
            throw new Exception("Debe seleccionar al menos una categoría");
        }
    }

    private void validarPeriodo(LocalDate desde, LocalDate hasta) throws Exception {
        if (desde == null || hasta == null) {
            throw new Exception("Las fechas desde y hasta son requeridas");
        }
        if (desde.isAfter(hasta)) {
            throw new Exception("La fecha desde no puede ser posterior a la fecha hasta");
        }
    }

    private void validarTexto(String valor, String campo) throws Exception {
        if (valor == null || valor.trim().isEmpty()) {
            throw new Exception(campo + " requerido");
        }
    }

    private String textoFiltro(String valor) {
        return valor == null ? "" : valor.trim().toLowerCase();
    }
}
