package main.java.hr.java.covidportal.iznimke;

/**
 * Predstavlja iznimku koja se javlja u slučaju unosa bolesti istih simptoma
 *
 * @author Matija
 */
public class BolestIstihSimptoma extends RuntimeException {

    /**
     * Inicijalizira podatak o opisu iznimke
     *
     * @param message podatak o opisu iznimke
     */
    public BolestIstihSimptoma(String message) {
        super(message);
    }

    /**
     * Inicijalizira podatak o opisu i uzroku iznimke
     *
     * @param message podatak o opisu iznimke
     * @param cause   podatak o uzroku iznimke
     */
    public BolestIstihSimptoma(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Inicijalizira podatak o uzroku iznimke
     *
     * @param cause podatak o uzroku iznimke
     */
    public BolestIstihSimptoma(Throwable cause) {
        super(cause);
    }
}
