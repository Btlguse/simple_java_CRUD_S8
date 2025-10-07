package vista;

import controlador.ReservaController;
import controlador.ClienteController;
import modelo.Reserva;
import modelo.Cliente;
import util.ValidacionReserva;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class PriceDocumentFilter extends DocumentFilter {
    private static final int MAX_DIGITS_BEFORE_DECIMAL = 8;
    private static final int MAX_DECIMALS = 2;
    
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (isValidInput(fb, offset, 0, string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (isValidInput(fb, offset, length, text)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
    
    private boolean isValidInput(FilterBypass fb, int offset, int length, String text) throws BadLocationException {
        if (text == null) return false;
        
        // Solo permitir dígitos y un punto decimal
        if (!text.matches("[\\d.]*")) {
            return false;
        }
        
        // Obtener el texto actual
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        
        // Simular el texto resultante después de la edición
        String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
        
        // No permitir texto vacío o solo punto
        if (newText.isEmpty() || newText.equals(".")) {
            return true; // Permitir estado intermedio
        }
        
        // No permitir múltiples puntos decimales
        int dotCount = 0;
        for (char c : newText.toCharArray()) {
            if (c == '.') dotCount++;
        }
        if (dotCount > 1) return false;
        
        // Validar formato de precio
        if (newText.contains(".")) {
            String[] parts = newText.split("\\.");
            
            // Parte entera no debe exceder el límite
            if (parts[0].length() > MAX_DIGITS_BEFORE_DECIMAL) {
                return false;
            }
            
            // Parte decimal no debe exceder 2 dígitos
            if (parts.length > 1 && parts[1].length() > MAX_DECIMALS) {
                return false;
            }
        } else {
            // Solo parte entera, verificar límite
            if (newText.length() > MAX_DIGITS_BEFORE_DECIMAL) {
                return false;
            }
        }
        
        return true;
    }
}

public class ReservaPanel extends javax.swing.JPanel {
    
    private ReservaController reservaController;
    private ClienteController clienteController;
    private MainView mainView; // Referencia al MainView para navegación
    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;
    private JButton btnRefrescar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JRadioButton rbMostrarTodas;
    private JScrollPane scrollPane;
    private JLabel lblReservasDe; // Etiqueta para mostrar el contexto de las reservas
    private List<Reserva> reservasActuales; // Lista actual que se está mostrando
    private Integer clienteIdActual; // ID del cliente actual (null si se muestran todas)
    
    public ReservaPanel() {
        this(null); // Constructor por defecto para compatibilidad
    }
    
    public ReservaPanel(MainView mainView) {
        super();
        this.mainView = mainView;
        reservaController = new ReservaController();
        clienteController = new ClienteController();
        initComponents();
        cargarReservas(); // Carga inicial con filtros aplicados si los hay
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Crear el modelo de la tabla
        String[] columnas = {"ID Reserva", "ID Cliente", "Fecha Reserva", "Destino", "Fecha Viaje", "Precio", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        // Crear la tabla
        tablaReservas = new JTable(modeloTabla);
        tablaReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaReservas.setRowHeight(25);
        
        // Agregar menú contextual (click derecho) a la tabla
        agregarMenuContextual();
        
        // Crear el scroll pane para la tabla
        scrollPane = new JScrollPane(tablaReservas);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        
        // Panel superior con filtros y refrescar
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbMostrarTodas = new JRadioButton("Mostrar todas las reservas", false);
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
                cargarReservas();
            }
        });
        
        panelFiltros.add(rbMostrarTodas);
        panelFiltros.add(btnRefrescar);
        
        // Panel de botones de acción (solo editar y eliminar)
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnEditar = new JButton("Editar Reserva");
        btnEliminar = new JButton("Eliminar Reserva");
        
        // Agregar listeners a los botones
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarReservaSeleccionada();
            }
        });
        
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarReservaSeleccionada();
            }
        });
        
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        
        // Título
        JLabel titulo = new JLabel("Gestión de Reservas", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Etiqueta para mostrar el contexto de las reservas
        lblReservasDe = new JLabel("Reservas de: (Sin cargar)", JLabel.CENTER);
        lblReservasDe.setFont(new Font("Arial", Font.ITALIC, 12));
        lblReservasDe.setForeground(Color.DARK_GRAY);
        lblReservasDe.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        // Panel central que contiene filtros y tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(panelFiltros, BorderLayout.NORTH);
        panelCentral.add(scrollPane, BorderLayout.CENTER);
        
        // Panel norte que contiene título y etiqueta de contexto
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(titulo, BorderLayout.NORTH);
        panelNorte.add(lblReservasDe, BorderLayout.CENTER);
        
        // Agregar componentes al panel principal
        add(panelNorte, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    /**
     * Carga las reservas en la tabla. Si el radiobutton está seleccionado,
     * carga todas las reservas; si no, mantiene los filtros actuales.
     */
    public void cargarReservas() {
        // Limpiar la tabla
        modeloTabla.setRowCount(0);
        
        try {
            if (rbMostrarTodas.isSelected()) {
                // Cargar todas las reservas usando listarReservas
                reservasActuales = reservaController.listarReservas();
                clienteIdActual = null; // Indicar que se muestran todas las reservas
            } else {
                // Si no está seleccionado "mostrar todas", mantener filtros actuales
                // Por defecto, si no hay filtros específicos, no mostrar nada o mostrar lista vacía
                if (reservasActuales == null) {
                    reservasActuales = List.of(); // Lista vacía
                    clienteIdActual = null;
                }
                // Si ya había reservas filtradas, las mantiene (clienteIdActual ya está establecido)
            }
            
            mostrarReservasEnTabla(reservasActuales);
            actualizarEtiquetaContexto(); // Actualizar la etiqueta de contexto
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar las reservas: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Carga reservas filtradas por cliente
     */
    public void cargarReservasPorCliente(int idCliente) {
        try {
            reservasActuales = reservaController.listarReservasPorCliente(idCliente);
            clienteIdActual = idCliente; // Establecer el ID del cliente actual
            rbMostrarTodas.setSelected(false); // Desmarcar "mostrar todas"
            mostrarReservasEnTabla(reservasActuales);
            actualizarEtiquetaContexto(); // Actualizar la etiqueta de contexto
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar las reservas del cliente: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Muestra el diálogo para crear una nueva reserva para un cliente específico
     */
    public void mostrarDialogoNuevaReserva(int idCliente) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        JTextField txtDestino = new JTextField(20);
        JTextField txtFechaViaje = new JTextField(sdf.format(new Date()), 20);
        JTextField txtPrecio = new JTextField("0.0", 20);
        
        String[] estados = {"Pendiente", "Confirmada", "Cancelada", "Completada"};
        JComboBox<String> cbEstado = new JComboBox<>(estados);
        cbEstado.setSelectedItem("Pendiente"); // Estado por defecto
        
        // Mostrar ID del cliente (solo lectura)
        JTextField txtIdCliente = new JTextField(String.valueOf(idCliente), 20);
        txtIdCliente.setEditable(false);
        txtIdCliente.setBackground(Color.LIGHT_GRAY);
        
        // Aplicar filtro numérico al campo de precio
        aplicarFiltroPrecio(txtPrecio);
        
        // Agregar tooltips con mensajes de ayuda
        txtDestino.setToolTipText(ValidacionReserva.obtenerMensajeAyuda("destino"));
        txtFechaViaje.setToolTipText(ValidacionReserva.obtenerMensajeAyuda("fechaViaje"));
        txtPrecio.setToolTipText(ValidacionReserva.obtenerMensajeAyuda("precio"));
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("ID Cliente:"), gbc);
        gbc.gridx = 1;
        panel.add(txtIdCliente, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Destino:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDestino, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Fecha Viaje (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        panel.add(txtFechaViaje, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPrecio, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        panel.add(cbEstado, gbc);
        
        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Nueva Reserva para Cliente " + idCliente, 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String destino = txtDestino.getText().trim();
                Date fechaViaje = sdf.parse(txtFechaViaje.getText().trim());
                String precio = txtPrecio.getText().trim();
                String estado = (String) cbEstado.getSelectedItem();
                Date fechaReserva = new Date(); // Fecha actual
                
                // Validar los datos usando ValidacionReserva
                ValidacionReserva.validarReserva(destino, fechaReserva, fechaViaje, precio);
                
                double precioNumerico = Double.parseDouble(precio);

                boolean exito = reservaController.agregarReserva(
                    idCliente,
                    fechaReserva,
                    destino,
                    fechaViaje,
                    precioNumerico,
                    estado
                );
                
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Reserva creada exitosamente.");
                    cargarReservasPorCliente(idCliente); // Refrescar la tabla con las reservas del cliente
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, 
                    e.getMessage(), 
                    "Error de Validación", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error en los datos ingresados: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Aplica el filtro de precio numérico a un campo de texto
     */
    private void aplicarFiltroPrecio(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new PriceDocumentFilter());
    }
    
    /**
     * Actualiza la etiqueta que muestra el contexto de las reservas cargadas
     */
    private void actualizarEtiquetaContexto() {
        if (clienteIdActual == null) {
            lblReservasDe.setText("Reservas de: ALL");
        } else {
            try {
                Cliente cliente = clienteController.consultarCliente(clienteIdActual);
                if (cliente != null) {
                    String nombreCompleto = cliente.getNombre() + " " + cliente.getApellido();
                    lblReservasDe.setText("Reservas de: " + nombreCompleto);
                } else {
                    lblReservasDe.setText("Reservas de: Cliente ID " + clienteIdActual);
                }
            } catch (Exception e) {
                lblReservasDe.setText("Reservas de: Cliente ID " + clienteIdActual);
            }
        }
    }
    
    /**
     * Método auxiliar para mostrar una lista de reservas en la tabla
     */
    private void mostrarReservasEnTabla(List<Reserva> reservas) {
        modeloTabla.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Reserva reserva : reservas) {
            Object[] fila = {
                reserva.getIdReserva(),
                reserva.getIdCliente(),
                sdf.format(reserva.getFechaReserva()),
                reserva.getDestino(),
                sdf.format(reserva.getFechaViaje()),
                String.format("$%.2f", reserva.getPrecio()),
                reserva.getEstado()
            };
            modeloTabla.addRow(fila);
        }
    }
    
    private void editarReservaSeleccionada() {
        int filaSeleccionada = tablaReservas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione una reserva para editar.");
            return;
        }
        
        int idReserva = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);
        int idCliente = (Integer) modeloTabla.getValueAt(filaSeleccionada, 1);
        String destino = (String) modeloTabla.getValueAt(filaSeleccionada, 3);
        String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 6);
        
        // Obtener la reserva completa para editar
        Reserva reserva = reservaController.consultarReserva(idReserva);
        if (reserva == null) {
            JOptionPane.showMessageDialog(this, "Error al obtener los datos de la reserva.");
            return;
        }
        
        mostrarDialogoEditarReserva(reserva);
    }
    
    private void mostrarDialogoEditarReserva(Reserva reserva) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        JTextField txtIdCliente = new JTextField(String.valueOf(reserva.getIdCliente()), 20);
        txtIdCliente.setEditable(false);
        txtIdCliente.setBackground(Color.LIGHT_GRAY);
        JTextField txtDestino = new JTextField(reserva.getDestino(), 20);
        txtDestino.setEditable(false);
        txtDestino.setBackground(Color.LIGHT_GRAY);
        JTextField txtFechaViaje = new JTextField(sdf.format(reserva.getFechaViaje()), 20);
        JTextField txtPrecio = new JTextField(String.valueOf(reserva.getPrecio()), 20);
        txtPrecio.setEditable(false);
        txtPrecio.setBackground(Color.LIGHT_GRAY);
        
        String[] estados = {"Confirmada", "Pendiente", "Cancelada", "Completada"};
        JComboBox<String> cbEstado = new JComboBox<>(estados);
        cbEstado.setSelectedItem(reserva.getEstado());
        
        // Aplicar filtro numérico al campo de precio
        aplicarFiltroPrecio(txtPrecio);
        
        // Agregar tooltips con mensajes de ayuda
        txtDestino.setToolTipText(ValidacionReserva.obtenerMensajeAyuda("destino"));
        txtFechaViaje.setToolTipText(ValidacionReserva.obtenerMensajeAyuda("fechaViaje"));
        txtPrecio.setToolTipText(ValidacionReserva.obtenerMensajeAyuda("precio"));
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("ID Cliente:"), gbc);
        gbc.gridx = 1;
        panel.add(txtIdCliente, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Destino:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDestino, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Fecha Viaje (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        panel.add(txtFechaViaje, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPrecio, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        panel.add(cbEstado, gbc);
        
        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Editar Reserva", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int idCliente = Integer.parseInt(txtIdCliente.getText().trim());
                String destino = txtDestino.getText().trim();
                Date fechaViaje = sdf.parse(txtFechaViaje.getText().trim());
                String precio = txtPrecio.getText().trim();
                String estado = (String) cbEstado.getSelectedItem();
                
                // Validar los datos usando ValidacionReserva
                ValidacionReserva.validarReserva(destino, reserva.getFechaReserva(), fechaViaje, precio);
                
                double precioNumerico = Double.parseDouble(precio);
                
                boolean exito = reservaController.actualizarReserva(
                    reserva.getIdReserva(),
                    idCliente,
                    reserva.getFechaReserva(), // Mantener fecha de reserva original
                    destino,
                    fechaViaje,
                    precioNumerico,
                    estado
                );
                
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Reserva actualizada exitosamente.");
                    cargarReservas(); // Refrescar la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, 
                    e.getMessage(), 
                    "Error de Validación", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error en los datos ingresados: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarReservaSeleccionada() {
        int filaSeleccionada = tablaReservas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione una reserva para eliminar.");
            return;
        }
        
        int idReserva = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);
        String destino = (String) modeloTabla.getValueAt(filaSeleccionada, 3);
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que desea eliminar la reserva a " + destino + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean exito = reservaController.eliminarReserva(idReserva);
                
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Reserva eliminada exitosamente.");
                    cargarReservas(); // Refrescar la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al eliminar la reserva: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Agrega un menú contextual (click derecho) a la tabla de reservas
     */
    private void agregarMenuContextual() {
        JPopupMenu menuContextual = new JPopupMenu();
        
        JMenuItem itemFacturaAsignada = new JMenuItem("Factura asignada");
        
        itemFacturaAsignada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verFacturaDeReservaSeleccionada();
            }
        });
        
        menuContextual.add(itemFacturaAsignada);
        
        // Agregar el listener del mouse para mostrar el menú contextual
        tablaReservas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    mostrarMenuContextual(e);
                }
            }
            
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    mostrarMenuContextual(e);
                }
            }
            
            private void mostrarMenuContextual(java.awt.event.MouseEvent e) {
                int fila = tablaReservas.rowAtPoint(e.getPoint());
                if (fila >= 0) {
                    tablaReservas.setRowSelectionInterval(fila, fila);
                    menuContextual.show(tablaReservas, e.getX(), e.getY());
                }
            }
        });
    }
    
    /**
     * Navega al panel de facturas y muestra la factura de la reserva seleccionada
     */
    private void verFacturaDeReservaSeleccionada() {
        int filaSeleccionada = tablaReservas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione una reserva.");
            return;
        }
        
        if (mainView == null) {
            JOptionPane.showMessageDialog(this, "No se puede navegar a las facturas desde este contexto.");
            return;
        }
        
        int idReserva = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);
        mainView.mostrarFacturaDeReserva(idReserva);
    }
}
