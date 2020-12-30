package main.java.hr.java.covidportal.model;

import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;

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
import java.util.stream.Collectors;

public class BazaPodataka {

    private static final String DATABASE_FILE = "./dat/database.properties";

    private static Connection connectToDatabase() throws IOException, SQLException {
        Properties svojstva = new Properties();
        svojstva.load(new FileReader(DATABASE_FILE));

        String urlBazePodataka = svojstva.getProperty("bazaPodatakaUrl");
        String korisnickoIme = svojstva.getProperty("korisnickoIme");
        String lozinka = svojstva.getProperty("lozinka");

        Connection veza = DriverManager.getConnection(urlBazePodataka, korisnickoIme, lozinka);

        return veza;
    }

    private static void disconnectFromDatabase(Connection veza) throws SQLException {
        veza.close();
    }

    public static List<Zupanija> dohvatiSveZupanije() throws IOException, SQLException {
        List<Zupanija> zupanije = new ArrayList<>();

        Connection veza = connectToDatabase();
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

        disconnectFromDatabase(veza);

        return zupanije;
    }

    public static Zupanija dohvatiZupaniju(Long trazeniId, Connection veza) throws SQLException {
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ZUPANIJA WHERE ID = " + trazeniId);

        Zupanija zupanija = null;
        if (rs.next()) {
            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");
            Integer brojStanovnika = rs.getInt("BROJ_STANOVNIKA");
            Integer brojZarazenih = rs.getInt("BROJ_ZARAZENIH_STANOVNIKA");

            zupanija = new Zupanija(naziv, brojStanovnika, brojZarazenih);
            zupanija.setId(id);
        }

        return zupanija;
    }

    public static void spremiNovuZupaniju(Zupanija novaZupanija) throws IOException, SQLException {
        Connection veza = connectToDatabase();

        PreparedStatement upit = veza.prepareStatement(
                "INSERT INTO ZUPANIJA(NAZIV, BROJ_STANOVNIKA, BROJ_ZARAZENIH_STANOVNIKA) VALUES(?, ?, ?)");

        upit.setString(1, novaZupanija.getNaziv());
        upit.setInt(2, novaZupanija.getBrojStanovnika());
        upit.setInt(3, novaZupanija.getBrojZarazenih());

        upit.executeUpdate();

        disconnectFromDatabase(veza);

    }


    public static List<Simptom> dohvatiSveSimptome() throws IOException, SQLException {
        List<Simptom> simptomi = new ArrayList<>();

        Connection veza = connectToDatabase();
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

        disconnectFromDatabase(veza);

        return simptomi;
    }

    public static Simptom dohvatiSimptom(Connection veza, Long trazeniId) throws SQLException {
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SIMPTOM WHERE ID = " + trazeniId);

        Simptom simptom = null;

        if (rs.next()) {
            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");
            VrijednostSimptoma vrijednost = VrijednostSimptoma.valueOf(rs.getString("VRIJEDNOST"));

            simptom = new Simptom(naziv, vrijednost);
            simptom.setId(id);
        }

        return simptom;
    }

    public static void spremiNoviSimptom(Simptom noviSimptom) throws IOException, SQLException {
        Connection veza = connectToDatabase();

        PreparedStatement upit = veza.prepareStatement(
                "INSERT INTO SIMPTOM(NAZIV, VRIJEDNOST) VALUES(?, ?)");

        upit.setString(1, noviSimptom.getNaziv());
        upit.setString(2, noviSimptom.getVrijednost().getVrijednost());

        upit.executeUpdate();

        disconnectFromDatabase(veza);
    }


    public static List<Bolest> dohvatiSveBolesti() throws IOException, SQLException {
        List<Bolest> bolesti = new ArrayList<>();

        Connection veza = connectToDatabase();
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

        disconnectFromDatabase(veza);

        return bolesti;
    }


    public static Bolest dohvatiBolest(Connection veza, Long trazeniId) throws SQLException {
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOLEST WHERE ID = " + trazeniId);

        Bolest bolest = null;

        if (rs.next()) {
            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");
            Boolean jeVirus = rs.getBoolean("VIRUS");
            List<Simptom> listaSimptoma = dohvatiSimptomeBolesti(veza, id);

            bolest = new Bolest(naziv, listaSimptoma);
            bolest.setJeVirus(jeVirus);

            bolest.setId(id);
        }

        if (bolest.getJeVirus()) {
            Virus virus = new Virus(bolest.getNaziv(), bolest.getSimptomi());
            virus.setId(bolest.getId());
            return virus;
        } else {
            return bolest;
        }
    }

    public static void spremiNovuBolest(Bolest novaBolest) throws IOException, SQLException {
        Connection veza = connectToDatabase();

        veza.setAutoCommit(false);

        PreparedStatement upit = veza.prepareStatement(
                "INSERT INTO BOLEST(NAZIV, VIRUS) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);

        upit.setString(1, novaBolest.getNaziv());
        upit.setBoolean(2, novaBolest.getJeVirus());
        upit.executeUpdate();

        ResultSet rs = upit.getGeneratedKeys();
        Long bolestId = 0L;
        if (rs.next()) {
            bolestId = rs.getLong(1);
        }


        for (Simptom simptom : novaBolest.getSimptomi()) {
            PreparedStatement upitSimptom = veza.prepareStatement(
                    "INSERT INTO BOLEST_SIMPTOM(BOLEST_ID, SIMPTOM_ID) VALUES(?, ?)");

            upitSimptom.setLong(1, bolestId);
            upitSimptom.setLong(2, simptom.getId());
            upitSimptom.executeUpdate();
        }

        veza.commit();
        veza.setAutoCommit(true);

        disconnectFromDatabase(veza);
    }


    private static List<Simptom> dohvatiSimptomeBolesti(Connection veza, Long id) throws SQLException {
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM BOLEST_SIMPTOM WHERE BOLEST_ID = " + id);

        List<Simptom> listaSimptoma = new ArrayList<>();

        while (rs.next()) {
            Long simptomId = rs.getLong("SIMPTOM_ID");
            Simptom simptom = dohvatiSimptom(veza, simptomId);
            listaSimptoma.add(simptom);
        }

        return listaSimptoma;
    }

    public static List<Osoba> dohvatiSveOsobe() throws IOException, SQLException {
        List<Osoba> osobe = new ArrayList<>();

        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM OSOBA");


        while (rs.next()) {
            Long id = rs.getLong("ID");
            Osoba osoba = dohvatiOsobu(veza, id);

            osobe.add(osoba);
        }

       for (Osoba osoba : osobe) {
           List<Osoba> kontakti =  dohvatiKontaktiraneOsobe(veza, osoba.getId(), osobe);
           if (osoba.getZarazenBolescu() instanceof Virus virus) {
               kontakti.forEach(os -> os.setZarazenBolescu(virus));
           }

           osoba.setKontaktiraneOsobe(kontakti);
       }

        disconnectFromDatabase(veza);

        return osobe;
    }

    // dohvaca osobu ali bez kontakata
    public static Osoba dohvatiOsobu(Connection veza, Long trazeniId) throws SQLException {
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM OSOBA WHERE ID = " + trazeniId);

        Osoba osoba = null;

        if (rs.next()) {
            Long id = rs.getLong("ID");
            String ime = rs.getString("IME");
            String prezime = rs.getString("PREZIME");

            Date datum = rs.getDate("DATUM_RODJENJA");
            Instant instant = Instant.ofEpochMilli(datum.getTime());
            LocalDate datumRodjenja = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

            Long zupanijaId = rs.getLong("ZUPANIJA_ID");
            Zupanija zupanija = dohvatiZupaniju(zupanijaId, veza);

            Long bolestId = rs.getLong("BOLEST_ID");
            Bolest bolest = dohvatiBolest(veza, bolestId);

            osoba = new Osoba.Builder()
                    .hasIme(ime)
                    .hasPrezime(prezime)
                    .isBornAt(datumRodjenja)
                    .atZupanija(zupanija)
                    .withBolest(bolest)
                    .build();

            osoba.setId(id);
        }

        return osoba;
    }

    public static List<Osoba> dohvatiKontaktiraneOsobe(Connection veza, Long id, List<Osoba> osobe) throws SQLException {
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM KONTAKTIRANE_OSOBE WHERE OSOBA_ID = " + id);

        List<Osoba> kontaktiraneOsobe = new ArrayList<>();

        while (rs.next()) {
            Long kontaktiranaOsobaId = rs.getLong("KONTAKTIRANA_OSOBA_ID");
            Osoba osoba = osobe.stream()
                    .filter(o -> o.getId().equals(kontaktiranaOsobaId))
                    .collect(Collectors.toList())
                    .get(0);
            kontaktiraneOsobe.add(osoba);
        }

        return kontaktiraneOsobe;
    }

    public static void spremiNovuOsobu(Osoba novaOsoba) throws IOException, SQLException {
        Connection veza = connectToDatabase();

        veza.setAutoCommit(false);

        PreparedStatement upit = veza.prepareStatement(
                "INSERT INTO OSOBA(IME, PREZIME, DATUM_RODJENJA, ZUPANIJA_ID, BOLEST_ID) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

        upit.setString(1, novaOsoba.getIme());
        upit.setString(2, novaOsoba.getPrezime());
        upit.setDate(3, Date.valueOf(novaOsoba.getDatumRodjenja()));
        upit.setLong(4, novaOsoba.getZupanija().getId());
        upit.setLong(5, novaOsoba.getZarazenBolescu().getId());

        upit.executeUpdate();

        ResultSet rs = upit.getGeneratedKeys();
        Long osobaId = 0L;
        if (rs.next()) {
            osobaId = rs.getLong(1);
        }

        for (Osoba kontakt : novaOsoba.getKontaktiraneOsobe()) {
            PreparedStatement upitKontakti = veza.prepareStatement("INSERT INTO KONTAKTIRANE_OSOBE VALUES(?, ?)");

            upitKontakti.setLong(1, osobaId);
            upitKontakti.setLong(2, kontakt.getId());
            upitKontakti.executeUpdate();
        }

        veza.commit();
        veza.setAutoCommit(true);

        disconnectFromDatabase(veza);
    }
}