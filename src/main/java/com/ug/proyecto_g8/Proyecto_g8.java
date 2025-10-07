
package com.ug.proyecto_g8;
import controlador.*;
import modelo.*;

import java.nio.channels.Pipe.SourceChannel;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author javi
 */
/* 
public class Proyecto_g8 {


    
    public static void main(String[] args) {
       /*  System.out.println("Testing Client Registration in Database...\n");
        
        ClienteController clienteController = new ClienteController();
        
        // Test data
        String testDni = "12345678"; // Use a unique DNI for each test run
        
        // 1. Test adding a client
        System.out.println("1. Testing add client...");
        boolean added = clienteController.agregarCliente(
            "John", 
            "Doe", 
            testDni, 
            "555-1234", 
            "john.doe@example.com", 
            "123 Main St"
        );
        
        System.out.println("Add client result: " + (added ? "SUCCESS" : "FAILED"));
        
        // 2. Test retrieving the client
        System.out.println("\n2. Testing get client...");
        Cliente cliente = null;
        
        // In a real app, you'd get the ID from the database
        // For testing, we'll list all clients and find our test client
        List<Cliente> clientes = clienteController.listarClientes();
        for (Cliente c : clientes) {
            if (c.getDni().equals(testDni)) {
                cliente = c;
                break;
            }
        }
        
        if (cliente != null) {
            System.out.println("Found client:");
            System.out.println("ID: " + cliente.getIdCliente());
            System.out.println("Name: " + cliente.getNombre() + " " + cliente.getApellido());
            System.out.println("DNI: " + cliente.getDni());
            System.out.println("Phone: " + cliente.getTelefono());
            System.out.println("Email: " + cliente.getEmail());
            System.out.println("Address: " + cliente.getDireccion());
        } else {
            System.out.println("Client not found!");
        }
        
        // 3. Test updating the client
        System.out.println("\n3. Testing update client...");
        if (cliente != null) {
            boolean updated = clienteController.actualizarCliente(
                cliente.getIdCliente(),
                "John Updated",
                "Doe Updated",
                testDni, // Keep same DNI
                "555-5678",
                "john.updated@example.com",
                "bbbbbbbbbbbbbbbbbbbbbbbbbbbb"
            );
            
            System.out.println("Update result: " + (updated ? "SUCCESS" : "FAILED"));
            
            // Verify update
            Cliente updatedCliente = clienteController.consultarCliente(cliente.getIdCliente());
            if (updatedCliente != null) {
                System.out.println("Updated client data:");
                System.out.println("Name: " + updatedCliente.getNombre() + " " + updatedCliente.getApellido());
                System.out.println("Phone: " + updatedCliente.getTelefono());
            }
        }
        
        //4. Test deleting the client
        System.out.println("\n4. Testing delete client...");
        if (cliente != null) {
            boolean deleted = clienteController.eliminarCliente(cliente.getIdCliente());
            System.out.println("Delete result: " + (deleted ? "SUCCESS" : "FAILED"));
            
            // Verify deletion
            Cliente deletedCliente = clienteController.consultarCliente(cliente.getIdCliente());
            System.out.println("Client after deletion: " + (deletedCliente == null ? "NOT FOUND (good)" : "STILL EXISTS (bad)"));
        }
        
        // 5. Test listing all clients
        System.out.println("\n5. Testing list all clients...");
        clientes = clienteController.listarClientes();
        System.out.println("Total clients in database: " + clientes.size());
        System.out.println("Client list:");
        for (Cliente c : clientes) {
            System.out.println("- " + c.getNombre() + " " + c.getApellido() + " (DNI: " + c.getDni() + ")");
        }*/





        /* 
        System.out.println("Reserva testing...\n");
        ReservaController reservaController = new ReservaController();
        
        //Test data
        int testIdCliente = 2; // Use an existing client ID
        String testDestino = "Paris";
        Date testFechReserva = new Date(System.currentTimeMillis());
        Date testFechaViaje = new Date(System.currentTimeMillis() + 86400000L); // 1 day later
        double testPrecio = 1000.0;
        String testEstado = "PENDIENTE";
           

        reservaController.agregarReserva(
            testIdCliente, 
            testFechReserva, 
            testDestino, 
            testFechaViaje, 
            testPrecio, 
            testEstado);
        System.out.println("Reserva added successfully!");
        */


       /*System.out.println("Consulting Reserva...");
        Reserva reserva = reservaController.consultarReserva(1); // Assuming ID 1 exists
        if (reserva != null) {
            System.out.println("Reserva found: " + reserva.getDestino() + " on " + reserva.getFechaViaje());
        } else {
            System.out.println("Reserva not found!");    
        }      

        System.out.println("Updating Reserva...");
        boolean updated = reservaController.actualizarReserva(
            1, // Assuming ID 1 exists
            2, // Existing client ID
            new Date(System.currentTimeMillis()), // Current date
            "Madrid", // New destination
            new Date(System.currentTimeMillis() + 86400000L), // 1 day later
            1200.0, // New price
            "CONFIRMADA" // New status
        );
        System.out.println("Update result: " + (updated ? "SUCCESS" : "FAILED"));
        */

        
        /*System.out.println("Deleting Reserva...");
        boolean deleted = reservaController.eliminarReserva(1); // Assuming ID 1 exists
        System.out.println("Delete result: " + (deleted ? "SUCCESS" : "FAILED"));
        */

        
        /* 
        System.out.println("Listing all Reservas...");
        List<Reserva> reservas = reservaController.listarReservas();
        System.out.println("Total Reservas: " + reservas.size());
        for (Reserva r : reservas) {
            System.out.println("Reserva ID: " + r.getIdReserva() + ", Destination: " + r.getDestino() + ", Date: " + r.getFechaViaje() + ", Price: " + r.getPrecio() + ", Status: " + r.getEstado());
        }
        */




        /* 
        System.out.println("Factura testing...\n");
        FacturaController facturaController = new FacturaController();
       
        // Test data
        int testIdReserva = 2; // Use an existing reservation ID
        Date testFechaEmision = new Date(System.currentTimeMillis());
        double testMontoTotal = 1500.0;
        String testEstadoPago = "PAGADA";

        facturaController.agregarFactura(
            testIdReserva, 
            testFechaEmision, 
            testMontoTotal, 
            testEstadoPago);
        System.out.println("Factura added successfully!");

        System.out.println("Consulting Factura...");
        Factura factura = facturaController.consultarFactura(1); // Assuming ID 1 exists
        if (factura != null) {
            System.out.println("Factura found: " + factura.getMontoTotal() + " on " + factura.getFechaEmision());
        } else {
            System.out.println("Factura not found!");  }

        System.out.println("Updating Factura...");
        boolean updatedFactura = facturaController.actualizarFactura(
            1, // Assuming ID 1 exists  
            2, // Existing reservation ID
            new Date(System.currentTimeMillis()), // Current date
            1800.0, // New total amount
            "ANULADA" // New payment status
        );
        System.out.println("Update result: " + (updatedFactura ? "SUCCESS" : "FAILED"));
        

        System.out.println("Deleting Factura...");
        boolean deletedFactura = facturaController.eliminarFactura(2); // Assuming ID 2 exists
        System.out.println("Delete result: " + (deletedFactura ? "SUCCESS" : "FAILED"));
        
        
        System.out.println("Listing all Facturas...");
        List<Factura> facturas = facturaController.listarFacturas();
        System.out.println("Total Facturas: " + facturas.size());
        for (Factura f : facturas) {
            System.out.println("Factura ID: " + f.getIdFactura() + ", Reservation ID: " + f.getIdReserva() + ", Total Amount: " + f.getMontoTotal() + ", Payment Status: " + f.getEstadoPago() + ", Issue Date: " + f.getFechaEmision());
        }
        
    }
}

*/