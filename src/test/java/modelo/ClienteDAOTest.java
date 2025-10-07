package modelo;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.List;
import util.TestConexionMySQL;

/**
 * Pruebas unitarias para la clase ClienteDAO.
 * Prueba las operaciones CRUD y el manejo de excepciones.
 */
public class ClienteDAOTest {
    
    private static TestClienteDAO clienteDAO;
    private static Connection conn;
    
    @BeforeAll
    public static void setUp() {
        // Inicializar el DAO para las pruebas
        clienteDAO = new TestClienteDAO();
    }
    
    @BeforeEach
    public void setUpEach() throws SQLException {
        // Limpiar datos antes de cada prueba
        conn = TestConexionMySQL.getConnection();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM factura");
        stmt.executeUpdate("DELETE FROM reserva");
        stmt.executeUpdate("DELETE FROM cliente");
        stmt.close();
        conn.close();
    }
    
    @Test
    public void testAgregarCliente() {
        // Arrange
        Cliente cliente = new Cliente("Juan", "Pérez", "0987654321", "0998765432", "juan@example.com", "Calle Principal 123");
        
        // Act
        boolean resultado = clienteDAO.agregarCliente(cliente);
        
        // Assert
        assertTrue(resultado, "Debe retornar true si el cliente se agregó correctamente");
        
        // Verificar que el cliente se agregó consultando la base de datos
        List<Cliente> clientes = clienteDAO.listarClientes();
        assertFalse(clientes.isEmpty(), "La lista de clientes no debe estar vacía");
        assertEquals("Juan", clientes.get(0).getNombre(), "El nombre del cliente debe ser Juan");
        assertEquals("Pérez", clientes.get(0).getApellido(), "El apellido del cliente debe ser Pérez");
        assertEquals("0987654321", clientes.get(0).getDni(), "El DNI del cliente debe ser 0987654321");
    }
    
    @Test
    public void testConsultarCliente() throws SQLException {
        // Arrange
        // Insertar un cliente directamente en la base de datos
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO cliente (nombre, apellido, dni, telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmt.setString(1, "María");
        pstmt.setString(2, "López");
        pstmt.setString(3, "1234567890");
        pstmt.setString(4, "0912345678");
        pstmt.setString(5, "maria@example.com");
        pstmt.setString(6, "Avenida 123");
        pstmt.executeUpdate();
        
        // Obtener el ID generado
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int clienteId = -1;
        if (generatedKeys.next()) {
            clienteId = generatedKeys.getInt(1);
        }
        pstmt.close();
        conn.close();
        
        // Act
        Cliente clienteConsultado = clienteDAO.consultarCliente(clienteId);
        
        // Assert
        assertNotNull(clienteConsultado, "El cliente consultado no debe ser null");
        assertEquals("María", clienteConsultado.getNombre(), "El nombre debe ser María");
        assertEquals("López", clienteConsultado.getApellido(), "El apellido debe ser López");
        assertEquals("1234567890", clienteConsultado.getDni(), "El DNI debe ser 1234567890");
    }
    
    @Test
    public void testActualizarCliente() throws SQLException {
        // Arrange
        // Insertar un cliente directamente en la base de datos
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO cliente (nombre, apellido, dni, telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmt.setString(1, "Carlos");
        pstmt.setString(2, "Gómez");
        pstmt.setString(3, "5555555555");
        pstmt.setString(4, "0955555555");
        pstmt.setString(5, "carlos@example.com");
        pstmt.setString(6, "Calle 456");
        pstmt.executeUpdate();
        
        // Obtener el ID generado
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int clienteId = -1;
        if (generatedKeys.next()) {
            clienteId = generatedKeys.getInt(1);
        }
        pstmt.close();
        conn.close();
        
        // Crear un objeto Cliente con los datos actualizados
        Cliente clienteActualizar = new Cliente("Carlos", "González", "5555555555", "0977777777", "carlos.nuevo@example.com", "Avenida 789");
        clienteActualizar.setIdCliente(clienteId);
        
        // Act
        boolean resultado = clienteDAO.actualizarCliente(clienteActualizar);
        
        // Assert
        assertTrue(resultado, "Debe retornar true si el cliente se actualizó correctamente");
        
        // Verificar que el cliente se actualizó consultándolo de nuevo
        Cliente clienteConsultado = clienteDAO.consultarCliente(clienteId);
        assertEquals("González", clienteConsultado.getApellido(), "El apellido debe haberse actualizado a González");
        assertEquals("0977777777", clienteConsultado.getTelefono(), "El teléfono debe haberse actualizado");
        assertEquals("carlos.nuevo@example.com", clienteConsultado.getEmail(), "El email debe haberse actualizado");
    }
    
    @Test
    public void testEliminarCliente() throws SQLException {
        // Arrange
        // Insertar un cliente directamente en la base de datos
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO cliente (nombre, apellido, dni, telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmt.setString(1, "Ana");
        pstmt.setString(2, "Martínez");
        pstmt.setString(3, "9999999999");
        pstmt.setString(4, "0999999999");
        pstmt.setString(5, "ana@example.com");
        pstmt.setString(6, "Calle 789");
        pstmt.executeUpdate();
        
        // Obtener el ID generado
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int clienteId = -1;
        if (generatedKeys.next()) {
            clienteId = generatedKeys.getInt(1);
        }
        pstmt.close();
        conn.close();
        
        // Act
        boolean resultado = clienteDAO.eliminarCliente(clienteId);
        
        // Assert
        assertTrue(resultado, "Debe retornar true si el cliente se eliminó correctamente");
        
        // Verificar que el cliente se eliminó consultándolo de nuevo
        Cliente clienteConsultado = clienteDAO.consultarCliente(clienteId);
        assertNull(clienteConsultado, "El cliente consultado debe ser null porque fue eliminado");
    }
    
    @Test
    public void testListarClientes() throws SQLException {
        // Arrange
        // Insertar varios clientes directamente en la base de datos
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO cliente (nombre, apellido, dni, telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?)"
        );
        
        // Cliente 1
        pstmt.setString(1, "Luis");
        pstmt.setString(2, "García");
        pstmt.setString(3, "1111111111");
        pstmt.setString(4, "0911111111");
        pstmt.setString(5, "luis@example.com");
        pstmt.setString(6, "Calle A");
        pstmt.executeUpdate();
        
        // Cliente 2
        pstmt.setString(1, "Marta");
        pstmt.setString(2, "Rodríguez");
        pstmt.setString(3, "2222222222");
        pstmt.setString(4, "0922222222");
        pstmt.setString(5, "marta@example.com");
        pstmt.setString(6, "Calle B");
        pstmt.executeUpdate();
        
        pstmt.close();
        conn.close();
        
        // Act
        List<Cliente> clientes = clienteDAO.listarClientes();
        
        // Assert
        assertNotNull(clientes, "La lista de clientes no debe ser null");
        assertEquals(2, clientes.size(), "Deben haber 2 clientes en la lista");
    }
    
    @Test
    public void testAgregarClienteConDniDuplicado() {
        // Arrange
        Cliente cliente1 = new Cliente("Juan", "Pérez", "0987654321", "0998765432", "juan@example.com", "Calle Principal 123");
        Cliente cliente2 = new Cliente("Pedro", "López", "0987654321", "0998765433", "pedro@example.com", "Calle Secundaria 456");
        
        // Act
        boolean resultado1 = clienteDAO.agregarCliente(cliente1);
        boolean resultado2 = clienteDAO.agregarCliente(cliente2);
        
        // Assertd
        assertTrue(resultado1, "El primer cliente debe agregarse correctamente");
        assertFalse(resultado2, "El segundo cliente no debe agregarse porque el DNI está duplicado");
    }
    
    @AfterEach
    public void tearDownEach() throws SQLException {
        // Limpiar datos después de cada prueba
        conn = TestConexionMySQL.getConnection();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM factura");
        stmt.executeUpdate("DELETE FROM reserva");
        stmt.executeUpdate("DELETE FROM cliente");
        stmt.close();
        conn.close();
    }
}
