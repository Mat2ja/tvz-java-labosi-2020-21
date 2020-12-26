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

    public static List<Zupanija> dohvatiSveZupanije() throws SQLException, IOException {
        List<Zupanija> zupanije = new ArrayList<>();

        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ZUPANIJA");

        while (rs.next()) {
            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");
            Integer brojStanovnika = rs.getInt("BROJ_STANOVNIKA");
            Integer brojZarazenih = rs.getInt("BROJ_ZARAZENIH_STANOVNIKA");
            Zupanija zupanija = new Zupanija(id, naziv, brojStanovnika, brojZarazenih);
            zupanije.add(zupanija);
        }

        disconnectFromDatabase(veza);

        return zupanije;
    }

    public static List<Simptom> dohvatiSveSimptome() throws SQLException, IOException {
        List<Simptom> simptomi = new ArrayList<>();

        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SIMPTOM");

        while (rs.next()) {
            Long id = rs.getLong("ID");
            String naziv = rs.getString("NAZIV");
            VrijednostSimptoma vrijednost = VrijednostSimptoma.valueOf(rs.getString("VRIJEDNOST"));
            Simptom simptom = new Simptom(id, naziv, vrijednost);
            simptomi.add(simptom);
        }

        disconnectFromDatabase(veza);

        return simptomi;
    }

    public static Simptom dohvatiSimptom(Long trazeniId) throws IOException, SQLException {
        Connection veza = connectToDatabase();
        Statement stmt = veza.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SIMPTOM WHERE ID = " + trazeniId);

        Long id = rs.getLong("ID");
        String naziv = rs.getString("NAZIV");
        VrijednostSimptoma vrijednost = VrijednostSimptoma.valueOf(rs.getString("VRIJEDNOST"));

        Simptom simptom = new Simptom(id, naziv, vrijednost);
        return simptom;

    }
}