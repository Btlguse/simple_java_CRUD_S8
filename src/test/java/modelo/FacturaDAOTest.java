package modelo;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.Date;
import java.util.List;
import util.TestConexionMySQL;

/**
 * Pruebas unitarias para la clase FacturaDAO.
 * Prueba las operaciones CRUD y el manejo de excepciones.
 */
public class FacturaDAOTest {
    
    private static TestFacturaDAO facturaDAO;
    private static TestReservaDAO reservaDAO;
    private static TestClienteDAO clienteDAO;
    private static Connection conn;
    private static int clienteId;
    private static int reservaId;
    
    @BeforeAll
    public static void setUp() {
        // Inicializar los DAOs para las pruebas
        facturaDAO = new TestFacturaDAO();
        reservaDAO = new TestReservaDAO();
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
        
        // Insertar un cliente para las pruebas
        PreparedStatement pstmtCliente = conn.prepareStatement(
            "INSERT INTO cliente (nombre, apellido, dni, telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmtCliente.setString(1, "Cliente");
        pstmtCliente.setString(2, "Factura");
        pstmtCliente.setString(3, "1122334455");
        pstmtCliente.setString(4, "0987654321");
        pstmtCliente.setString(5, "cliente@factura.com");
        pstmtCliente.setString(6, "Calle Factura 123");
        pstmtCliente.executeUpdate();
        
        ResultSet generatedKeysCliente = pstmtCliente.getGeneratedKeys();
        if (generatedKeysCliente.next()) {
            clienteId = generatedKeysCliente.getInt(1);
        }
        pstmtCliente.close();
        
        // Insertar una reserva para las pruebas de factura
        Date fechaActual = new Date();
        Date fechaViaje = new Date(fechaActual.getTime() + 86400000); // Un día después
        
        PreparedStatement pstmtReserva = conn.prepareStatement(
            "INSERT INTO reserva (id_cliente, fecha_reserva, destino, fecha_viaje, precio, estado) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmtReserva.setInt(1, clienteId);
        pstmtReserva.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmtReserva.setString(3, "Nueva York");
        pstmtReserva.setDate(4, new java.sql.Date(fechaViaje.getTime()));
        pstmtReserva.setDouble(5, 2000.00);
        pstmtReserva.setString(6, "CONFIRMADA");
        pstmtReserva.executeUpdate();
        
        ResultSet generatedKeysReserva = pstmtReserva.getGeneratedKeys();
        if (generatedKeysReserva.next()) {
            reservaId = generatedKeysReserva.getInt(1);
        }
        
        pstmtReserva.close();
        stmt.close();
        conn.close();
    }
    
    @Test
    public void testAgregarFactura() {
        // Arrange
        Date fechaActual = new Date();
        Factura factura = new Factura(reservaId, fechaActual, 2000.00, "PENDIENTE");

        // Act
        boolean resultado = facturaDAO.agregarFactura(factura);

        // Assert
        assertTrue(resultado, "Debe retornar true si la factura se agregó correctamente");

        // Verificar que la factura se agregó consultando la base de datos
        Factura facturaConsultada = facturaDAO.consultarFacturaPorReserva(reservaId);
        assertNotNull(facturaConsultada, "La factura consultada no debe ser null");
        assertEquals(reservaId, facturaConsultada.getIdReserva(), "El ID de la reserva debe coincidir");
        assertEquals(2240.00, facturaConsultada.getMontoTotal(), 0.01, "El monto total debe ser 2240.00 (2000 + 12% IVA)");
    }
    
    @Test
    public void testConsultarFactura() throws SQLException {
        // Arrange
        Date fechaActual = new Date();
        
        // Insertar una factura directamente en la base de datos
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO factura (id_reserva, fecha_emision, monto_total, estado) VALUES (?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmt.setInt(1, reservaId);
        pstmt.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmt.setDouble(3, 2240.00); // 2000 + 12% IVA
        pstmt.setString(4, "PENDIENTE");
        pstmt.executeUpdate();
        
        // Obtener el ID generado
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int facturaId = -1;
        if (generatedKeys.next()) {
            facturaId = generatedKeys.getInt(1);
        }
        pstmt.close();
        conn.close();
        
        // Act
        Factura facturaConsultada = facturaDAO.consultarFactura(facturaId);
        
        // Assert
        assertNotNull(facturaConsultada, "La factura consultada no debe ser null");
        assertEquals(reservaId, facturaConsultada.getIdReserva(), "El ID de la reserva debe coincidir");
        assertEquals(2240.00, facturaConsultada.getMontoTotal(), 0.01, "El monto total debe ser 2240.00");
        assertEquals("PENDIENTE", facturaConsultada.getEstadoPago(), "El estado debe ser PENDIENTE");
    }
    
    @Test
    public void testActualizarFactura() throws SQLException {
        // Arrange
        Date fechaActual = new Date();
        
        // Insertar una factura directamente en la base de datos
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO factura (id_reserva, fecha_emision, monto_total, estado) VALUES (?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmt.setInt(1, reservaId);
        pstmt.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmt.setDouble(3, 2240.00); // 2000 + 12% IVA
        pstmt.setString(4, "PENDIENTE");
        pstmt.executeUpdate();
        
        // Obtener el ID generado
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int facturaId = -1;
        if (generatedKeys.next()) {
            facturaId = generatedKeys.getInt(1);
        }
        pstmt.close();
        conn.close();
        
        // Crear un objeto Factura con los datos actualizados
        Factura facturaActualizar = new Factura();
        facturaActualizar.setIdFactura(facturaId);
        facturaActualizar.setIdReserva(reservaId);
        facturaActualizar.setFechaEmision(fechaActual);
        facturaActualizar.setMontoTotal(2240.00);
        facturaActualizar.setEstadoPago("PAGADA");
        
        // Act
        boolean resultado = facturaDAO.actualizarFactura(facturaActualizar);
        
        // Assert
        assertTrue(resultado, "Debe retornar true si la factura se actualizó correctamente");
        
        // Verificar que la factura se actualizó consultándola de nuevo
        Factura facturaConsultada = facturaDAO.consultarFactura(facturaId);
        assertEquals("PAGADA", facturaConsultada.getEstadoPago(), "El estado debe haberse actualizado a PAGADA");
    }
    
    @Test
    public void testEliminarFactura() throws SQLException {
        // Arrange
        Date fechaActual = new Date();
        
        // Insertar una factura directamente en la base de datos
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO factura (id_reserva, fecha_emision, monto_total, estado) VALUES (?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmt.setInt(1, reservaId);
        pstmt.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmt.setDouble(3, 2240.00);
        pstmt.setString(4, "PENDIENTE");
        pstmt.executeUpdate();
        
        // Obtener el ID generado
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int facturaId = -1;
        if (generatedKeys.next()) {
            facturaId = generatedKeys.getInt(1);
        }
        pstmt.close();
        conn.close();
        
        // Act
        boolean resultado = facturaDAO.eliminarFactura(facturaId);
        
        // Assert
        assertTrue(resultado, "Debe retornar true si la factura se eliminó correctamente");
        
        // Verificar que la factura se eliminó consultándola de nuevo
        Factura facturaConsultada = facturaDAO.consultarFactura(facturaId);
        assertNull(facturaConsultada, "La factura consultada debe ser null porque fue eliminada");
    }
    
    @Test
    public void testListarFacturas() throws SQLException {
        // Arrange
        Date fechaActual = new Date();
        
        // Insertar varias facturas directamente en la base de datos
        conn = TestConexionMySQL.getConnection();
        
        // Crear una segunda reserva para tener dos facturas
        PreparedStatement pstmtReserva = conn.prepareStatement(
            "INSERT INTO reserva (id_cliente, fecha_reserva, destino, fecha_viaje, precio, estado) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmtReserva.setInt(1, clienteId);
        pstmtReserva.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmtReserva.setString(3, "Chicago");
        pstmtReserva.setDate(4, new java.sql.Date(fechaActual.getTime() + 172800000)); // Dos días después
        pstmtReserva.setDouble(5, 1800.00);
        pstmtReserva.setString(6, "CONFIRMADA");
        pstmtReserva.executeUpdate();
        
        ResultSet generatedKeysReserva = pstmtReserva.getGeneratedKeys();
        int segundaReservaId = -1;
        if (generatedKeysReserva.next()) {
            segundaReservaId = generatedKeysReserva.getInt(1);
        }
        pstmtReserva.close();
        
        // Insertar factura para la primera reserva
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO factura (id_reserva, fecha_emision, monto_total, estado) VALUES (?, ?, ?, ?)"
        );
        pstmt.setInt(1, reservaId);
        pstmt.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmt.setDouble(3, 2240.00);
        pstmt.setString(4, "PENDIENTE");
        pstmt.executeUpdate();
        
        // Insertar factura para la segunda reserva
        pstmt.setInt(1, segundaReservaId);
        pstmt.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmt.setDouble(3, 2016.00); // 1800 + 12% IVA
        pstmt.setString(4, "PAGADA");
        pstmt.executeUpdate();
        
        pstmt.close();
        conn.close();
        
        // Act
        List<Factura> facturas = facturaDAO.listarFacturas();
        
        // Assert
        assertNotNull(facturas, "La lista de facturas no debe ser null");
        assertEquals(2, facturas.size(), "Deben haber 2 facturas en la lista");
    }
    
    @Test
    public void testListarFacturasCliente() throws SQLException {
        // Arrange
        Date fechaActual = new Date();
        
        // Insertar otro cliente y una reserva para ese cliente
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmtCliente = conn.prepareStatement(
            "INSERT INTO cliente (nombre, apellido, dni, telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmtCliente.setString(1, "Otro");
        pstmtCliente.setString(2, "Cliente");
        pstmtCliente.setString(3, "9988776655");
        pstmtCliente.setString(4, "0912345678");
        pstmtCliente.setString(5, "otro@cliente.com");
        pstmtCliente.setString(6, "Otra Calle 456");
        pstmtCliente.executeUpdate();
        
        ResultSet generatedKeysCliente = pstmtCliente.getGeneratedKeys();
        int otroClienteId = -1;
        if (generatedKeysCliente.next()) {
            otroClienteId = generatedKeysCliente.getInt(1);
        }
        pstmtCliente.close();
        
        // Crear reservas para ambos clientes
        PreparedStatement pstmtReserva = conn.prepareStatement(
            "INSERT INTO reserva (id_cliente, fecha_reserva, destino, fecha_viaje, precio, estado) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        
        // Reserva para el otro cliente
        pstmtReserva.setInt(1, otroClienteId);
        pstmtReserva.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmtReserva.setString(3, "Los Ángeles");
        pstmtReserva.setDate(4, new java.sql.Date(fechaActual.getTime() + 86400000));
        pstmtReserva.setDouble(5, 1500.00);
        pstmtReserva.setString(6, "PENDIENTE");
        pstmtReserva.executeUpdate();
        
        ResultSet generatedKeysReserva = pstmtReserva.getGeneratedKeys();
        int otraReservaId = -1;
        if (generatedKeysReserva.next()) {
            otraReservaId = generatedKeysReserva.getInt(1);
        }
        pstmtReserva.close();
        
        // Insertar facturas para ambas reservas
        PreparedStatement pstmtFactura = conn.prepareStatement(
            "INSERT INTO factura (id_reserva, fecha_emision, monto_total, estado) VALUES (?, ?, ?, ?)"
        );
        
        // Factura para la reserva del primer cliente
        pstmtFactura.setInt(1, reservaId);
        pstmtFactura.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmtFactura.setDouble(3, 2240.00);
        pstmtFactura.setString(4, "PENDIENTE");
        pstmtFactura.executeUpdate();
        
        // Factura para la reserva del segundo cliente
        pstmtFactura.setInt(1, otraReservaId);
        pstmtFactura.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmtFactura.setDouble(3, 1680.00); // 1500 + 12% IVA
        pstmtFactura.setString(4, "PENDIENTE");
        pstmtFactura.executeUpdate();
        
        pstmtFactura.close();
        conn.close();
        
        // Act
        List<Factura> facturasCliente1 = facturaDAO.listarFacturasCliente(clienteId);
        List<Factura> facturasCliente2 = facturaDAO.listarFacturasCliente(otroClienteId);
        
        // Assert
        assertNotNull(facturasCliente1, "La lista de facturas del cliente 1 no debe ser null");
        assertNotNull(facturasCliente2, "La lista de facturas del cliente 2 no debe ser null");
        assertEquals(1, facturasCliente1.size(), "Debe haber 1 factura para el cliente 1");
        assertEquals(1, facturasCliente2.size(), "Debe haber 1 factura para el cliente 2");
    }
    
    @Test
    public void testConsultarFacturaPorReserva() throws SQLException {
        // Arrange
        Date fechaActual = new Date();
        
        // Insertar una factura directamente en la base de datos
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO factura (id_reserva, fecha_emision, monto_total, estado) VALUES (?, ?, ?, ?)"
        );
        pstmt.setInt(1, reservaId);
        pstmt.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmt.setDouble(3, 2240.00);
        pstmt.setString(4, "PENDIENTE");
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
        
        // Act
        Factura facturaConsultada = facturaDAO.consultarFacturaPorReserva(reservaId);
        
        // Assert
        assertNotNull(facturaConsultada, "La factura consultada no debe ser null");
        assertEquals(reservaId, facturaConsultada.getIdReserva(), "El ID de la reserva debe coincidir");
        assertEquals(2240.00, facturaConsultada.getMontoTotal(), 0.01, "El monto total debe ser 2240.00");
        assertEquals("PENDIENTE", facturaConsultada.getEstadoPago(), "El estado debe ser PENDIENTE");
    }
    
    @Test
    public void testManejoExcepcionReservaNoExistente() {
        // Arrange
        Date fechaActual = new Date();
        // Intentar crear una factura para una reserva que no existe
        Factura factura = new Factura(99999, fechaActual, 1000.00, "PENDIENTE");
        
        // Act & Assert
        // La base de datos MySQL debería rechazar esta operación debido a la restricción de clave foránea
        boolean resultado = facturaDAO.agregarFactura(factura);
        assertFalse(resultado, "Debe retornar false porque la reserva no existe");
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
