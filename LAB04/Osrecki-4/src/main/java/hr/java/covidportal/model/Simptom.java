package main.java.hr.java.covidportal.model;

import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;

import java.util.Objects;

/**
 * Predstavlja entitet simptoma definiranog nazivom i vrijedošću
 *
 * @author Matija
 */
public class Simptom extends ImenovaniEntitet {
    private VrijednostSimptoma vrijednost;


    /**
     * Inicijalizira podatak o nazivu i vrijednosti simptoma
     *
     * @param naziv      podatak o nazivu simptoma
     * @param vrijednost podatak o vrijednosti simptoma
     */
    public Simptom(String naziv, VrijednostSimptoma vrijednost) {
        super(naziv);
        this.vrijednost = vrijednost;
    }

    public VrijednostSimptoma getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(VrijednostSimptoma vrijednost) {
        this.vrijednost = vrijednost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Simptom simptom = (Simptom) o;
        return vrijednost == simptom.vrijednost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vrijednost);
    }
}

