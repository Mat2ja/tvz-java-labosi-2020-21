package main.java.hr.java.covidportal.model;

public class Osoba {
    private String ime;
    private String prezime;
    private Integer starost;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private Osoba[] kontaktiraneOsobe;

    public static class Builder {
        private String ime; 
        private String prezime;
        private Integer starost;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private Osoba[] kontaktiraneOsobe;

        public Builder(String ime) {
            this.ime = ime;
        }

        public Builder hasPrezime(String prezime) {
            this.prezime = prezime;
            return this;
        }

        public Builder isAged(Integer starost) {
            this.starost = starost;
            return this;
        }

        public Builder atZupanija(Zupanija zupanija) {
            this.zupanija = zupanija;
            return this;
        }

        public Builder withBolest(Bolest zarazenBolescu) {
            this.zarazenBolescu = zarazenBolescu;
            return this;
        }

        public Builder withKontaktiraneOsobe(Osoba[] kontaktiraneOsobe) {
            this.kontaktiraneOsobe = kontaktiraneOsobe;
            return this;
        }

        public Osoba build() {
            Osoba osoba = new Osoba();
            osoba.ime = ime;
            osoba.prezime = prezime;
            osoba.starost = starost;
            osoba.zupanija = zupanija;
            osoba.zarazenBolescu = zarazenBolescu;
            osoba.kontaktiraneOsobe = kontaktiraneOsobe;

            // ako je osoba zarazena virusom, zarazi sve osobe s kojima je bila u kontaktu
            if (osoba.zarazenBolescu instanceof Virus virus && osoba.kontaktiraneOsobe != null) {
                for (Osoba kontakt : kontaktiraneOsobe) {
                    virus.prelazakZarazeNaOsobu(kontakt);
                }
            }

            return osoba;
        }

    }

    private Osoba() {
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

    public Integer getStarost() {
        return starost;
    }

    public void setStarost(Integer starost) {
        this.starost = starost;
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
