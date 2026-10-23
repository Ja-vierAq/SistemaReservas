package reservas.data;

import reservas.logic.Categoria;
import reservas.logic.Recurso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecursoDao {

    private static final String CONSULTA = """
            select r.id as r_id, r.descripcion as r_descripcion,
                   c.id as c_id, c.descripcion as c_descripcion
            from recurso r join categoria c on r.categoriaId=c.id
            """;

    public void create(Recurso r) throws Exception {
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

    public void create(Recurso r, Connection cnx) throws Exception {
        new SecuenciaDao().registrarId("REC", r.getId(), cnx);
        String sql = "insert into recurso (id, categoriaId, descripcion) values (?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, r.getId());
            stm.setString(2, r.getCategoria().getId());
            stm.setString(3, r.getDescripcion());
            stm.executeUpdate();
        }
    }

    public Recurso read(String id) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            return read(id, cnx);
        }
    }

    public Recurso read(String id, Connection cnx) throws Exception {
        try (PreparedStatement stm = cnx.prepareStatement(CONSULTA + " where r.id=?")) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) return from(rs);
            }
        }
        throw new Exception("Recurso no existe");
    }

    public void update(Recurso r) throws Exception {
        String sql = "update recurso set categoriaId=?, descripcion=? where id=?";
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, r.getCategoria().getId());
            stm.setString(2, r.getDescripcion());
            stm.setString(3, r.getId());
            if (stm.executeUpdate() == 0) throw new Exception("Recurso no existe");
        }
    }

    public void delete(String id) throws Exception {
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement("delete from recurso where id=?")) {
            stm.setString(1, id);
            if (stm.executeUpdate() == 0) throw new Exception("Recurso no existe");
        }
    }

    public List<Recurso> search(String id, String descripcion, String categoriaId) throws Exception {
        String sql = CONSULTA + " where lower(r.id) like ? and lower(r.descripcion) like ? "
                + "and (? = '' or c.id = ?) order by r.id";
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, "%" + (id == null ? "" : id.trim().toLowerCase()) + "%");
            stm.setString(2, "%" + (descripcion == null ? "" : descripcion.trim().toLowerCase()) + "%");
            String cat = categoriaId == null ? "" : categoriaId.trim();
            stm.setString(3, cat);
            stm.setString(4, cat);
            try (ResultSet rs = stm.executeQuery()) {
                List<Recurso> resultado = new ArrayList<>();
                while (rs.next()) resultado.add(from(rs));
                return resultado;
            }
        }
    }

    public List<Recurso> findAll() throws Exception {
        return search("", "", "");
    }

    public boolean estaEnReservas(String recursoId) throws Exception {
        String sql = "select 1 from reservarecurso where recursoId=? limit 1";
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, recursoId);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Recurso from(ResultSet rs) throws SQLException {
        Categoria c = new Categoria(rs.getString("c_id"), rs.getString("c_descripcion"));
        return new Recurso(rs.getString("r_id"), c, rs.getString("r_descripcion"));
    }
}
