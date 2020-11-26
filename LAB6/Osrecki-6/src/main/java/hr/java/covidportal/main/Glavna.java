package main.java.hr.java.covidportal.main;

import main.java.hr.java.covidportal.enumeracije.Serijalizacija;
import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.hr.java.covidportal.genericsi.KlinikaZaInfektivneBolesti;
import main.java.hr.java.covidportal.model.*;
import main.java.hr.java.covidportal.sort.CovidSorter;

import java.io.*;
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


    /**
     * Izvodi glavnu metodu programa
     *
     * @param args argumenti iz komandne linije
     */
    public static void main(String[] args) {

        System.out.println("\n***** Pokretanje programa *****\n");

        Scanner scanner = new Scanner(System.in);

        SortedSet<Zupanija> zupanije = new TreeSet<>(new CovidSorter());
        Set<Simptom> simptomi = new HashSet<>();
        Set<Bolest> bolesti = new LinkedHashSet<>();
        Set<Virus> virusi = new LinkedHashSet<>();
        List<Osoba> osobe = new ArrayList<>();

        System.out.println("Učitavanje podataka o županijama...");
        ucitavanjeZupanija(zupanije);

        System.out.println("Učitavanje podataka o simptomima...");
        ucitavanjeSimptoma(simptomi);

        System.out.println("Učitavanje podataka o bolestima...");
        ucitavanjeBolesti(bolesti, simptomi);

        System.out.println("Učitavanje podataka o virusima...");
        ucitavanjeVirusa(virusi, simptomi);

        System.out.println("Učitavanje osoba...");
        ucitavanjeOsoba(osobe, zupanije, bolesti, virusi);

        Set<Bolest> sveBolesti = new LinkedHashSet<>(bolesti);
        sveBolesti.addAll(virusi);

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


        serializeZupanije(zupanije, Serijalizacija.ZARAZENE_ZUPANIJE.getPath());
    }

    /**
     * Serijalizira županije sa zaraženošću većom od 2%
     * @param zupanije podatak o setu županija
     * @param path podatak o putanji do datoteke
     */
    private static void serializeZupanije(SortedSet<Zupanija> zupanije, String path) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(path))) {

            List<Zupanija> zarazeneZupanije = zupanije.stream()
                    .filter(z -> z.getPostotakZarazenih() > 2)
                    .collect(Collectors.toList());

            out.writeObject(zarazeneZupanije);

            System.out.println("\nSerijalizirane županije:");
            for (Zupanija zupanija : zarazeneZupanije) {
                System.out.println("\t- " + zupanija);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * erijalizira županije sa zaraženošću većom od 2%
     * @param path podatak o putanji do datoteke
     */
    private static void deserializeZupanije(String path) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {

            List<Zupanija> procitaneZupanija = (List<Zupanija>) in.readObject();

            System.out.println("\nDeserijalizirane županije:");
            for (Zupanija zupanija : procitaneZupanija) {
                System.out.println("\t- " + zupanija);

            }


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
     */
    private static void ucitavanjeZupanija(SortedSet<Zupanija> zupanije) {
        File zupanijeFile = new File("./dat/zupanije.txt");

        try (FileReader fileReader = new FileReader(zupanijeFile);
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
            System.out.println("File " + zupanijeFile.getName() + " not found.");
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Učitava podatke o simptomima iz datoteke
     *
     * @param simptomi podatak o setu simptoma
     */
    private static void ucitavanjeSimptoma(Set<Simptom> simptomi) {
        File simptomiFile = new File("./dat/simptomi.txt");

        try (FileReader fileReader = new FileReader(simptomiFile);
             BufferedReader reader = new BufferedReader(fileReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Long id = Long.parseLong(line);
                String naziv = reader.readLine();
                VrijednostSimptoma vrijednost = switch (reader.readLine()) {
                    case "RIJETKO" -> VrijednostSimptoma.RIJETKO;
                    case "SREDNJE" -> VrijednostSimptoma.SREDNJE;
                    case "ČESTO" -> VrijednostSimptoma.CESTO;
                    default -> throw new IllegalStateException("Unexpected value: " + reader.readLine());
                };

                Simptom simptom = new Simptom(id, naziv, vrijednost);
                simptomi.add(simptom);
            }
        } catch (IOException e) {
            System.out.println("File " + simptomiFile.getName() + " not found.");
            logger.error(e.getMessage(), e);
        }
    }


    /**
     * Učitava podatke o bolestima iz datoteke
     *
     * @param bolesti  podatak o setu bolesti
     * @param simptomi podatak o setu simptoma
     */
    private static void ucitavanjeBolesti(Set<Bolest> bolesti, Set<Simptom> simptomi) {
        File bolestiFile = new File("./dat/bolesti.txt");

        try (FileReader fileReader = new FileReader(bolestiFile);
             BufferedReader reader = new BufferedReader(fileReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Long id = Long.parseLong(line);
                String naziv = reader.readLine();

                Set<Simptom> simptomiBolesti = new HashSet<>();
                for (String idS : reader.readLine().split(",")) {
                    Long idSimptoma = Long.parseLong(idS);
                    Simptom simptom = simptomi.stream()
                            .filter(s -> s.getId().equals(idSimptoma))
                            .findAny()
                            .orElse(null);
                    simptomiBolesti.add(simptom);
                }

                Bolest bolest = new Bolest(id, naziv, simptomiBolesti);
                bolesti.add(bolest);
            }
        } catch (IOException e) {
            System.out.println("File " + bolestiFile.getName() + " not found.");
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Učitava podatke o virusima iz datoteke
     *
     * @param virusi   podatak o setu virusa
     * @param simptomi podatak o setu simptoma
     */
    private static void ucitavanjeVirusa(Set<Virus> virusi, Set<Simptom> simptomi) {
        File virusiFile = new File("./dat/virusi.txt");

        try (FileReader fileReader = new FileReader(virusiFile);
             BufferedReader reader = new BufferedReader(fileReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Long id = Long.parseLong(line);
                String naziv = reader.readLine();

                Set<Simptom> simptomiVirusa = new HashSet<>();
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
            System.out.println("File " + virusiFile.getName() + " not found.");
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Učitava podatke o osobama iz datoteke
     *
     * @param osobe    podatak o listi osoba
     * @param zupanije podatak o setu zupanija
     * @param bolesti  podatak o setu bolesti
     * @param virusi   podatak o setu virusa
     */
    private static void ucitavanjeOsoba(List<Osoba> osobe, SortedSet<Zupanija> zupanije, Set<Bolest> bolesti, Set<Virus> virusi) {
        File osobeFile = new File("./dat/osobe.txt");

        try (FileReader fileReader = new FileReader(osobeFile);
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

                List<Osoba> kontaktiOsobe = new ArrayList<>();
                String konkaktiId = reader.readLine();
                if (!konkaktiId.trim().isEmpty()) {
                    for (String kId : konkaktiId.split(",")) {
                        Long kontkatkId = Long.parseLong(kId);
                        osobe.stream()
                                .filter(osoba -> osoba.getId().equals(kontkatkId))
                                .findAny()
                                .ifPresent(kontaktiOsobe::add);
                    }
                }
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
            System.out.println("File " + osobeFile.getName() + " not found.");
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
        }
    }

    /**
     * Ispisuje sortirane viruse
     *
     * @param virusi podatak o setu virusa
     */
    private static void ispisSortiranihVirusa(Set<Virus> virusi) {
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
