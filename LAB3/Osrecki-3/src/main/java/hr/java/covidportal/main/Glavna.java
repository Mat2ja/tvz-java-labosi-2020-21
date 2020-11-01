package main.java.hr.java.covidportal.main;

import main.java.hr.java.covidportal.iznimke.BolestIstihSimptoma;
import main.java.hr.java.covidportal.iznimke.DuplikatKontaktiraneOsobe;
import main.java.hr.java.covidportal.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Predstavlja glavnu klasu u kojoj se izvodi program
 *
 * @author Matija
 */
public class Glavna {
    private static final Integer BROJ_ZUPANIJA = 1;
    private static final Integer BROJ_SIMPTOMA = 3;
    private static final Integer BROJ_BOLESTI = 3;
    private static final Integer BROJ_OSOBA = 3;

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    /**
     * Izvodi glavnu metodu programa
     *
     * @param args argumenti iz komandne linije
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Zupanija[] zupanije = new Zupanija[BROJ_ZUPANIJA];
        Simptom[] simptomi = new Simptom[BROJ_SIMPTOMA];
        Bolest[] bolesti = new Bolest[BROJ_BOLESTI];
        Osoba[] osobe = new Osoba[BROJ_OSOBA];

        final String[] vrijednostiSimptoma = new String[]{"RIJETKO", "SREDNJE", "ČESTO"};
        final String[] vrsteBolesti = new String[]{"BOLEST", "VIRUS"};

        printDividerWithHeading("Corona Finder 9000");
        logger.info("Pokrenuli smo program!");

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
            boolean ponoviPetlju;
            do {
                try {
                    bolesti[i] = unosBolesti(scanner, vrsteBolesti, simptomi, bolesti, i);
                    ponoviPetlju = false;
                } catch (BolestIstihSimptoma ex) {
                    logger.info(ex.getMessage(), ex);
                    System.out.println(ex.getMessage());
                    ponoviPetlju = true;
                }
            } while (ponoviPetlju);
        }

        System.out.println("\nUnesite podatke o " + BROJ_OSOBA + " osobe:");
        for (int i = 0; i < BROJ_OSOBA; i++) {
            osobe[i] = unosOsobe(scanner, zupanije, bolesti, osobe, i);
        }

        ispisiOsobe(osobe);
    }

    /**
     * Dohvaća podatak unesen na ulazu
     *
     * @param scanner scanner koji dohvaća unos s ulaza
     * @param poruka  poruka koja će se ispitati trazeći unos podatka
     * @return podatak o unešenoj vrijednosti
     */
    private static String unosPodatka(Scanner scanner, String poruka) {
        System.out.print(poruka);
        return scanner.nextLine();
    }

    /**
     * Dohvaća podatak unesen na ulazu
     *
     * @param scanner scanner koji dohvaća unos s ulaza
     * @param poruka  poruka koja će se ispitati trazeći unos podatka
     * @return brojčani podatak o unešenoj vrijednosti
     */
    private static Integer unosBroja(Scanner scanner, String poruka) {
        Integer broj = 0;
        boolean pogresanUnos;
        do {
            try {
                System.out.print(poruka);
                broj = scanner.nextInt();
                pogresanUnos = false;
            } catch (InputMismatchException ex) {
                System.out.println("## Pogreška u formatu podataka, molimo ponovite unos!");
                pogresanUnos = true;
                logger.info("Pogreška u formatu podataka, jer je unesen String koji se ne može parsirati", ex);
            } finally {
                scanner.nextLine();
            }
        } while (pogresanUnos);

        logger.info("Generiran je broj: " + broj);
        return broj;
    }

    /**
     * Provjerava da je li vrijednost izvan granica intervala
     *
     * @param value podatak o vrijedosti
     * @param min   podatak o minimalnoj granici intervala
     * @param max   podatak o minimalnoj granici intervala
     * @return true ako je vrijednost izvan zadanog intervala, inače false
     */
    private static boolean isOutsideRange(Integer value, Integer min, Integer max) {
        return value < min || value > max;
    }

    /**
     * Unosi podatke o županiji
     *
     * @param scanner scanner koji dohvaća unos s ulaza
     * @param i       podatak o trenutnoj iteraciji petlje
     * @return instanca županije
     */
    private static Zupanija unosZupanije(Scanner scanner, int i) {
        String naziv = unosPodatka(scanner, ">> Unesite naziv " + (i + 1) + ". županije: ");
        Integer brojStanovnika = unosBroja(scanner, ">> Unesite broj stanovnika " + (i + 1) + ". županije: ");
        return new Zupanija(naziv, brojStanovnika);
    }

    /**
     * Unosi podatke o simptomima
     *
     * @param scanner             scanner koji dohvaća unos s ulaza
     * @param vrijednostiSimptoma podatak o dostupnim simptomima
     * @param i                   podatak o trenutnoj iteraciji petlje
     * @return instanca simptoma
     */
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

    /**
     * Unosi podatke o bolesti
     *
     * @param scanner      scanner koji dohvaća unos s ulaza
     * @param vrsteBolesti podatak o vrstama bolesti
     * @param simptomi     podatak o dostupnim simptomima
     * @param bolesti      podatak o evidentiranim bolestima
     * @param i            podatak o trenutnoj iteraciji petlje
     * @return instanca bolesti
     * @throws BolestIstihSimptoma
     */
    private static Bolest unosBolesti(Scanner scanner, String[] vrsteBolesti, Simptom[] simptomi, Bolest[] bolesti, int i) throws BolestIstihSimptoma {

        // Odabir bolesti ili virusa
        Integer indexVrsteBolesti;
        do {
            String poruka = "Unosite li bolest ili virus?\n";
            for (int k = 0; k < vrsteBolesti.length; k++) {
                poruka += (k + 1) + ") " + vrsteBolesti[k] + "\n";
            }
            poruka += ">> Odabir: ";

            indexVrsteBolesti = unosBroja(scanner, poruka);

            if (isOutsideRange(indexVrsteBolesti, 1, vrsteBolesti.length)) prikaziPorukuNeispravnogUnosa();

        } while (isOutsideRange(indexVrsteBolesti, 1, vrsteBolesti.length));
        String vrstaBolesti = vrsteBolesti[indexVrsteBolesti - 1];

        String naziv = unosPodatka(scanner, ">> Unesite naziv " + (i + 1) + ". bolesti ili virusa: ");

        Integer brojSimptoma;
        do {
            brojSimptoma = unosBroja(scanner, ">> Unesite broj simptoma: ");

            if (isOutsideRange(brojSimptoma, 1, simptomi.length)) {
                if (simptomi.length <= 1) {
                    System.out.println("## Molimo izaberite barem 1 simptom.");
                } else {
                    System.out.println("## Molimo izaberite izmedu 1 i " + simptomi.length + " simptoma.");
                }
            }
        } while (isOutsideRange(brojSimptoma, 1, simptomi.length));

        Simptom[] odabraniSimptomi = new Simptom[brojSimptoma];

        // Odabir simptoma za odabranu bolest
        for (int j = 0; j < brojSimptoma; j++) {
            Integer indexOdabranogSimptoma;
            do {
                String poruka = "Odaberite " + (j + 1) + ". simptom:\n";
                for (int k = 0; k < simptomi.length; k++) {
                    poruka += (k + 1) + ") "
                            + simptomi[k].getNaziv()
                            + " (" + simptomi[k].getVrijednost() + ")\n";
                }
                poruka += ">> Odabir: ";

                indexOdabranogSimptoma = unosBroja(scanner, poruka);

                if (isOutsideRange(indexOdabranogSimptoma, 1, simptomi.length)) prikaziPorukuNeispravnogUnosa();

            } while (isOutsideRange(indexOdabranogSimptoma, 1, simptomi.length));

            odabraniSimptomi[j] = simptomi[indexOdabranogSimptoma - 1];
        }

        Bolest odabranaBolest = switch (vrstaBolesti.toUpperCase()) {
            case "VIRUS" -> new Virus(naziv, odabraniSimptomi);
            default -> new Bolest(naziv, odabraniSimptomi);
        };

        provjeraBolesti(bolesti, odabranaBolest);

        return odabranaBolest;

    }

    /**
     * Unosi podakte o osobi
     *
     * @param scanner  scanner koji dohvaća unos s ulaza
     * @param zupanije podatak o evidentiranim županijama
     * @param bolesti  podatak o evidentiranim bolestima
     * @param osobe    podatak o evidentiranim osobama
     * @param i        podatak o trenutnoj iteraciji petlje
     * @return instanca osobe
     */
    private static Osoba unosOsobe(Scanner scanner, Zupanija[] zupanije, Bolest[] bolesti, Osoba[] osobe, int i) {
        String ime = unosPodatka(scanner, ">> Unesite ime " + (i + 1) + ". osobe: ");
        String prezime = unosPodatka(scanner, ">> Unesite prezime osobe: ");
        Integer starost = unosBroja(scanner, ">> Unesite starost osobe: ");

        // Unos županije osobe
        Integer indexOdabraneZupanije;
        do {
            String poruka = "Unesite županiju osobe:\n";
            for (int k = 0; k < zupanije.length; k++) {
                poruka += (k + 1) + ") " + zupanije[k].getNaziv() + "\n";
            }
            poruka += ">> Odabir: ";

            indexOdabraneZupanije = unosBroja(scanner, poruka);

            if (isOutsideRange(indexOdabraneZupanije, 1, zupanije.length)) prikaziPorukuNeispravnogUnosa();

        } while (isOutsideRange(indexOdabraneZupanije, 1, zupanije.length));
        Zupanija zupanija = zupanije[indexOdabraneZupanije - 1];


        // Unos bolesti osobe
        Integer indexOdabraneBolesti;
        do {
            String poruka = "Unesite bolest ili virus osobe:\n";

            for (int k = 0; k < bolesti.length; k++) {
                poruka += (k + 1) + ") " + bolesti[k].getNaziv() + "\n";
            }
            poruka += ">> Odabir: ";

            indexOdabraneBolesti = unosBroja(scanner, poruka);

            if (isOutsideRange(indexOdabraneBolesti, 1, bolesti.length)) prikaziPorukuNeispravnogUnosa();

        } while (isOutsideRange(indexOdabraneBolesti, 1, bolesti.length));
        Bolest bolest = bolesti[indexOdabraneBolesti - 1];


        // Unos kontakata osobe
        Osoba[] kontaktiraneOsobe = null;
        // ako je unesena bar jedna osoba
        if (i > 0) {
            Integer brojKontakata;
            do {
                brojKontakata = unosBroja(scanner, ">> Unesite broj osoba koje su bile u kontaktu s tom osobom: ");

                if (isOutsideRange(brojKontakata, 0, i)) {
                    System.out.println("## Molimo izaberite izmedu 0 i " + i + " osobe.");
                }
            } while (isOutsideRange(brojKontakata, 0, i));

            if (brojKontakata > 0) {
                kontaktiraneOsobe = new Osoba[brojKontakata];
                System.out.println("Unesite osobe koje su bile u kontaktu s tom osobom:");
            }

            for (int j = 0; j < brojKontakata; j++) {

                Integer indexOdabraneOsobe;
                boolean neispravanKontakt = false;
                do {
                    String poruka = "Odaberite " + (j + 1) + ". osobu:\n";
                    for (int k = 0; k < osobe.length; k++) {
                        if (osobe[k] == null) break;
                        poruka += (k + 1) + ") " + osobe[k].getIme() + " " + osobe[k].getPrezime() + "\n";
                    }
                    poruka += ">> Odabir: ";

                    indexOdabraneOsobe = unosBroja(scanner, poruka);

                    if (isOutsideRange(indexOdabraneOsobe, 1, i)) {
                        prikaziPorukuNeispravnogUnosa();
                    } else {
                        try {
                            provjeraKontakta(indexOdabraneOsobe, osobe, kontaktiraneOsobe);
                            neispravanKontakt = false;
                        } catch (DuplikatKontaktiraneOsobe ex) {
                            logger.info(ex.getMessage(), ex);
                            System.out.println(ex.getMessage());
                            neispravanKontakt = true;
                        }
                    }
                } while (isOutsideRange(indexOdabraneOsobe, 1, i) || neispravanKontakt);
                Osoba odabranaOsoba = osobe[indexOdabraneOsobe - 1];
                kontaktiraneOsobe[j] = odabranaOsoba;
                logger.info("Generirana je osoba: " + odabranaOsoba.getIme() + " " + odabranaOsoba.getPrezime());
            }
        }

        return new Osoba.Builder(ime)
                .hasPrezime(prezime)
                .isAged(starost)
                .atZupanija(zupanija)
                .withBolest(bolest)
                .withKontaktiraneOsobe(kontaktiraneOsobe)
                .build();
    }

    /**
     * Provjerava da li je unesena osoba duplikat već unesene osobe, ako je, baca iznimku
     *
     * @param indexOdabraneOsobe podatak o odabiru osobe
     * @param osobe              podatak o evidentiranim osobama
     * @param kontaktiraneOsobe  podatak o kontaktiranim osobama trenutne osobe
     * @throws DuplikatKontaktiraneOsobe iznimka bačena u slučaju unosa duplikata osobe
     */
    private static void provjeraKontakta(Integer indexOdabraneOsobe, Osoba[] osobe, Osoba[] kontaktiraneOsobe) throws DuplikatKontaktiraneOsobe {
        Osoba odabranaOsoba = osobe[indexOdabraneOsobe - 1];
        for (Osoba kontakt : kontaktiraneOsobe) {
            if (kontakt == null) break;
            if (kontakt.equals(odabranaOsoba)) {
                throw new DuplikatKontaktiraneOsobe("## Odabrana osoba se već nalazi među kontaktiranim osobama.\n" +
                        "## Molimo Vas da odaberete neku drugu osobu.");
            }
        }
    }

    /**
     * Provjerava da li unesena bolest ima jednake simptoma kao već unesena bolest
     *
     * @param bolesti        podatak o evidentiranim bolestima
     * @param odabranaBolest podatak o odabranoj bolesti
     * @throws BolestIstihSimptoma iznimka bačena u slučaju unosa bolesti s istim simptomima kao već unesena bolest
     */
    private static void provjeraBolesti(Bolest[] bolesti, Bolest odabranaBolest) throws BolestIstihSimptoma {
        for (Bolest bolest : bolesti) {
            if (bolest == null) break;
            //todo
            if (bolest.getSimptomi().length == odabranaBolest.getSimptomi().length) {
                Simptom[] simptomi = bolest.getSimptomi();
                Simptom[] simptomiOdabrane = odabranaBolest.getSimptomi();
                Arrays.sort(simptomi, Comparator.comparing(Simptom::getNaziv));
                Arrays.sort(simptomiOdabrane, Comparator.comparing(Simptom::getNaziv));

                for (int i = 0; i < bolest.getSimptomi().length; i++) {
                    if (!simptomi[i].equals(simptomiOdabrane[i])) {
                        return;
                    }
                }
                throw new BolestIstihSimptoma("## Bolest s izabranim simptomima već postoji. Molimo ponovite unos bolesti.");
            }
        }
        logger.info("Generirana je bolest: " + odabranaBolest.getNaziv());
    }

    /**
     * Prikazuje poruku upozorenja u slučaju neispravnog unosa
     */
    private static void prikaziPorukuNeispravnogUnosa() {
        System.out.println("## Neispravan unos, molimo pokušajte ponovno!");
    }

    /**
     * Prikazuje naslov sekcije
     */
    private static void printDividerWithHeading(String title) {
        System.out.println("\n-------------- " + title + " --------------");
    }

    /**
     * Ispisuje podatke o evidentiranim osobama
     */
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
