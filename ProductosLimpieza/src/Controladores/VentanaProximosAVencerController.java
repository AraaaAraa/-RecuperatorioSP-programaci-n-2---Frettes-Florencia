package Controladores;

import Modelos.ProductosGeneral;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class VentanaProximosAVencerController implements Initializable {
    
    private ProdManager manager;

    @FXML private TableView<ProductosGeneral> tblReporte;
    @FXML private TableColumn<ProductosGeneral, String> colNombre;
    @FXML private TableColumn<ProductosGeneral, String> colConcentracion;
    @FXML private TableColumn<ProductosGeneral, String> colVencimiento;
    @FXML private TableColumn<ProductosGeneral, String> colTipo;
    @FXML private TableColumn<ProductosGeneral, String> colDetalle;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colConcentracion.setCellValueFactory(new PropertyValueFactory<>("concentracion"));
        colVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento")); // DIRECTO
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colDetalle.setCellValueFactory(new PropertyValueFactory<>("detalle"));
    }
    
    public void setManager(ProdManager manager) {
        this.manager = manager;
        cargarReporte();
    }
    
    private void cargarReporte() {
        if (manager != null) {
            tblReporte.setItems(FXCollections.observableArrayList(manager.getProductosProximosAVencer()));
        }
    }

    @FXML
    private void handleExportarJSON() {
        try {
            manager.exportarProximosAVencerJSON();
            
            VentanaPrincipalController.mostrarAlerta(AlertType.INFORMATION, "Exportación Exitosa", 
                    "Reporte de productos próximos a vencer exportado exitosamente a productos_proximos_a_vencer.json");
            
        } catch (IOException e) {
            VentanaPrincipalController.mostrarAlerta(AlertType.ERROR, "Error de Archivo", 
                    "No se pudo guardar el archivo JSON: " + e.getMessage());
        }
    }
}