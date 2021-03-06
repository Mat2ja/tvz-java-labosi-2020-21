package main.java.hr.java.covidportal.niti;

import main.java.hr.java.covidportal.model.Zupanija;

import java.util.List;

public class NajviseZarazenaNit implements Runnable {

    private static List<Zupanija> zupanije;

    public NajviseZarazenaNit(List<Zupanija> listaZupanija) {
        this.zupanije = listaZupanija;
    }

    @Override
    public void run() {
        Zupanija najzarazenijaZupanija = zupanije.stream()
                .min((z1, z2) -> z2.getPostotakZarazenih().compareTo(z1.getPostotakZarazenih()))
                .get();
        System.out.println("Najzaraženija županija: "
                + najzarazenijaZupanija.getNaziv() + " - "
                + najzarazenijaZupanija.getPostotakZarazenih() + "%");
    }
}
