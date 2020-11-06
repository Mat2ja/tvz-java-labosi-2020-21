package main.java.hr.java.covidportal.iznimke;

/**
 * Predstavlja iznimku koja se javlja u slučaju unosa neispravnog naziva simptoma
 *
 * @author Matija
 */
public class NeispravanNazivException extends Exception {

    /**
     * Inicijalizira podatak o opisu iznimke
     *
     * @param message podatak o opisu iznimke
     */
    public NeispravanNazivException(String message) {
        super(message);
    }

    /**
     * Inicijalizira podatak o opisu i uzroku iznimke
     *
     * @param message podatak o opisu iznimke
     * @param cause   podatak o uzroku iznimke
     */
    public NeispravanNazivException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Inicijalizira podatak o uzroku iznimke
     *
     * @param cause podatak o uzroku iznimke
     */
    public NeispravanNazivException(Throwable cause) {
        super(cause);
    }
}
