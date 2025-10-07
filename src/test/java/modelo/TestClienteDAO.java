package modelo;

import java.sql.Connection;
import java.sql.SQLException;
import util.TestConexionMySQL;

/**
 * Clase DAO de Cliente para pruebas. Extiende de ClienteDAO y sobrescribe el método getConnection
 * para utilizar la conexión de prueba en lugar de la conexión de producción.
 */
public class TestClienteDAO extends ClienteDAO {
    
    /**
     * Sobrescribe el método getConnection para utilizar la conexión de prueba.
     */
    @Override
    protected Connection getConnection() throws SQLException {
        return TestConexionMySQL.getConnection();
    }
}
