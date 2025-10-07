package util;

import java.util.regex.Pattern;

public class ValidacionCliente {
    
    // Patrones de validación
    private static final Pattern PATTERN_ALFABETICO = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$");
    private static final Pattern PATTERN_DNI_TELEFONO = Pattern.compile("^\\d{10}$");
    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PATTERN_DIRECCION = Pattern.compile("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ .,]+$");
    
    /**
     * Valida que el campo no esté vacío
     */
    public static boolean validarCampoNoVacio(String campo) {
        return campo != null && !campo.trim().isEmpty();
    }
    
    /**
     * Valida que el nombre o apellido contenga solo caracteres alfabéticos y espacios
     */
    public static boolean validarNombreApellido(String texto) {
        if (!validarCampoNoVacio(texto)) {
            return false;
        }
        return PATTERN_ALFABETICO.matcher(texto.trim()).matches();
    }
    
    /**
     * Valida que el DNI o teléfono contenga exactamente 10 dígitos numéricos
     */
    public static boolean validarDniTelefono(String numero) {
        if (!validarCampoNoVacio(numero)) {
            return false;
        }
        return PATTERN_DNI_TELEFONO.matcher(numero.trim()).matches();
    }
    
    /**
     * Valida que el email tenga el formato correcto: texto@texto.texto
     */
    public static boolean validarEmail(String email) {
        if (!validarCampoNoVacio(email)) {
            return false;
        }
        return PATTERN_EMAIL.matcher(email.trim()).matches();
    }
    
    /**
     * Valida que la dirección contenga solo caracteres alfanuméricos, espacios, puntos y comas
     */
    public static boolean validarDireccion(String direccion) {
        if (!validarCampoNoVacio(direccion)) {
            return false;
        }
        return PATTERN_DIRECCION.matcher(direccion.trim()).matches();
    }
    
    /**
     * Valida todos los campos de un cliente
     * @return String con el primer error encontrado, null si todo está válido
     */
    public static String validarCliente(String nombre, String apellido, String dni, 
                                       String telefono, String email, String direccion) {
        
        if (!validarCampoNoVacio(nombre)) {
            return "El campo Nombre es obligatorio.";
        }
        if (!validarNombreApellido(nombre)) {
            return "El Nombre solo puede contener letras y espacios.";
        }
        
        if (!validarCampoNoVacio(apellido)) {
            return "El campo Apellido es obligatorio.";
        }
        if (!validarNombreApellido(apellido)) {
            return "El Apellido solo puede contener letras y espacios.";
        }
        
        if (!validarCampoNoVacio(dni)) {
            return "El campo DNI es obligatorio.";
        }
        if (!validarDniTelefono(dni)) {
            return "El DNI debe contener exactamente 10 dígitos numéricos.";
        }
        
        if (!validarCampoNoVacio(telefono)) {
            return "El campo Teléfono es obligatorio.";
        }
        if (!validarDniTelefono(telefono)) {
            return "El Teléfono debe contener exactamente 10 dígitos numéricos.";
        }
        
        if (!validarCampoNoVacio(email)) {
            return "El campo Email es obligatorio.";
        }
        if (!validarEmail(email)) {
            return "El Email debe tener el formato: texto@texto.texto";
        }
        
        if (!validarCampoNoVacio(direccion)) {
            return "El campo Dirección es obligatorio.";
        }
        if (!validarDireccion(direccion)) {
            return "La Dirección solo puede contener letras, números, espacios, puntos y comas.";
        }
        
        return null; // Todas las validaciones pasaron
    }
    
    /**
     * Obtiene mensajes de ayuda para cada campo
     */
    public static String obtenerMensajeAyuda(String campo) {
        switch (campo.toLowerCase()) {
            case "dni":
            case "telefono":
                return "10 dígitos";
            case "email":
                return "Formato: texto@sample.com";
            case "direccion":
            default:
                return "Campo obligatorio";
        }
    }
}
