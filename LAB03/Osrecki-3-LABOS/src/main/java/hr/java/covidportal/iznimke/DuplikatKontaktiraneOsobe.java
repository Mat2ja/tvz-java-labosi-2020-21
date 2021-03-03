package main.java.hr.java.covidportal.iznimke;

/**
 * Predstavlja iznimku koja se javlja u slučaju unosa duplikata osobe
 *
 * @author Matija
 */
public class DuplikatKontaktiraneOsobe extends Exception {

    /**
     * Inicijalizira podatak o opisu iznimke
     *
     * @param message podatak o opisu iznimke
     */
    public DuplikatKontaktiraneOsobe(String message) {
        super(message);
    }


    /**
     * Inicijalizira podatak o opisu i uzroku iznimke
     *
     * @param message podatak o opisu iznimke
     * @param cause   podatak o uzroku iznimke
     */
    public DuplikatKontaktiraneOsobe(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * Inicijalizira podatak o uzroku iznimke
     *
     * @param cause podatak o uzroku iznimke
     */
    public DuplikatKontaktiraneOsobe(Throwable cause) {
        super(cause);
    }

}
