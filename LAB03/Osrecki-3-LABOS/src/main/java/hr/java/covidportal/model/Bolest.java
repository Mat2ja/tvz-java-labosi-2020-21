package main.java.hr.java.covidportal.model;


/**
 * Predstavlja entitet bolesti definiran nazivom i simptomima
 *
 * @author Matija
 */
public class Bolest extends ImenovaniEntitet {
    private Simptom[] simptomi;

    /**
     * Inicijalizira podatak o nazivu i simptomima bolesti
     *
     * @param naziv    podatak o nazivu bolesti
     * @param simptomi podatak o listi simptoma bolesti
     */
    public Bolest(String naziv, Simptom[] simptomi) {
        super(naziv);
        this.simptomi = simptomi;
    }

    public Simptom[] getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(Simptom[] simptomi) {
        this.simptomi = simptomi;
    }

}
