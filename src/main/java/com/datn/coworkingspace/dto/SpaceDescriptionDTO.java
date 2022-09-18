package com.datn.coworkingspace.dto;

import java.util.Date;

public class SpaceDescriptionDTO {

    private Date openingDate;

    private String shortDescription;

    private String description;


    public SpaceDescriptionDTO() {
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
