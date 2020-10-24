package main.java.hr.java.covidportal.main;

import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.Zupanija;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Glavna {
    private static final Integer BROJ_SIMPTOMA = 3;
    private static final Integer BROJ_BOLESTI = 3;
    private static final Integer BROJ_OSOBA = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nCORONA FINDER 9000\n");

        Integer BROJ_ZUPANIJA = unosBroja(scanner, ">> Unesite broj županija: ");

        Zupanija[] zupanije = new Zupanija[BROJ_ZUPANIJA];
        Simptom[] simptomi = new Simptom[BROJ_SIMPTOMA];
        Bolest[] bolesti = new Bolest[BROJ_BOLESTI];
        Osoba[] osobe = new Osoba[BROJ_OSOBA];
        final String[] vrijednostiSimptoma = new String[]{"RIJETKO", "SREDNJE", "ČESTO"};


        System.out.println("\nUnesite podatke o " + BROJ_ZUPANIJA + " županije:");
        for (int i = 0; i < BROJ_ZUPANIJA; i++) {
            zupanije[i] = unosZupanije(scanner, i);
        }

        System.out.println("\nUnesite podatke o " + BROJ_SIMPTOMA + " simptoma:");
        for (int i = 0; i < BROJ_SIMPTOMA; i++) {
            simptomi[i] = unosSimptoma(scanner, vrijednostiSimptoma, i);
        }

        System.out.println("\nUnesite podatke o " + BROJ_BOLESTI + " bolesti:");
        for (int i = 0; i < BROJ_BOLESTI; i++) {
            bolesti[i] = unosBolesti(scanner, simptomi, i);
        }

        System.out.println("\nUnesite podatke o " + BROJ_OSOBA + " osobe:");
        for (int i = 0; i < BROJ_OSOBA; i++) {
            osobe[i] = unosOsobe(scanner, zupanije, simptomi, bolesti, osobe, i);
        }

        ispisiStarostZupanija(BROJ_ZUPANIJA, zupanije, osobe);
        ispisiOsobe(osobe);
    }

    private static String unosPodatka(Scanner scanner, String poruka) {
        System.out.print(poruka);
        return scanner.nextLine();
    }

    private static Integer unosBroja(Scanner scanner, String poruka) {
        System.out.print(poruka);
        Integer broj = scanner.nextInt();
        scanner.nextLine();
        return broj;
    }

    private static Zupanija unosZupanije(Scanner scanner, int i) {
        String naziv = unosPodatka(scanner, ">> Unesite naziv " + (i + 1) + ". županije: ");
        Integer brojStanovnika = unosBroja(scanner, ">> Unesite broj stanovnika " + (i + 1) + ". županije: ");

        String regex = "^[0-9]{3}$";
        Boolean doesMatch = null;
        String sifra;
        do {
            sifra = unosPodatka(scanner, ">> Unesite šifru županije " + (i + 1) + ". županije: ");
            doesMatch = Pattern.matches(regex,sifra);

            if (!doesMatch) {
                System.out.println("## Neispravan unos, molimo pokušajte ponovno! ##");
            }
        } while (!doesMatch);

        return new Zupanija(naziv, brojStanovnika, sifra);
    }

    private static Simptom unosSimptoma(Scanner scanner, String[] vrijednostiSimptoma, int i) {
        String naziv = unosPodatka(scanner, ">> Unesite naziv " + (i + 1) + ". simptoma: ");

        // bez provjere unesene vrijednosti
        String vrijednostSimptoma = unosPodatka(scanner,
                ">> Unesite vrijednost " +
                        (i + 1) + ". simptoma (" +
                        vrijednostiSimptoma[0] + " / " +
                        vrijednostiSimptoma[1] + " / " +
                        vrijednostiSimptoma[2] + "): ");

        return new Simptom(naziv, vrijednostSimptoma.toUpperCase());
    }

    private static Bolest unosBolesti(Scanner scanner, Simptom[] simptomi, int i) {
        String naziv = unosPodatka(scanner, ">> Unesite naziv " + (i + 1) + ". bolesti: ");

        Integer brojSimptoma = unosBroja(scanner, ">> Unesite broj simptoma: ");

        Simptom[] odabraniSimptomi = new Simptom[brojSimptoma];

        for (int j = 0; j < brojSimptoma; j++) {
            Integer indexOdabranogSimptoma;
            do {
                System.out.println("Odaberite " + (j + 1) + ". simptom:");

                for (int k = 0; k < simptomi.length; k++) {
                    System.out.println((k + 1) + ". "
                            + simptomi[k].getNaziv()
                            + " (" + simptomi[k].getVrijednost() + ")");
                }

                indexOdabranogSimptoma = unosBroja(scanner, ">> Odabir: ");

                if (indexOdabranogSimptoma < 1 || indexOdabranogSimptoma > simptomi.length) {
                    System.out.println("## Neispravan unos, molimo pokušajte ponovno! ##");
                }

            } while (indexOdabranogSimptoma < 1 || indexOdabranogSimptoma > simptomi.length);

            odabraniSimptomi[j] = simptomi[indexOdabranogSimptoma - 1];
        }

        return new Bolest(naziv, odabraniSimptomi);
    }

    private static Osoba unosOsobe(Scanner scanner, Zupanija[] zupanije, Simptom[] simptomi, Bolest[] bolesti, Osoba[] osobe, int i) {
        String ime = unosPodatka(scanner, ">> Unesite ime " + (i + 1) + ". osobe: ");
        String prezime = unosPodatka(scanner, ">> Unesite prezime osobe: ");
        Integer starost = unosBroja(scanner, ">> Unesite starost osobe: ");

        // UNOS ZUPANIJE
        Integer indexOdabraneZupanije;
        do {
            System.out.println("Unesite županiju osobe:");

            for (int k = 0; k < zupanije.length; k++) {
                System.out.println((k + 1) + ". " + zupanije[k].getNaziv());
            }

            indexOdabraneZupanije = unosBroja(scanner, ">> Odabir: ");

            if (indexOdabraneZupanije < 1 || indexOdabraneZupanije > zupanije.length) {
                System.out.println("## Neispravan unos, molimo pokušajte ponovno! ##");
            }
        } while (indexOdabraneZupanije < 1 || indexOdabraneZupanije > zupanije.length);
        Zupanija zupanija = zupanije[indexOdabraneZupanije - 1];


        // UNOS BOLESTI
        Integer indexOdabraneBolesti;
        do {
            System.out.println("Unesite bolest osobe:");

            for (int k = 0; k < bolesti.length; k++) {
                System.out.println((k + 1) + ". " + bolesti[k].getNaziv());
            }

            indexOdabraneBolesti = unosBroja(scanner, ">> Odabir: ");

            if (indexOdabraneBolesti < 1 || indexOdabraneBolesti > bolesti.length) {
                System.out.println("## Neispravan unos, molimo pokušajte ponovno! ##");
            }
        } while (indexOdabraneBolesti < 1 || indexOdabraneBolesti > bolesti.length);
        Bolest bolest = bolesti[indexOdabraneBolesti - 1];

        // ako je unesena bar jedna osoba
        // UNOS OSOBA U KONTAKTIRANE OSOBE
        Osoba[] kontaktiraneOsobe = null;
        if (i > 0) {

            Integer brojKontakata = unosBroja(scanner, ">> Unesite broj osoba koje su bile u kontaktu s tom osobom: ");

            if (brojKontakata > 0) {
                kontaktiraneOsobe = new Osoba[brojKontakata];
                System.out.println("Unesite osobe koje su bile u kontaktu s tom osobom:");
            }

            for (int j = 0; j < brojKontakata; j++) {
                System.out.println("Odaberite " + (j + 1) + ". osobu:");

                Integer indexOdabraneOsobe;
                do {
                    for (int k = 0; k < osobe.length; k++) {
                        if (osobe[k] == null) break;
                        System.out.println((k + 1) + ". " + osobe[k].getIme() + " " + osobe[k].getPrezime());
                    }

                    indexOdabraneOsobe = unosBroja(scanner, ">> Odabir: ");

                    if (indexOdabraneOsobe < 1 || indexOdabraneOsobe > i) {
                        System.out.println("## Neispravan unos, molimo pokušajte ponovno! ##");
                    }
                } while (indexOdabraneOsobe < 1 || indexOdabraneOsobe > i);
                kontaktiraneOsobe[j] = osobe[indexOdabraneOsobe - 1];
            }
        }

        return new Osoba(ime, prezime, starost, zupanija, bolest, kontaktiraneOsobe);
    }

    private static void ispisiStarostZupanija(Integer BROJ_ZUPANIJA, Zupanija[] zupanije, Osoba[] osobe) {
        System.out.println("\n---------------------------------------------\n");
        System.out.println("Prosječna starost osoba po županijama:");
        for (int i = 0; i < BROJ_ZUPANIJA; i++) {
            Integer sumaGodina = 0;
            Integer brojOsoba = 0;

            for (int j = 0; j < BROJ_OSOBA; j++) {
                if (osobe[j].getZupanija().getNaziv().equals(zupanije[i].getNaziv())) {
                    sumaGodina += osobe[j].getStarost();
                    brojOsoba++;
                }
            }

            float prosjekGodina = 0;
            if (brojOsoba > 0) {
                prosjekGodina = (float) sumaGodina / brojOsoba;
            }

            System.out.println("- " + zupanije[i].getNaziv() + " - " + prosjekGodina + " godina");
        }
    }

    private static void ispisiOsobe(Osoba[] osobe) {
        System.out.println("\n---------------------------------------------\n");
        System.out.println("Popis osoba:");
        for (Osoba osoba : osobe) {
            System.out.println("\nIme i prezime: " + osoba.getIme() + " " + osoba.getPrezime());
            System.out.println("Starost: " + osoba.getStarost());
            System.out.println("Županija prebivališta: " + osoba.getZupanija().getNaziv());
            System.out.println("Zaražen bolešću: " + osoba.getZarazenBolescu().getNaziv());
            System.out.println("Kontaktirane osobe:");
            if (osoba.getKontaktiraneOsobe() == null) {
                System.out.println("Nema kontaktiranih osoba.");
            } else {
                for (Osoba kontaktiranaOsoba : osoba.getKontaktiraneOsobe()) {
                    System.out.println("- " + kontaktiranaOsoba.getIme() + " " + kontaktiranaOsoba.getPrezime());
                }
            }
        }
    }

}
