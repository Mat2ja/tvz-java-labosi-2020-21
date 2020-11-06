package main.java.hr.java.covidportal.model;

import java.util.Objects;

/**
 * Predstavlja entitet simptoma definiranog nazivom i vrijedošću
 *
 * @author Matija
 */
public class Simptom extends ImenovaniEntitet {
    private String vrijednost;


    /**
     * Inicijalizira podatak o nazivu i vrijednosti simptoma
     *
     * @param naziv      podatak o nazivu simptoma
     * @param vrijednost podatak o vrijednosti simptoma
     */
    public Simptom(String naziv, String vrijednost) {
        super(naziv);
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(String vrijednost) {
        this.vrijednost = vrijednost;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Simptom simptom = (Simptom) o;
        return Objects.equals(vrijednost, simptom.vrijednost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vrijednost);
    }
}

