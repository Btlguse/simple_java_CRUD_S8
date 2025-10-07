package modelo;

import java.util.Date;

public class Reserva {
    private int idReserva;
    private int idCliente;
    private Date fechaReserva;
    private String destino;
    private Date fechaViaje;
    private double precio;
    private String estado;
    
    // Constructors
    public Reserva() {
    }
    
    public Reserva(int idCliente, Date fechaReserva, String destino, Date fechaViaje, double precio, String estado) {
        this.idCliente = idCliente;
        this.fechaReserva = fechaReserva;
        this.destino = destino;
        this.fechaViaje = fechaViaje;
        this.precio = precio;
        this.estado = estado;
    }
    
    // Getters and setters
    public int getIdReserva() {
        return idReserva;
    }
    
    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }
    
    public int getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    public Date getFechaReserva() {
        return fechaReserva;
    }
    
    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }
    
    public String getDestino() {
        return destino;
    }
    
    public void setDestino(String destino) {
        this.destino = destino;
    }
    
    public Date getFechaViaje() {
        return fechaViaje;
    }
    
    public void setFechaViaje(Date fechaViaje) {
        this.fechaViaje = fechaViaje;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
}