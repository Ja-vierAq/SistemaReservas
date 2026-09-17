package reservas.logic;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class CancelacionTest extends ServiceTestSupport {

    @Test
    void cancelarReservaFuturaLiberaLosRecursos() throws Exception {
        Categoria sala = crearCategoria("CAT-0000001", "Sala");
        crearRecurso("REC-001", sala, "Sala 1");
        LocalDate fecha = LocalDate.now().plusDays(10);

        Reserva original = nuevaReserva(null, "Reunion original", fecha,
                LocalTime.of(8, 0), LocalTime.of(10, 0), sala);
        service.create(original);
        String recursoAsignado = original.getRecursos().get(0).getId();

        service.cancel(original);

        Reserva reemplazo = nuevaReserva(null, "Reunion reemplazo", fecha,
                LocalTime.of(8, 0), LocalTime.of(10, 0), sala);
        assertDoesNotThrow(() -> service.create(reemplazo));

        assertAll(
                () -> assertEquals("CANCELADA", original.getEstado()),
                () -> assertTrue(original.getRecursos().isEmpty()),
                () -> assertEquals(recursoAsignado, reemplazo.getRecursos().get(0).getId())
        );
    }

    @Test
    void noPermiteCancelarDosVecesLaMismaReserva() throws Exception {
        Categoria sala = crearCategoria("CAT-0000001", "Sala");
        crearRecurso("REC-001", sala, "Sala 1");

        Reserva reserva = nuevaReserva(null, "Reunion", LocalDate.now().plusDays(10),
                LocalTime.of(8, 0), LocalTime.of(9, 0), sala);
        service.create(reserva);
        service.cancel(reserva);

        Exception ex = assertThrows(Exception.class, () -> service.cancel(reserva));

        assertTrue(ex.getMessage().toLowerCase().contains("cancelada"));
    }
}
