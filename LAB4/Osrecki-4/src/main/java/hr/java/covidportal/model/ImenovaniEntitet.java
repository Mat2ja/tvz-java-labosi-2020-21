package main.java.hr.java.covidportal.model;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImenovaniEntitet that = (ImenovaniEntitet) o;
        return Objects.equals(naziv, that.naziv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naziv);
    }
}
