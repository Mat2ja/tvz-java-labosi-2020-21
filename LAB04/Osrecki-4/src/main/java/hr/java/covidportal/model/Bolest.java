package main.java.hr.java.covidportal.model;


import java.util.*;

/**
 * Predstavlja entitet bolesti definiran nazivom i simptomima
 *
 * @author Matija
 */
public class Bolest extends ImenovaniEntitet {
    private Set<Simptom> simptomi = new HashSet<>();

    /**
     * Inicijalizira podatak o nazivu i simptomima bolesti
     *  @param naziv    podatak o nazivu bolesti
     * @param simptomi podatak o listi simptoma bolesti
     */
    public Bolest(String naziv, Set<Simptom> simptomi) {
        super(naziv);
        this.simptomi = simptomi;
    }

    public Set<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(Set<Simptom> simptomi) {
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
}
