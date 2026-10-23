package reservas.data;

import org.junit.jupiter.api.Test;
import reservas.logic.Categoria;
import reservas.logic.Funcionario;
import reservas.logic.Recurso;
import reservas.logic.Reserva;
import reservas.logic.Usuario;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class DaoIT {

    @Test
    public void lecturaDeDatos() throws Exception {
        assertEquals("ADMIN", new UsuarioDao().read("ADM001").getRol());
        assertFalse(new FuncionarioDao().findAll().isEmpty());
        assertFalse(new CategoriaDao().findAll().isEmpty());
        assertFalse(new RecursoDao().findAll().isEmpty());

        Reserva primera = new ReservaDao().read("RES-0000001");
        assertNotNull(primera.getFuncionario());
        assertFalse(primera.getCategorias().isEmpty());
        assertFalse(primera.getRecursos().isEmpty());
        assertNotNull(primera.getFecha());
        assertNotNull(primera.getHoraInicio());
        assertNotNull(primera.getHoraFin());
    }

    @Test
    public void consultasConFiltros() throws Exception {
        assertTrue(new FuncionarioDao().search("FUN001", "").stream()
                .anyMatch(f -> f.getId().equals("FUN001")));
        assertTrue(new CategoriaDao().search("Laptop").stream()
                .anyMatch(c -> c.getId().equals("CAT-0000001")));
        assertTrue(new RecursoDao().search("", "", "CAT-0000001").stream()
                .anyMatch(r -> r.getId().equals("REC001")));
        assertFalse(new ReservaDao().findByFuncionario("FUN001").isEmpty());
    }
}
