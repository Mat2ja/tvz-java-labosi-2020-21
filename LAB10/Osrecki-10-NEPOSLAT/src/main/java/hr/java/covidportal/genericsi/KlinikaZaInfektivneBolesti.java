package main.java.hr.java.covidportal.genericsi;

import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Virus;

import java.util.List;

public class KlinikaZaInfektivneBolesti<T extends Virus, S extends Osoba> {

    List<T> virusi;
    List<S> osobe;

    public KlinikaZaInfektivneBolesti(List<T> virusi, List<S> osobe) {
        this.virusi = virusi;
        this.osobe = osobe;
    }

    public void addVirus(T virus) {
        this.virusi.add(virus);
    }

    public void addOsoba(S osoba) {
        this.osobe.add(osoba);
    }

    public List<T> getVirusi() {
        return virusi;
    }

    public void setVirusi(List<T> virusi) {
        this.virusi = virusi;
    }

    public List<S> getOsobe() {
        return osobe;
    }

    public void setOsobe(List<S> osobe) {
        this.osobe = osobe;
    }


}
