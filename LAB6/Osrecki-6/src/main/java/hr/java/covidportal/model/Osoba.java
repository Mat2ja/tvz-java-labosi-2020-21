package main.java.hr.java.covidportal.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Predstavlja entitet osobe definirane imenom, prezimenom, starosti, županijom,
 * zaraženom bolesti i listom kontaktiranih osoba
 *
 * @author Matija
 */
public class Osoba {
    private Long id;
    private String ime;
    private String prezime;
    private Integer starost;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private List<Osoba> kontaktiraneOsobe = new ArrayList<>();

    /**
     * Predstavlja Builder Pattern kojim se inicijalizira nova osoba
     */
    public static class Builder {
        private Long id;
        private String ime;
        private String prezime;
        private Integer starost;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private List<Osoba> kontaktiraneOsobe = new ArrayList<>();

        /**
         * Inicijalizira Builder sa podatkom o id osobe
         *
         * @param id podatak o id osobe
         */
        public Builder(Long id) {
            this.id = id;
        }

        /**
         * Postavlja Builderu podatak o prezimenu osobe
         *
         * @param ime podatak o imenu osobe
         * @return objekt Buildera
         */
        public Builder hasIme(String ime) {
            this.ime = ime;
            return this;
        }

        /**
         * Postavlja Builderu podatak o prezimenu osobe
         *
         * @param prezime podatak o prezimenu osobe
         * @return objekt Buildera
         */
        public Builder hasPrezime(String prezime) {
            this.prezime = prezime;
            return this;
        }

        /**
         * Postavlja Builderu podatak o starosti osobe
         *
         * @param starost podatak o starosti osobe
         * @return objekt Buildera
         */
        public Builder isAged(Integer starost) {
            this.starost = starost;
            return this;
        }

        /**
         * Postavlja Builderu podatak o županiji osobe
         *
         * @param zupanija podatak o županiji osobe
         * @return objekt Buildera
         */
        public Builder atZupanija(Zupanija zupanija) {
            this.zupanija = zupanija;
            return this;
        }

        /**
         * Postavlja Builderu podatak o zaraženoj bolesti osobe
         *
         * @param zarazenBolescu podatak o zaraženoj bolesti osobe
         * @return objekt Buildera
         */
        public Builder withBolest(Bolest zarazenBolescu) {
            this.zarazenBolescu = zarazenBolescu;
            return this;
        }

        /**
         * Postavlja Builderu podatak o kontaktiranim osoboma
         *
         * @param kontaktiraneOsobe podatak o listi kontaktiranih osoba
         * @return objekt Buildera
         */
        public Builder withKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
            this.kontaktiraneOsobe = kontaktiraneOsobe;
            return this;
        }

        /**
         * Inicijalzira instancu osobe i postavlja podatke o imenu, prezimenu, starosti, županiji,
         * zaraženoj bolesti i kontaktiranim osobama osobe
         *
         * @return instanca osobe
         */
        public Osoba build() {
            Osoba osoba = new Osoba();
            osoba.id = id;
            osoba.ime = ime;
            osoba.prezime = prezime;
            osoba.starost = starost;
            osoba.zupanija = zupanija;
            osoba.zarazenBolescu = zarazenBolescu;
            osoba.kontaktiraneOsobe = kontaktiraneOsobe;

            if (osoba.zarazenBolescu instanceof Virus virus && osoba.kontaktiraneOsobe != null) {
                for (Osoba kontakt : kontaktiraneOsobe) {
                    virus.prelazakZarazeNaOsobu(kontakt);
                }
            }

            return osoba;
        }

    }

    /**
     * Inicijalizira praznu instancu osobe
     */
    private Osoba() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Osoba> getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(List<Osoba> kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }

    @Override
    public String toString() {
        return ime + " " + prezime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Osoba osoba = (Osoba) o;
        return Objects.equals(id, osoba.id) &&
                Objects.equals(ime, osoba.ime) &&
                Objects.equals(prezime, osoba.prezime) &&
                Objects.equals(starost, osoba.starost) &&
                Objects.equals(zupanija, osoba.zupanija) &&
                Objects.equals(zarazenBolescu, osoba.zarazenBolescu) &&
                Objects.equals(kontaktiraneOsobe, osoba.kontaktiraneOsobe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ime, prezime, starost, zupanija, zarazenBolescu, kontaktiraneOsobe);
    }
}
