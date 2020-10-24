package hr.java.covidportal.main;


import hr.java.covidportal.model.Bolest;
import hr.java.covidportal.model.Osoba;
import hr.java.covidportal.model.Simptom;
import hr.java.covidportal.model.Zupanija;

import java.util.Scanner;

public class Glavna {
    private static final Integer BROJ_ZUPANIJA = 3;
    private static final Integer BROJ_SIMPTOMA = 3;
    private static final Integer BROJ_BOLESTI = 3;
    private static final Integer BROJ_OSOBA = 3;

    public static void main(String[] args) {

        Scanner tipkovnica = new Scanner(System.in);

        Zupanija[] zupanije = new Zupanija[BROJ_ZUPANIJA];
        Simptom[] simptomi = new Simptom[BROJ_SIMPTOMA];
        Bolest[] bolesti = new Bolest[BROJ_BOLESTI];
        Osoba[] osobe = new Osoba[BROJ_OSOBA];

        for (int i = 0; i < BROJ_ZUPANIJA; i++) {
            zupanije[i] = unosZupanije(tipkovnica, i);
        }

        for (int i = 0; i < BROJ_SIMPTOMA; i++) {
            simptomi[i] = unosSimptoma(tipkovnica, i);
        }

        for (int i = 0; i < BROJ_BOLESTI; i++) {
            bolesti[i] = unosBolesti(tipkovnica, simptomi, i);
        }

        for (int i = 0; i < BROJ_OSOBA; i++) {
            //osobe[i] = unosOsoba(tipkovnica, simptomi, i);
            System.out.println("Unesite ime za " + (i + 1) + ". osobu: ");
            String ime = tipkovnica.nextLine();
            System.out.println("Unesite prezime za " + (i + 1) + ". osobu: ");
            String prezime = tipkovnica.nextLine();

            Integer indexOdabraneZupanije = 0;
            do {
                System.out.println("Unesite županiju: ");

                for (int j = 0; j < BROJ_ZUPANIJA; j++) {
                    System.out.println((j+1) + ". " + zupanije[i].getNaziv());
                }

                System.out.print("Odabir >> ");
                indexOdabraneZupanije = tipkovnica.nextInt();
                tipkovnica.nextLine();

                if (indexOdabraneZupanije < 1 || indexOdabraneZupanije > BROJ_ZUPANIJA) {
                    System.out.println("Neispravan unos, napisi opet");
                }
                //todo isInRange metoda
            } while (indexOdabraneZupanije < 1 || indexOdabraneZupanije > BROJ_ZUPANIJA);
        }
    }

    private static Zupanija unosZupanije(Scanner tipkovnica, Integer i) {
        System.out.print("Unesite podatke za " + (i + 1) + ". županiju: ");
        String naziv = tipkovnica.nextLine();
        System.out.print("Unesite broj stanovnika za " + (i + 1) + ". županiju: ");
        Integer brojStanovnika = tipkovnica.nextInt();
        tipkovnica.nextLine(); // ocisti buffer jer 'proguta' enter

        return new Zupanija(naziv, brojStanovnika);
    }

    private static Simptom unosSimptoma(Scanner tipkovnica, int i) {
        System.out.print("Unesite naziv za " + (i + 1) + ". simptom: ");
        String naziv = tipkovnica.nextLine();
        System.out.print("Unesite vrijednost za " + (i + 1) + ". simptom (RIJETKO, SREDNJE ILI ČESTO): ");
        String vrijednost = tipkovnica.nextLine();

        return new Simptom(naziv, vrijednost);
    }


    private static Bolest unosBolesti(Scanner tipkovnica, Simptom[] simptomi,  int i) {
        System.out.print("Unesite naziv za " + (i + 1) + ". bolest: ");
        String naziv = tipkovnica.nextLine();
        System.out.print("Unesite broj simptoma za " + (i + 1) + ". bolest: ");
        Integer brojSimptomaZaBolest = tipkovnica.nextInt();
        tipkovnica.nextLine();

        Simptom[] simptomiBolesti = new Simptom[brojSimptomaZaBolest];

        for (int j = 0; j < brojSimptomaZaBolest; j++) {

            Integer indexOdabranogSimptoma = odabirSimptoma(tipkovnica, simptomi);

            simptomiBolesti[j] = simptomi[indexOdabranogSimptoma - 1];
        }

        return new Bolest(naziv, simptomiBolesti);
    }

    private static Integer odabirSimptoma(Scanner tipkovnica, Simptom[] simptomi) {
        Integer odabraniSimptom = 0;

        do {
            System.out.println("Odaberite simptom: ");

            for (int k = 0; k < BROJ_SIMPTOMA; k++) {
                System.out.println((k + 1) + ". " + simptomi[k].getNaziv() + " " + simptomi[k].getVrijednost());
            }

            System.out.print("Odabir >> ");
            odabraniSimptom = tipkovnica.nextInt();
            tipkovnica.nextLine();

            if (odabraniSimptom < 1|| odabraniSimptom > BROJ_SIMPTOMA) {
                System.out.println("Neispravan unos, molim pokušsajte ponovno!");
            }

        } while (odabraniSimptom < 1|| odabraniSimptom > BROJ_SIMPTOMA);

        return odabraniSimptom;
    }

}
