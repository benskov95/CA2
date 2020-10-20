package exceptions;

public class PersonNotFound extends Exception {
    public PersonNotFound(String message) {
        super(message);
    }
}