package reservas.data;

import reservas.logic.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    public void create(Usuario u) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            create(u, cnx);
        }
    }

    public void create(Usuario u, Connection cnx) throws SQLException {
        String sql = "insert into usuario (id, clave, rol) values (?, ?, ?)";
        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, u.getId());
            stm.setString(2, u.getClave());
            stm.setString(3, u.getRol());
            stm.executeUpdate();
        }
    }

    public Usuario read(String id) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            return read(id, cnx);
        }
    }

    public Usuario read(String id, Connection cnx) throws Exception {
        String sql = "select id, clave, rol from usuario where id=?";
        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getString("id"), rs.getString("clave"), rs.getString("rol"));
                }
            }
        }
        throw new Exception("Usuario no existe");
    }

    public void cambiarClave(String id, String nuevaClave) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            cambiarClave(id, nuevaClave, cnx);
        }
    }

    public void cambiarClave(String id, String nuevaClave, Connection cnx) throws Exception {
        String sql = "update usuario set clave=? where id=?";
        try (PreparedStatement stm = cnx.prepareStatement(sql)) {
            stm.setString(1, nuevaClave);
            stm.setString(2, id);
            if (stm.executeUpdate() == 0) throw new Exception("Usuario no existe");
        }
    }

    public void delete(String id) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            delete(id, cnx);
        }
    }

    public void delete(String id, Connection cnx) throws Exception {
        try (PreparedStatement stm = cnx.prepareStatement("delete from usuario where id=?")) {
            stm.setString(1, id);
            if (stm.executeUpdate() == 0) throw new Exception("Usuario no existe");
        }
    }

    public List<Usuario> findAll() throws Exception {
        try (Connection cnx = Database.instance().getConnection();
             PreparedStatement stm = cnx.prepareStatement("select id, clave, rol from usuario order by id");
             ResultSet rs = stm.executeQuery()) {
            List<Usuario> resultado = new ArrayList<>();
            while (rs.next()) {
                resultado.add(new Usuario(rs.getString("id"), rs.getString("clave"), rs.getString("rol")));
            }
            return resultado;
        }
    }
}
