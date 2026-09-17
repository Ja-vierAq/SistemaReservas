package reservas.logic;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TraslapeTest extends ServiceTestSupport {

    @Test
    void rechazaReservaQueSeTraslapaCuandoSoloHayUnRecurso() throws Exception {
        Categoria sala = crearCategoria("CAT-0000001", "Sala de reuniones");
        crearRecurso("REC-001", sala, "Sala 1");
        LocalDate fecha = LocalDate.of(2099, 1, 10);

        Reserva primera = nuevaReserva(null, "Reunion A", fecha,
                LocalTime.of(8, 0), LocalTime.of(10, 0), sala);
        service.create(primera);

        Reserva traslapada = nuevaReserva(null, "Reunion B", fecha,
                LocalTime.of(9, 0), LocalTime.of(11, 0), sala);

        Exception ex = assertThrows(Exception.class, () -> service.create(traslapada));

        assertAll(
                () -> assertTrue(ex.getMessage().contains("No hay disponibilidad")),
                () -> assertEquals(1, service.findAllReservas().size()),
                () -> assertEquals("REC-001", primera.getRecursos().get(0).getId())
        );
    }

    @Test
    void permiteReservasContiguasPorqueNoHayTraslape() throws Exception {
        Categoria sala = crearCategoria("CAT-0000001", "Sala de reuniones");
        crearRecurso("REC-001", sala, "Sala 1");
        LocalDate fecha = LocalDate.of(2099, 1, 10);

        Reserva primera = nuevaReserva(null, "Reunion A", fecha,
                LocalTime.of(8, 0), LocalTime.of(10, 0), sala);
        Reserva segunda = nuevaReserva(null, "Reunion B", fecha,
                LocalTime.of(10, 0), LocalTime.of(11, 0), sala);

        service.create(primera);
        assertDoesNotThrow(() -> service.create(segunda));

        assertAll(
                () -> assertEquals(2, service.findAllReservas().size()),
                () -> assertEquals("REC-001", segunda.getRecursos().get(0).getId())
        );
    }
}
