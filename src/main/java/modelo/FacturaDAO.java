package modelo;

import util.ConexionMySQL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {
    
    protected Connection getConnection() throws SQLException {
        return ConexionMySQL.getConnection();
    }
    
    public boolean agregarFactura(Factura factura) {
        String sql = "INSERT INTO factura (id_reserva, fecha_emision, monto_total, estado) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, factura.getIdReserva());
            pstmt.setDate(2, new java.sql.Date(factura.getFechaEmision().getTime()));
            pstmt.setDouble(3, factura.getMontoTotal());
            pstmt.setString(4, factura.getEstadoPago());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Factura consultarFactura(int idFactura) {
        String sql = "SELECT * FROM factura WHERE id_factura = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idFactura);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Factura factura = new Factura();
                    factura.setIdFactura(rs.getInt("id_factura"));
                    factura.setIdReserva(rs.getInt("id_reserva"));
                    factura.setFechaEmision(rs.getDate("fecha_emision"));
                    factura.setMontoTotal(rs.getDouble("monto_total"));
                    factura.setEstadoPago(rs.getString("estado"));
                    return factura;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean actualizarFactura(Factura factura) {
        String sql = "UPDATE factura SET id_reserva = ?, fecha_emision = ?, monto_total = ?, estado = ? WHERE id_factura = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, factura.getIdReserva());
            pstmt.setDate(2, new java.sql.Date(factura.getFechaEmision().getTime()));
            pstmt.setDouble(3, factura.getMontoTotal());
            pstmt.setString(4, factura.getEstadoPago());
            pstmt.setInt(5, factura.getIdFactura());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarFactura(int idFactura) {
        String sql = "DELETE FROM factura WHERE id_factura = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idFactura);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Factura> listarFacturas() {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM factura";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Factura factura = new Factura();
                factura.setIdFactura(rs.getInt("id_factura"));
                factura.setIdReserva(rs.getInt("id_reserva"));
                factura.setFechaEmision(rs.getDate("fecha_emision"));
                factura.setMontoTotal(rs.getDouble("monto_total"));
                factura.setEstadoPago(rs.getString("estado"));
                facturas.add(factura);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facturas;
    }

    public List<Factura> listarFacturasCliente(int idCliente) {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT f.* FROM factura f JOIN reserva r ON f.id_reserva = r.id_reserva WHERE r.id_cliente = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCliente);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Factura factura = new Factura();
                    factura.setIdFactura(rs.getInt("id_factura"));
                    factura.setIdReserva(rs.getInt("id_reserva"));
                    factura.setFechaEmision(rs.getDate("fecha_emision"));
                    factura.setMontoTotal(rs.getDouble("monto_total"));
                    factura.setEstadoPago(rs.getString("estado"));
                    facturas.add(factura);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facturas;
    }

    public Factura consultarFacturaPorReserva(int idReserva) {
        String sql = "SELECT * FROM factura WHERE id_reserva = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idReserva);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Factura factura = new Factura();
                    factura.setIdFactura(rs.getInt("id_factura"));
                    factura.setIdReserva(rs.getInt("id_reserva"));
                    factura.setFechaEmision(rs.getDate("fecha_emision"));
                    factura.setMontoTotal(rs.getDouble("monto_total"));
                    factura.setEstadoPago(rs.getString("estado"));
                    return factura;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}