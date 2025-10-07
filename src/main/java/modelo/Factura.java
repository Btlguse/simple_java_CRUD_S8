package modelo;

import java.util.Date;

public class Factura {
    private int idFactura;
    private int idReserva;
    private Date fechaEmision;
    private double montoTotal;
    private String estadoPago;

    // Constructors
    public Factura() {
    }

    public Factura(int idReserva, Date fechaEmision, double montoTotal, String estadoPago) {
        this.idReserva = idReserva;
        this.fechaEmision = fechaEmision;
        this.montoTotal = montoTotal * 1.12; // Adding 12% iva
        this.estadoPago = estadoPago;
    }

    // Getters and setters
    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
}