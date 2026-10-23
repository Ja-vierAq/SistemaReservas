package reservas.data;

import reservas.logic.Categoria;
import reservas.logic.Funcionario;
import reservas.logic.Recurso;
import reservas.logic.Reserva;
import reservas.logic.Usuario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDao {

    private static final String CONSULTA = """
            select r.id as r_id, r.actividad as r_actividad, r.estado as r_estado,
                   r.fecha as r_fecha, r.horaInicio as r_inicio, r.horaFin as r_fin,
                   f.id as f_id, f.nombre as f_nombre, f.telefono as f_telefono,
                   u.id as u_id, u.clave as u_clave, u.rol as u_rol
            from reserva r
            join funcionario f on r.funcionarioId=f.id
            left join usuario u on f.usuarioId=u.id
            """;

    public void create(Reserva r) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            cnx.setAutoCommit(false);
            try {
                create(r, cnx);
                cnx.commit();
            } catch (Exception ex) {
                cnx.rollback();
                throw ex;
            }
        }
    }

    // Service debe validar previamente que la reserva tiene disponibilidad.
    // Para evitar carreras entre clientes, usar seleccionarRecursoDisponible
    // en la MISMA transaccion antes de llamar a create(r, cnx).
    public void create(Reserva r, Connection cnx) throws Exception {
        if (r.getId() == null || r.getId().isBlank()) {
            r.setId(new SecuenciaDao().siguienteId("RES", cnx));
        } else {
            new SecuenciaDao().registrarId("RES", r.getId(), cnx);
        }
        if (r.getEstado() == null || r.getEstado().isBlank()) r.setEstado("ACTIVA");

        String sql = """
                insert into reserva (id, funcionarioId, actividad, estado, fecha, horaInicio, horaFin)
                values (?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, r.getId());
            stm.setString(2, r.getFuncionario().getId());
            stm.setString(3, r.getActividad());
            stm.setString(4, r.getEstado());
            stm.setDate(5, Date.valueOf(r.getFecha()));
            stm.setTime(6, Time.valueOf(r.getHoraInicio()));
            stm.setTime(7, Time.valueOf(r.getHoraFin()));
            stm.executeUpdate();
        }

        String sqlCategoria = "insert into reservacategoria (reservaId, categoriaId) values (?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(sqlCategoria)) {
            for (Categoria c : r.getCategorias()) {
                stm.setString(1, r.getId());
                stm.setString(2, c.getId());
                stm.addBatch();
            }
            stm.executeBatch();
        }

        String sqlRecurso = "insert into reservarecurso (reservaId, recursoId) values (?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(sqlRecurso)) {
            for (Recurso recurso : r.getRecursos()) {
                stm.setString(1, r.getId());
                stm.setString(2, recurso.getId());
                stm.addBatch();
            }
            stm.executeBatch();
        }
    }

    public Reserva read(String id) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            return read(id, cnx);
        }
    }

    public Reserva read(String id, Connection cnx) throws Exception {
        Reserva r;
        try (PreparedStatement stm = cnx.prepareStatement(CONSULTA + " where r.id=?")) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (!rs.next()) throw new Exception("Reserva no existe");
                r = from(rs);
            }
        }

        String sqlCategoria = """
                select c.id, c.descripcion
                from reservacategoria rc join categoria c on rc.categoriaId=c.id
                where rc.reservaId=? order by c.id
                """;
        try (PreparedStatement stm = cnx.prepareStatement(sqlCategoria)) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    r.getCategorias().add(new Categoria(rs.getString("id"), rs.getString("descripcion")));
                }
            }
        }

        String sqlRecurso = """
                select rec.id as rec_id, rec.descripcion as rec_descripcion,
                       c.id as cat_id, c.descripcion as cat_descripcion
                from reservarecurso rr
                join recurso rec on rr.recursoId=rec.id
                join categoria c on rec.categoriaId=c.id
                where rr.reservaId=? order by rec.id
                """;
        try (PreparedStatement stm = cnx.prepareStatement(sqlRecurso)) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    Categoria c = new Categoria(rs.getString("cat_id"), rs.getString("cat_descripcion"));
                    r.getRecursos().add(new Recurso(rs.getString("rec_id"), c, rs.getString("rec_descripcion")));
                }
            }
        }
        return r;
    }

    public List<Reserva> findAll() throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            return buscarPorIds("select id from reserva order by fecha, horaInicio", null, cnx);
        }
    }

    public List<Reserva> findByFuncionario(String funcionarioId) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            return buscarPorIds("select id from reserva where funcionarioId=? order by fecha, horaInicio",
                    funcionarioId, cnx);
        }
    }

    public List<Reserva> findActivasEnPeriodo(LocalDate desde, LocalDate hasta) throws Exception {
        String sql = "select id from reserva where estado='ACTIVA' and fecha between ? and ? "
                + "order by fecha, horaInicio";
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setDate(1, Date.valueOf(desde));
            stm.setDate(2, Date.valueOf(hasta));
            List<String> ids = new ArrayList<>();
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) ids.add(rs.getString("id"));
            }
            List<Reserva> resultado = new ArrayList<>();
            for (String id : ids) resultado.add(read(id, cnx));
            return resultado;
        }
    }

    public void cancel(String id) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            cnx.setAutoCommit(false);
            try {
                cancel(id, cnx);
                cnx.commit();
            } catch (Exception ex) {
                cnx.rollback();
                throw ex;
            }
        }
    }

    // La comprobacion de fecha futura corresponde a Service.
    public void cancel(String id, Connection cnx) throws Exception {
        String sql = "update reserva set estado='CANCELADA' where id=? and estado='ACTIVA'";
        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, id);
            if (stm.executeUpdate() == 0) throw new Exception("Reserva no existe o ya está cancelada");
        }
        try (PreparedStatement stm = cnx.prepareStatement("delete from reservarecurso where reservaId=?")) {
            stm.setString(1, id);
            stm.executeUpdate();
        }
    }

    // Bloquea los recursos de la categoria durante la transaccion, para que
    // otros clientes no puedan adjudicar uno al mismo tiempo.
    public Recurso seleccionarRecursoDisponible(String categoriaId, LocalDate fecha,
                                                LocalTime inicio, LocalTime fin,
                                                Connection cnx) throws Exception {
        if (cnx.getAutoCommit()) {
            throw new IllegalStateException("Se requiere una transaccion (autoCommit=false)");
        }
        List<String> candidatos = new ArrayList<>();
        String sqlRecursos = "select id from recurso where categoriaId=? order by id for update";
        try (PreparedStatement stm = cnx.prepareStatement(sqlRecursos)) {
            stm.setString(1, categoriaId);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) candidatos.add(rs.getString("id"));
            }
        }

        String sqlOcupado = """
                select r.id from reserva r
                join reservarecurso rr on rr.reservaId=r.id
                where rr.recursoId=? and r.fecha=? and r.estado='ACTIVA'
                  and r.horaInicio < ? and r.horaFin > ?
                for update
                """;
        for (String recursoId : candidatos) {
            try (PreparedStatement stm = cnx.prepareStatement(sqlOcupado)) {
                stm.setString(1, recursoId);
                stm.setDate(2, Date.valueOf(fecha));
                stm.setTime(3, Time.valueOf(fin));
                stm.setTime(4, Time.valueOf(inicio));
                try (ResultSet rs = stm.executeQuery()) {
                    if (!rs.next()) return new RecursoDao().read(recursoId, cnx);
                }
            }
        }
        return null;
    }

    private List<Reserva> buscarPorIds(String sql, String parametro, Connection cnx) throws Exception {
        List<String> ids = new ArrayList<>();
        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            if (parametro != null) stm.setString(1, parametro);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) ids.add(rs.getString("id"));
            }
        }
        List<Reserva> resultado = new ArrayList<>();
        for (String id : ids) resultado.add(read(id, cnx));
        return resultado;
    }

    private Reserva from(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setId(rs.getString("r_id"));
        r.setActividad(rs.getString("r_actividad"));
        r.setEstado(rs.getString("r_estado"));
        r.setFecha(rs.getDate("r_fecha").toLocalDate());
        r.setHoraInicio(rs.getTime("r_inicio").toLocalTime());
        r.setHoraFin(rs.getTime("r_fin").toLocalTime());

        Funcionario f = new Funcionario();
        f.setId(rs.getString("f_id"));
        f.setNombre(rs.getString("f_nombre"));
        f.setTelefono(rs.getString("f_telefono"));
        String usuarioId = rs.getString("u_id");
        if (usuarioId != null) {
            f.setUsuario(new Usuario(usuarioId, rs.getString("u_clave"), rs.getString("u_rol")));
        }
        r.setFuncionario(f);
        return r;
    }
}
