package main.java.hr.java.covidportal.iznimke;

/**
 * Predstavlja iznimku koja se javlja prilikom unosa simptoma koji je već unesen
 *
 * @author Matija
 */
public class DupliSimptomException extends RuntimeException {

    /**
     * Inicijalizira podatak o opisu iznimke
     *
     * @param message podatak o opisu iznimke
     */
    public DupliSimptomException(String message) {
        super(message);
    }

    /**
     * Inicijalizira podatak o opisu i uzroku iznimke
     *
     * @param message podatak o opisu iznimke
     * @param cause   podatak o uzroku iznimke
     */
    public DupliSimptomException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Inicijalizira podatak o uzroku iznimke
     *
     * @param cause podatak o uzroku iznimke
     */
    public DupliSimptomException(Throwable cause) {
        super(cause);
    }
}
