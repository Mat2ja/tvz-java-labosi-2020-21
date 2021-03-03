package main.java.hr.java.covidportal.model;

import java.util.List;
import java.util.Objects;

/**
 * Predstavlja entitet virusa definiranog nazivom i simtptomima
 *
 * @author Matija
 */
public class Virus extends Bolest implements Zarazno {

    private String opis;

    public Virus(Long id, String naziv, List<Simptom> simptomi) {
        super(id, naziv, simptomi);
    }


    /**
     * Inicijalizira podatak o nazivu i simptomima bolesti
     *
     * @param id    podatak o id virusa
     * @param naziv    podatak o nazivu virusa
     * @param simptomi podatak o simptomima virusa
     * @param opis podatak o opisu virusa
     */
    public Virus(Long id, String naziv,  String opis, List<Simptom> simptomi) {
        super(id, naziv, simptomi);
        this.opis = opis;
    }


    /**
     * Zaražuje novu osobu virusom trenutno zaražene osobe
     *
     * @param osoba objekt osobe
     */
    @Override
    public void prelazakZarazeNaOsobu(Osoba osoba) {
        osoba.setZarazenBolescu(this);
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
        Virus virus = (Virus) o;
        return Objects.equals(opis, virus.opis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), opis);
    }
}
