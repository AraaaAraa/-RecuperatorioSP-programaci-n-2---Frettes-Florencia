package Controladores;

import Modelos.ProductosGeneral;
import Modelos.Ecologicos;
import Modelos.Quimicos;
import Excepciones.ProductoDuplicado;
import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ProdManager {
    
    private List<ProductosGeneral> listaProductos;
    private static final String ARCHIVO_JSON = "productos_data.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProdManager() {
        this.listaProductos = new ArrayList<>();
        cargarProductos();
    }

    // =========================================================================
    // Métodos de UTILIDAD
    // =========================================================================

    public List<ProductosGeneral> getListaProductos() {
        return listaProductos;
    }
    
    private boolean existeProducto(ProductosGeneral producto) {
        for (ProductosGeneral p : listaProductos) {
            if (p.equals(producto)) {
                return true;
            }
        }
        return false;
    }
    
    // =========================================================================
    // CRUD
    // =========================================================================
    
    public void agregarProducto(ProductosGeneral nuevoProducto) throws ProductoDuplicado {
        if (existeProducto(nuevoProducto)) {
            throw new ProductoDuplicado("Error: Ya existe un producto con el mismo nombre, concentración y fecha de vencimiento.");
        }
        
        this.listaProductos.add(nuevoProducto);
        guardarProductos();
    }

    public void eliminarProducto(String nombre, String concentracion, String fechaVencimiento) {
        boolean eliminado = this.listaProductos.removeIf(p -> 
            p.getNombre().equals(nombre) &&
            p.getConcentracion().equals(concentracion) &&
            p.getFechaVencimiento().equals(fechaVencimiento) // COMPARACIÓN DE STRING
        );
        if (eliminado) {
            guardarProductos();
        }
    }
    
    public void modificarProducto(ProductosGeneral productoAntiguo, ProductosGeneral productoModificado) throws ProductoDuplicado {
        boolean encontrado = this.listaProductos.removeIf(p -> 
            p.getNombre().equals(productoAntiguo.getNombre()) &&
            p.getConcentracion().equals(productoAntiguo.getConcentracion()) &&
            p.getFechaVencimiento().equals(productoAntiguo.getFechaVencimiento()) // COMPARACIÓN DE STRING
        );

        if (!encontrado) {
            throw new RuntimeException("El producto a modificar no fue encontrado en la lista.");
        }

        if (existeProducto(productoModificado)) {
            this.listaProductos.add(productoAntiguo);
            throw new ProductoDuplicado("Error: La modificación resulta en un producto duplicado.");
        }
        
        this.listaProductos.add(productoModificado);
        guardarProductos();
    }

    // =========================================================================
    // Persistencia JSON
    // =========================================================================

    private void guardarProductos() {
        try (FileWriter writer = new FileWriter(ARCHIVO_JSON)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(listaProductos, writer);
            System.out.println("Colección guardada automáticamente en JSON.");
        } catch (IOException e) {
            System.err.println("Error al guardar la colección en JSON: " + e.getMessage()); 
        }
    }
    
    private void cargarProductos() {
        File file = new File(ARCHIVO_JSON);
        if (file.exists()) {
            try (FileReader reader = new FileReader(ARCHIVO_JSON)) {
                
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(ProductosGeneral.class, new JsonDeserializer<ProductosGeneral>() {
                    @Override
                    public ProductosGeneral deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
                            throws JsonParseException {
                        
                        JsonObject jsonObject = json.getAsJsonObject();

                        if (jsonObject.has("etiquetaEco")) {
                            return context.deserialize(jsonObject, Ecologicos.class);
                        } else if (jsonObject.has("AdvertenciaQui")) {
                            return context.deserialize(jsonObject, Quimicos.class);
                        }
                        
                        throw new JsonParseException("No se pudo determinar el tipo de producto: " + jsonObject.toString());
                    }
                });
                
                // YA NO NECESITAMOS EL ADAPTADOR PARA LocalDate
                
                Gson gson = builder.create();
                Type tipoLista = new TypeToken<List<ProductosGeneral>>() {}.getType();
                
                List<ProductosGeneral> productosCargados = gson.fromJson(reader, tipoLista);
                if (productosCargados != null) {
                    this.listaProductos = productosCargados;
                }
                System.out.println("Colección cargada desde JSON.");
            } catch (IOException e) {
                System.err.println("Error al cargar la colección de JSON (IO): " + e.getMessage()); 
            } catch (JsonParseException e) {
                System.err.println("Advertencia: El archivo JSON está vacío o malformado. Iniciando con lista vacía. Detalle: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error inesperado al cargar JSON: " + e.getMessage());
            }
        } else {
            System.out.println("Archivo JSON no encontrado. Iniciando con lista vacía.");
        }
    }
    
    // =========================================================================
    // Reportes y Filtros
    // =========================================================================
    
    /**
     * Calcula productos próximos a vencer (60 días) convirtiendo String a LocalDate
     */
    public List<ProductosGeneral> getProductosProximosAVencer() {
        List<ProductosGeneral> proximosAVencer = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        
        for (ProductosGeneral producto : listaProductos) {
            try {
                // Convertir String a LocalDate para la comparación
                LocalDate fechaVenc = LocalDate.parse(producto.getFechaVencimiento(), DATE_FORMATTER);
                long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaVenc);
                
                if (diasRestantes >= 0 && diasRestantes <= 60) {
                    proximosAVencer.add(producto);
                }
            } catch (Exception e) {
                System.err.println("Error al parsear fecha del producto " + producto.getNombre() + ": " + e.getMessage());
            }
        }
        return proximosAVencer;
    }

    public void exportarProximosAVencerJSON() throws IOException {
        List<ProductosGeneral> productosAExportar = getProductosProximosAVencer();
        String archivoReporte = "productos_proximos_a_vencer.json";

        try (FileWriter writer = new FileWriter(archivoReporte)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(productosAExportar, writer);
        } catch (IOException e) {
            throw new IOException("Error al exportar el reporte JSON: " + e.getMessage());
        }
        System.out.println("Reporte de próximos a vencer exportado a: " + archivoReporte);
    }
}