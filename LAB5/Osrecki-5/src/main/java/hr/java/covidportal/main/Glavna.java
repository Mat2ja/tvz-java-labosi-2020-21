package main.java.hr.java.covidportal.main;

import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.hr.java.covidportal.genericsi.KlinikaZaInfektivneBolesti;
import main.java.hr.java.covidportal.iznimke.*;
import main.java.hr.java.covidportal.model.*;

import main.java.hr.java.covidportal.sort.CovidSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Predstavlja glavnu klasu u kojoj se izvodi program
 *
 * @author Matija
 */
public class Glavna {
    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    /**
     * Izvodi glavnu metodu programa
     *
     * @param args argumenti iz komandne linije
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        SortedSet<Zupanija> zupanije = new TreeSet<>(new CovidSorter());
        Set<Simptom> simptomi = new HashSet<>();
        Set<Bolest> bolesti = new HashSet<>();
        List<Osoba> osobe = new ArrayList<>();

        final Set<String> vrsteBolesti = new HashSet<>(Arrays.asList("BOLEST", "VIRUS"));

        printHeader("Corona Finder 9000");
        logger.info("Pokrenuli smo program!");

        printHeader("Županije");
        Integer brojZupanija = unesiBrojZeljenihPodataka(scanner, "\n>> Unesite broj županija koje želite unijeti: ");
        System.out.println("\nUnesite podatke o " + brojZupanija + " županije:");
        for (int i = 0; i < brojZupanija; i++) {
            zupanije.add(unosZupanije(scanner, i));
        }

        printHeader("Simptomi");
        Integer brojSimptoma = unesiBrojZeljenihPodataka(scanner, "\n>> Unesite broj simptoma koje želite unijeti: ");
        System.out.println("\nUnesite podatke o " + brojSimptoma + " simptoma:");
        for (int i = 0; i < brojSimptoma; i++) {
            simptomi.add(unosSimptoma(scanner, i));
        }

        printHeader("Bolesti");
        Integer brojBolesti = unesiBrojZeljenihPodataka(scanner, "\n>> Unesite broj bolesti koje želite unijeti: ");
        System.out.println("\nUnesite podatke o " + brojBolesti + " bolesti:");
        for (int i = 0; i < brojBolesti; i++) {
            Boolean ponoviPetlju;
            do {
                try {
                    bolesti.add(unosBolesti(scanner, vrsteBolesti, simptomi, bolesti, i));
                    ponoviPetlju = false;
                } catch (BolestIstihSimptoma ex) {
                    logger.error(ex.getMessage(), ex);
                    System.out.println("## " + ex.getMessage());
                    ponoviPetlju = true;
                }
            } while (ponoviPetlju);
        }

        printHeader("Osobe");
        Integer brojOsoba = unesiBrojZeljenihPodataka(scanner, "\n>> Unesite broj osoba koje želite unijeti: ");
        System.out.println("\nUnesite podatke o " + brojOsoba + " osobe:");
        for (int i = 0; i < brojOsoba; i++) {
            osobe.add(unosOsobe(scanner, zupanije, bolesti, osobe, i));
        }

        ispisOsoba(osobe);

        Map<Bolest, List<Osoba>> mapaBolesti = osobe.stream().collect(Collectors.groupingBy(Osoba::getZarazenBolescu));
        ispisMapeBolesti(mapaBolesti);


        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        System.out.println("\nNajviše zaraženih osoba ima u županiji "
                + zupanije.first().getNaziv() + ": "
                + df.format(zupanije.first().getPostotakZarazenih()) + "%");


        KlinikaZaInfektivneBolesti<Virus, Osoba> klinika = new KlinikaZaInfektivneBolesti<>(
                bolesti.stream()
                        .filter(bolest -> bolest instanceof Virus)
                        .map(virus -> (Virus) virus)
                        .collect(Collectors.toList()),
                osobe.stream()
                        .filter(osoba -> osoba.getZarazenBolescu() instanceof Virus)
                        .collect(Collectors.toList()));

        Instant start1 = Instant.now();
        List<Virus> sortedVirusi = sortiranjeUzLambdu(klinika.getVirusi());
        Instant end1 = Instant.now();
        Long vrijeme1 = Duration.between(start1, end1).toMillis();

        Instant start2 = Instant.now();
        List<Virus> sortedVirusi2 = sortiranjeBezLambde(klinika.getVirusi());
        Instant end2 = Instant.now();
        Long vrijeme2 = Duration.between(start2, end2).toMillis();

        klinika.setVirusi(sortedVirusi);

        System.out.println("\nVirusi sortirani po nazivu suprotno od poretka abecede:");
        Integer i = 0;
        for (Virus virus : klinika.getVirusi()) {
            System.out.println(++i + ". " + virus);
        }

        System.out.println("\nSortiranje objekata korištenjem lambdi traje " +
                vrijeme1 + " milisekundi, a bez lambda traje " +
                vrijeme2 + " milisekundi.");

        printHeader("Pretraga osoba");
        String pretraga = unosPodatka(scanner, "\n>> Unesite string za pretragu po prezimenu: ");
        ispisPretrazenihOsoba(osobe, pretraga);

        bolesti.stream()
                .map(b -> b.getNaziv() + " ima " + b.getSimptomi().size() + " simptoma.")
                .forEach(System.out::println);

    }

    /**
     * Sortira viruse putem lambde
     *
     * @param virusi podatak o listi virusa
     * @return lista sortiranih virusa
     */
    private static List<Virus> sortiranjeUzLambdu(List<Virus> virusi) {
        return virusi.stream()
                .sorted(Comparator.comparing(Virus::getNaziv).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Sortira viruse bez lambde
     *
     * @param virusi podatak o listi virusa
     * @return lista sortiranih virusa
     */
    private static List<Virus> sortiranjeBezLambde(List<Virus> virusi) {
        return virusi.stream()
                .sorted(new Comparator<>() {
                    @Override
                    public int compare(Virus o1, Virus o2) {
                        return o2.getNaziv().compareTo(o1.getNaziv());
                    }
                })
                .collect(Collectors.toList());
    }


    /**
     * Dohvaća podatak unesen na ulazu
     *
     * @param scanner scanner koji dohvaća unos s ulaza
     * @param poruka  poruka koja će se ispisati trazeći unos podatka
     * @return podatak o unesenoj vrijednosti
     */
    private static String unosPodatka(Scanner scanner, String poruka) {
        System.out.print(poruka);
        return scanner.nextLine();
    }

    /**
     * Dohvaća podatak unesen na ulazu
     *
     * @param scanner scanner koji dohvaća unos s ulaza
     * @param ispis   poruka koja će se ispisati trazeći unos podatka
     * @return brojčani podatak o unesenoj vrijednosti
     */
    private static Integer unosBroja(Scanner scanner, String ispis) {
        Integer broj = 0;
        Boolean pogresanUnos;
        do {
            try {
                System.out.print(ispis);
                broj = scanner.nextInt();
                pogresanUnos = false;
            } catch (InputMismatchException ex) {
                System.out.println("## Pogreška u formatu podataka, molimo ponovite unos!");
                logger.error("Pogreška u formatu podataka, jer je unesen String koji se ne može parsirati", ex);
                pogresanUnos = true;
            } finally {
                scanner.nextLine();
            }
        } while (pogresanUnos);

        logger.info("Unesen je broj: " + broj);
        return broj;
    }


    /**
     * Dohvaća podatak o broju željenih entiteta za unos
     *
     * @param scanner scanner koji dohvaća unos s ulaza
     * @param poruka  poruka koja će se ispisati trazeći unos podatka
     * @return podatak o broju podataka
     */
    private static Integer unesiBrojZeljenihPodataka(Scanner scanner, String poruka) {
        Integer brojPodataka;
        do {
            brojPodataka = unosBroja(scanner, poruka);
            if (brojPodataka <= 0)
                System.out.println("## Molimo unesite broj veći od 0");
        } while (brojPodataka <= 0);

        return brojPodataka;
    }

    /**
     * Provjerava da je li vrijednost izvan granica intervala
     *
     * @param value podatak o vrijedosti
     * @param min   podatak o minimalnoj granici intervala
     * @param max   podatak o minimalnoj granici intervala
     * @return true ako je vrijednost izvan zadanog intervala, inače false
     */
    private static Boolean isOutsideRange(Integer value, Integer min, Integer max) {
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
        Integer brojZarazenih = unosBroja(scanner, ">> Unesite broj zaraženih " + (i + 1) + ". županije: ");
        return new Zupanija(naziv, brojStanovnika, brojZarazenih);
    }

    /**
     * Unosi podatke o simptomima
     *
     * @param scanner scanner koji dohvaća unos s ulaza
     * @param i       podatak o trenutnoj iteraciji petlje
     * @return instanca simptoma
     */
    private static Simptom unosSimptoma(Scanner scanner, int i) {
        String naziv = unosPodatka(scanner, ">> Unesite naziv " + (i + 1) + ". simptoma: ");

        String poruka = ">> Unesite vrijednost " + (i + 1) + ". simptoma (" +
                VrijednostSimptoma.RIJETKO.getVrijednost() + " / " +
                VrijednostSimptoma.SREDNJE.getVrijednost() + " / " +
                VrijednostSimptoma.CESTO.getVrijednost() + "): ";


        VrijednostSimptoma vrijednostSimptomaEnum = null;
        do {
            String unesenaVrijednost = unosPodatka(scanner, poruka);

            for (VrijednostSimptoma vrijednost : VrijednostSimptoma.values()) {
                if (vrijednost.getVrijednost().equalsIgnoreCase(unesenaVrijednost)) {
                    vrijednostSimptomaEnum = vrijednost;
                }
            }

            if (vrijednostSimptomaEnum == null) {
                prikaziPorukuNeispravnogUnosa();
                logger.info("Unesena je vrijednost simptoma koja ne postoji.");
            }
        } while (vrijednostSimptomaEnum == null);

        return new Simptom(naziv, vrijednostSimptomaEnum);
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
     */
    private static Bolest unosBolesti(Scanner scanner, Set<String> vrsteBolesti, Set<Simptom> simptomi, Set<Bolest> bolesti, int i) {

        // Odabir bolesti ili virusa
        Integer indexVrsteBolesti;
        do {
            String ponudeniOdabiri = ispisOdabira(vrsteBolesti, "Unosite li bolest ili virus?\n");
            indexVrsteBolesti = unosBroja(scanner, ponudeniOdabiri);

            if (isOutsideRange(indexVrsteBolesti, 1, vrsteBolesti.size())) {
                prikaziPorukuNeispravnogUnosa();
                logger.info("Pogreška u odabiru, odabir je izvan intervala dopuštenih vrijednosti");
            }

        } while (isOutsideRange(indexVrsteBolesti, 1, vrsteBolesti.size()));

        String vrstaBolesti = new ArrayList<>(vrsteBolesti).get(indexVrsteBolesti - 1);

        String naziv = unosPodatka(scanner, ">> Unesite naziv " + (i + 1) + ". bolesti ili virusa: ");

        Integer brojSimptoma;
        do {
            brojSimptoma = unosBroja(scanner, ">> Unesite broj simptoma: ");

            if (isOutsideRange(brojSimptoma, 1, simptomi.size())) {
                logger.error("Pogreška u odabiru, odabir je izvan intervala dopuštenih vrijednosti");
                if (simptomi.size() <= 1) {
                    System.out.println("## Molimo izaberite 1 simptom.");
                } else {
                    System.out.println("## Molimo izaberite izmedu 1 i " + simptomi.size() + " simptoma.");
                }
            }
        } while (isOutsideRange(brojSimptoma, 1, simptomi.size()));

        Set<Simptom> odabraniSimptomi = new HashSet<>();

        // Odabir simptoma za odabranu bolest
        for (int j = 0; j < brojSimptoma; j++) {
            Integer indexOdabranogSimptoma;
            do {
                String ponudeniOdabiri = ispisOdabira(simptomi, "Odaberite " + (j + 1) + ". simptom:\n");
                indexOdabranogSimptoma = unosBroja(scanner, ponudeniOdabiri);

                if (isOutsideRange(indexOdabranogSimptoma, 1, simptomi.size())) {
                    prikaziPorukuNeispravnogUnosa();
                    logger.info("Pogreška u odabiru, odabir je izvan intervala dopuštenih vrijednosti");
                }

            } while (isOutsideRange(indexOdabranogSimptoma, 1, simptomi.size()));

            Simptom odabraniSimptom = new ArrayList<>(simptomi).get(indexOdabranogSimptoma - 1);
            odabraniSimptomi.add(odabraniSimptom);
        }

        Bolest odabranaBolest = switch (vrstaBolesti.toUpperCase()) {
            case "VIRUS" -> new Virus(naziv, odabraniSimptomi);
            default -> new Bolest(naziv, odabraniSimptomi);
        };

        provjeraBolesti(bolesti, odabranaBolest);

        return odabranaBolest;
    }

    /**
     * Unosi podatke o osobi
     *
     * @param scanner  scanner koji dohvaća unos s ulaza
     * @param zupanije podatak o evidentiranim županijama
     * @param bolesti  podatak o evidentiranim bolestima
     * @param osobe    podatak o evidentiranim osobama
     * @param i        podatak o trenutnoj iteraciji petlje
     * @return instanca osobe
     */
    private static Osoba unosOsobe(Scanner scanner, Set<Zupanija> zupanije, Set<Bolest> bolesti, List<Osoba> osobe, int i) {
        String ime = unosPodatka(scanner, ">> Unesite ime " + (i + 1) + ". osobe: ");
        String prezime = unosPodatka(scanner, ">> Unesite prezime osobe: ");
        Integer starost = unosBroja(scanner, ">> Unesite starost osobe: ");

        Integer indexOdabraneZupanije;
        do {
            String ponudeniOdabiri = ispisOdabira(zupanije, "Unesite županiju osobe:\n");
            indexOdabraneZupanije = unosBroja(scanner, ponudeniOdabiri);

            if (isOutsideRange(indexOdabraneZupanije, 1, zupanije.size())) {
                prikaziPorukuNeispravnogUnosa();
                logger.info("Pogreška u odabiru, odabir je izvan intervala dopuštenih vrijednosti");
            }
        } while (isOutsideRange(indexOdabraneZupanije, 1, zupanije.size()));

        Zupanija zupanija = new ArrayList<>(zupanije).get(indexOdabraneZupanije - 1);


        Integer indexOdabraneBolesti;
        do {
            String ponudeniOdabiri = ispisOdabira(bolesti, "Unesite bolest ili virus osobe:\n");
            indexOdabraneBolesti = unosBroja(scanner, ponudeniOdabiri);

            if (isOutsideRange(indexOdabraneBolesti, 1, bolesti.size())) {
                prikaziPorukuNeispravnogUnosa();
                logger.info("Pogreška u odabiru, odabir je izvan intervala dopuštenih vrijednosti");
            }
        } while (isOutsideRange(indexOdabraneBolesti, 1, bolesti.size()));

        Bolest bolest = new ArrayList<>(bolesti).get(indexOdabraneBolesti - 1);


        List<Osoba> kontaktiraneOsobe = new ArrayList<>();
        if (!osobe.isEmpty()) {
            Integer brojKontakata;
            do {
                brojKontakata = unosBroja(scanner, ">> Unesite broj osoba koje su bile u kontaktu s tom osobom: ");

                if (isOutsideRange(brojKontakata, 0, i)) {
                    System.out.println("## Molimo izaberite izmedu 1 i " + i + " osoba.");
                    logger.error("Pogreška u odabiru, odabir je izvan intervala dopuštenih vrijednosti");
                }
            } while (isOutsideRange(brojKontakata, 0, i));

            if (brojKontakata > 0) {
                System.out.println("Unesite osobe koje su bile u kontaktu s tom osobom:");
            }

            for (int j = 0; j < brojKontakata; j++) {
                Integer indexOdabraneOsobe;
                Boolean neispravanKontakt = false;
                do {
                    String ponudeniOdabiri = ispisOdabira(osobe, "Odaberite " + (j + 1) + ". osobu:\n");
                    indexOdabraneOsobe = unosBroja(scanner, ponudeniOdabiri);

                    if (isOutsideRange(indexOdabraneOsobe, 1, i)) {
                        prikaziPorukuNeispravnogUnosa();
                        logger.info("Pogreška u odabiru, odabir je izvan intervala dopuštenih vrijednosti");
                    } else {
                        try {
                            provjeraKontakta(indexOdabraneOsobe, osobe, kontaktiraneOsobe);
                            neispravanKontakt = false;
                        } catch (DuplikatKontaktiraneOsobe ex) {
                            logger.error(ex.getMessage(), ex);
                            System.out.println("## " + ex.getMessage());
                            neispravanKontakt = true;
                        }
                    }
                } while (isOutsideRange(indexOdabraneOsobe, 1, i) || neispravanKontakt);

                Osoba konktaktOsoba = new ArrayList<>(osobe).get(indexOdabraneOsobe - 1);
                kontaktiraneOsobe.add(konktaktOsoba);
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
    private static void provjeraKontakta(Integer indexOdabraneOsobe, List<Osoba> osobe, List<Osoba> kontaktiraneOsobe) throws DuplikatKontaktiraneOsobe {
        Osoba odabranaOsoba = new ArrayList<>(osobe).get(indexOdabraneOsobe - 1);
        for (Osoba kontakt : kontaktiraneOsobe) {
            if (kontakt == null) break;
            if (kontakt.equals(odabranaOsoba)) {
                throw new DuplikatKontaktiraneOsobe("Odabrana osoba se već nalazi među kontaktiranim osobama. " +
                        "Odaberite neku drugu osobu.");
            }
        }
    }

    /**
     * Provjerava da li unesena bolest ima jednake simptoma kao već unesena bolest
     *
     * @param bolesti        podatak o evidentiranim bolestima
     * @param odabranaBolest podatak o odabranoj bolesti
     */
    private static void provjeraBolesti(Set<Bolest> bolesti, Bolest odabranaBolest) {
        for (Bolest bolest : bolesti) {
            if (bolest == null) break;
            Set<Simptom> simptomi = bolest.getSimptomi();
            Set<Simptom> simptomiOdabrane = odabranaBolest.getSimptomi();
            if (simptomi.size() == simptomiOdabrane.size() && simptomi.containsAll(simptomiOdabrane)) {
                throw new BolestIstihSimptoma("Bolest s izabranim simptomima već postoji. Molimo unesite novu bolest.");
            }
        }
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
    private static void printHeader(String title) {
        Integer size = 50;
        Integer remaining = size - title.length();
        String left = "";
        String right = "";
        for (int i = 0; i < (remaining / 2); i++) {
            left += '-';
        }
        for (int i = 0; i < remaining - left.length(); i++) {
            right += '-';
        }
        System.out.println("\n" + left + " " + title + " " + right);
    }

    /**
     * Ispisuje ponuđene opcije za odabir
     *
     * @param lista kolekcija koja sadrzi vrijednosti ponudenih odabira
     * @param ispis podatak o ispisu koji će se ispisati
     * @param <T>   tip podatka kolekcije
     * @return ispis odabira koji se ispisuje na izlazu
     */
    private static <T> String ispisOdabira(Collection<T> lista, String ispis) {
        Integer index = 0;
        for (T element : lista) {
            ispis += (index++ + 1) + ") " + element + "\n";
        }
        ispis += ">> Odabir: ";

        return ispis;
    }

    /**
     * Ispisuje podatke o evidentiranim osobama
     *
     * @param osobe podatak o unesenim osobama
     */
    private static void ispisOsoba(List<Osoba> osobe) {
        printHeader("Popis osoba");

        for (Osoba osoba : osobe) {
            System.out.println("\nIme i prezime: " + osoba.getIme() + " " + osoba.getPrezime());
            System.out.println("Starost: " + osoba.getStarost());
            System.out.println("Županija prebivališta: " + osoba.getZupanija().getNaziv());
            System.out.println("Zaražen bolešću: " + osoba.getZarazenBolescu().getNaziv());
            System.out.println("Kontaktirane osobe:");
            if (osoba.getKontaktiraneOsobe().isEmpty()) {
                System.out.println("Nema kontaktiranih osoba.");
            } else {
                osoba.getKontaktiraneOsobe().forEach(kontakt -> System.out.println("\t- " + kontakt));
            }
        }
    }

    /**
     * Ispisuje podatke o bolestima i osobama oboljelim od te bolesti
     *
     * @param mapaBolesti podatak o mapi bolesti i oboljelih
     */
    private static void ispisMapeBolesti(Map<Bolest, List<Osoba>> mapaBolesti) {
        printHeader("Popis bolesti i oboljelih");

        for (Map.Entry<Bolest, List<Osoba>> entry : mapaBolesti.entrySet()) {
            Bolest bolest = entry.getKey();
            List<Osoba> oboljeleOsobe = entry.getValue();

            if (oboljeleOsobe.isEmpty()) return;

            if (bolest instanceof Virus virus) {
                System.out.println("\nOd virusa " + virus.getNaziv().toUpperCase() + " boluju: ");
            } else {
                System.out.println("\nOd bolesti " + bolest.getNaziv().toUpperCase() + " boluju: ");
            }
            oboljeleOsobe.forEach(osoba -> System.out.println("\t- " + osoba));
        }
    }

    /**
     * Ispis osoba koje smo pretrazili kljucnom riječju
     *
     * @param osobe    podatak o listi osoba
     * @param pretraga podatak o riječi koju pretražujemo
     */
    private static void ispisPretrazenihOsoba(List<Osoba> osobe, String pretraga) {

        List<Osoba> filtriraneOsobe = osobe.stream()
                .filter(osoba -> osoba.getPrezime().toUpperCase().contains(pretraga.toUpperCase()))
                .collect(Collectors.toList());


        Optional<Osoba> optionalOsoba = Optional.of(filtriraneOsobe.stream().findAny())
                .get();

        if (optionalOsoba.isPresent()) {
            System.out.println("\nOsobe čije prezime sadrži " + pretraga + " su sljedeće: ");
            filtriraneOsobe.forEach(o -> System.out.println("\t- " + o));
        } else {
            System.out.println("Osoba nije pronađena.");
        }

        System.out.println();
    }

}
