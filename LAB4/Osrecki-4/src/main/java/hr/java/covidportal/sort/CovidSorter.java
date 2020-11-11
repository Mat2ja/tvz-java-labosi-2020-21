package main.java.hr.java.covidportal.sort;

import main.java.hr.java.covidportal.model.Zupanija;

import java.util.Comparator;

public class CovidSorter implements Comparator<Zupanija> {

    @Override
    public int compare(Zupanija z1, Zupanija z2) {
//        if (z1.getPostotakZarazenih() > z2.getPostotakZarazenih()) return -1;
//        else if (z1.getPostotakZarazenih() < z2.getPostotakZarazenih()) return 1;
//        else return 0;
        return z2.getPostotakZarazenih().compareTo(z1.getPostotakZarazenih());
    }
}
