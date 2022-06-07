package eu.europeana.entity.client.exception;

public class MultipleEntityFoundException extends RuntimeException {

    public MultipleEntityFoundException(String message, Exception e) {
        super(message, e);
    }

    public MultipleEntityFoundException(String message) {
        super(message);
    }
}