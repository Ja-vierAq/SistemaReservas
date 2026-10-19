package reservas.logic;
import reservas.logic.ai.ReservaIA;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface IService {

    // usuarios
    Usuario login(String id, String clave) throws Exception;
    Usuario read(Usuario usuario) throws Exception;
    void cambiarClave(Usuario usuario, String claveActual, String claveNueva) throws Exception;

    // funcionarios
    void create(Funcionario funcionario) throws Exception;
    Funcionario read(Funcionario funcionario) throws Exception;
    void update(Funcionario funcionario) throws Exception;
    void delete(Funcionario funcionario) throws Exception;
    List<Funcionario> search(Funcionario filtro);
    List<Funcionario> findAllFuncionarios();

    // categorias
    void create(Categoria categoria) throws Exception;
    Categoria read(Categoria categoria) throws Exception;
    void update(Categoria categoria) throws Exception;
    void delete(Categoria categoria) throws Exception;
    List<Categoria> search(Categoria filtro);
    List<Categoria> findAllCategorias();

    // recursos
    void create(Recurso recurso) throws Exception;
    Recurso read(Recurso recurso) throws Exception;
    void update(Recurso recurso) throws Exception;
    void delete(Recurso recurso) throws Exception;
    List<Recurso> search(Recurso filtro);
    List<Recurso> findAllRecursos();

    // reservas
    void create(Reserva reserva) throws Exception;
    Reserva read(Reserva reserva) throws Exception;
    void cancel(Reserva reserva) throws Exception;
    List<Reserva> findReservas(Funcionario funcionario) throws Exception;
    List<Reserva> findAllReservas();

    // calendarios y actividades
    List<Reserva> calendarizacionRecursos(LocalDate fecha, Categoria categoria) throws Exception;
    Reserva reservaEn(Recurso recurso, LocalDate fecha, LocalTime hora) throws Exception;
    LocalDate inicioSemana(LocalDate fecha);
    List<Reserva> actividadesSemana(LocalDate fecha) throws Exception;
    List<Reserva> actividadesEn(LocalDate fecha, LocalTime hora) throws Exception;

    // stats
    Map<String, Integer> estadisticasRecursos(LocalDate desde, LocalDate hasta) throws Exception;
    Map<LocalDate, Integer> estadisticasActividades(LocalDate desde, LocalDate hasta) throws Exception;

    // ia
    ReservaIA extractIA(String frase) throws Exception;
    Categoria findCategoriaByDescripcion(String descripcion);
}
