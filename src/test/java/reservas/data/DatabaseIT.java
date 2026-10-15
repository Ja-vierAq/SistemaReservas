package reservas.data;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseIT {

    @Test
    public void conexionYTablas() throws Exception {

        try (Connection cnx = Database.instance().getConnection();
             Statement stm = cnx.createStatement()) {

            System.out.println("CONEXION EXITOSA");
            System.out.println("URL: " + cnx.getMetaData().getURL());
            System.out.println("Catalogo: " + cnx.getCatalog());

            try (ResultSet rs = stm.executeQuery("SELECT DATABASE()")) {
                if (rs.next()) {
                    System.out.println("Base de datos: " + rs.getString(1));
                }
            }

            int cantidad = 0;

            try (ResultSet rs = stm.executeQuery("SHOW TABLES")) {
                System.out.println("TABLAS ENCONTRADAS:");

                while (rs.next()) {
                    System.out.println("- " + rs.getString(1));
                    cantidad++;
                }
            }

            System.out.println("Total: " + cantidad);

            assertEquals(8, cantidad,
                    "No se encontraron las 8 tablas esperadas");
        }
    }
}
