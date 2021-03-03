package main.java.hr.java.covidportal.model;

import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.sample.Main;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Stack;
import java.util.stream.Collectors;

public final class BazaPodataka {

    private static final String DATABASE_FILE = "./dat/database.properties";

    /**
     * Otvara vezu s bazom podataka
     *
     * @return veza s bazom podataka
     * @throws IOException
     * @throws SQLException
     */
    private static Connection connectToDatabase() throws IOException, SQLException {
        Properties svojstva = new Properties();
        svojstva.load(new FileReader(DATABASE_FILE));

        String urlBazePodataka = svojstva.getProperty("bazaPodatakaUrl");
        String korisnickoIme = svojstva.getProperty("korisnickoIme");
        String lozinka = svojstva.getProperty("lozinka");

        Connection veza = DriverManager.getConnection(urlBazePodataka, korisnickoIme, lozinka);

        return veza;
    }

    /**
     * Zatvara vezu s bazom podataka
     *
     * @param veza veza s bazom podataka
     * @throws SQLException
     */
    private static void disconnectFromDatabase(Connection veza) throws SQLException {
        veza.close();
    }

    /**
     * Dohvaća sve županije iz baze podataka
     *
     * @return lista županija
     */
    public static List<Zupanija> dohvatiSveZupanije() {
        List<Zupanija> zupanije = new ArrayList<>();

        try (Connection veza = connectToDatabase()) {
            Statement stmt = veza.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ZUPANIJA");

            while (rs.next()) {
                Long id = rs.getLong("ID");
                String naziv = rs.getString("NAZIV");
                Integer brojStanovnika = rs.getInt("BROJ_STANOVNIKA");
                Integer brojZarazenih = rs.getInt("BROJ_ZARAZENIH_STANOVNIKA");

                Zupanija zupanija = new Zupanija(naziv, brojStanovnika, brojZarazenih);
                zupanija.setId(id);

                zupanije.add(zupanija);
            }
        } catch (IOException | SQLException e) {
            Main.logger.error("Greška kod dohvata svih županija");
            e.printStackTrace();
        }

        return zupanije;
    }

    /**
     * Dohvaća županiju iz baze podataka
     *
     * @param veza      veza s bazom podataka
     * @param trazeniId podatak o id-u
     * @return podatak o županiji
     */
    public static Zupanija dohvatiZupaniju(Connection veza, Long trazeniId) {
        Zupanija zupanija = null;
        try {
            String sql = "SELECT * FROM ZUPANIJA WHERE ID = " + trazeniId;
            Statement stmt = veza.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                Long id = rs.getLong("ID");
                String naziv = rs.getString("NAZIV");
                Integer brojStanovnika = rs.getInt("BROJ_STANOVNIKA");
                Integer brojZarazenih = rs.getInt("BROJ_ZARAZENIH_STANOVNIKA");
                zupanija = new Zupanija(naziv, brojStanovnika, brojZarazenih);
                zupanija.setId(id);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return zupanija;
    }

    /**
     * Sprema novu županiju u bazu podataka
     *
     * @param novaZupanija podatak o novoj županiji
     */
    public static void spremiNovuZupaniju(Zupanija novaZupanija) {
        try (Connection veza = connectToDatabase()) {
            String sql = "INSERT INTO ZUPANIJA(NAZIV, BROJ_STANOVNIKA, BROJ_ZARAZENIH_STANOVNIKA) VALUES(?, ?, ?)";
            PreparedStatement upit = veza.prepareStatement(sql);

            upit.setString(1, novaZupanija.getNaziv());
            upit.setInt(2, novaZupanija.getBrojStanovnika());
            upit.setInt(3, novaZupanija.getBrojZarazenih());

            upit.executeUpdate();

        } catch (IOException | SQLException e) {
            Main.logger.error("Greška kod spremanja županije");
            e.printStackTrace();
        }
    }

    /**
     * Dohvaća sve simptome iz baze podataka
     *
     * @return lista simptoma
     */
    public static List<Simptom> dohvatiSveSimptome() {
        List<Simptom> simptomi = new ArrayList<>();

        try (Connection veza = connectToDatabase()) {
            Statement stmt = veza.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM SIMPTOM");

            while (rs.next()) {
                Long id = rs.getLong("ID");
                String naziv = rs.getString("NAZIV");
                VrijednostSimptoma vrijednost = VrijednostSimptoma.valueOf(rs.getString("VRIJEDNOST"));
                Simptom simptom = new Simptom(naziv, vrijednost);

                simptom.setId(id);
                simptomi.add(simptom);
            }
        } catch (IOException | SQLException e) {
            Main.logger.error("Greška kod dohvata svih simptoma");
            e.printStackTrace();
        }

        return simptomi;
    }

    /**
     * Dohvaća simptom iz baze podataka
     *
     * @param veza      veza s bazom podataka
     * @param trazeniId podatak o id-u
     * @return podatak o simptomu
     */
    public static Simptom dohvatiSimptom(Connection veza, Long trazeniId) {

        Simptom simptom = null;

        try {
            Statement stmt = veza.createStatement();
            String sql = "SELECT * FROM SIMPTOM WHERE ID = " + trazeniId;
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                Long id = rs.getLong("ID");
                String naziv = rs.getString("NAZIV");
                VrijednostSimptoma vrijednost = VrijednostSimptoma.valueOf(rs.getString("VRIJEDNOST"));

                simptom = new Simptom(naziv, vrijednost);
                simptom.setId(id);
            }
        } catch (SQLException throwables) {
            Main.logger.error("Greška kod dohvata simptoma");
            throwables.printStackTrace();
        }


        return simptom;
    }

    /**
     * Sprema novi simptom u bazu podataka
     *
     * @param noviSimptom podatak o novom simptomu
     */
    public static void spremiNoviSimptom(Simptom noviSimptom) {
        try (Connection veza = connectToDatabase()) {
            String sql = "INSERT INTO SIMPTOM(NAZIV, VRIJEDNOST) VALUES(?, ?)";
            PreparedStatement upit = veza.prepareStatement(sql);

            upit.setString(1, noviSimptom.getNaziv());
            upit.setString(2, noviSimptom.getVrijednost().getVrijednost());

            upit.executeUpdate();
        } catch (IOException | SQLException e) {
            Main.logger.error("Greška kod spremanja simptoma");
            e.printStackTrace();
        }
    }

    /**
     * Dohvaća sve bolesti iz baze podataka
     *
     * @return lista bolesti
     */
    public static List<Bolest> dohvatiSveBolesti() {
        List<Bolest> bolesti = new ArrayList<>();

        try (Connection veza = connectToDatabase()) {
            Statement stmt = veza.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM BOLEST");

            while (rs.next()) {
                Long id = rs.getLong("ID");
                String naziv = rs.getString("NAZIV");
                Boolean jeVirus = rs.getBoolean("VIRUS");
                List<Simptom> listaSimptoma = dohvatiSimptomeBolesti(veza, id);

                if (jeVirus) {
                    Virus virus = new Virus(naziv, listaSimptoma);
                    virus.setId(id);
                    bolesti.add(virus);
                } else {
                    Bolest bolest = new Bolest(naziv, listaSimptoma);
                    bolest.setId(id);
                    bolesti.add(bolest);
                }
            }
        } catch (IOException | SQLException e) {
            Main.logger.error("Greška kod dohvata svih bolesti");
            e.printStackTrace();
        }

        return bolesti;
    }

    /**
     * Dohvaća bolest iz baze podataka
     *
     * @param veza      veza s bazom podataka
     * @param trazeniId podatak o id-u
     * @return podatak o bolesti
     */
    public static Bolest dohvatiBolest(Connection veza, Long trazeniId) {
        Bolest bolest = null;

        try {
            String sql = "SELECT * FROM BOLEST WHERE ID = " + trazeniId;
            Statement stmt = veza.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                Long id = rs.getLong("ID");
                String naziv = rs.getString("NAZIV");
                Boolean jeVirus = rs.getBoolean("VIRUS");
                List<Simptom> listaSimptoma = dohvatiSimptomeBolesti(veza, id);

                bolest = new Bolest(naziv, listaSimptoma);
                bolest.setJeVirus(jeVirus);

                bolest.setId(id);
            }
        } catch (SQLException throwables) {
            Main.logger.error("Greška kod dohvata bolesti");
            throwables.printStackTrace();
        }

        if (bolest.getJeVirus()) {
            Virus virus = new Virus(bolest.getNaziv(), bolest.getSimptomi());
            virus.setId(bolest.getId());
            return virus;
        } else {
            return bolest;
        }
    }

    /**
     * Sprema novu bolest u bazu podataka
     *
     * @param novaBolest podatak o novoj bolesti
     */
    public static void spremiNovuBolest(Bolest novaBolest) {
        try (Connection veza = connectToDatabase()) {

            veza.setAutoCommit(false);

            String sql = "INSERT INTO BOLEST(NAZIV, VIRUS) VALUES(?, ?)";
            PreparedStatement upit = veza.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            upit.setString(1, novaBolest.getNaziv());
            upit.setBoolean(2, novaBolest.getJeVirus());
            upit.executeUpdate();

            Long bolestId = dohvatiId(upit);

            for (Simptom simptom : novaBolest.getSimptomi()) {
                PreparedStatement upitSimptom = veza.prepareStatement(
                        "INSERT INTO BOLEST_SIMPTOM(BOLEST_ID, SIMPTOM_ID) VALUES(?, ?)");

                upitSimptom.setLong(1, bolestId);
                upitSimptom.setLong(2, simptom.getId());
                upitSimptom.executeUpdate();
            }

            veza.commit();
            veza.setAutoCommit(true);
        } catch (SQLException | IOException throwables) {
            Main.logger.error("Greška kod spremanja bolesti");
            throwables.printStackTrace();
        }
    }

    /**
     * Dohvaća sve simptome bolesti iz baze podataka
     *
     * @param veza veza s bazom podataka
     * @param id   podatak o id-u
     * @return lista simptoma
     */
    private static List<Simptom> dohvatiSimptomeBolesti(Connection veza, Long id) {
        List<Simptom> listaSimptoma = new ArrayList<>();

        try {
            String sql = "SELECT * FROM BOLEST_SIMPTOM WHERE BOLEST_ID = " + id;
            Statement stmt = veza.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Long simptomId = rs.getLong("SIMPTOM_ID");
                Simptom simptom = dohvatiSimptom(veza, simptomId);
                listaSimptoma.add(simptom);
            }
        } catch (SQLException throwables) {
            Main.logger.error("Greška kod dohvata simptoma bolesti");
            throwables.printStackTrace();
        }

        return listaSimptoma;
    }

    /**
     * Dohvaća sve osobe iz baze podataka
     *
     * @return lista osoba
     */
    public static List<Osoba> dohvatiSveOsobe() {
        List<Osoba> osobe = new ArrayList<>();

        try (Connection veza = connectToDatabase()) {
            Statement stmt = veza.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM OSOBA");

            while (rs.next()) {
                Long id = rs.getLong("ID");
                String ime = rs.getString("IME");
                String prezime = rs.getString("PREZIME");

                Date datum = rs.getDate("DATUM_RODJENJA");
                Instant instant = Instant.ofEpochMilli(datum.getTime());
                LocalDate datumRodjenja = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

                Long zupanijaId = rs.getLong("ZUPANIJA_ID");
                Zupanija zupanija = dohvatiZupaniju(veza, zupanijaId);

                Long bolestId = rs.getLong("BOLEST_ID");
                Bolest bolest = dohvatiBolest(veza, bolestId);

                Osoba osoba = new Osoba.Builder()
                        .hasIme(ime)
                        .hasPrezime(prezime)
                        .isBornAt(datumRodjenja)
                        .atZupanija(zupanija)
                        .withBolest(bolest)
                        .build();

                osoba.setId(id);

                osobe.add(osoba);
            }

            for (Osoba osoba : osobe) {
                List<Osoba> kontakti = dohvatiKontaktiraneOsobe(veza, osoba.getId(), osobe);
                if (osoba.getZarazenBolescu() instanceof Virus virus) {
                    kontakti.forEach(virus::prelazakZarazeNaOsobu);
                }
                osoba.setKontaktiraneOsobe(kontakti);
            }
        } catch (SQLException | IOException e) {
            Main.logger.error("Greška kod dohvata svih osoba");
            e.printStackTrace();
        }

        return osobe;
    }

    /**
     * Dohvaća osobu iz baze podataka
     *
     * @param veza      veza s bazom podataka
     * @param trazeniId podatak o id-u
     * @return podatak o osobi
     */
    public static Osoba dohvatiOsobu(Connection veza, Long trazeniId) {
        Osoba osoba = null;

        try {
            String sql = "SELECT * FROM OSOBA WHERE ID = " + trazeniId;
            Statement stmt = veza.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            if (rs.next()) {
                Long id = rs.getLong("ID");
                String ime = rs.getString("IME");
                String prezime = rs.getString("PREZIME");

                Date datum = rs.getDate("DATUM_RODJENJA");
                Instant instant = Instant.ofEpochMilli(datum.getTime());
                LocalDate datumRodjenja = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

                Long zupanijaId = rs.getLong("ZUPANIJA_ID");
                Zupanija zupanija = dohvatiZupaniju(veza, zupanijaId);

                Long bolestId = rs.getLong("BOLEST_ID");
                Bolest bolest = dohvatiBolest(veza, bolestId);

                List<Osoba> kontakti = dohvatiKontaktiraneOsobe(veza, id, dohvatiSveOsobe());

                osoba = new Osoba.Builder()
                        .hasIme(ime)
                        .hasPrezime(prezime)
                        .isBornAt(datumRodjenja)
                        .atZupanija(zupanija)
                        .withBolest(bolest)
                        .withKontaktiraneOsobe(kontakti)
                        .build();

                osoba.setId(id);
            }
        } catch (SQLException throwables) {
            Main.logger.error("Greška kod dohvata osobe");
            throwables.printStackTrace();
        }

        return osoba;
    }

    /**
     * Dohvaća kontaktirane osobe iz baze podataka
     *
     * @param veza  veza s bazom podataka
     * @param id    podatak o id-u
     * @param osobe podatak o listi svih osoba
     * @return lista kontakata
     */
    public static List<Osoba> dohvatiKontaktiraneOsobe(Connection veza, Long id, List<Osoba> osobe) {
        List<Osoba> kontaktiraneOsobe = new ArrayList<>();

        try {
            String sql = "SELECT * FROM KONTAKTIRANE_OSOBE WHERE OSOBA_ID = " + id;
            Statement stmt = veza.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Long kontaktiranaOsobaId = rs.getLong("KONTAKTIRANA_OSOBA_ID");
                Osoba osoba = osobe.stream()
                        .filter(o -> o.getId().equals(kontaktiranaOsobaId))
                        .findFirst()
                        .get();
                kontaktiraneOsobe.add(osoba);
            }

        } catch (SQLException throwables) {
            Main.logger.error("Greška kod dohvata kontaktiranih osoba");
            throwables.printStackTrace();
        }

        return kontaktiraneOsobe;
    }

    /**
     * Sprema novu osobu u bazu podataka
     *
     * @param novaOsoba podatak o novoj osobi
     */
    public static void spremiNovuOsobu(Osoba novaOsoba) {
        try (Connection veza = connectToDatabase()) {

            veza.setAutoCommit(false);

            String sql = "INSERT INTO OSOBA(IME, PREZIME, DATUM_RODJENJA, ZUPANIJA_ID, BOLEST_ID) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement upit = veza.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            upit.setString(1, novaOsoba.getIme());
            upit.setString(2, novaOsoba.getPrezime());
            upit.setDate(3, Date.valueOf(novaOsoba.getDatumRodjenja()));
            upit.setLong(4, novaOsoba.getZupanija().getId());
            upit.setLong(5, novaOsoba.getZarazenBolescu().getId());

            upit.executeUpdate();

            Long osobaId = dohvatiId(upit);

            for (Osoba kontakt : novaOsoba.getKontaktiraneOsobe()) {
                String sql2 = "INSERT INTO KONTAKTIRANE_OSOBE VALUES(?, ?)";
                PreparedStatement upitKontakti = veza.prepareStatement(sql2);

                upitKontakti.setLong(1, osobaId);
                upitKontakti.setLong(2, kontakt.getId());
                upitKontakti.executeUpdate();
            }

            veza.commit();
            veza.setAutoCommit(true);

        } catch (SQLException | IOException e) {
            Main.logger.error("Greška kod spremanja osobe");
            e.printStackTrace();
        }
    }


    /**
     * Dohvati id entiteta iz baze podataka
     *
     * @param upit upit iz baze podataka
     * @return podatak o id-u
     * @throws SQLException
     */
    private static Long dohvatiId(PreparedStatement upit) throws SQLException {
        ResultSet rs = upit.getGeneratedKeys();
        Long entitetId = 0L;
        if (rs.next()) {
            entitetId = rs.getLong(1);
        }
        return entitetId;
    }

    // zadatak 1
    public static void izbrisiSimptom(Long id) {
        try (Connection veza = connectToDatabase()) {
            String sql = """
                    DELETE FROM BOLEST_SIMPTOM WHERE SIMPTOM_ID = ?;
                    DELETE FROM SIMPTOM WHERE ID = ?;
                    """;
            PreparedStatement upit = veza.prepareStatement(sql);

            upit.setLong(1, id);
            upit.setLong(2, id);

            upit.executeUpdate();

        } catch (IOException |
                SQLException e) {
            Main.logger.error("Greška kod brisanja simptoma");
            e.printStackTrace();
        }
    }

    // zadatak 2
    public static List<Bolest> dohvatiBolestiSaSimptomom(Long id) {
        List<Bolest> bolesti = new ArrayList<>();

        try (Connection veza = connectToDatabase()) {
            String sql = """
                    SELECT DISTINCT BOLEST.*
                    FROM BOLEST INNER JOIN
                         BOLEST_SIMPTOM ON BOLEST.ID = BOLEST_SIMPTOM.BOLEST_ID
                    WHERE BOLEST_SIMPTOM.SIMPTOM_ID = ?;
                    """;

            PreparedStatement upit = veza.prepareStatement(sql);

            upit.setLong(1, id);

            ResultSet rs = upit.executeQuery();

            while (rs.next()) {
                Long idBolesti = rs.getLong("ID");
                Bolest bolest = dohvatiBolest(veza, idBolesti);
                bolesti.add(bolest);
            }
        } catch (IOException |
                SQLException e) {
            Main.logger.error("Greška kod ispisa bolesti sa odabranim simptomom");
            e.printStackTrace();
        }

        return bolesti;
    }

}