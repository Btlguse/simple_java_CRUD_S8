package modelo;

import util.ConexionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    // cRUD operations Reserva
    
    /**
     * Obtiene una conexión a la base de datos.
     * Este método puede ser sobrescrito en pruebas para proporcionar una conexión mock o a H2.
     */
    protected Connection getConnection() throws SQLException {
        return ConexionMySQL.getConnection();
    }
    public boolean agregarReserva(Reserva reserva) {
        String sql = "INSERT INTO reserva (id_cliente, fecha_reserva, destino, fecha_viaje, precio, estado) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reserva.getIdCliente());
            pstmt.setDate(2, new java.sql.Date(reserva.getFechaReserva().getTime()));
            pstmt.setString(3, reserva.getDestino());
            pstmt.setDate(4, new java.sql.Date(reserva.getFechaViaje().getTime()));
            pstmt.setDouble(5, reserva.getPrecio());
            pstmt.setString(6, reserva.getEstado());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Reserva consultarReserva(int idReserva) {
        String sql = "SELECT * FROM reserva WHERE id_reserva = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idReserva);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Reserva reserva = new Reserva();
                    reserva.setIdReserva(rs.getInt("id_reserva"));
                    reserva.setIdCliente(rs.getInt("id_cliente"));
                    reserva.setFechaReserva(rs.getDate("fecha_reserva"));
                    reserva.setDestino(rs.getString("destino"));
                    reserva.setFechaViaje(rs.getDate("fecha_viaje"));
                    reserva.setPrecio(rs.getDouble("precio"));
                    reserva.setEstado(rs.getString("estado"));
                    return reserva;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean actualizarReserva(Reserva reserva) {
        String sql = "UPDATE reserva SET id_cliente = ?, fecha_reserva = ?, destino = ?, fecha_viaje = ?, precio = ?, estado = ? WHERE id_reserva = ?";
        
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reserva.getIdCliente());
            pstmt.setDate(2, new java.sql.Date(reserva.getFechaReserva().getTime()));
            pstmt.setString(3, reserva.getDestino());
            pstmt.setDate(4, new java.sql.Date(reserva.getFechaViaje().getTime()));
            pstmt.setDouble(5, reserva.getPrecio());
            pstmt.setString(6, reserva.getEstado());
            pstmt.setInt(7, reserva.getIdReserva());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarReserva(int idReserva) {
        String sql = "DELETE FROM reserva WHERE id_reserva = ?";
        
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idReserva);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Reserva> listarReservas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reserva";
        
        try (Connection conn = ConexionMySQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setIdReserva(rs.getInt("id_reserva"));
                reserva.setIdCliente(rs.getInt("id_cliente"));
                reserva.setFechaReserva(rs.getDate("fecha_reserva"));
                reserva.setDestino(rs.getString("destino"));
                reserva.setFechaViaje(rs.getDate("fecha_viaje"));
                reserva.setPrecio(rs.getDouble("precio"));
                reserva.setEstado(rs.getString("estado"));
                reservas.add(reserva);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }
    
    public List<Reserva> listarReservasPorCliente(int idCliente) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reserva WHERE id_cliente = ?";
        
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCliente);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reserva reserva = new Reserva();
                    reserva.setIdReserva(rs.getInt("id_reserva"));
                    reserva.setIdCliente(rs.getInt("id_cliente"));
                    reserva.setFechaReserva(rs.getDate("fecha_reserva"));
                    reserva.setDestino(rs.getString("destino"));
                    reserva.setFechaViaje(rs.getDate("fecha_viaje"));
                    reserva.setPrecio(rs.getDouble("precio"));
                    reserva.setEstado(rs.getString("estado"));
                    reservas.add(reserva);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }


    public boolean agregarReservaConFactura(Reserva reserva) {
    String sqlReserva = "INSERT INTO reserva (id_cliente, fecha_reserva, destino, fecha_viaje, precio, estado) VALUES (?, ?, ?, ?, ?, ?)";
    String sqlFactura = "INSERT INTO factura (id_reserva, fecha_emision, monto_total, estado) VALUES (?, ?, ?, ?)";
    
    Connection conn = null;
    try {
        conn = ConexionMySQL.getConnection();
        conn.setAutoCommit(false); // Iniciar transacción
        
        // Insertar reserva
        try (PreparedStatement pstmtReserva = conn.prepareStatement(sqlReserva, Statement.RETURN_GENERATED_KEYS)) {
            pstmtReserva.setInt(1, reserva.getIdCliente());
            pstmtReserva.setDate(2, new java.sql.Date(reserva.getFechaReserva().getTime()));
            pstmtReserva.setString(3, reserva.getDestino());
            pstmtReserva.setDate(4, new java.sql.Date(reserva.getFechaViaje().getTime()));
            pstmtReserva.setDouble(5, reserva.getPrecio());
            pstmtReserva.setString(6, reserva.getEstado());
            
            int affectedRows = pstmtReserva.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear la reserva");
            }
            
            // Obtener el ID de la reserva generada
            try (ResultSet generatedKeys = pstmtReserva.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idReserva = generatedKeys.getInt(1);
                    
                    // Insertar factura
                    try (PreparedStatement pstmtFactura = conn.prepareStatement(sqlFactura)) {
                        pstmtFactura.setInt(1, idReserva);
                        pstmtFactura.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                        pstmtFactura.setDouble(3, reserva.getPrecio()* 1.12); // Assuming 12% IVA
                        pstmtFactura.setString(4, "Pendiente");
                        
                        pstmtFactura.executeUpdate();
                    }
                } else {
                    throw new SQLException("Error al obtener el ID de la reserva");
                }
            }
        }
        
        conn.commit(); // Confirmar transacción
        return true;
        
    } catch (SQLException e) {
        try {
            if (conn != null) {
                conn.rollback(); // Revertir transacción en caso de error
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        e.printStackTrace();
        return false;
    } finally {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }
    }

    public boolean eliminarReservaConFactura(int idReserva) {
        String sqlFactura = "DELETE FROM factura WHERE id_reserva = ?";
        String sqlReserva = "DELETE FROM reserva WHERE id_reserva = ?";
        
        Connection conn = null;
        try {
            conn = ConexionMySQL.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Primero eliminar la factura asociada
            try (PreparedStatement pstmtFactura = conn.prepareStatement(sqlFactura)) {
                pstmtFactura.setInt(1, idReserva);
                pstmtFactura.executeUpdate(); // No verificamos si hay filas afectadas porque puede no existir factura
            }
            
            // Luego eliminar la reserva
            try (PreparedStatement pstmtReserva = conn.prepareStatement(sqlReserva)) {
                pstmtReserva.setInt(1, idReserva);
                int affectedRows = pstmtReserva.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("La reserva no existe o no se pudo eliminar");
                }
            }
            
            conn.commit(); // Confirmar transacción
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Revertir transacción en caso de error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
