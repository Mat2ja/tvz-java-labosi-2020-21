package main.java.hr.java.covidportal.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Predstavlja entitet definiram nazivom
 */
public abstract class ImenovaniEntitet implements Serializable {
    private Long id;
    private String naziv;

    /**
     * Inicijalizira podatak o nazivu entiteta
     *
     * @param id    podatak od id entiteta
     * @param naziv podatak o nazivu entiteta
     */
    public ImenovaniEntitet(Long id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImenovaniEntitet that = (ImenovaniEntitet) o;
        return Objects.equals(naziv, that.naziv) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naziv, id);
    }
}
