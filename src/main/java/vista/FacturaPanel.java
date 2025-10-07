package vista;

import controlador.FacturaController;
import controlador.ClienteController;
import modelo.Factura;
import modelo.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class FacturaPanel extends javax.swing.JPanel {
    
    private FacturaController facturaController;
    private ClienteController clienteController;
    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;
    private JButton btnRefrescar;
    private JButton btnAnularFactura;
    private JRadioButton rbMostrarTodas;
    private JScrollPane scrollPane;
    private JLabel lblFacturasDe; // Etiqueta para mostrar el contexto de las facturas
    private List<Factura> facturasActuales; // Lista actual que se está mostrando
    private Integer clienteIdActual; // ID del cliente actual (null si se muestran todas)
    private Integer reservaIdActual; // ID de la reserva actual (null si no es factura específica)
    
    public FacturaPanel() {
        super();
        facturaController = new FacturaController();
        clienteController = new ClienteController();
        initComponents();
        cargarFacturas(); // Carga inicial con filtros aplicados si los hay
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Crear el modelo de la tabla
        String[] columnas = {"ID Factura", "ID Reserva", "Fecha Emisión", "Monto Total", "Estado Pago"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        // Crear la tabla
        tablaFacturas = new JTable(modeloTabla);
        tablaFacturas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaFacturas.setRowHeight(25);
        
        // Crear el scroll pane para la tabla
        scrollPane = new JScrollPane(tablaFacturas);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        
        // Panel superior con filtros y refrescar
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbMostrarTodas = new JRadioButton("Mostrar todas las facturas", false);
        rbMostrarTodas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // No refrescamos automáticamente, solo cuando se presione refrescar
            }
        });
        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarFacturas();
            }
        });
        
        panelFiltros.add(rbMostrarTodas);
        panelFiltros.add(btnRefrescar);
        
        // Panel de botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnAnularFactura = new JButton("Anular Factura");
        btnAnularFactura.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anularFacturaSeleccionada();
            }
        });
        
        panelBotones.add(btnAnularFactura);
        
        // Título
        JLabel titulo = new JLabel("Gestión de Facturas", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Etiqueta para mostrar el contexto de las facturas
        lblFacturasDe = new JLabel("Facturas de: (Sin cargar)", JLabel.CENTER);
        lblFacturasDe.setFont(new Font("Arial", Font.ITALIC, 12));
        lblFacturasDe.setForeground(Color.DARK_GRAY);
        lblFacturasDe.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        // Panel central que contiene filtros y tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(panelFiltros, BorderLayout.NORTH);
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        
        // Panel norte que contiene título y etiqueta de contexto
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(titulo, BorderLayout.NORTH);
        panelNorte.add(lblFacturasDe, BorderLayout.CENTER);
        
        // Agregar componentes al panel principal
        add(panelNorte, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    /**
     * Carga las facturas en la tabla. Si el radiobutton está seleccionado,
     * carga todas las facturas; si no, mantiene los filtros actuales.
     */
    public void cargarFacturas() {
        // Limpiar la tabla
        modeloTabla.setRowCount(0);
        
        try {
            if (rbMostrarTodas.isSelected()) {
                // Cargar todas las facturas usando listarFacturas
                facturasActuales = facturaController.listarFacturas();
                clienteIdActual = null; // Indicar que se muestran todas las facturas
                reservaIdActual = null;
            } else {
                // Si no está seleccionado "mostrar todas", mantener filtros actuales
                // Por defecto, si no hay filtros específicos, no mostrar nada o mostrar lista vacía
                if (facturasActuales == null) {
                    facturasActuales = List.of(); // Lista vacía
                    clienteIdActual = null;
                    reservaIdActual = null;
                }
                // Si ya había facturas filtradas, las mantiene
            }
            
            mostrarFacturasEnTabla(facturasActuales);
            actualizarEtiquetaContexto(); // Actualizar la etiqueta de contexto
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar las facturas: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Carga facturas filtradas por cliente
     */
    public void cargarFacturasPorCliente(int idCliente) {
        try {
            facturasActuales = facturaController.listarFacturasCliente(idCliente);
            clienteIdActual = idCliente; // Establecer el ID del cliente actual
            reservaIdActual = null;
            rbMostrarTodas.setSelected(false); // Desmarcar "mostrar todas"
            mostrarFacturasEnTabla(facturasActuales);
            actualizarEtiquetaContexto(); // Actualizar la etiqueta de contexto
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar las facturas del cliente: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Carga la factura asociada a una reserva específica
     */
    public void cargarFacturaPorReserva(int idReserva) {
        try {
            Factura factura = facturaController.consultarFacturaPorReserva(idReserva);
            if (factura != null) {
                facturasActuales = List.of(factura);
            } else {
                facturasActuales = List.of(); // Lista vacía si no hay factura
                JOptionPane.showMessageDialog(this, 
                    "No se encontró factura asociada a la reserva seleccionada.", 
                    "Información", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            clienteIdActual = null;
            reservaIdActual = idReserva; // Establecer el ID de la reserva actual
            rbMostrarTodas.setSelected(false); // Desmarcar "mostrar todas"
            mostrarFacturasEnTabla(facturasActuales);
            actualizarEtiquetaContexto(); // Actualizar la etiqueta de contexto
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar la factura de la reserva: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Actualiza la etiqueta que muestra el contexto de las facturas cargadas
     */
    private void actualizarEtiquetaContexto() {
        if (reservaIdActual != null) {
            lblFacturasDe.setText("Factura de: Reserva ID " + reservaIdActual);
        } else if (clienteIdActual == null) {
            lblFacturasDe.setText("Facturas de: ALL");
        } else {
            try {
                Cliente cliente = clienteController.consultarCliente(clienteIdActual);
                if (cliente != null) {
                    String nombreCompleto = cliente.getNombre() + " " + cliente.getApellido();
                    lblFacturasDe.setText("Facturas de: " + nombreCompleto);
                } else {
                    lblFacturasDe.setText("Facturas de: Cliente ID " + clienteIdActual);
                }
            } catch (Exception e) {
                lblFacturasDe.setText("Facturas de: Cliente ID " + clienteIdActual);
            }
        }
    }
    
    /**
     * Método auxiliar para mostrar una lista de facturas en la tabla
     */
    private void mostrarFacturasEnTabla(List<Factura> facturas) {
        modeloTabla.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Factura factura : facturas) {
            Object[] fila = {
                factura.getIdFactura(),
                factura.getIdReserva(),
                sdf.format(factura.getFechaEmision()),
                String.format("$%.2f", factura.getMontoTotal()),
                factura.getEstadoPago()
            };
            modeloTabla.addRow(fila);
        }
    }
    
    /**
     * Anula la factura seleccionada cambiando su estado a "ANULADA"
     */
    private void anularFacturaSeleccionada() {
        int filaSeleccionada = tablaFacturas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione una factura para anular.");
            return;
        }
        
        int idFactura = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);
        String estadoActual = (String) modeloTabla.getValueAt(filaSeleccionada, 4);
        
        // Verificar si la factura ya está anulada
        if ("ANULADA".equals(estadoActual)) {
            JOptionPane.showMessageDialog(this, 
                "La factura seleccionada ya está anulada.", 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que desea anular la factura ID " + idFactura + "?",
            "Confirmar Anulación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Obtener la factura completa
                Factura factura = facturaController.consultarFactura(idFactura);
                if (factura == null) {
                    JOptionPane.showMessageDialog(this, "Error al obtener los datos de la factura.");
                    return;
                }
                
                // Actualizar solo el estado a "ANULADA"
                boolean exito = facturaController.actualizarFactura(
                    factura.getIdFactura(),
                    factura.getIdReserva(),
                    factura.getFechaEmision(),
                    factura.getMontoTotal(),
                    "ANULADA"
                );
                
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Factura anulada exitosamente.");
                    cargarFacturas(); // Refrescar la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "Error al anular la factura.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al anular la factura: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
