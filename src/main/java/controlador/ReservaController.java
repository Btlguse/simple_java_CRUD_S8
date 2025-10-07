package controlador;

import modelo.Reserva;
import modelo.ReservaDAO;
import java.util.Date;
import java.util.List;

public class ReservaController {
    private ReservaDAO reservaDAO;
    
    public ReservaController() {
        this.reservaDAO = new ReservaDAO();
    }
    
    public boolean agregarReserva(int idCliente, Date fechaReserva, String destino, Date fechaViaje, double precio, String estado) {
        Reserva reserva = new Reserva(idCliente, fechaReserva, destino, fechaViaje, precio, estado);
        return reservaDAO.agregarReservaConFactura(reserva);
    }
    
    public Reserva consultarReserva(int idReserva) {
        return reservaDAO.consultarReserva(idReserva);
    }
    
    public boolean actualizarReserva(int idReserva, int idCliente, Date fechaReserva, String destino, Date fechaViaje, double precio, String estado) {
        Reserva reserva = new Reserva(idCliente, fechaReserva, destino, fechaViaje, precio, estado);
        reserva.setIdReserva(idReserva);
        return reservaDAO.actualizarReserva(reserva);
    }
    
    public boolean eliminarReserva(int idReserva) {
        return reservaDAO.eliminarReservaConFactura(idReserva);
    }
    
    public List<Reserva> listarReservas() {
        return reservaDAO.listarReservas();
    }
    
    public List<Reserva> listarReservasPorCliente(int idCliente) {
        return reservaDAO.listarReservasPorCliente(idCliente);
    }

    
}