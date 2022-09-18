package com.datn.coworkingspace.dto;


public class SpaceAmenityDTO {

    private boolean internet;

    private boolean parking;

    private boolean airConditioner;

    private boolean heater;

    private boolean cableTV;

    private boolean tv;

    private boolean toilet;

    private boolean motel;

    private boolean catering;

    public SpaceAmenityDTO() {
    }

    public boolean isInternet() {
        return internet;
    }

    public void setInternet(boolean internet) {
        this.internet = internet;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public boolean isAirConditioner() {
        return airConditioner;
    }

    public void setAirConditioner(boolean airConditioner) {
        this.airConditioner = airConditioner;
    }

    public boolean isHeater() {
        return heater;
    }

    public void setHeater(boolean heater) {
        this.heater = heater;
    }

    public boolean isCableTV() {
        return cableTV;
    }

    public void setCableTV(boolean cableTV) {
        this.cableTV = cableTV;
    }

    public boolean isTv() {
        return tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }

    public boolean isToilet() {
        return toilet;
    }

    public void setToilet(boolean toilet) {
        this.toilet = toilet;
    }

    public boolean isMotel() {
        return motel;
    }

    public void setMotel(boolean motel) {
        this.motel = motel;
    }

    public boolean isCatering() {
        return catering;
    }

    public void setCatering(boolean catering) {
        this.catering = catering;
    }
}
