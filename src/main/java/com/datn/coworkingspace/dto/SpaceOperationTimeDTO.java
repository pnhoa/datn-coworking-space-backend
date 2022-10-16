package com.datn.coworkingspace.dto;


public class SpaceOperationTimeDTO {

    private Long id;

    private String day;

    private String openTime;

    private String closeTime;

    public SpaceOperationTimeDTO() {
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

}
