package modelo;

import java.sql.Connection;
import java.sql.SQLException;
import util.TestConexionMySQL;

/**
 * Clase DAO de Factura para pruebas. Esta clase es necesaria porque FacturaDAO no tiene un método getConnection sobreescribible.
 * Implementa los mismos métodos que FacturaDAO pero utiliza la conexión de prueba.
 */
public class TestFacturaDAO extends FacturaDAO {
    
    /**
     * Obtiene una conexión a la base de datos de prueba.
     */
    protected Connection getConnection() throws SQLException {
        return TestConexionMySQL.getConnection();
    }
}
