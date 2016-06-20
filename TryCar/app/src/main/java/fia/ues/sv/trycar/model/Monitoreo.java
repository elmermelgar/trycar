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
    private Double latitud;
    private Double longitud;
    private Double altitud;


    public Monitoreo(Double rpm, Double speed, Double tempOil, Double tempAmb, Double tempRefri, Double engine, Double levelFuel, Double perFuel, String fecha, Double latitud, Double longitud, Double altitud) {
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

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getAltitud() {
        return altitud;
    }

    public void setAltitud(Double altitud) {
        this.altitud = altitud;
    }
}
