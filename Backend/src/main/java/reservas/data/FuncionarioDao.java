package reservas.data;

import reservas.logic.Funcionario;
import reservas.logic.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDao {

    private static final String CONSULTA = """
            select f.id as f_id, f.nombre as f_nombre, f.telefono as f_telefono,
                   u.id as u_id, u.clave as u_clave, u.rol as u_rol
            from funcionario f left join usuario u on f.usuarioId=u.id
            """;

    // Crear funcionario y usuario juntos para no dejar registros incompletos.
    public void createConUsuario(Funcionario f) throws Exception {
        Usuario usuario = new Usuario(f.getId(), f.getId(), "FUNCIONARIO");
        try (Connection cnx = Database.instance().getConnection()) {
            cnx.setAutoCommit(false);
            try {
                new UsuarioDao().create(usuario, cnx);
                f.setUsuario(usuario);
                create(f, cnx);
                cnx.commit();
            } catch (Exception ex) {
                cnx.rollback();
                f.setUsuario(null);
                throw ex;
            }
        }
    }

    public void deleteConUsuario(String id) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            cnx.setAutoCommit(false);
            try {
                Funcionario funcionario = read(id, cnx);
                delete(id, cnx);
                if (funcionario.getUsuario() != null) {
                    new UsuarioDao().delete(funcionario.getUsuario().getId(), cnx);
                }
                cnx.commit();
            } catch (Exception ex) {
                cnx.rollback();
                throw ex;
            }
        }
    }

    public void create(Funcionario f) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            create(f, cnx);
        }
    }

    public void create(Funcionario f, Connection cnx) throws SQLException {
        String sql = "insert into funcionario (id, nombre, telefono, usuarioId) values (?, ?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, f.getId());
            stm.setString(2, f.getNombre());
            stm.setString(3, f.getTelefono());
            stm.setString(4, f.getUsuario() == null ? null : f.getUsuario().getId());
            stm.executeUpdate();
        }
    }

    public Funcionario read(String id) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            return read(id, cnx);
        }
    }

    public Funcionario read(String id, Connection cnx) throws Exception {
        try (PreparedStatement stm = cnx.prepareStatement(CONSULTA + " where f.id=?")) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) return from(rs);
            }
        }
        throw new Exception("Funcionario no existe");
    }

    public void update(Funcionario f) throws Exception {
        String sql = "update funcionario set nombre=?, telefono=? where id=?";
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, f.getNombre());
            stm.setString(2, f.getTelefono());
            stm.setString(3, f.getId());
            if (stm.executeUpdate() == 0) throw new Exception("Funcionario no existe");
        }
    }

    public void delete(String id, Connection cnx) throws Exception {
        try (PreparedStatement stm = cnx.prepareStatement("delete from funcionario where id=?")) {
            stm.setString(1, id);
            if (stm.executeUpdate() == 0) throw new Exception("Funcionario no existe");
        }
    }

    public List<Funcionario> search(String id, String nombre) throws Exception {
        String sql = CONSULTA + " where lower(f.id) like ? and lower(f.nombre) like ? order by f.id";
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, "%" + (id == null ? "" : id.trim().toLowerCase()) + "%");
            stm.setString(2, "%" + (nombre == null ? "" : nombre.trim().toLowerCase()) + "%");
            try (ResultSet rs = stm.executeQuery()) {
                List<Funcionario> resultado = new ArrayList<>();
                while (rs.next()) resultado.add(from(rs));
                return resultado;
            }
        }
    }

    public List<Funcionario> findAll() throws Exception {
        return search("", "");
    }

    private Funcionario from(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();
        f.setId(rs.getString("f_id"));
        f.setNombre(rs.getString("f_nombre"));
        f.setTelefono(rs.getString("f_telefono"));
        String usuarioId = rs.getString("u_id");
        if (usuarioId != null) {
            f.setUsuario(new Usuario(usuarioId, rs.getString("u_clave"), rs.getString("u_rol")));
        }
        return f;
    }
}
