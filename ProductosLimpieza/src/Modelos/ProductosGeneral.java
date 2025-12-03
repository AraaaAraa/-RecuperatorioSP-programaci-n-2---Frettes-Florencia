package Modelos;

import java.io.Serializable;
import java.util.Objects;

import Excepciones.NombreVacioException;
import Excepciones.ProductoVencidoException;

public abstract class ProductosGeneral implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String concentracion;
    private String fechaVencimiento; // CAMBIADO A STRING

    public ProductosGeneral(String nombre, String concentracion, String fechaVencimiento) {
        setNombre(nombre);
        setConcentracion(concentracion);
        setFechaVencimiento(fechaVencimiento);
    }

    // =========================================================================
    // Getters
    // =========================================================================
    
    public String getNombre() {
        return nombre;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public String getFechaVencimiento() { // RETORNA STRING
        return fechaVencimiento;
    }
    
    public abstract String getTipo(); 
    
    public abstract String getDetalle();
    
    // =========================================================================
    // Setters con Validación
    // =========================================================================
    
    public final void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new NombreVacioException("El nombre del producto no puede ser nulo ni vacío.");
        }
        this.nombre = nombre.trim();
    }
    
    public final void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public final void setFechaVencimiento(String fechaVencimiento) {
        if (fechaVencimiento == null || fechaVencimiento.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser nula o vacía.");
        }
        // Opcional: Puedes agregar validación de formato aquí si quieres
        // Por ejemplo, verificar que tenga formato dd/MM/yyyy
        this.fechaVencimiento = fechaVencimiento.trim();
    }

    // =========================================================================
    // Lógica de Unicidad
    // =========================================================================

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProductosGeneral)) return false;
        ProductosGeneral other = (ProductosGeneral) obj;
        
        return Objects.equals(this.nombre, other.nombre) &&
               Objects.equals(this.concentracion, other.concentracion) &&
               Objects.equals(this.fechaVencimiento, other.fechaVencimiento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, concentracion, fechaVencimiento);
    }
}