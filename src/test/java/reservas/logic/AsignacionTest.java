package reservas.logic;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AsignacionTest extends ServiceTestSupport {

    @Test
    void asignaElPrimerRecursoDisponibleDeCadaCategoria() throws Exception {
        Categoria sala = crearCategoria("CAT-0000001", "Sala");
        Categoria proyector = crearCategoria("CAT-0000002", "Proyector");

        crearRecurso("REC-020", sala, "Sala 20");
        crearRecurso("REC-010", sala, "Sala 10");
        crearRecurso("REC-040", proyector, "Proyector 40");
        crearRecurso("REC-030", proyector, "Proyector 30");

        Reserva reserva = nuevaReserva(null, "Capacitacion", LocalDate.of(2099, 3, 5),
                LocalTime.of(13, 0), LocalTime.of(15, 0), sala, proyector);

        service.create(reserva);

        List<String> idsAsignados = reserva.getRecursos().stream()
                .map(Recurso::getId)
                .collect(Collectors.toList());

        assertAll(
                () -> assertEquals(List.of("REC-010", "REC-030"), idsAsignados),
                () -> assertEquals("ACTIVA", reserva.getEstado()),
                () -> assertEquals(2, reserva.getCategorias().size())
        );
    }
}
