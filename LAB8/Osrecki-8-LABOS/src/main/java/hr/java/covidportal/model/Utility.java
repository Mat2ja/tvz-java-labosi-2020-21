package main.java.hr.java.covidportal.model;

import main.java.sample.Main;

public final class Utility {

    public static Integer ucitajBroj(String value){
        Integer num;
        try{
            num = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Main.logger.error("Unesen je podatak koji nije broj");
            return null;
        }
        return num;
    }

}
