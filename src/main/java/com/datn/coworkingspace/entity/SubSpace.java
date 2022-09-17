package com.datn.coworkingspace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sub_space")
public class SubSpace extends BaseEntity {

    private String title;

    private BigDecimal price;

    private String imageUrl;

    private Integer numberOfPeople;

    private boolean status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_space_id")
    @JsonIgnoreProperties("subSpaces")
    private ServiceSpace serviceSpace;

    public SubSpace() {
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

    public ServiceSpace getServiceSpace() {
        return serviceSpace;
    }

    public void setServiceSpace(ServiceSpace serviceSpace) {
        this.serviceSpace = serviceSpace;
    }
}
