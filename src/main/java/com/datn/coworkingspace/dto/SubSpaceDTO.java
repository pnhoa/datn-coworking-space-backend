package com.datn.coworkingspace.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SubSpaceDTO {

    @NotNull(message = "Please input title")
    private String title;

    @NotNull(message = "Please input price")
    private BigDecimal price;

    private String imageUrl;

    private Integer numberOfPeople;

    private boolean status;

    private PackageDTO packageSubSpaceDTO;

    public SubSpaceDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public PackageDTO getPackageSubSpaceDTO() {
        return packageSubSpaceDTO;
    }

    public void setPackageSubSpaceDTO(PackageDTO packageSubSpaceDTO) {
        this.packageSubSpaceDTO = packageSubSpaceDTO;
    }
}
