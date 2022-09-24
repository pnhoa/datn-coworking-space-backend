package com.datn.coworkingspace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sub_space")
@JsonIgnoreProperties({"packageSubSpace"})
public class SubSpace extends BaseEntity {

    private String title;

    private BigDecimal price;

    private String imageUrl;

    private Integer numberOfPeople;

    private boolean status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id")
    @JsonIgnoreProperties("subSpaces")
    @JsonProperty(value = "package")
    private Package packageSubSpace;

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

    public Package getPackageSubSpace() { return packageSubSpace; }

    public void setPackageSubSpace(Package packageSubSpace) { this.packageSubSpace = packageSubSpace; }
}
