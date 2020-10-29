package main.java.hr.java.covidportal.model;

public abstract class Tegoba{

    private String naziv;
    private Simptom[] simptomi;

    public Tegoba(String naziv, Simptom[] simptomi) {
        this.naziv = naziv;
        this.simptomi = simptomi;
    }

    public Simptom[] getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(Simptom[] simptomi) {
        this.simptomi = simptomi;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
