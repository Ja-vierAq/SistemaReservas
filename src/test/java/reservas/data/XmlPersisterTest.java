package reservas.data;

import org.junit.jupiter.api.Test;
import reservas.logic.Categoria;
import reservas.logic.Funcionario;
import reservas.logic.Recurso;
import reservas.logic.Reserva;
import reservas.logic.Usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;

public class XmlPersisterTest {

    @Test
    void guardarDatos() throws Exception {

        Data data = new Data();
        Usuario usuario = new Usuario("USR001", "001", "FUNCIONARIO");
        Funcionario funcionario = new Funcionario("FUN001", "Juan Perez", "88888888", usuario);
        Categoria categoria = new Categoria("CAT001", "Laptop Windows 11");
        Recurso recurso = new Recurso("REC001", categoria, "Laptop #238715");
        Reserva reserva = new Reserva("RES001", funcionario, "Reunión de trabajo", LocalDate.of(2026, 9, 10), LocalTime.of(8, 0), LocalTime.of(10, 0));
        reserva.getCategorias().add(categoria);
        reserva.getRecursos().add(recurso);

        data.getUsuarios().add(usuario);
        data.getFuncionarios().add(funcionario);
        data.getCategorias().add(categoria);
        data.getRecursos().add(recurso);
        data.getReservas().add(reserva);

        XmlPersister persister =
                new XmlPersister("test-data.xml");

        persister.store(data);
    }
    @Test
    void cargarDatos() throws Exception {

        XmlPersister persister = new XmlPersister("test-data.xml");

        Data data = persister.load();

        assertEquals(1, data.getUsuarios().size());
        assertEquals(1, data.getFuncionarios().size());
        assertEquals(1, data.getCategorias().size());
        assertEquals(1, data.getRecursos().size());
        assertEquals(1, data.getReservas().size());

        Reserva reserva = data.getReservas().get(0);

        assertEquals("Reunión de trabajo", reserva.getActividad());
        assertEquals(LocalDate.of(2026, 9, 10), reserva.getFecha());
        assertEquals(LocalTime.of(8, 0), reserva.getHoraInicio());
        assertEquals("Juan Perez", reserva.getFuncionario().getNombre());
    }
}