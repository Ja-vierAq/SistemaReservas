package reservas.logic;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class DisponibilidadTest extends ServiceTestSupport {

    @Test
    void usaOtroRecursoCuandoElPrimeroEstaOcupado() throws Exception {
        Categoria sala = crearCategoria("CAT-0000001", "Sala de reuniones");
        crearRecurso("REC-001", sala, "Sala 1");
        crearRecurso("REC-002", sala, "Sala 2");
        LocalDate fecha = LocalDate.of(2099, 2, 1);

        Reserva primera = nuevaReserva(null, "Actividad 1", fecha,
                LocalTime.of(8, 0), LocalTime.of(10, 0), sala);
        service.create(primera);

        Reserva segunda = nuevaReserva(null, "Actividad 2", fecha,
                LocalTime.of(9, 0), LocalTime.of(11, 0), sala);
        service.create(segunda);

        assertAll(
                () -> assertEquals("REC-001", primera.getRecursos().get(0).getId()),
                () -> assertEquals("REC-002", segunda.getRecursos().get(0).getId()),
                () -> assertEquals(2, service.findAllReservas().size())
        );
    }

    @Test
    void informaFaltaDeDisponibilidadCuandoTodosEstanOcupados() throws Exception {
        Categoria sala = crearCategoria("CAT-0000001", "Sala de reuniones");
        crearRecurso("REC-001", sala, "Sala 1");
        crearRecurso("REC-002", sala, "Sala 2");
        LocalDate fecha = LocalDate.of(2099, 2, 1);

        service.create(nuevaReserva(null, "Actividad 1", fecha,
                LocalTime.of(8, 0), LocalTime.of(10, 0), sala));
        service.create(nuevaReserva(null, "Actividad 2", fecha,
                LocalTime.of(9, 0), LocalTime.of(11, 0), sala));

        Reserva tercera = nuevaReserva(null, "Actividad 3", fecha,
                LocalTime.of(9, 30), LocalTime.of(10, 30), sala);

        Exception ex = assertThrows(Exception.class, () -> service.create(tercera));

        assertAll(
                () -> assertTrue(ex.getMessage().contains("Sala de reuniones")),
                () -> assertEquals(2, service.findAllReservas().size())
        );
    }
}
