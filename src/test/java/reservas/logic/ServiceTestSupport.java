package reservas.logic;

import org.junit.jupiter.api.BeforeEach;
import reservas.data.Data;

import java.time.LocalDate;
import java.time.LocalTime;

abstract class ServiceTestSupport {

    protected Service service;
    protected Funcionario funcionario;

    @BeforeEach
    void prepararServicio() throws Exception {
        service = new Service(new Data());
        funcionario = new Funcionario("FUN-001", "Ana Perez", "88888888", null);
        service.create(funcionario);
    }

    protected Categoria crearCategoria(String id, String descripcion) throws Exception {
        Categoria categoria = new Categoria(id, descripcion);
        service.create(categoria);
        return categoria;
    }

    protected Recurso crearRecurso(String id, Categoria categoria, String descripcion) throws Exception {
        Recurso recurso = new Recurso(id, categoria, descripcion);
        service.create(recurso);
        return recurso;
    }

    protected Reserva nuevaReserva(String id, String actividad, LocalDate fecha,
                                   LocalTime inicio, LocalTime fin,
                                   Categoria... categorias) {
        Reserva reserva = new Reserva(id, funcionario, actividad, fecha, inicio, fin);
        for (Categoria categoria : categorias) {
            reserva.getCategorias().add(categoria);
        }
        return reserva;
    }
}
