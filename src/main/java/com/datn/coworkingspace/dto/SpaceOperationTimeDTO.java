package com.datn.coworkingspace.dto;


import java.util.Date;

public class SpaceOperationTimeDTO {

    private Integer day;

    private Date openTime;

    private Date closeTime;

    public SpaceOperationTimeDTO() {
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

}
