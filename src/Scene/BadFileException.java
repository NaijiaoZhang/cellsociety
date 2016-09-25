package Scene;

public class BadFileException extends Exception{

    public BadFileException () {
        super();
    }

    public BadFileException (String message) {
        super(message);
    }
}
