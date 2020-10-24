package hr.java.covidportal.model;

public class Osoba {
    private String ime, prezime;
    private Integer bolest;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private Osoba[] kontaktiraneOsobe;

    public Osoba(String ime, String prezime, Integer bolest, Zupanija zupanija, Bolest zarazenBolescu,
                 Osoba[] kontaktiraneOsobe) {
        this.ime = ime;
        this.prezime = prezime;
        this.bolest = bolest;
        this.zupanija = zupanija;
        this.zarazenBolescu = zarazenBolescu;
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Integer getBolest() {
        return bolest;
    }

    public void setBolest(Integer bolest) {
        this.bolest = bolest;
    }

    public Zupanija getZupanija() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    public Bolest getZarazenBolescu() {
        return zarazenBolescu;
    }

    public void setZarazenBolescu(Bolest zarazenBolescu) {
        this.zarazenBolescu = zarazenBolescu;
    }

    public Osoba[] getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(Osoba[] kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }
}
