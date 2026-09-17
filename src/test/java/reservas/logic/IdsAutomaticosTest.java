package reservas.logic;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class IdsAutomaticosTest extends ServiceTestSupport {

    @Test
    void generaSiguienteIdDeCategoriaSegunElMayorExistente() throws Exception {
        service.create(new Categoria("CAT-0000007", "Existente"));
        Categoria nueva = new Categoria(null, "Nueva categoria");

        service.create(nueva);

        assertAll(
                () -> assertEquals("CAT-0000008", nueva.getId()),
                () -> assertEquals("Nueva categoria", service.read(nueva).getDescripcion())
        );
    }

    @Test
    void generaIdsConsecutivosParaReservas() throws Exception {
        Categoria sala = crearCategoria("CAT-0000001", "Sala");
        crearRecurso("REC-001", sala, "Sala 1");
        LocalDate fecha = LocalDate.of(2099, 4, 1);

        Reserva existente = nuevaReserva("RES-0000041", "Existente", fecha,
                LocalTime.of(8, 0), LocalTime.of(9, 0), sala);
        service.create(existente);

        Reserva nueva1 = nuevaReserva(null, "Nueva 1", fecha,
                LocalTime.of(9, 0), LocalTime.of(10, 0), sala);
        Reserva nueva2 = nuevaReserva(null, "Nueva 2", fecha,
                LocalTime.of(10, 0), LocalTime.of(11, 0), sala);

        service.create(nueva1);
        service.create(nueva2);

        assertAll(
                () -> assertEquals("RES-0000042", nueva1.getId()),
                () -> assertEquals("RES-0000043", nueva2.getId()),
                () -> assertNotEquals(nueva1.getId(), nueva2.getId())
        );
    }
}
