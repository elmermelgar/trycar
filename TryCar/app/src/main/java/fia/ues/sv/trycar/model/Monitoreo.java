package fia.ues.sv.trycar.model;

import java.util.Date;

/**
 * Created by David-PC on 17/6/2016.
 */
public class Monitoreo {

    private Double rpm;
    private Double speed;
    private Double tempOil;
    private Double tempAmb;
    private Double tempRefri;
    private Double engine;
    private Double levelFuel;
    private Double perFuel;
    private String fecha;
    private String latitud;
    private String longitud;
    private String altitud;


    public Monitoreo(Double rpm, Double speed, Double tempOil, Double tempAmb, Double tempRefri, Double engine, Double levelFuel, Double perFuel, String fecha, String latitud, String longitud, String altitud) {
        this.rpm = rpm;
        this.speed = speed;
        this.tempOil = tempOil;
        this.tempAmb = tempAmb;
        this.tempRefri = tempRefri;
        this.engine = engine;
        this.levelFuel = levelFuel;
        this.perFuel = perFuel;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
        this.altitud = altitud;
    }

    public Monitoreo() {
    }

    public Double getRpm() {
        return rpm;
    }

    public void setRpm(Double rpm) {
        this.rpm = rpm;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getTempOil() {
        return tempOil;
    }

    public void setTempOil(Double tempOil) {
        this.tempOil = tempOil;
    }

    public Double getTempAmb() {
        return tempAmb;
    }

    public void setTempAmb(Double tempAmb) {
        this.tempAmb = tempAmb;
    }

    public Double getTempRefri() {
        return tempRefri;
    }

    public void setTempRefri(Double tempRefri) {
        this.tempRefri = tempRefri;
    }

    public Double getEngine() {
        return engine;
    }

    public void setEngine(Double engine) {
        this.engine = engine;
    }

    public Double getLevelFuel() {
        return levelFuel;
    }

    public void setLevelFuel(Double levelFuel) {
        this.levelFuel = levelFuel;
    }

    public Double getPerFuel() {
        return perFuel;
    }

    public void setPerFuel(Double perFuel) {
        this.perFuel = perFuel;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getAltitud() {
        return altitud;
    }

    public void setAltitud(String altitud) {
        this.altitud = altitud;
    }
}
