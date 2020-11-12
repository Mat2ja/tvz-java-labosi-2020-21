package main.java.hr.java.covidportal.model;


import java.util.*;

/**
 * Predstavlja entitet bolesti definiran nazivom i simptomima
 *
 * @author Matija
 */
public class Bolest extends ImenovaniEntitet {
    private Set<Simptom> simptomi = new HashSet<>();
    private String opis;

    /**
     * Inicijalizira podatak o nazivu i simptomima bolesti
     *  @param naziv    podatak o nazivu bolesti
     * @param simptomi podatak o listi simptoma bolesti
     */
    public Bolest(String naziv, Set<Simptom> simptomi, String opis) {
        super(naziv);
        this.simptomi = simptomi;
        this.opis = opis;
    }

    public Set<Simptom> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(Set<Simptom> simptomi) {
        this.simptomi = simptomi;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Bolest bolest = (Bolest) o;
        return Objects.equals(simptomi, bolest.simptomi) &&
                Objects.equals(opis, bolest.opis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), simptomi, opis);
    }

    @Override
    public String toString() {
        return getNaziv() + " (" + opis + ")";
    }
}
