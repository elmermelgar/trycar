package fia.ues.sv.trycar.model;

import java.util.Date;

/**
 * Created by David-PC on 17/6/2016.
 */
public class Monitoreo {


    private Double rpm;
    private Double speed;
    private Double tempRefri;
    private Double engine;
    private Double levelFuel;
    private String fecha;
    private String latitud;
    private String longitud;
    private String altitud;
    private Double posAcel;
    private Double tempAir;
    private Double star;

    public Monitoreo(Double rpm, Double speed, Double posAcel, Double tempAir, Double tempRefri, Double engine, Double levelFuel, Double star, String fecha, String latitud, String longitud, String altitud) {
        this.rpm = rpm;
        this.speed = speed;
        this.posAcel = posAcel;
        this.tempAir = tempAir;
        this.tempRefri = tempRefri;
        this.engine = engine;
        this.levelFuel = levelFuel;
        this.star = star;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
        this.altitud = altitud;
    }

    public Monitoreo() {
    }

    public Double getEngine() {
        return engine;
    }

    public void setEngine(Double engine) {
        this.engine = engine;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getPosAcel() {
        return posAcel;
    }

    public void setPosAcel(Double posAcel) {
        this.posAcel = posAcel;
    }

    public Double getTempAir() {
        return tempAir;
    }

    public void setTempAir(Double tempAir) {
        this.tempAir = tempAir;
    }

    public Double getTempRefri() {
        return tempRefri;
    }

    public void setTempRefri(Double tempRefri) {
        this.tempRefri = tempRefri;
    }

    public Double getRpm() {
        return rpm;
    }

    public void setRpm(Double rpm) {
        this.rpm = rpm;
    }

    public Double getLevelFuel() {
        return levelFuel;
    }

    public void setLevelFuel(Double levelFuel) {
        this.levelFuel = levelFuel;
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
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

    public String getAltitud() {
        return altitud;
    }

    public void setAltitud(String altitud) {
        this.altitud = altitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
