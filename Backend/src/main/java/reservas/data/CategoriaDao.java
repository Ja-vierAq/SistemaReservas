package reservas.data;

import reservas.logic.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDao {

    public void create(Categoria c) throws Exception {
        String idAnterior = c.getId();
        try (Connection cnx = Database.instance().getConnection()) {
            cnx.setAutoCommit(false);
            try {
                create(c, cnx);
                cnx.commit();
            } catch (Exception ex) {
                cnx.rollback();
                c.setId(idAnterior);
                throw ex;
            }
        }
    }

    public void create(Categoria c, Connection cnx) throws Exception {
        if (c.getId() == null || c.getId().isBlank()) {
            c.setId(new SecuenciaDao().siguienteId("CAT", cnx));
        } else {
            new SecuenciaDao().registrarId("CAT", c.getId(), cnx);
        }
        try (PreparedStatement stm = cnx.prepareStatement("insert into categoria (id, descripcion) values (?, ?)")) {
            stm.setString(1, c.getId());
            stm.setString(2, c.getDescripcion());
            stm.executeUpdate();
        }
    }

    public Categoria read(String id) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            return read(id, cnx);
        }
    }

    public Categoria read(String id, Connection cnx) throws Exception {
        try (PreparedStatement stm = cnx.prepareStatement("select id, descripcion from categoria where id=?")) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) return from(rs);
            }
        }
        throw new Exception("Categoría no existe");
    }

    public void update(Categoria c) throws Exception {
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement("update categoria set descripcion=? where id=?")) {
            stm.setString(1, c.getDescripcion());
            stm.setString(2, c.getId());
            if (stm.executeUpdate() == 0) throw new Exception("Categoría no existe");
        }
    }

    public void delete(String id) throws Exception {
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement("delete from categoria where id=?")) {
            stm.setString(1, id);
            if (stm.executeUpdate() == 0) throw new Exception("Categoría no existe");
        }
    }

    public List<Categoria> search(String descripcion) throws Exception {
        String sql = "select id, descripcion from categoria where lower(descripcion) like ? order by descripcion";
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, "%" + (descripcion == null ? "" : descripcion.trim().toLowerCase()) + "%");
            try (ResultSet rs = stm.executeQuery()) {
                List<Categoria> resultado = new ArrayList<>();
                while (rs.next()) resultado.add(from(rs));
                return resultado;
            }
        }
    }

    public List<Categoria> findAll() throws Exception {
        return search("");
    }

    public boolean estaEnReservas(String categoriaId) throws Exception {
        String sql = "select 1 from reservacategoria where categoriaId=? limit 1";
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, categoriaId);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean tieneRecursos(String categoriaId) throws Exception {
        String sql = "select 1 from recurso where categoriaId=? limit 1";
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, categoriaId);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Categoria from(ResultSet rs) throws SQLException {
        return new Categoria(rs.getString("id"), rs.getString("descripcion"));
    }
}
