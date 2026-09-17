package reservas.logic;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EstadisticasTest extends ServiceTestSupport {

    @Test
    void cuentaRecursosReservadosPorCategoriaYExcluyeCanceladas() throws Exception {
        EscenarioEstadisticas e = crearEscenario();

        Map<String, Integer> resultado = service.estadisticasRecursos(e.lunes, e.lunes.plusDays(7));

        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals(Integer.valueOf(2), resultado.get("Sala")),
                () -> assertEquals(Integer.valueOf(2), resultado.get("Laptop")),
                () -> assertFalse(resultado.containsValue(3))
        );
    }

    @Test
    void agrupaActividadesPorSemanaYExcluyeCanceladas() throws Exception {
        EscenarioEstadisticas e = crearEscenario();

        Map<LocalDate, Integer> resultado = service.estadisticasActividades(e.lunes, e.lunes.plusDays(7));

        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals(Integer.valueOf(2), resultado.get(e.lunes)),
                () -> assertEquals(Integer.valueOf(1), resultado.get(e.lunes.plusWeeks(1)))
        );
    }

    private EscenarioEstadisticas crearEscenario() throws Exception {
        Categoria sala = crearCategoria("CAT-0000001", "Sala");
        Categoria laptop = crearCategoria("CAT-0000002", "Laptop");
        crearRecurso("REC-SALA", sala, "Sala 1");
        crearRecurso("REC-LAP", laptop, "Laptop 1");

        LocalDate lunes = LocalDate.now()
                .plusWeeks(2)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        service.create(nuevaReserva(null, "Actividad 1", lunes,
                LocalTime.of(8, 0), LocalTime.of(9, 0), sala, laptop));
        service.create(nuevaReserva(null, "Actividad 2", lunes.plusDays(2),
                LocalTime.of(10, 0), LocalTime.of(11, 0), laptop));
        service.create(nuevaReserva(null, "Actividad 3", lunes.plusWeeks(1),
                LocalTime.of(8, 0), LocalTime.of(9, 0), sala));

        Reserva cancelada = nuevaReserva(null, "Actividad cancelada", lunes.plusDays(1),
                LocalTime.of(12, 0), LocalTime.of(13, 0), sala);
        service.create(cancelada);
        service.cancel(cancelada);

        return new EscenarioEstadisticas(lunes);
    }

    private static class EscenarioEstadisticas {
        private final LocalDate lunes;

        private EscenarioEstadisticas(LocalDate lunes) {
            this.lunes = lunes;
        }
    }
}
