package controlador;

import modelo.Factura;
import modelo.FacturaDAO;
import java.util.Date;
import java.util.List;

public class FacturaController {
    private FacturaDAO facturaDAO;
    
    public FacturaController() {
        this.facturaDAO = new FacturaDAO();
    }
    
    public boolean agregarFactura(int idReserva, Date fechaEmision, double montoTotal, String estadoPago) {
        Factura factura = new Factura(idReserva, fechaEmision, montoTotal, estadoPago);
        return facturaDAO.agregarFactura(factura);
    }
    
    public Factura consultarFactura(int idFactura) {
        return facturaDAO.consultarFactura(idFactura);
    }
    
    public boolean actualizarFactura(int idFactura, int idReserva, Date fechaEmision, double montoTotal, String estadoPago) {
        Factura factura = new Factura(idReserva, fechaEmision, montoTotal, estadoPago);
        factura.setIdFactura(idFactura);
        return facturaDAO.actualizarFactura(factura);
    }
    
    public boolean eliminarFactura(int idFactura) {
        return facturaDAO.eliminarFactura(idFactura);
    }
    
    public List<Factura> listarFacturas() {
        return facturaDAO.listarFacturas();
    }

    public List<Factura> listarFacturasCliente(int idCliente) {
        return facturaDAO.listarFacturasCliente(idCliente);
    }

    public Factura consultarFacturaPorReserva(int idReserva) {
        return facturaDAO.consultarFacturaPorReserva(idReserva);
    }
}