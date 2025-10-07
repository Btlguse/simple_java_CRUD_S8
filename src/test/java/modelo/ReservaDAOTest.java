package modelo;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.util.Date;
import java.util.List;
import util.TestConexionMySQL;

/**
 * Pruebas unitarias para la clase ReservaDAO.
 * Prueba las operaciones CRUD y el manejo de excepciones.
 */
public class ReservaDAOTest {
    
    private static TestReservaDAO reservaDAO;
    private static TestClienteDAO clienteDAO;
    private static Connection conn;
    private static int clienteId;
    
    @BeforeAll
    public static void setUp() {
        // Inicializar los DAOs para las pruebas
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
        
        // Insertar un cliente para las pruebas de reserva
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO cliente (nombre, apellido, dni, telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmt.setString(1, "Cliente");
        pstmt.setString(2, "Prueba");
        pstmt.setString(3, "1234567890");
        pstmt.setString(4, "0987654321");
        pstmt.setString(5, "cliente@prueba.com");
        pstmt.setString(6, "Calle Test 123");
        pstmt.executeUpdate();
        
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            clienteId = generatedKeys.getInt(1);
        }
        
        pstmt.close();
        stmt.close();
        conn.close();
    }
    
    @Test
    public void testAgregarReserva() {
        // Arrange
        Date fechaActual = new Date();
        Date fechaViaje = new Date(fechaActual.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva(clienteId, fechaActual, "Cancún", fechaViaje, 1200.00, "PENDIENTE");
        
        // Act
        boolean resultado = reservaDAO.agregarReserva(reserva);
        
        // Assert
        assertTrue(resultado, "Debe retornar true si la reserva se agregó correctamente");
        
        // Verificar que la reserva se agregó consultando la base de datos
        List<Reserva> reservas = reservaDAO.listarReservasPorCliente(clienteId);
        assertFalse(reservas.isEmpty(), "La lista de reservas no debe estar vacía");
        assertEquals("Cancún", reservas.get(0).getDestino(), "El destino de la reserva debe ser Cancún");
        assertEquals(1200.00, reservas.get(0).getPrecio(), 0.01, "El precio de la reserva debe ser 1200.00");
    }
    
    @Test
    public void testConsultarReserva() throws SQLException {
        // Arrange
        Date fechaActual = new Date();
        Date fechaViaje = new Date(fechaActual.getTime() + 86400000); // Un día después
        
        // Insertar una reserva directamente en la base de datos
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO reserva (id_cliente, fecha_reserva, destino, fecha_viaje, precio, estado) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmt.setInt(1, clienteId);
        pstmt.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmt.setString(3, "Madrid");
        pstmt.setDate(4, new java.sql.Date(fechaViaje.getTime()));
        pstmt.setDouble(5, 1500.00);
        pstmt.setString(6, "CONFIRMADA");
        pstmt.executeUpdate();
        
        // Obtener el ID generado
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int reservaId = -1;
        if (generatedKeys.next()) {
            reservaId = generatedKeys.getInt(1);
        }
        pstmt.close();
        conn.close();
        
        // Act
        Reserva reservaConsultada = reservaDAO.consultarReserva(reservaId);
        
        // Assert
        assertNotNull(reservaConsultada, "La reserva consultada no debe ser null");
        assertEquals("Madrid", reservaConsultada.getDestino(), "El destino debe ser Madrid");
        assertEquals("CONFIRMADA", reservaConsultada.getEstado(), "El estado debe ser CONFIRMADA");
        assertEquals(1500.00, reservaConsultada.getPrecio(), 0.01, "El precio debe ser 1500.00");
    }
    
    @Test
    public void testActualizarReserva() throws SQLException {
        // Arrange
        Date fechaActual = new Date();
        Date fechaViaje = new Date(fechaActual.getTime() + 86400000); // Un día después
        
        // Insertar una reserva directamente en la base de datos
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO reserva (id_cliente, fecha_reserva, destino, fecha_viaje, precio, estado) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmt.setInt(1, clienteId);
        pstmt.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmt.setString(3, "París");
        pstmt.setDate(4, new java.sql.Date(fechaViaje.getTime()));
        pstmt.setDouble(5, 2000.00);
        pstmt.setString(6, "PENDIENTE");
        pstmt.executeUpdate();
        
        // Obtener el ID generado
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int reservaId = -1;
        if (generatedKeys.next()) {
            reservaId = generatedKeys.getInt(1);
        }
        pstmt.close();
        conn.close();
        
        // Crear un objeto Reserva con los datos actualizados
        Reserva reservaActualizar = new Reserva();
        reservaActualizar.setIdReserva(reservaId);
        reservaActualizar.setIdCliente(clienteId);
        reservaActualizar.setFechaReserva(fechaActual);
        reservaActualizar.setDestino("Roma");
        reservaActualizar.setFechaViaje(fechaViaje);
        reservaActualizar.setPrecio(2200.00);
        reservaActualizar.setEstado("CONFIRMADA");
        
        // Act
        boolean resultado = reservaDAO.actualizarReserva(reservaActualizar);
        
        // Assert
        assertTrue(resultado, "Debe retornar true si la reserva se actualizó correctamente");
        
        // Verificar que la reserva se actualizó consultándola de nuevo
        Reserva reservaConsultada = reservaDAO.consultarReserva(reservaId);
        assertEquals("Roma", reservaConsultada.getDestino(), "El destino debe haberse actualizado a Roma");
        assertEquals("CONFIRMADA", reservaConsultada.getEstado(), "El estado debe haberse actualizado a CONFIRMADA");
        assertEquals(2200.00, reservaConsultada.getPrecio(), 0.01, "El precio debe haberse actualizado a 2200.00");
    }
    
    @Test
    public void testEliminarReserva() throws SQLException {
        // Arrange
        Date fechaActual = new Date();
        Date fechaViaje = new Date(fechaActual.getTime() + 86400000); // Un día después
        
        // Insertar una reserva directamente en la base de datos
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO reserva (id_cliente, fecha_reserva, destino, fecha_viaje, precio, estado) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        pstmt.setInt(1, clienteId);
        pstmt.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmt.setString(3, "Tokio");
        pstmt.setDate(4, new java.sql.Date(fechaViaje.getTime()));
        pstmt.setDouble(5, 3000.00);
        pstmt.setString(6, "PENDIENTE");
        pstmt.executeUpdate();
        
        // Obtener el ID generado
        ResultSet generatedKeys = pstmt.getGeneratedKeys();
        int reservaId = -1;
        if (generatedKeys.next()) {
            reservaId = generatedKeys.getInt(1);
        }
        pstmt.close();
        conn.close();
        
        // Act
        boolean resultado = reservaDAO.eliminarReserva(reservaId);
        
        // Assert
        assertTrue(resultado, "Debe retornar true si la reserva se eliminó correctamente");
        
        // Verificar que la reserva se eliminó consultándola de nuevo
        Reserva reservaConsultada = reservaDAO.consultarReserva(reservaId);
        assertNull(reservaConsultada, "La reserva consultada debe ser null porque fue eliminada");
    }
    
    @Test
    public void testListarReservasPorCliente() throws SQLException {
        // Arrange
        Date fechaActual = new Date();
        Date fechaViaje = new Date(fechaActual.getTime() + 86400000); // Un día después
        
        // Insertar varias reservas para el mismo cliente
        conn = TestConexionMySQL.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO reserva (id_cliente, fecha_reserva, destino, fecha_viaje, precio, estado) VALUES (?, ?, ?, ?, ?, ?)"
        );
        
        // Reserva 1
        pstmt.setInt(1, clienteId);
        pstmt.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmt.setString(3, "Barcelona");
        pstmt.setDate(4, new java.sql.Date(fechaViaje.getTime()));
        pstmt.setDouble(5, 1000.00);
        pstmt.setString(6, "PENDIENTE");
        pstmt.executeUpdate();
        
        // Reserva 2
        pstmt.setInt(1, clienteId);
        pstmt.setDate(2, new java.sql.Date(fechaActual.getTime()));
        pstmt.setString(3, "Berlín");
        pstmt.setDate(4, new java.sql.Date(fechaViaje.getTime() + 86400000)); // Dos días después
        pstmt.setDouble(5, 1100.00);
        pstmt.setString(6, "CONFIRMADA");
        pstmt.executeUpdate();
        
        pstmt.close();
        conn.close();
        
        // Act
        List<Reserva> reservas = reservaDAO.listarReservasPorCliente(clienteId);
        
        // Assert
        assertNotNull(reservas, "La lista de reservas no debe ser null");
        assertEquals(2, reservas.size(), "Deben haber 2 reservas en la lista");
    }
    
    @Test
    public void testAgregarReservaConFactura() {
        // Arrange
        Date fechaActual = new Date();
        Date fechaViaje = new Date(fechaActual.getTime() + 86400000); // Un día después
        Reserva reserva = new Reserva(clienteId, fechaActual, "Miami", fechaViaje, 1800.00, "PENDIENTE");
        
        // Act
        boolean resultado = reservaDAO.agregarReservaConFactura(reserva);
        
        // Assert
        assertTrue(resultado, "Debe retornar true si la reserva y la factura se agregaron correctamente");
        
        // Verificar que la reserva se agregó
        List<Reserva> reservas = reservaDAO.listarReservasPorCliente(clienteId);
        assertFalse(reservas.isEmpty(), "La lista de reservas no debe estar vacía");
        
        // Verificar que se creó una factura asociada a la reserva
        int reservaId = reservas.get(0).getIdReserva();
        
        try {
            conn = TestConexionMySQL.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM factura WHERE id_reserva = ?");
            pstmt.setInt(1, reservaId);
            ResultSet rs = pstmt.executeQuery();
            
            assertTrue(rs.next(), "Debe existir una factura asociada a la reserva");
            assertEquals(1800.00 * 1.12, rs.getDouble("monto_total"), 0.01, "El monto total debe ser el precio más el 12% de IVA");
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            fail("No debería lanzar una excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testEliminarReservaConFactura() throws SQLException {
        // Arrange
        Date fechaActual = new Date();
        Date fechaViaje = new Date(fechaActual.getTime() + 86400000); // Un día después
        
        // Crear una reserva con factura usando el método correspondiente
        Reserva reserva = new Reserva(clienteId, fechaActual, "Lisboa", fechaViaje, 1600.00, "PENDIENTE");
        reservaDAO.agregarReservaConFactura(reserva);
        
        // Obtener el ID de la reserva creada
        List<Reserva> reservas = reservaDAO.listarReservasPorCliente(clienteId);
        int reservaId = reservas.get(0).getIdReserva();
        
        // Act
        boolean resultado = reservaDAO.eliminarReservaConFactura(reservaId);
        
        // Assert
        assertTrue(resultado, "Debe retornar true si la reserva y la factura se eliminaron correctamente");
        
        // Verificar que la reserva se eliminó
        Reserva reservaConsultada = reservaDAO.consultarReserva(reservaId);
        assertNull(reservaConsultada, "La reserva debe haberse eliminado");
        
        // Verificar que la factura también se eliminó
        try {
            conn = TestConexionMySQL.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM factura WHERE id_reserva = ?");
            pstmt.setInt(1, reservaId);
            ResultSet rs = pstmt.executeQuery();
            
            assertFalse(rs.next(), "No debe existir una factura asociada a la reserva eliminada");
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            fail("No debería lanzar una excepción: " + e.getMessage());
        }
    }
    
    @Test
    public void testManejoExcepcionClienteNoExistente() {
        // Arrange
        Date fechaActual = new Date();
        Date fechaViaje = new Date(fechaActual.getTime() + 86400000); // Un día después
        
        // Intentar crear una reserva para un cliente que no existe (ID inexistente)
        Reserva reserva = new Reserva(99999, fechaActual, "Sydney", fechaViaje, 2500.00, "PENDIENTE");
        
        // Act & Assert
        // La base de datos MySQL debería rechazar esta operación debido a la restricción de clave foránea
        boolean resultado = reservaDAO.agregarReserva(reserva);
        assertFalse(resultado, "Debe retornar false porque el cliente no existe");
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
