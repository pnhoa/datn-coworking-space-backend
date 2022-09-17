package com.datn.coworkingspace.entity;

import javax.persistence.*;

@Entity
@Table(name = "amenity")
public class Amenity extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL,
            mappedBy = "amenity",
            fetch = FetchType.LAZY,
            optional = false)
    private Space space;

    private boolean internet;

    private boolean parking;

    private boolean airConditioner;

    private boolean heater;

    private boolean cableTV;

    private boolean tv;

    private boolean toilet;

    private boolean motel;

    public Amenity() {
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
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
}
