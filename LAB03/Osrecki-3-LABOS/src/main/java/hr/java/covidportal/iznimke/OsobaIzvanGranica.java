package main.java.hr.java.covidportal.iznimke;

/**
 * Predstavlja iznimku koja se javlja u slučaju pogrešnog odabira broja kontakata
 *
 * @author Matija
 */
public class OsobaIzvanGranica extends Exception {

    /**
     * Inicijalizira podatak o opisu iznimke
     *
     * @param message podatak o opisu iznimke
     */
    public OsobaIzvanGranica(String message) {
        super(message);
    }

    /**
     * Inicijalizira podatak o opisu i uzroku iznimke
     *
     * @param message podatak o opisu iznimke
     * @param cause   podatak o uzroku iznimke
     */
    public OsobaIzvanGranica(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Inicijalizira podatak o uzroku iznimke
     *
     * @param cause podatak o uzroku iznimke
     */
    public OsobaIzvanGranica(Throwable cause) {
        super(cause);
    }
}
