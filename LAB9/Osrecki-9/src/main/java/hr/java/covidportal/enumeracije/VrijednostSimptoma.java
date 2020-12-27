package main.java.hr.java.covidportal.enumeracije;

public enum VrijednostSimptoma {

    Produktivni("Produktivni"),
    Intenzivno("Intenzivno"),
    Visoka("Visoka"),
    Jaka("Jaka");

    private final String vrijednost;

    VrijednostSimptoma(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }
}
