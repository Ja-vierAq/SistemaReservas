package reservas.logic;

import org.junit.jupiter.api.Test;
import reservas.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceIT {

    @Test
    public void consultaDeDatosReales() throws Exception {
        Service service = Service.instance();
        Usuario administrador = new Usuario();
        administrador.setId("ADM001");
        assertEquals("ADMIN", service.read(administrador).getRol());
        assertFalse(service.findAllCategorias().isEmpty());
        assertFalse(service.findAllRecursos().isEmpty());
        assertFalse(service.findAllReservas().isEmpty());
    }

    @Test
    public void crearConsultarCancelarReserva() throws Exception {
        Service service = Service.instance();
        String sufijo = UUID.randomUUID().toString().substring(0, 8);
        String catId = "CAT-TEST-" + sufijo;
        String recId = "REC-TEST-" + sufijo;
        String resId = "RES-TEST-" + sufijo;
        String res2Id = "RES2-TEST-" + sufijo;

        Categoria categoria = new Categoria(catId, "Sala de prueba " + sufijo);
        Recurso recurso = new Recurso(recId, categoria, "Recurso de prueba " + sufijo);
        Funcionario funcionario = new Funcionario();
        funcionario.setId("FUN001");
        LocalDate fecha = LocalDate.now().plusDays(30);

        try {
            service.create(categoria);
            service.create(recurso);

            Reserva primera = new Reserva(resId, funcionario, "Prueba transaccional", fecha,
                    LocalTime.of(8, 0), LocalTime.of(10, 0));
            primera.getCategorias().add(categoria);
            service.create(primera);

            Reserva guardada = service.read(primera);
            assertEquals("ACTIVA", guardada.getEstado());
            assertEquals(1, guardada.getCategorias().size());
            assertEquals(recId, guardada.getRecursos().getFirst().getId());

            Reserva segunda = new Reserva(res2Id, funcionario, "Reserva con conflicto", fecha,
                    LocalTime.of(9, 0), LocalTime.of(11, 0));
            segunda.getCategorias().add(categoria);
            Exception error = assertThrows(Exception.class, () -> service.create(segunda));
            assertTrue(error.getMessage().contains("No hay disponibilidad"));
            assertEquals(res2Id, segunda.getId());

            service.cancel(primera);
            assertEquals("CANCELADA", service.read(primera).getEstado());
            assertTrue(service.read(primera).getRecursos().isEmpty());

            service.create(segunda);
            assertEquals(recId, service.read(segunda).getRecursos().getFirst().getId());
            service.cancel(segunda);
        } finally {
            // La prueba no deja registros residuales en la base compartida.
            try (Connection cnx = Database.instance().getConnection()) {
                cnx.setAutoCommit(false);
                try {
                    borrar(cnx, "delete from reservarecurso where reservaId in (?, ?)", resId, res2Id);
                    borrar(cnx, "delete from reservacategoria where reservaId in (?, ?)", resId, res2Id);
                    borrar(cnx, "delete from reserva where id in (?, ?)", resId, res2Id);
                    borrar(cnx, "delete from recurso where id=?", recId);
                    borrar(cnx, "delete from categoria where id=?", catId);
                    cnx.commit();
                } catch (Exception ex) {
                    cnx.rollback();
                    throw ex;
                }
            }
        }
    }

    private void borrar(Connection cnx, String sql, String... ids) throws Exception {
        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            for (int i = 0; i < ids.length; i++) stm.setString(i + 1, ids[i]);
            stm.executeUpdate();
        }
    }
}
