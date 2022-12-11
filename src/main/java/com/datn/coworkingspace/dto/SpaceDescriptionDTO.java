package com.datn.coworkingspace.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class SpaceDescriptionDTO {

    @NotNull(message = "Please input opening date")
    private Date openingDate;

    @NotNull(message = "Please input short description")
    @Size(min = 50)
    private String shortDescription;

    @NotNull(message = "Please input description")
    @Size(min = 300)
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
