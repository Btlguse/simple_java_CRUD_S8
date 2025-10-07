package util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ValidacionReserva {
    
    // Patrones de validación
    private static final Pattern DESTINO_PATTERN = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s.,]+$");
    private static final Pattern PRECIO_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
    
    // Mensajes de error
    private static final Map<String, String> MENSAJES_ERROR = new HashMap<>();
    private static final Map<String, String> MENSAJES_AYUDA = new HashMap<>();
    
    static {
        // Mensajes de error
        MENSAJES_ERROR.put("destino", "El destino solo puede contener letras, espacios, puntos y comas.");
        MENSAJES_ERROR.put("destino_vacio", "El destino es obligatorio.");
        MENSAJES_ERROR.put("fecha_viaje", "La fecha de viaje no puede ser anterior a la fecha de reserva.");
        MENSAJES_ERROR.put("precio", "El precio debe ser un número válido con máximo 2 decimales.");
        MENSAJES_ERROR.put("precio_negativo", "El precio debe ser mayor a 0.");
        
        // Mensajes de ayuda
        MENSAJES_AYUDA.put("destino", "Ingrese el destino usando solo letras, espacios, puntos y comas");
        MENSAJES_AYUDA.put("fechaViaje", "La fecha de viaje debe ser posterior a la fecha de reserva");
        MENSAJES_AYUDA.put("precio", "Ingrese el precio en formato numérico (ej: 150.50)");
    }
    
    /**
     * Valida que el destino contenga solo caracteres alfabéticos, puntos y comas
     */
    public static void validarDestino(String destino) throws IllegalArgumentException {
        if (destino == null || destino.trim().isEmpty()) {
            throw new IllegalArgumentException(MENSAJES_ERROR.get("destino_vacio"));
        }
        
        if (!DESTINO_PATTERN.matcher(destino.trim()).matches()) {
            throw new IllegalArgumentException(MENSAJES_ERROR.get("destino"));
        }
    }
    
    /**
     * Valida que la fecha de viaje no sea anterior a la fecha de reserva
     */
    public static void validarFechaViaje(Date fechaReserva, Date fechaViaje) throws IllegalArgumentException {
        if (fechaViaje == null) {
            throw new IllegalArgumentException("La fecha de viaje es obligatoria.");
        }
        
        if (fechaReserva == null) {
            throw new IllegalArgumentException("La fecha de reserva es obligatoria.");
        }
        
        // Comparar solo las fechas, ignorando las horas
        Calendar calReserva = Calendar.getInstance();
        calReserva.setTime(fechaReserva);
        calReserva.set(Calendar.HOUR_OF_DAY, 0);
        calReserva.set(Calendar.MINUTE, 0);
        calReserva.set(Calendar.SECOND, 0);
        calReserva.set(Calendar.MILLISECOND, 0);
        
        Calendar calViaje = Calendar.getInstance();
        calViaje.setTime(fechaViaje);
        calViaje.set(Calendar.HOUR_OF_DAY, 0);
        calViaje.set(Calendar.MINUTE, 0);
        calViaje.set(Calendar.SECOND, 0);
        calViaje.set(Calendar.MILLISECOND, 0);
        
        if (calViaje.before(calReserva)) {
            throw new IllegalArgumentException(MENSAJES_ERROR.get("fecha_viaje"));
        }
    }
    
    /**
     * Valida que el precio sea un número válido con máximo 2 decimales
     */
    public static void validarPrecio(String precio) throws IllegalArgumentException {
        if (precio == null || precio.trim().isEmpty()) {
            throw new IllegalArgumentException("El precio es obligatorio.");
        }
        
        if (!PRECIO_PATTERN.matcher(precio.trim()).matches()) {
            throw new IllegalArgumentException(MENSAJES_ERROR.get("precio"));
        }
        
        try {
            double precioNumerico = Double.parseDouble(precio.trim());
            if (precioNumerico <= 0) {
                throw new IllegalArgumentException(MENSAJES_ERROR.get("precio_negativo"));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(MENSAJES_ERROR.get("precio"));
        }
    }
    
    /**
     * Validación completa de una reserva
     */
    public static void validarReserva(String destino, Date fechaReserva, Date fechaViaje, String precio) 
            throws IllegalArgumentException {
        validarDestino(destino);
        validarFechaViaje(fechaReserva, fechaViaje);
        validarPrecio(precio);
    }
    
    /**
     * Obtiene un mensaje de ayuda para un campo específico
     */
    public static String obtenerMensajeAyuda(String campo) {
        return MENSAJES_AYUDA.getOrDefault(campo, "Ingrese un valor válido");
    }
    
    /**
     * Obtiene un mensaje de error para un campo específico
     */
    public static String obtenerMensajeError(String campo) {
        return MENSAJES_ERROR.getOrDefault(campo, "Valor inválido");
    }
}
