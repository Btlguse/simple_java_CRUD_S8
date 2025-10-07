package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para proporcionar conexiones a la base de datos de prueba.
 */
public class TestConexionMySQL {
    private static final String URL = "";
    private static final String USER = "";
    private static final String PASSWORD = "";

    /**
     * Obtiene una conexión a la base de datos de prueba.
     * @return Connection - conexión a la base de datos de prueba
     * @throws SQLException si hay un error al conectar con la BD
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
}
