package vista;
import javax.swing.*;
import java.awt.*;
import modelo.*;
import controlador.*;

public class MainView {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private ClientePanel clientePanel;
    private ReservaPanel reservaPanel;
    private FacturaPanel facturaPanel;

    public MainView() {
        frame = new JFrame("Sistema de Gestión de Reservas y Facturas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        clientePanel = new ClientePanel(this); // Pasar referencia de MainView
        reservaPanel = new ReservaPanel(this); // Pasar referencia de MainView
        facturaPanel = new FacturaPanel();

        tabbedPane.addTab("Clientes", clientePanel);
        tabbedPane.addTab("Reservas", reservaPanel);
        tabbedPane.addTab("Facturas", facturaPanel);


        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void show() {
        frame.setVisible(true);
    }
    
    /**
     * Cambia a la pestaña de Reservas y carga las reservas del cliente especificado
     */
    public void mostrarReservasDelCliente(int idCliente) {
        reservaPanel.cargarReservasPorCliente(idCliente);
        tabbedPane.setSelectedComponent(reservaPanel);
    }
    
    /**
     * Cambia a la pestaña de Reservas, carga las reservas del cliente y abre el diálogo de nueva reserva
     */
    public void reservarVueloParaCliente(int idCliente) {
        reservaPanel.cargarReservasPorCliente(idCliente);
        tabbedPane.setSelectedComponent(reservaPanel);
        reservaPanel.mostrarDialogoNuevaReserva(idCliente);
    }
    
    /**
     * Cambia a la pestaña de Facturas y carga las facturas del cliente especificado
     */
    public void mostrarFacturasDelCliente(int idCliente) {
        facturaPanel.cargarFacturasPorCliente(idCliente);
        tabbedPane.setSelectedComponent(facturaPanel);
    }
    
    /**
     * Cambia a la pestaña de Facturas y muestra la factura de la reserva especificada
     */
    public void mostrarFacturaDeReserva(int idReserva) {
        facturaPanel.cargarFacturaPorReserva(idReserva);
        tabbedPane.setSelectedComponent(facturaPanel);
    }


        public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.show();
        });
    }
}
