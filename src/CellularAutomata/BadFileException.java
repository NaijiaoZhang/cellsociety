package CellularAutomata;

public class BadFileException extends Exception{

    public BadFileException () {
        super();
    }

    public BadFileException (String message) {
        super(message);
    }
    
    public BadFileException (Throwable cause, String message) {
        super(String.format(message), cause);
    }
}
