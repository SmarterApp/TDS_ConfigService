package tds.config.web.resources;

/**
 * A HATEOAS representation of an exception that occurred in the system.
 */
public class ExceptionMessageResource {
    private final String message;
    private final String code;

    public ExceptionMessageResource(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
