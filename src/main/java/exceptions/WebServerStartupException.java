package exceptions;

public class WebServerStartupException extends RuntimeException {
    public WebServerStartupException(Exception e) {
        super(e);
    }
}
