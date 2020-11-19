package main.java.hr.java.covidportal.genericsi;

import main.java.hr.java.covidportal.model.Simptom;

import java.util.ArrayList;
import java.util.List;

public class SamoSimptomi<S extends Simptom> {

    List<S> simptomi;

    public SamoSimptomi() {
        simptomi = new ArrayList<>();
    }

    public void add(S simptom) {
        this.simptomi.add(simptom);
    }

    public S get(Integer i) {
        return this.simptomi.get(i);
    }

    public Integer getIndex(S simptom) {
        return this.simptomi.indexOf(simptom);
    }

    @Override
    public String toString() {
        return "SamoSimptomi{" +
                "simptomi=" + simptomi +
                '}';
    }

    public List<S> getSimptomi() {
        return simptomi;
    }

    public void setSimptomi(List<S> simptomi) {
        this.simptomi = simptomi;
    }
}
