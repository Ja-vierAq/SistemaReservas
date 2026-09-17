package reservas.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import reservas.logic.Categoria;
import reservas.logic.Funcionario;
import reservas.logic.Recurso;
import reservas.logic.Reserva;
import reservas.logic.Usuario;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class XmlPersisterIT {

    @TempDir
    Path tempDir;

    @Test
    void guardaYCargaElGrafoCompletoSinDependerDeOtroTest() throws Exception {
        Data original = new Data();

        Usuario usuario = new Usuario("USR001", "clave", "FUNCIONARIO");
        Funcionario funcionario = new Funcionario("FUN001", "Juan Perez", "88888888", usuario);
        Categoria categoria = new Categoria("CAT001", "Laptop Windows 11");
        Recurso recurso = new Recurso("REC001", categoria, "Laptop #238715");
        Reserva reserva = new Reserva("RES001", funcionario, "Reunion de trabajo",
                LocalDate.of(2026, 9, 10), LocalTime.of(8, 0), LocalTime.of(10, 0));
        reserva.getCategorias().add(categoria);
        reserva.getRecursos().add(recurso);

        original.getUsuarios().add(usuario);
        original.getFuncionarios().add(funcionario);
        original.getCategorias().add(categoria);
        original.getRecursos().add(recurso);
        original.getReservas().add(reserva);

        Path archivo = tempDir.resolve("data-integracion.xml");
        XmlPersister persister = new XmlPersister(archivo.toString());

        persister.store(original);
        Data cargada = persister.load();

        assertTrue(Files.exists(archivo));
        assertTrue(Files.size(archivo) > 0);

        assertAll(
                () -> assertEquals(1, cargada.getUsuarios().size()),
                () -> assertEquals(1, cargada.getFuncionarios().size()),
                () -> assertEquals(1, cargada.getCategorias().size()),
                () -> assertEquals(1, cargada.getRecursos().size()),
                () -> assertEquals(1, cargada.getReservas().size())
        );

        Reserva cargadaReserva = cargada.getReservas().get(0);

        assertAll(
                () -> assertEquals("Reunion de trabajo", cargadaReserva.getActividad()),
                () -> assertEquals(LocalDate.of(2026, 9, 10), cargadaReserva.getFecha()),
                () -> assertEquals(LocalTime.of(8, 0), cargadaReserva.getHoraInicio()),
                () -> assertEquals(LocalTime.of(10, 0), cargadaReserva.getHoraFin()),
                () -> assertEquals("Juan Perez", cargadaReserva.getFuncionario().getNombre()),
                () -> assertEquals("CAT001", cargadaReserva.getCategorias().get(0).getId()),
                () -> assertEquals("REC001", cargadaReserva.getRecursos().get(0).getId()),
                () -> assertSame(cargada.getFuncionarios().get(0), cargadaReserva.getFuncionario()),
                () -> assertSame(cargada.getCategorias().get(0), cargadaReserva.getCategorias().get(0)),
                () -> assertSame(cargada.getRecursos().get(0), cargadaReserva.getRecursos().get(0))
        );
    }
}
