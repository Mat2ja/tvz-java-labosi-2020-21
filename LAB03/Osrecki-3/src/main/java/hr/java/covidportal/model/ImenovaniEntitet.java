package main.java.hr.java.covidportal.model;

/**
 * Predstavlja entitet definiram nazivom
 */
public abstract class ImenovaniEntitet {
    private String naziv;

    /**
     * Inicijalizira podatak o nazivu entiteta
     *
     * @param naziv podatak o nazivu entiteta
     */
    public ImenovaniEntitet(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
