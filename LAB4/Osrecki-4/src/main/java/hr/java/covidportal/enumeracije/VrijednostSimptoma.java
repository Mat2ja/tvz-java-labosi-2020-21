package main.java.hr.java.covidportal.enumeracije;

public enum VrijednostSimptoma {

    RIJETKO("RIJETKO"),
    SREDNJE("SREDNJE"),
    CESTO("ČESTO");

    private final String vrijednost;

    VrijednostSimptoma(String vrijednost) {
        this.vrijednost = vrijednost;
    }

    public String getVrijednost() {
        return vrijednost;
    }
}
