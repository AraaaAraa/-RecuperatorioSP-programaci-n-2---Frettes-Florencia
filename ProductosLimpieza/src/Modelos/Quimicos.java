package Modelos;

public class Quimicos extends ProductosGeneral {
    private String AdvertenciaQui;

    public Quimicos(String nombre, String concentracion, String fechaVencimiento, String AdvertenciaQui) {
        super(nombre, concentracion, fechaVencimiento);
        this.AdvertenciaQui = AdvertenciaQui;
    }
    
    @Override
    public String getTipo() {
        return "Químico";
    }
    
    @Override
    public String getDetalle() {
        return "Advertencia: " + this.AdvertenciaQui;
    }

    public String getAdvertenciaQui() {
        return AdvertenciaQui;
    }

    public void setAdvertenciaQui(String AdvertenciaQui) {
        this.AdvertenciaQui = AdvertenciaQui;
    }
}