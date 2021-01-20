package main.java.hr.java.covidportal.model;

/**
 * Predstavlja sučelje koje traži implementaciju prelaska zaraze na osobu
 *
 * @author Matija
 */
public interface Zarazno {

    /**
     * Definira prelazak zaraze sa osobe na osobu
     *
     * @param osoba osoba koju treba zaraziti
     */
     void prelazakZarazeNaOsobu(Osoba osoba);

}
