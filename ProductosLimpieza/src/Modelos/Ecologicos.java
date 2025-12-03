package Modelos;

public class Ecologicos extends ProductosGeneral {
    private String etiquetaEco;

    public Ecologicos(String nombre, String concentracion, String fechaVencimiento, String etiquetaEco) {
        super(nombre, concentracion, fechaVencimiento);
        this.etiquetaEco = etiquetaEco;
    }

    @Override
    public String getDetalle() {
        return "Etiqueta: " + etiquetaEco;
    }

    public String getEtiquetaEco() {
        return etiquetaEco;
    }

    public void setEtiquetaEco(String etiquetaEco) {
        this.etiquetaEco = etiquetaEco;
    }
    
    @Override
    public String getTipo() {
        return "Ecológico";
    }
}