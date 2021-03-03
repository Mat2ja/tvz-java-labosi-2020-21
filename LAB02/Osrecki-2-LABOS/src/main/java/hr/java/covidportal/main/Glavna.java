package main.java.hr.java.covidportal.main;

import main.java.hr.java.covidportal.model.*;

import java.util.Scanner;

public class Glavna {
    private static final Integer BROJ_ZUPANIJA = 3;
    private static final Integer BROJ_SIMPTOMA = 3;
    private static final Integer BROJ_TEGOBA = 4;
    private static final Integer BROJ_OSOBA = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Zupanija[] zupanije = new Zupanija[BROJ_ZUPANIJA];
        Simptom[] simptomi = new Simptom[BROJ_SIMPTOMA];
        Tegoba[] tegobe = new Tegoba[BROJ_TEGOBA];
        Osoba[] osobe = new Osoba[BROJ_OSOBA];
        final String[] vrijednostiSimptoma = new String[]{"RIJETKO", "SREDNJE", "ČESTO"};
        final String[] vrsteBolesti = new String[]{"BOLEST", "VIRUS"};
        final String[] vrsteTegoba = new String[]{"LJENINGITIS", "JAVABOLJITIS", "TEVEZEITIS"};


        printDividerWithHeading("Corona Finder 9000");

        System.out.println("\nUnesite podatke o " + BROJ_ZUPANIJA + " županije:");
        for (int i = 0; i < BROJ_ZUPANIJA; i++) {
            zupanije[i] = unosZupanije(scanner, i);
        }

        System.out.println("\nUnesite podatke o " + BROJ_SIMPTOMA + " simptoma:");
        for (int i = 0; i < BROJ_SIMPTOMA; i++) {
            simptomi[i] = unosSimptoma(scanner, vrijednostiSimptoma, i);
        }

        System.out.println("\nUnesite podatke o " + BROJ_TEGOBA + " bolesti:");
        for (int i = 0; i < BROJ_TEGOBA; i++) {
            tegobe[i] = unosTegoba(scanner, vrsteBolesti, vrsteTegoba, simptomi, tegobe, i);
        }

        System.out.println("\nUnesite podatke o " + BROJ_OSOBA + " osobe:");
        for (int i = 0; i < BROJ_OSOBA; i++) {
            osobe[i] = unosOsobe(scanner, zupanije, tegobe, osobe, i);
        }

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

    private static boolean isInsideRange(Integer value, Integer min, Integer max) {
        return value >= min && value <= max;
    }

    private static Zupanija unosZupanije(Scanner scanner, int i) {
        String naziv = unosPodatka(scanner, ">> Unesite naziv " + (i + 1) + ". županije: ");
        Integer brojStanovnika = unosBroja(scanner, ">> Unesite broj stanovnika " + (i + 1) + ". županije: ");
        return new Zupanija(naziv, brojStanovnika);
    }

    private static Simptom unosSimptoma(Scanner scanner, String[] vrijednostiSimptoma, int i) {
        String naziv = unosPodatka(scanner, ">> Unesite naziv " + (i + 1) + ". simptoma: ");
        String vrijednostSimptoma = unosPodatka(scanner,
                ">> Unesite vrijednost " +
                        (i + 1) + ". simptoma (" +
                        vrijednostiSimptoma[0] + " / " +
                        vrijednostiSimptoma[1] + " / " +
                        vrijednostiSimptoma[2] + "): ");

        return new Simptom(naziv, vrijednostSimptoma.toUpperCase());
    }

    private static Tegoba unosTegoba(Scanner scanner, String[] vrsteBolesti, String[] vrsteTegoba, Simptom[] simptomi, Tegoba[] tegobe, int i) {

        // Odabir bolesti ili virusa
        Integer indexVrsteBolesti;
        boolean uvjetVrsteBolesti;
        do {
            System.out.println("Unosite li bolest ili virus?");
            for (int k = 0; k < vrsteBolesti.length; k++) {
                System.out.println((k + 1) + ") " + vrsteBolesti[k]);
            }

            indexVrsteBolesti = unosBroja(scanner, ">> Odabir: ");

            uvjetVrsteBolesti = isInsideRange(indexVrsteBolesti, 1, vrsteBolesti.length);
            if (!uvjetVrsteBolesti) prikaziPorukuUpozorenja();

        } while (!uvjetVrsteBolesti);

        // zadatak 2
        String naziv = null;
        String nazivTegobe = null;
        Integer indexTegobe = 0;
        boolean uvjetTegobe;
        if (vrsteBolesti[indexVrsteBolesti - 1].toUpperCase().equals("VIRUS")) {
            naziv = unosPodatka(scanner, ">> Unesite naziv " + (i + 1) + ". bolesti ili virusa: ");
        } else {
            do {
                System.out.println("Unesite naziv tegobe:");

                for (int k = 0; k < vrsteTegoba.length; k++) {
                    System.out.println((k + 1) + ") " + vrsteTegoba[k]);
                }

                indexTegobe = unosBroja(scanner, ">> Odabir: ");

                uvjetTegobe = isInsideRange(indexTegobe, 1, vrsteTegoba.length);
                if (!uvjetTegobe) prikaziPorukuUpozorenja();

            } while (!uvjetTegobe);

            nazivTegobe = vrsteTegoba[indexTegobe - 1];
        }


        Integer brojSimptoma = unosBroja(scanner, ">> Unesite broj simptoma: ");
        Simptom[] odabraniSimptomi = new Simptom[brojSimptoma];

        // Odabir simptoma za odabranu bolest
        for (int j = 0; j < brojSimptoma; j++) {
            Integer indexOdabranogSimptoma;
            boolean uvjetSimptoma;
            do {
                System.out.println("Odaberite " + (j + 1) + ". simptom:");

                for (int k = 0; k < simptomi.length; k++) {
                    System.out.println((k + 1) + ") "
                            + simptomi[k].getNaziv()
                            + " (" + simptomi[k].getVrijednost() + ")");
                }

                indexOdabranogSimptoma = unosBroja(scanner, ">> Odabir: ");

                uvjetSimptoma = isInsideRange(indexOdabranogSimptoma, 1, simptomi.length);
                if (!uvjetSimptoma) prikaziPorukuUpozorenja();

            } while (!uvjetSimptoma);

            odabraniSimptomi[j] = simptomi[indexOdabranogSimptoma - 1];
        }

        Tegoba tegoba = null;

        if (naziv != null) {
            tegoba = new Virus(naziv, simptomi);
        } else if (nazivTegobe != null) {
            tegoba = switch (nazivTegobe.toUpperCase()) {
                case "LJENINGITIS" -> new Ljeningitis(simptomi);
                case "JAVABOLJITIS" -> new JavaBoljitis(simptomi);
                case "TEVEZEITIS" -> new TeVeZeitis(simptomi);
                default -> throw new IllegalStateException("Unexpected value: " + nazivTegobe.toUpperCase());
            };
        }

        return tegoba;
    }

    private static Osoba unosOsobe(Scanner scanner, Zupanija[] zupanije, Tegoba[] tegobe, Osoba[] osobe, int i) {
        String ime = unosPodatka(scanner, ">> Unesite ime " + (i + 1) + ". osobe: ");
        String prezime = unosPodatka(scanner, ">> Unesite prezime osobe: ");
        Integer starost = unosBroja(scanner, ">> Unesite starost osobe: ");

        // Unos županije osobe
        Integer indexOdabraneZupanije;
        boolean uvjetZupanije;
        do {
            System.out.println("Unesite županiju osobe:");

            for (int k = 0; k < zupanije.length; k++) {
                System.out.println((k + 1) + ") " + zupanije[k].getNaziv());
            }

            indexOdabraneZupanije = unosBroja(scanner, ">> Odabir: ");

            uvjetZupanije = isInsideRange(indexOdabraneZupanije, 1, zupanije.length);
            if (!uvjetZupanije) prikaziPorukuUpozorenja();

        } while (!uvjetZupanije);
        Zupanija zupanija = zupanije[indexOdabraneZupanije - 1];


        // Unos bolesti osobe
        Integer indexOdabraneBolesti;
        boolean uvjetBolesti;
        do {
            System.out.println("Unesite bolest ili virus osobe:");

            for (int k = 0; k < tegobe.length; k++) {
                System.out.println((k + 1) + ") " + tegobe[k].getNaziv());
            }

            indexOdabraneBolesti = unosBroja(scanner, ">> Odabir: ");

            uvjetBolesti = isInsideRange(indexOdabraneBolesti, 1, tegobe.length);
            if (!uvjetBolesti) prikaziPorukuUpozorenja();

        } while (!uvjetBolesti);
        Tegoba tegoba = tegobe[indexOdabraneBolesti - 1];


        // ako je unesena bar jedna osoba
        // Unos kontakata osobe
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
                boolean uvjetOsobe;
                do {
                    for (int k = 0; k < osobe.length; k++) {
                        if (osobe[k] == null) break;
                        System.out.println((k + 1) + ") " + osobe[k].getIme() + " " + osobe[k].getPrezime());
                    }

                    indexOdabraneOsobe = unosBroja(scanner, ">> Odabir: ");

                    uvjetOsobe = isInsideRange(indexOdabraneOsobe, 1, i);
                    if (!uvjetOsobe) prikaziPorukuUpozorenja();

                } while (!uvjetOsobe);
                kontaktiraneOsobe[j] = osobe[indexOdabraneOsobe - 1];
            }
        }

        return new Osoba.Builder(ime)
                .hasPrezime(prezime)
                .isAged(starost)
                .atZupanija(zupanija)
                .withBolest(tegoba)
                .withKontaktiraneOsobe(kontaktiraneOsobe)
                .build();
    }

    private static void prikaziPorukuUpozorenja() {
        System.out.println("## Neispravan unos, molimo pokušajte ponovno! ##");
    }

    private static void printDividerWithHeading(String title) {
        System.out.println("\n-------------- " + title + " --------------");
    }

    private static void ispisiOsobe(Osoba[] osobe) {
        printDividerWithHeading("Popis osoba");
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
