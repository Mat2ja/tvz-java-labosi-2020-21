package main.java.hr.java.covidportal.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Predstavlja entitet bolesti definiran nazivom i simptomima
 *
 * @author Matija
 */
public class Bolest extends ImenovaniEntitet {
    private List<Simptom> simptomi;


    /**
     * Inicijalizira podatak o nazivu i simptomima bolesti
     *
     * @param id       podatak o id bolesti
     * @param naziv    podatak o nazivu bolesti
     * @param simptomi podatak o listi simptoma bolesti
     */
    public Bolest(Long id, String naziv, List<Simptom> simptomi) {
        super(id, naziv);
        this.simptomi = simptomi;
    }

    public List<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(List<Simptom> simptomi) {
        this.simptomi = simptomi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Bolest bolest = (Bolest) o;
        return Objects.equals(simptomi, bolest.simptomi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), simptomi);
    }

    @Override
    public String toString() {
        return getNaziv();
    }
}
