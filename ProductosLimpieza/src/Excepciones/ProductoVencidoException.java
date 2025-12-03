package Excepciones;

// Extiende de RuntimeException, al igual que SerieInvalido
public class ProductoVencidoException extends RuntimeException {
    public ProductoVencidoException(String message) {
        super(message);
    }
}