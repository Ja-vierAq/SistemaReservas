
package reservas.data;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Database {

    private static final String PROPERTIES_FILE_NAME = "/database.properties";
    private static final Database INSTANCE = new Database();

    private final String url;
    private final String user;
    private final String password;

    private Database() {
        Properties prop = new Properties();

        try (InputStream input = Database.class.getResourceAsStream(PROPERTIES_FILE_NAME)) {
            if (input == null) {
                throw new IllegalStateException(
                        "No se encontro " + PROPERTIES_FILE_NAME + " en src/main/resources"
                );
            }
            prop.load(input);
        } catch (IOException ex) {
            throw new IllegalStateException("Error leyendo database.properties", ex);
        }

        String driver = required(prop, "database_driver");
        String server = required(prop, "database_server");
        String port = required(prop, "database_port");
        String databaseName = required(prop, "database_name");

        this.user = required(prop, "database_user");
        this.password = prop.getProperty("database_password", "");

        this.url = "jdbc:mysql://" + server + ":" + port + "/" + databaseName + "?connectTimeout=5000";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(
                    "No se encontro el driver JDBC de MySQL", ex
            );
        }
    }

    public static Database instance() {return INSTANCE;}
    public Connection getConnection() throws SQLException {return DriverManager.getConnection(url, user, password);}

    private static String required(Properties prop, String key) {
        String value = prop.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Falta configurar " + key + " en database.properties"
            );
        }
        return value.trim();
    }
}
