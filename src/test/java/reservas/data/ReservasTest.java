package reservas.logic;

import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class ReservasTest {
    @Test
    void traslapeDeHoras() {
        assertTrue(Service.seTraslapan(LocalTime.of(8, 0), LocalTime.of(10, 0),
                LocalTime.of(9, 0), LocalTime.of(11, 0)));
        assertFalse(Service.seTraslapan(LocalTime.of(8, 0), LocalTime.of(10, 0),
                LocalTime.of(10, 0), LocalTime.of(11, 0)));
    }
}
