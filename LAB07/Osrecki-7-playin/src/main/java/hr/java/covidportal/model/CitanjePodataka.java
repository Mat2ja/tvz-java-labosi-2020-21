package main.java.hr.java.covidportal.model;

import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.sample.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface CitanjePodataka {

    String FILE_NAME_ZUPANIJE = "./dat/zupanije.txt";
    String FILE_NAME_SIMPTOMI = "./dat/simptomi.txt";
    String FILE_NAME_BOLESTI = "./dat/bolesti.txt";
    String FILE_NAME_VIRUSA = "./dat/virusi.txt";
    String FILE_NAME_OSOBA = "./dat/osobe.txt";

    /**
     * Čita županije iz datoteke
     *
     * @return lista županija
     */
    static List<Zupanija> ucitajZupanije() {
        File zupanijeFile = new File(FILE_NAME_ZUPANIJE);
        List<Zupanija> zupanije = new ArrayList<>();

        if (zupanijeFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(zupanijeFile))) {

                String line;
                while ((line = br.readLine()) != null) {
                    Long id = Long.parseLong(line);
                    String naziv = br.readLine();
                    Integer brojStanovnika = Integer.parseInt(br.readLine());
                    Integer brojZarazenih = Integer.parseInt(br.readLine());

                    Zupanija zupanija = new Zupanija(id, naziv, brojStanovnika, brojZarazenih);
                    zupanije.add(zupanija);
                }
            } catch (IOException e) {
                Main.logger.error("File not found", e);
                e.printStackTrace();
                System.out.println("File " + FILE_NAME_ZUPANIJE + " not found.");
            }
        }

        return zupanije;
    }


    /**
     * Čita simptome iz datoteke
     *
     * @return lista simptoma
     */
    static List<Simptom> ucitajSimptome() {

        File simptomiFile = new File(FILE_NAME_SIMPTOMI);
        List<Simptom> simptomi = new ArrayList<>();

        if (simptomiFile.exists()) {

            try (FileReader fileReader = new FileReader(FILE_NAME_SIMPTOMI);
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
                Main.logger.error("File not found", e);
                System.out.println("File " + FILE_NAME_SIMPTOMI + " not found.");
            }
        }

        return simptomi;
    }

    /**
     * Čita bolesti iz datoteke
     *
     * @return lista bolesti
     */
    static List<Bolest> ucitajBolesti() {

        File bolestiFile = new File(FILE_NAME_BOLESTI);

        List<Bolest> bolesti = new ArrayList<>();

        if (bolestiFile.exists()) {
            List<Simptom> simptomi = ucitajSimptome();

            try (FileReader fileReader = new FileReader(FILE_NAME_BOLESTI);
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
                Main.logger.error("File not found", e);
                System.out.println("File " + FILE_NAME_BOLESTI + " not found.");
            }
        }

        return bolesti;
    }

    /**
     * Čita viruse iz datoteke
     *
     * @return lista virusa
     */
    static List<Virus> ucitajViruse() {
        File virusiFile = new File(FILE_NAME_VIRUSA);
        List<Virus> virusi = new ArrayList<>();

        if (virusiFile.exists()) {
            List<Simptom> simptomi = ucitajSimptome();

            try (FileReader fileReader = new FileReader(FILE_NAME_VIRUSA);
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
                Main.logger.error("File not found", e);
                System.out.println("File " + FILE_NAME_VIRUSA + " not found.");
            }
        }

        return virusi;
    }

    /**
     * Čita osobe iz datoteke
     *
     * @return lista osoba
     */
    static List<Osoba> ucitajOsobe() {
        File osobeFile = new File(FILE_NAME_OSOBA);
        List<Osoba> osobe = new ArrayList<>();

        if (osobeFile.exists()) {
            List<Zupanija> zupanije = ucitajZupanije();
            List<Bolest> bolesti = ucitajBolesti();
            List<Virus> virusi = ucitajViruse();

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
                Main.logger.error("File not found", e);
                System.out.println("File " + FILE_NAME_OSOBA + " not found.");
            }
        }

        return osobe;
    }
}
