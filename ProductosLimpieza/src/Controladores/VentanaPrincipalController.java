package Controladores;

import Excepciones.NombreVacioException;
import Excepciones.ProductoDuplicado;
import Excepciones.ProductoVencidoException;
import Modelos.Ecologicos;
import Modelos.ProductosGeneral;
import Modelos.Quimicos;

import javafx.event.ActionEvent; 
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

public class VentanaPrincipalController implements Initializable {
    
    private ProdManager manager = new ProdManager(); 

    @FXML private TableView<ProductosGeneral> tblProductos;
    @FXML private TableColumn<ProductosGeneral, String> colNombre;
    @FXML private TableColumn<ProductosGeneral, String> colConcentracion;
    @FXML private TableColumn<ProductosGeneral, String> colVencimiento;
    @FXML private TableColumn<ProductosGeneral, String> colTipo;
    @FXML private TableColumn<ProductosGeneral, String> colDetalle;
    
    @FXML private TextField txtNombre;
    @FXML private TextField txtConcentracion;
    @FXML private TextField txtVencimiento; // CAMBIADO DE DatePicker A TextField
    @FXML private ComboBox<String> cmbTipo; 
    
    @FXML private Label lblEspecifico1;
    @FXML private TextField txtAdvertenciaQui; 
    
    @FXML private Label lblEspecifico2;
    @FXML private TextField txtEtiquetaEco;   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización de Columnas
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colConcentracion.setCellValueFactory(new PropertyValueFactory<>("concentracion"));
        colVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento")); // DIRECTO
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colDetalle.setCellValueFactory(new PropertyValueFactory<>("detalle"));

        // Inicialización del ComboBox
        cmbTipo.setItems(FXCollections.observableArrayList("Químico", "Ecológico"));
        cmbTipo.setValue("Químico");
        handleTipoProducto(null);

        cargarTabla(); 

        // Listener para precargar datos al seleccionar
        tblProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDatosSeleccionados(newSelection);
            }
        });
    }

    private void cargarDatosSeleccionados(ProductosGeneral producto) {
        txtNombre.setText(producto.getNombre());
        txtConcentracion.setText(producto.getConcentracion());
        txtVencimiento.setText(producto.getFechaVencimiento()); // DIRECTO
        cmbTipo.setValue(producto.getTipo());
        
        handleTipoProducto(null);
        
        if (producto instanceof Quimicos) {
            txtAdvertenciaQui.setText(((Quimicos) producto).getAdvertenciaQui());
            txtEtiquetaEco.clear();
        } else if (producto instanceof Ecologicos) {
            txtEtiquetaEco.setText(((Ecologicos) producto).getEtiquetaEco());
            txtAdvertenciaQui.clear();
        }
    }
        
    private void cargarTabla() {
        tblProductos.setItems(FXCollections.observableArrayList(manager.getListaProductos()));
    }

    // =========================================================================
    // Métodos CRUD
    // =========================================================================

    @FXML
    private void handleAgregarProducto() {
        try {
            String nombre = txtNombre.getText();
            String concentracion = txtConcentracion.getText();
            String fechaVencimiento = txtVencimiento.getText(); // STRING DIRECTO
            String tipo = cmbTipo.getValue();
            
            // Validaciones
            if (nombre.isEmpty() || concentracion.isEmpty() || fechaVencimiento.isEmpty()) {
                mostrarAlerta(AlertType.ERROR, "Error de Formato", "Nombre, Concentración y Fecha no pueden estar vacíos.");
                return;
            }

            ProductosGeneral nuevoProducto;

            if ("Químico".equals(tipo)) {
                String advertencia = txtAdvertenciaQui.getText();
                if (advertencia.isEmpty()) {
                    mostrarAlerta(AlertType.ERROR, "Error de Formato", "El campo de Advertencia Química no puede estar vacío.");
                    return;
                }
                nuevoProducto = new Quimicos(nombre, concentracion, fechaVencimiento, advertencia);
                
            } else if ("Ecológico".equals(tipo)) {
                String etiqueta = txtEtiquetaEco.getText();
                if (etiqueta.isEmpty()) {
                    mostrarAlerta(AlertType.ERROR, "Error de Formato", "El campo de Etiqueta Ecológica no puede estar vacío.");
                    return;
                }
                nuevoProducto = new Ecologicos(nombre, concentracion, fechaVencimiento, etiqueta);
            } else {
                mostrarAlerta(AlertType.ERROR, "Error de Tipo", "Debe seleccionar un tipo de producto válido.");
                return;
            }

            manager.agregarProducto(nuevoProducto); 
            
            cargarTabla();
            handleLimpiarCampos(); 
            mostrarAlerta(AlertType.INFORMATION, "Éxito", "Producto agregado correctamente.");

        } catch (NombreVacioException | ProductoVencidoException | ProductoDuplicado e) {
            mostrarAlerta(AlertType.ERROR, "Error de Validación", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error Inesperado", "Ocurrió un error: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleModificarProducto() {
        ProductosGeneral productoAntiguo = tblProductos.getSelectionModel().getSelectedItem();
       
        if (productoAntiguo == null) {
            mostrarAlerta(AlertType.ERROR, "Error", "Debe seleccionar un producto de la tabla para modificar.");
            return;
        }
    
        try {
            String nombre = txtNombre.getText();
            String concentracion = txtConcentracion.getText();
            String fechaVencimiento = txtVencimiento.getText(); // STRING DIRECTO
            String tipo = cmbTipo.getValue();
        
            if (nombre.isEmpty() || concentracion.isEmpty() || fechaVencimiento.isEmpty()) {
                mostrarAlerta(AlertType.ERROR, "Error de Formato", "Nombre, Concentración y Fecha no pueden estar vacíos.");
                return;
            }

            ProductosGeneral productoModificado;

            if ("Químico".equals(tipo)) {
                String advertencia = txtAdvertenciaQui.getText();
                if (advertencia.isEmpty()) {
                    mostrarAlerta(AlertType.ERROR, "Error de Formato", "El campo de Advertencia Química no puede estar vacío.");
                    return;
                }
                productoModificado = new Quimicos(nombre, concentracion, fechaVencimiento, advertencia);
                
            } else if ("Ecológico".equals(tipo)) {
                String etiqueta = txtEtiquetaEco.getText();
                if (etiqueta.isEmpty()) {
                    mostrarAlerta(AlertType.ERROR, "Error de Formato", "El campo de Etiqueta Ecológica no puede estar vacío.");
                    return;
                }
                productoModificado = new Ecologicos(nombre, concentracion, fechaVencimiento, etiqueta);
            } else {
                mostrarAlerta(AlertType.ERROR, "Error de Tipo", "Debe seleccionar un tipo de producto válido.");
                return;
            }

            manager.modificarProducto(productoAntiguo, productoModificado); 
            
            cargarTabla();
            handleLimpiarCampos(); 
            mostrarAlerta(AlertType.INFORMATION, "Éxito", "Producto modificado correctamente.");

        } catch (NombreVacioException | ProductoVencidoException | ProductoDuplicado e) {
            mostrarAlerta(AlertType.ERROR, "Error de Validación", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error Inesperado", "Ocurrió un error: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleEliminarProducto() {
        ProductosGeneral productoSeleccionado = tblProductos.getSelectionModel().getSelectedItem();
        
        if (productoSeleccionado == null) {
            mostrarAlerta(AlertType.ERROR, "Error", "Debe seleccionar un producto de la tabla para eliminar.");
            return;
        }
        
        try {
            manager.eliminarProducto(
                productoSeleccionado.getNombre(),
                productoSeleccionado.getConcentracion(),
                productoSeleccionado.getFechaVencimiento()
            );
            
            cargarTabla();
            handleLimpiarCampos();
            mostrarAlerta(AlertType.INFORMATION, "Éxito", "Producto eliminado correctamente.");
            
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error Inesperado", "Ocurrió un error al intentar eliminar: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLimpiarCampos() { 
        txtNombre.clear();
        txtConcentracion.clear();
        txtVencimiento.clear(); // CAMBIO
        txtAdvertenciaQui.clear();
        txtEtiquetaEco.clear();
        cmbTipo.setValue("Químico");
        handleTipoProducto(null);
    }

    @FXML
    private void handleAbrirVentanaReporte() { 
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/VentanaProximosAVencer.fxml"));
            Parent root = loader.load();
            
            VentanaProximosAVencerController reporteController = loader.getController();
            reporteController.setManager(manager);

            Stage stage = new Stage();
            stage.setTitle("Reporte de Productos Próximos a Vencer (60 días)");
            stage.setScene(new Scene(root));
            stage.show();
            
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error de Vista", "No se pudo cargar la ventana de reporte: " + e.getMessage());
        }
    }

    @FXML
    private void handleTipoProducto(ActionEvent event) {
        String tipoSeleccionado = cmbTipo.getSelectionModel().getSelectedItem();

        if (tipoSeleccionado == null || tipoSeleccionado.isEmpty()) {
            // ocultar todo
        } else if (tipoSeleccionado.equals("Químico")) {
            lblEspecifico1.setText("Tipo de Advertencia:");
            txtAdvertenciaQui.setVisible(true);
            txtAdvertenciaQui.setDisable(false);
            
            lblEspecifico2.setText("");
            txtEtiquetaEco.setVisible(false);
            txtEtiquetaEco.setDisable(true);
            txtEtiquetaEco.clear(); 
            
        } else if (tipoSeleccionado.equals("Ecológico")) {
            lblEspecifico2.setText("Etiqueta Ecológica:");
            txtEtiquetaEco.setVisible(true);
            txtEtiquetaEco.setDisable(false);
            
            lblEspecifico1.setText("");
            txtAdvertenciaQui.setVisible(false);
            txtAdvertenciaQui.setDisable(true);
            txtAdvertenciaQui.clear(); 
        }
    }
    
    public static void mostrarAlerta(AlertType tipo, String titulo, String mensaje) { 
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}