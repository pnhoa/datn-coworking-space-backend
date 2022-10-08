package com.datn.coworkingspace.dto;


import java.math.BigDecimal;

public class SpaceOverviewDTO {

    private Long id;

    private String name;

    private BigDecimal price;

    private String unit;

    private String largeImage;

    private BigDecimal ratingAverage;

    private String address;

    private String country;

    private String province;

    private String district;

    private Long categoryId;

    private Integer numberOfRoom;

    private boolean status;

    private boolean approved;

    private boolean notApproved;

    private Long userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getRatingAverage() { return ratingAverage; }

    public void setRatingAverage(BigDecimal ratingAverage) { this.ratingAverage = ratingAverage; }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUnit() { return unit; }

    public void setUnit(String unit) { this.unit = unit; }

    public Long getCategoryId() { return categoryId; }

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Integer getNumberOfRoom() { return numberOfRoom; }

    public void setNumberOfRoom(Integer numberOfRoom) { this.numberOfRoom = numberOfRoom; }

    public boolean isStatus() { return status; }

    public void setStatus(boolean status) { this.status = status; }

    public boolean isApproved() { return approved; }

    public void setApproved(boolean approved) { this.approved = approved; }

    public boolean isNotApproved() { return notApproved; }

    public void setNotApproved(boolean notApproved) { this.notApproved = notApproved; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public SpaceOverviewDTO() {
    }
}
