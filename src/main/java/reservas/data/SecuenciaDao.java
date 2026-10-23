package reservas.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;

public class SecuenciaDao {

    public String siguienteId(String tipo) throws Exception {
        try (Connection cnx = Database.instance().getConnection()) {
            cnx.setAutoCommit(false);
            try {
                String id = siguienteId(tipo, cnx);
                cnx.commit();
                return id;
            } catch (Exception ex) {
                cnx.rollback();
                throw ex;
            }
        }
    }

    // Este metodo requiere una transaccion abierta en cnx.
    public String siguienteId(String tipo, Connection cnx) throws Exception {
        if (!"CAT".equals(tipo) && !"REC".equals(tipo) && !"RES".equals(tipo)) {
            throw new IllegalArgumentException("Tipo de secuencia no valido: " + tipo);
        }
        if (cnx.getAutoCommit()) {
            throw new IllegalStateException("siguienteId requiere una transaccion (autoCommit=false)");
        }

        long siguiente;
        try (PreparedStatement stm = cnx.prepareStatement("select ultimo from secuencia where tipo=? for update")) {
            stm.setString(1, tipo);
            try (ResultSet rs = stm.executeQuery()) {
                if (!rs.next()) throw new Exception("Secuencia no existe: " + tipo);
                siguiente = rs.getLong("ultimo") + 1;
            }
        }
        try (PreparedStatement stm = cnx.prepareStatement("update secuencia set ultimo=? where tipo=?")) {
            stm.setLong(1, siguiente);
            stm.setString(2, tipo);
            stm.executeUpdate();
        }
        return String.format(Locale.ROOT, "%s-%07d", tipo, siguiente);
    }
    // Si se asigna manualmente un ID consecutivo, evita generar uno repetido.
    public void registrarId(String tipo, String id, Connection cnx) throws Exception {
        if (id == null || !id.matches(java.util.regex.Pattern.quote(tipo) + "-[0-9]{7}")) return;
        long numero = Long.parseLong(id.substring(tipo.length() + 1));
        try (PreparedStatement stm = cnx.prepareStatement("select ultimo from secuencia where tipo=? for update")) {
            stm.setString(1, tipo);
            try (ResultSet rs = stm.executeQuery()) {
                if (!rs.next()) throw new Exception("Secuencia no existe: " + tipo);
                long ultimo = rs.getLong(1);
                if (numero > ultimo) {
                    try (PreparedStatement update = cnx.prepareStatement("update secuencia set ultimo=? where tipo=?")) {
                        update.setLong(1, numero);
                        update.setString(2, tipo);
                        update.executeUpdate();
                    }
                }
            }
        }
    }

}
