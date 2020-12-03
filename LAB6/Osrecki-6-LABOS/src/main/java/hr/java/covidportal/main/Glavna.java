package main.java.hr.java.covidportal.main;

import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.hr.java.covidportal.genericsi.KlinikaZaInfektivneBolesti;
import main.java.hr.java.covidportal.model.*;
import main.java.hr.java.covidportal.sort.CovidSorter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Predstavlja glavnu klasu u kojoj se izvodi program
 *
 * @author Matija
 */
public class Glavna {

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);
    private static final String FILE_NAME_ZUPANIJE = "./dat/zupanije.txt";
    private static final String FILE_NAME_SIMPTOMI = "./dat/simptomi.txt";
    private static final String FILE_NAME_BOLESTI = "./dat/bolesti.txt";
    private static final String FILE_NAME_VIRUSI = "./dat/virusi.txt";
    private static final String FILE_NAME_OSOBE = "./dat/osobe.txt";
    private static final String FILE_NAME_SERIJALIZACIJA_ZUPANIJA = "./dat/zarazene_zupanije.dat";
    private static final String FILE_NAME_SERIJALIZACIJA_VIRUSA = "./dat/virusi_serijalizirani.dat";
    private static final String FILE_NAME_VIRUSI_STATS = "./dat/virusi_stats.txt";


    /**
     * Izvodi glavnu metodu programa
     *
     * @param args argumenti iz komandne linije
     */
    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);

        SortedSet<Zupanija> zupanije = new TreeSet<>(new CovidSorter());
        List<Simptom> simptomi = new ArrayList<>();
        List<Bolest> bolesti = new ArrayList<>();
        List<Virus> virusi = new ArrayList<>();
        List<Osoba> osobe = new ArrayList<>();

        // 2. zadatak
        printHeader("Virus stats");
        ispisStatseVirusa(FILE_NAME_VIRUSI_STATS);

        printHeader("Učitavanje podataka");
        System.out.println("Učitavanje podataka o županijama...");
        ucitavanjeZupanija(zupanije, FILE_NAME_ZUPANIJE);

        System.out.println("Učitavanje podataka o simptomima...");
        ucitavanjeSimptoma(simptomi, FILE_NAME_SIMPTOMI);

        System.out.println("Učitavanje podataka o bolestima...");
        ucitavanjeBolesti(bolesti, simptomi, FILE_NAME_BOLESTI);

        System.out.println("Učitavanje podataka o virusima...");
        ucitavanjeVirusa(virusi, simptomi, FILE_NAME_VIRUSI);

        System.out.println("Učitavanje osoba...");
        ucitavanjeOsoba(osobe, zupanije, bolesti, virusi, FILE_NAME_OSOBE);

        List<Bolest> sveBolesti = new ArrayList<>(bolesti);
        sveBolesti.addAll(virusi);

        // 1. zadatak
        printHeader("Deserijalizacija virusa");
        deserializeVirusi(FILE_NAME_SERIJALIZACIJA_VIRUSA);
        serializeVirusi(virusi, FILE_NAME_SERIJALIZACIJA_VIRUSA);

        // 2. zadatak
        zapisiStatseVirusa(virusi, FILE_NAME_VIRUSI_STATS);


        printHeader("Popis osoba");
        ispisOsoba(osobe);

        printHeader("Popis bolesti i oboljelih");
        ispisMapeBolesti(osobe);


        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        System.out.println("Najviše zaraženih osoba ima u županiji "
                + zupanije.first().getNaziv() + ": "
                + df.format(zupanije.first().getPostotakZarazenih()) + "%");


        KlinikaZaInfektivneBolesti<Virus, Osoba> klinika = new KlinikaZaInfektivneBolesti<>(
                new ArrayList<>(virusi),
                osobe.stream()
                        .filter(osoba -> osoba.getZarazenBolescu() instanceof Virus)
                        .collect(Collectors.toList()));


        System.out.println("\nSortiranje korištenjem lambdi traje " +
                trajanjeSortiranjaLambdom(klinika.getVirusi()) + " ms, a bez lambda " +
                trajanjeSortiranjaBezLambde(klinika.getVirusi()) + " ms.");


        System.out.println("\nVirusi sortirani po nazivu suprotno od poretka abecede:");
        ispisSortiranihVirusa(virusi);


        printHeader("Pretraga osoba");
        String pretraga = unosPodatka(scanner, ">> Unesite string za pretragu po prezimenu: ");
        ispisPretrazenihOsoba(osobe, pretraga);


        printHeader("Popis broja simptoma bolesti");
        sveBolesti.stream()
                .map(b -> b.getClass().getSimpleName() + " " + b.getNaziv() + " ima " + b.getSimptomi().size() + " simptoma.")
                .forEach(System.out::println);


        //printHeader("Serijalizacija");
        //serializeZupanije(zupanije, FILE_NAME_SERIJALIZACIJA_ZUPANIJA);
        //deserializeZupanije(FILE_NAME_SERIJALIZACIJA_ZUPANIJA);

    }

    /**
     * Ispisuje statse virusa iz datoteke
     *
     * @param filename podatak o putanji do datoteke
     */
    private static void ispisStatseVirusa(String filename) {
        Path path = Path.of(filename);
        if (Files.exists(path)) {
            try {
                String ucitano = Files.readString(path);
                System.out.println(ucitano);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Datoteka nije nađena.");
        }
    }

    /**
     * Ispisuje statse virusa u datoteku
     *
     * @param virusi   podatak o listi virusa
     * @param filename podatak o putanji do datoteke
     */
    private static void zapisiStatseVirusa(List<Virus> virusi, String filename) {

        Integer sumaDuljine = virusi.stream()
                .mapToInt(v -> v.getNaziv().length())
                .sum();

        Double prosjekDuljine = virusi.stream()
                .mapToInt(v -> v.getNaziv().length())
                .average()
                .getAsDouble();

        Virus najkraci = virusi.stream()
                .min(Comparator.comparingInt(v -> v.getNaziv().length()))
                .get();

        Virus najdulji = virusi.stream()
                .max(Comparator.comparingInt(v -> v.getNaziv().length()))
                .get();

        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("Suma duljine: " + sumaDuljine);
            pw.println("Prosjek duljine: " + prosjekDuljine);
            pw.println("Najkraći naziv: " + najkraci);
            pw.println("Najdulji naziv: " + najdulji);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serijalizira viruse
     *
     * @param virusi   podatak o listi virusa
     * @param filename podatak o putanji do datoteke
     */
    private static void serializeVirusi(List<Virus> virusi, String filename) {
        File file = new File(filename);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {

            List<Virus> serijaliziraniSimptomi = virusi.stream()
                    .filter(s -> s.getNaziv().toLowerCase().startsWith("e"))
                    .filter(s -> s.getNaziv().toLowerCase().contains("bola"))
                    .collect(Collectors.toList());

            out.writeObject(serijaliziraniSimptomi);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Deserijalizira viruse
     *
     * @param filename podatak o putanji do datoteke
     */
    private static void deserializeVirusi(String filename) {
        File file = new File(filename);

        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {

                List<Virus> procitaniVirusi = (List<Virus>) in.readObject();
                procitaniVirusi.forEach(System.out::println);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Datoteka nije nađena.");
        }


    }

    /**
     * Serijalizira županije sa zaraženošću većom od 2%
     *
     * @param zupanije podatak o setu županija
     * @param filename podatak o putanji do datoteke
     */
    private static void serializeZupanije(SortedSet<Zupanija> zupanije, String filename) {
        File file = new File(filename);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {

            List<Zupanija> zarazeneZupanije = zupanije.stream()
                    .filter(z -> z.getPostotakZarazenih() > 2)
                    .collect(Collectors.toList());

            out.writeObject(zarazeneZupanije);

            System.out.println("Serijalizirane županije:");
            zarazeneZupanije.forEach(zupanija -> System.out.println("\t- " + zupanija));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserijalizira županije sa zaraženošću većom od 2%
     *
     * @param filename podatak o putanji do datoteke
     */
    private static void deserializeZupanije(String filename) {
        File file = new File(filename);

        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {

            List<Zupanija> procitaneZupanije = (List<Zupanija>) in.readObject();

            System.out.println("\nDeserijalizirane županije:");
            procitaneZupanije.forEach(zupanija -> System.out.println("\t- " + zupanija));

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
     * Učitava podatke o županijama iz datoteke
     *
     * @param zupanije podatak o setu županija
     * @param filename podatak o putanji do datoteke
     */
    private static void ucitavanjeZupanija(SortedSet<Zupanija> zupanije, String filename) {
        try (FileReader fileReader = new FileReader(filename);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                Long id = Long.parseLong(line);
                String naziv = reader.readLine();
                Integer brojStanovnika = Integer.parseInt(reader.readLine());
                Integer brojZarazenih = Integer.parseInt(reader.readLine());

                Zupanija zupanija = new Zupanija(id, naziv, brojStanovnika, brojZarazenih);
                zupanije.add(zupanija);
            }

        } catch (IOException e) {
            System.out.println("File " + filename + " not found.");
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Učitava podatke o simptomima iz datoteke
     *
     * @param simptomi podatak o listi simptoma
     * @param filename podatak o putanji do datoteke
     */
    private static void ucitavanjeSimptoma(List<Simptom> simptomi, String filename) {

        try (FileReader fileReader = new FileReader(filename);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                Long id = Long.parseLong(line);
                String naziv = reader.readLine();
                VrijednostSimptoma vrijednost = VrijednostSimptoma.valueOf(reader.readLine());

                Simptom simptom = new Simptom(id, naziv, vrijednost);
                simptomi.add(simptom);
            }
        } catch (IOException e) {
            System.out.println("File " + filename + " not found.");
            logger.error(e.getMessage(), e);
        }
    }


    /**
     * Učitava podatke o bolestima iz datoteke
     *
     * @param bolesti  podatak o listi bolesti
     * @param simptomi podatak o listi simptoma
     * @param filename podatak o putanji do datoteke
     */
    private static void ucitavanjeBolesti(List<Bolest> bolesti, List<Simptom> simptomi, String filename) {

        try (FileReader fileReader = new FileReader(filename);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                Long id = Long.parseLong(line);
                String naziv = reader.readLine();


                List<Long> simptomiIds = Arrays.stream(reader.readLine()
                        .split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                List<Simptom> simptomiBolesti = simptomi.stream()
                        .filter(s -> simptomiIds.contains(s.getId()))
                        .collect(Collectors.toList());


                Bolest bolest = new Bolest(id, naziv, simptomiBolesti);
                bolesti.add(bolest);
            }
        } catch (IOException e) {
            System.out.println("File " + filename + " not found.");
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Učitava podatke o virusima iz datoteke
     *
     * @param virusi   podatak o listi virusa
     * @param simptomi podatak o listi simptoma
     * @param filename podatak o putanji do datoteke
     */
    private static void ucitavanjeVirusa(List<Virus> virusi, List<Simptom> simptomi, String filename) {

        try (FileReader fileReader = new FileReader(filename);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                Long id = Long.parseLong(line);
                String naziv = reader.readLine();

                List<Simptom> simptomiVirusa = new ArrayList<>();
                for (String sId : reader.readLine().split(",")) {
                    Long simptomId = Long.parseLong(sId);
                    simptomi.stream()
                            .filter(s -> s.getId().equals(simptomId))
                            .findAny()
                            .ifPresent(simptomiVirusa::add);
                }

                Virus virus = new Virus(id, naziv, simptomiVirusa);
                virusi.add(virus);
            }
        } catch (IOException e) {
            System.out.println("File " + filename + " not found.");
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Učitava podatke o osobama iz datoteke
     *
     * @param osobe    podatak o listi osoba
     * @param zupanije podatak o listi zupanija
     * @param bolesti  podatak o listi bolesti
     * @param virusi   podatak o listi virusa
     * @param filename podatak o putanji do datoteke
     */
    private static void ucitavanjeOsoba(List<Osoba> osobe, SortedSet<Zupanija> zupanije, List<Bolest> bolesti, List<Virus> virusi, String filename) {

        try (FileReader fileReader = new FileReader(filename);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String line;
            while ((line = reader.readLine()) != null) {

                Long id = Long.parseLong(line);
                String ime = reader.readLine();
                String prezime = reader.readLine();
                Integer starost = Integer.parseInt(reader.readLine());

                Long zupanijaId = Long.parseLong(reader.readLine());
                Zupanija zupanija = zupanije.stream()
                        .filter(zup -> zup.getId().equals(zupanijaId))
                        .findAny()
                        .orElse(null);

                Long bolestId = Long.parseLong(reader.readLine());
                Bolest bolest = bolesti.stream()
                        .filter(b -> b.getId().equals(bolestId))
                        .findAny()
                        .orElse(null);
                if (bolest == null) {
                    bolest = virusi.stream()
                            .filter(v -> v.getId().equals(bolestId))
                            .findAny()
                            .orElse(null);
                }

                List<Long> kontaktiIds = Arrays.stream(reader.readLine()
                        .split(","))
                        .filter(item -> !item.isBlank())
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                List<Osoba> kontaktiOsobe = osobe.stream()
                        .filter(o -> kontaktiIds.contains(o.getId()))
                        .collect(Collectors.toList());

                Osoba osoba = new Osoba.Builder(id)
                        .hasIme(ime)
                        .hasPrezime(prezime)
                        .isAged(starost)
                        .atZupanija(zupanija)
                        .withBolest(bolest)
                        .withKontaktiraneOsobe(kontaktiOsobe)
                        .build();
                osobe.add(osoba);

            }
        } catch (IOException e) {
            System.out.println("File " + filename + " not found.");
            logger.error(e.getMessage(), e);
        }
    }


    /**
     * Prikazuje naslov sekcije
     */
    private static void printHeader(String title) {
        Integer size = 50;
        Integer remaining = size - title.length();
        StringBuilder left = new StringBuilder();
        StringBuilder right = new StringBuilder();
        left.append("-".repeat(Math.max(0, (remaining / 2))));
        right.append("-".repeat(Math.max(0, remaining - left.length())));
        System.out.println("\n" + left + " " + title + " " + right + "\n");
    }

    /**
     * Ispisuje podatke o evidentiranim osobama
     *
     * @param osobe podatak o unesenim osobama
     */
    private static void ispisOsoba(List<Osoba> osobe) {
        for (Osoba osoba : osobe) {
            System.out.println("Ime i prezime: " + osoba.getIme() + " " + osoba.getPrezime());
            System.out.println("Starost: " + osoba.getStarost());
            System.out.println("Županija prebivališta: " + osoba.getZupanija().getNaziv());
            System.out.println("Zaražen bolešću: " + osoba.getZarazenBolescu().getNaziv());
            System.out.println("Kontaktirane osobe:");
            if (osoba.getKontaktiraneOsobe().isEmpty()) {
                System.out.println("Nema kontaktiranih osoba.");
            } else {
                osoba.getKontaktiraneOsobe().forEach(kontakt -> System.out.println("\t- " + kontakt));
            }
            System.out.println();
        }
    }

    /**
     * Ispisuje sortirane viruse
     *
     * @param virusi podatak o listi virusa
     */
    private static void ispisSortiranihVirusa(List<Virus> virusi) {
        List<Virus> sortiraniVirusi = virusi.stream()
                .sorted(Comparator.comparing(Virus::getNaziv).reversed())
                .collect(Collectors.toList());
        Integer i = 0;
        for (Virus virus : sortiraniVirusi) {
            System.out.println(++i + ". " + virus);
        }
    }


    /**
     * Ispisuje podatke o bolestima i osobama oboljelim od te bolesti
     *
     * @param osobe podatak o listi osoba
     */
    private static void ispisMapeBolesti(List<Osoba> osobe) {
        Map<Bolest, List<Osoba>> mapaBolesti = osobe.stream()
                .collect(Collectors.groupingBy(Osoba::getZarazenBolescu));

        for (Map.Entry<Bolest, List<Osoba>> entry : mapaBolesti.entrySet()) {
            Bolest bolest = entry.getKey();
            List<Osoba> oboljeleOsobe = entry.getValue();

            if (oboljeleOsobe.isEmpty()) return;

            if (bolest instanceof Virus virus) {
                System.out.println("Od virusa " + virus.getNaziv().toUpperCase() + " boluju:");
            } else {
                System.out.println("Od bolesti " + bolest.getNaziv().toUpperCase() + " boluju: ");
            }
            oboljeleOsobe.forEach(osoba -> System.out.println("\t- " + osoba));
            System.out.println();
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
            System.out.println("\nOsobe čije prezime sadrži '" + pretraga + "' su sljedeće: ");
            filtriraneOsobe.forEach(o -> System.out.println("\t- " + o));
        } else {
            System.out.println("\nOsoba nije pronađena.");
        }
    }


    /**
     * Sortira viruse pomoću lambde  i mjeri vrijeme izvršenja
     *
     * @param virusi podatak o listi virusa
     * @return podatak o trajanju sortiranja u milisekundama
     */
    private static Long trajanjeSortiranjaLambdom(List<Virus> virusi) {
        Instant start = Instant.now();
        virusi.stream()
                .sorted(Comparator.comparing(Virus::getNaziv).reversed());
        Instant end = Instant.now();
        return Duration.between(start, end).toMillis();
    }

    /**
     * Sortira viruse bez lambde i mjeri vrijeme izvršenja
     *
     * @param virusi podatak o listi virusa
     * @return podatak o trajanju sortiranja u milisekundama
     */
    private static Long trajanjeSortiranjaBezLambde(List<Virus> virusi) {
        Instant start = Instant.now();

        virusi.stream()
                .sorted(new Comparator<>() {
                    @Override
                    public int compare(Virus o1, Virus o2) {
                        return o2.getNaziv().compareTo(o1.getNaziv());
                    }
                });
        Instant end = Instant.now();
        return Duration.between(start, end).toMillis();

    }
}
