package controlador;

import modelo.Cliente;
import modelo.ClienteDAO;
import util.ValidacionCliente;

public class ClienteController {
    private ClienteDAO clienteDAO;
    
    public ClienteController() {
        clienteDAO = new ClienteDAO();
    }
    
    public boolean agregarCliente(String nombre, String apellido, String dni, String telefono, String email, String direccion) {
        // Validar los datos antes de procesar
        String errorValidacion = ValidacionCliente.validarCliente(nombre, apellido, dni, telefono, email, direccion);
        if (errorValidacion != null) {
            throw new IllegalArgumentException("Error de validación: " + errorValidacion);
        }
        
        // Verificar si ya existe un cliente con el mismo DNI
        if (existeClienteConDni(dni)) {
            throw new IllegalArgumentException("Ya existe un cliente registrado con el DNI: " + dni);
        }
        
        Cliente cliente = new Cliente(nombre, apellido, dni, telefono, email, direccion);
        return clienteDAO.agregarCliente(cliente);
    }
    
    public Cliente consultarCliente(int idCliente) {
        return clienteDAO.consultarCliente(idCliente);
    }
    
    public boolean actualizarCliente(int idCliente, String nombre, String apellido, String dni, String telefono, String email, String direccion) {
        // Validar los datos antes de procesar
        String errorValidacion = ValidacionCliente.validarCliente(nombre, apellido, dni, telefono, email, direccion);
        if (errorValidacion != null) {
            throw new IllegalArgumentException("Error de validación: " + errorValidacion);
        }
        
        // Verificar si ya existe otro cliente con el mismo DNI (excluyendo el cliente actual)
        if (existeClienteConDniExcluyendo(dni, idCliente)) {
            throw new IllegalArgumentException("Ya existe otro cliente registrado con el DNI: " + dni);
        }
        
        Cliente cliente = new Cliente(nombre, apellido, dni, telefono, email, direccion);
        cliente.setIdCliente(idCliente);
        return clienteDAO.actualizarCliente(cliente);
    }
    
    public boolean eliminarCliente(int idCliente) {
        return clienteDAO.eliminarCliente(idCliente);
    }
    
    public java.util.List<Cliente> listarClientes() {
        return clienteDAO.listarClientes();
    }
    
    /**
     * Verifica si existe un cliente con el DNI especificado
     */
    private boolean existeClienteConDni(String dni) {
        java.util.List<Cliente> clientes = clienteDAO.listarClientes();
        for (Cliente cliente : clientes) {
            if (cliente.getDni().equals(dni)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Verifica si existe otro cliente con el DNI especificado, excluyendo el cliente con el ID dado
     */
    private boolean existeClienteConDniExcluyendo(String dni, int idExcluir) {
        java.util.List<Cliente> clientes = clienteDAO.listarClientes();
        for (Cliente cliente : clientes) {
            if (cliente.getDni().equals(dni) && cliente.getIdCliente() != idExcluir) {
                return true;
            }
        }
        return false;
    }
}