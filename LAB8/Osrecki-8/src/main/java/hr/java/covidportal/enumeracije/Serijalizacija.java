package main.java.hr.java.covidportal.enumeracije;

public enum Serijalizacija {

    ZARAZENE_ZUPANIJE("./dat/zarazene_zupanije.dat");

    private final String path;

    Serijalizacija(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
