package vista;

import controlador.ClienteController;
import modelo.Cliente;
import util.ValidacionCliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

class NumericDocumentFilter extends DocumentFilter {
    private static final int MAX_LENGTH = 10;
    
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string != null && string.matches("\\d*")) {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = currentText.substring(0, offset) + string + currentText.substring(offset);
            
            if (newText.length() <= MAX_LENGTH) {
                super.insertString(fb, offset, string, attr);
            }
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text != null && text.matches("\\d*")) {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
            
            if (newText.length() <= MAX_LENGTH) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}

public class ClientePanel extends javax.swing.JPanel {
    
    private ClienteController clienteController;
    private MainView mainView; // Referencia al MainView para navegación
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JButton btnRefrescar;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JScrollPane scrollPane;

    public ClientePanel() {
        this(null); // Constructor por defecto para compatibilidad
    }
    
    public ClientePanel(MainView mainView) {
        super();
        this.mainView = mainView;
        clienteController = new ClienteController();
        initComponents();
        cargarClientes();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Crear el modelo de la tabla
        String[] columnas = {"ID", "Nombre", "Apellido", "DNI", "Teléfono", "Email", "Dirección"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        // Crear la tabla
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.setRowHeight(25);
        
        // Agregar menú contextual (click derecho) a la tabla
        agregarMenuContextual();
        
        // Crear el scroll pane para la tabla
        scrollPane = new JScrollPane(tablaClientes);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnRefrescar = new JButton("Refrescar");
        btnAgregar = new JButton("Agregar Cliente");
        btnEditar = new JButton("Editar Cliente");
        btnEliminar = new JButton("Eliminar Cliente");
        
        // Agregar listeners a los botones
        btnRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarClientes();
            }
        });
        
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoAgregarCliente();
            }
        });
        
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarClienteSeleccionado();
            }
        });
        
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarClienteSeleccionado();
            }
        });
        
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        
        // Agregar componentes al panel principal
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Título
        JLabel titulo = new JLabel("Gestión de Clientes", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);
    }
    
    private void aplicarFiltroNumerico(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }
    
    private void cargarClientes() {
        // Limpiar la tabla
        modeloTabla.setRowCount(0);
        
        try {
            List<Cliente> clientes = clienteController.listarClientes();
            
            for (Cliente cliente : clientes) {
                Object[] fila = {
                    cliente.getIdCliente(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getDni(),
                    cliente.getTelefono(),
                    cliente.getEmail(),
                    cliente.getDireccion()
                };
                modeloTabla.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar los clientes: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarDialogoAgregarCliente() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField txtNombre = new JTextField(20);
        JTextField txtApellido = new JTextField(20);
        JTextField txtDni = new JTextField(20);
        JTextField txtTelefono = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        JTextField txtDireccion = new JTextField(20);
        
        // Aplicar filtros numéricos a DNI y teléfono
        aplicarFiltroNumerico(txtDni);
        aplicarFiltroNumerico(txtTelefono);
        
        // Agregar tooltips con mensajes de ayuda
        txtNombre.setToolTipText(ValidacionCliente.obtenerMensajeAyuda("nombre"));
        txtApellido.setToolTipText(ValidacionCliente.obtenerMensajeAyuda("apellido"));
        txtDni.setToolTipText(ValidacionCliente.obtenerMensajeAyuda("dni"));
        txtTelefono.setToolTipText(ValidacionCliente.obtenerMensajeAyuda("telefono"));
        txtEmail.setToolTipText(ValidacionCliente.obtenerMensajeAyuda("email"));
        txtDireccion.setToolTipText(ValidacionCliente.obtenerMensajeAyuda("direccion"));
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        panel.add(txtApellido, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDni, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        panel.add(txtTelefono, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDireccion, gbc);
        
        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Agregar Nuevo Cliente", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            // Obtener los datos del formulario
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String dni = txtDni.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            String direccion = txtDireccion.getText().trim();
            
            try {
                boolean exito = clienteController.agregarCliente(
                    nombre, apellido, dni, telefono, email, direccion
                );
                
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente.");
                    cargarClientes();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, 
                    e.getMessage(), 
                    "Error de Validación", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al agregar el cliente: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editarClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un cliente para editar.");
            return;
        }
        
        int idCliente = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
        String apellido = (String) modeloTabla.getValueAt(filaSeleccionada, 2);
        String dni = (String) modeloTabla.getValueAt(filaSeleccionada, 3);
        String telefono = (String) modeloTabla.getValueAt(filaSeleccionada, 4);
        String email = (String) modeloTabla.getValueAt(filaSeleccionada, 5);
        String direccion = (String) modeloTabla.getValueAt(filaSeleccionada, 6);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField txtNombre = new JTextField(nombre, 20);
        JTextField txtApellido = new JTextField(apellido, 20);
        JTextField txtDni = new JTextField(dni, 20);
        JTextField txtTelefono = new JTextField(telefono, 20);
        JTextField txtEmail = new JTextField(email, 20);
        JTextField txtDireccion = new JTextField(direccion, 20);
        
        // Aplicar filtros numéricos a DNI y teléfono
        aplicarFiltroNumerico(txtDni);
        aplicarFiltroNumerico(txtTelefono);
        
        // Agregar tooltips con mensajes de ayuda
        txtNombre.setToolTipText(ValidacionCliente.obtenerMensajeAyuda("nombre"));
        txtApellido.setToolTipText(ValidacionCliente.obtenerMensajeAyuda("apellido"));
        txtDni.setToolTipText(ValidacionCliente.obtenerMensajeAyuda("dni"));
        txtTelefono.setToolTipText(ValidacionCliente.obtenerMensajeAyuda("telefono"));
        txtEmail.setToolTipText(ValidacionCliente.obtenerMensajeAyuda("email"));
        txtDireccion.setToolTipText(ValidacionCliente.obtenerMensajeAyuda("direccion"));
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        panel.add(txtApellido, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDni, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        panel.add(txtTelefono, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDireccion, gbc);
        
        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Editar Cliente", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            // Obtener los datos actualizados del formulario
            String nombreActualizado = txtNombre.getText().trim();
            String apellidoActualizado = txtApellido.getText().trim();
            String dniActualizado = txtDni.getText().trim();
            String telefonoActualizado = txtTelefono.getText().trim();
            String emailActualizado = txtEmail.getText().trim();
            String direccionActualizada = txtDireccion.getText().trim();
            
            try {
                boolean exito = clienteController.actualizarCliente(
                    idCliente,
                    nombreActualizado,
                    apellidoActualizado,
                    dniActualizado,
                    telefonoActualizado,
                    emailActualizado,
                    direccionActualizada
                );
                
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente.");
                    cargarClientes();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, 
                    e.getMessage(), 
                    "Error de Validación", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al actualizar el cliente: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un cliente para eliminar.");
            return;
        }
        
        int idCliente = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
        String apellido = (String) modeloTabla.getValueAt(filaSeleccionada, 2);
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que desea eliminar al cliente " + nombre + " " + apellido + "?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean exito = clienteController.eliminarCliente(idCliente);
                
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.");
                    cargarClientes();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al eliminar el cliente: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Agrega un menú contextual (click derecho) a la tabla de clientes
     */
    private void agregarMenuContextual() {
        JPopupMenu menuContextual = new JPopupMenu();
        
        JMenuItem itemReservarVuelo = new JMenuItem("Reservar vuelo");
        JMenuItem itemVerReservas = new JMenuItem("Ver reservas");
        JMenuItem itemFacturacion = new JMenuItem("Facturación");
        
        itemReservarVuelo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reservarVueloParaClienteSeleccionado();
            }
        });
        
        itemVerReservas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verReservasDelClienteSeleccionado();
            }
        });
        
        itemFacturacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verFacturasDelClienteSeleccionado();
            }
        });
        
        menuContextual.add(itemReservarVuelo);
        menuContextual.add(itemVerReservas);
        menuContextual.add(itemFacturacion);
        
        // Agregar el listener del mouse para mostrar el menú contextual
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    mostrarMenuContextual(e);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    mostrarMenuContextual(e);
                }
            }
            
            private void mostrarMenuContextual(MouseEvent e) {
                int fila = tablaClientes.rowAtPoint(e.getPoint());
                if (fila >= 0) {
                    tablaClientes.setRowSelectionInterval(fila, fila);
                    menuContextual.show(tablaClientes, e.getX(), e.getY());
                }
            }
        });
    }
    
    /**
     * Navega al panel de reservas y abre el diálogo para crear una nueva reserva
     */
    private void reservarVueloParaClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un cliente.");
            return;
        }
        
        if (mainView == null) {
            JOptionPane.showMessageDialog(this, "No se puede navegar a las reservas desde este contexto.");
            return;
        }
        
        int idCliente = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);
        mainView.reservarVueloParaCliente(idCliente);
    }
    
    /**
     * Navega al panel de reservas y muestra las reservas del cliente seleccionado
     */
    private void verReservasDelClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un cliente.");
            return;
        }
        
        if (mainView == null) {
            JOptionPane.showMessageDialog(this, "No se puede navegar a las reservas desde este contexto.");
            return;
        }
        
        int idCliente = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);
        mainView.mostrarReservasDelCliente(idCliente);
    }
    
    /**
     * Navega al panel de facturas y muestra las facturas del cliente seleccionado
     */
    private void verFacturasDelClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un cliente.");
            return;
        }
        
        if (mainView == null) {
            JOptionPane.showMessageDialog(this, "No se puede navegar a las facturas desde este contexto.");
            return;
        }
        
        int idCliente = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);
        mainView.mostrarFacturasDelCliente(idCliente);
    }

}
