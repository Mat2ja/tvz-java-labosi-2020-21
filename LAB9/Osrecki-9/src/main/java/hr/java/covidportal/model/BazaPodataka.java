package main.java.hr.java.covidportal.model;

import main.java.hr.java.covidportal.enumeracije.VrijednostSimptoma;
import main.java.sample.Main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

    public static Zupanija dohvatiZupaniju(Long trazeniId, Connection veza) throws IOException, SQLException {
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ZUPANIJA WHERE ID = " + trazeniId);

        Zupanija zupanija = null;
        while (rs.next()) {
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

    public static Simptom dohvatiSimptom(Long trazeniId, Connection veza) throws SQLException {
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SIMPTOM WHERE ID = " + trazeniId);

        Simptom simptom = null;

        while (rs.next()) {
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

            List<Simptom> listaSimptoma = new ArrayList<>();

            Statement stmtBS = veza.createStatement();
            ResultSet rsBS = stmtBS.executeQuery("SELECT * FROM BOLEST_SIMPTOM");

            while (rsBS.next()) {
                Long idBolesti = rsBS.getLong("BOLEST_ID");
                if (idBolesti.equals(id)) {
                    Long idSimptoma = rsBS.getLong("SIMPTOM_ID");
                    Simptom simptom = dohvatiSimptom(idSimptoma, veza);
                    listaSimptoma.add(simptom);
                }
            }

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
}