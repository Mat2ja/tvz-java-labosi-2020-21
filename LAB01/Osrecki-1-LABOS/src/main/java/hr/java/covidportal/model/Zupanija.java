package main.java.hr.java.covidportal.model;

public class Zupanija {
    private String naziv;
    private Integer brojStanovnika;
    private String sifra;

    public Zupanija(String naziv, Integer brojStanovnika, String sifra) {
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.sifra = sifra;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(Integer brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }
}
