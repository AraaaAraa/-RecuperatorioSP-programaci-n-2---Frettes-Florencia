package productoslimpieza;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ProductosLimpieza extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            // Carga la interfaz gráfica (FXML) de la ventana principal
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Vistas/VentanaPrincipal.fxml"));
            
            Parent root = loader.load();

            Scene scene = new Scene(root);
            
            primaryStage.setTitle("Sistema de Gestión de Productos de Limpieza - UTN");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (IOException e) {
            System.err.println("Error al cargar la interfaz gráfica: " + e.getMessage());
            e.printStackTrace();
            // Se puede comentar System.exit(1) para evitar cierre abrupto, solo informando el error.
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}