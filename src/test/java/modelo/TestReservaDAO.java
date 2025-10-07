package modelo;

import java.sql.Connection;
import java.sql.SQLException;
import util.TestConexionMySQL;

/**
 * Clase DAO de Reserva para pruebas. Extiende de ReservaDAO y sobrescribe el método getConnection
 * para utilizar la conexión de prueba en lugar de la conexión de producción.
 */
public class TestReservaDAO extends ReservaDAO {
    
    /**
     * Sobrescribe el método getConnection para utilizar la conexión de prueba.
     */
    @Override
    protected Connection getConnection() throws SQLException {
        return TestConexionMySQL.getConnection();
    }
}
