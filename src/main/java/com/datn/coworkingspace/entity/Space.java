package com.datn.coworkingspace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "space")
@JsonIgnoreProperties({"category"})
public class Space extends BaseEntity {

    private String name;

    @Column(name = "price")
    private BigDecimal price;

    private String largeImage;

    @Column(name = "min_price")
    private BigDecimal minPrice;

    @Column(name = "max_price")
    private BigDecimal maxPrice;

    @Column(name = "number_of_room")
    private Integer numberOfRoom;

    private BigDecimal acreage;

    private BigDecimal electricPrice;

    private BigDecimal waterPrice;

    private boolean status;

    private boolean approved;

    private double xCoordinate;

    private double yCoordinate;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "rating_average")
    private BigDecimal ratingAverage;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "space_description_id")
    private SpaceDescription spaceDescription;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "space_contact_id")
    private SpaceContact spaceContact;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "space_amenity_id")
    private SpaceAmenity spaceAmenity;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "space_address_id")
    private SpaceAddress spaceAddress;

    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("space")
    private Set<SpaceOperationTime> spaceOperationTimes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("spaces")
    private Category category;

    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("space")
    private Set<ServiceSpace> serviceSpaces = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "space",
            orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "space",
            orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Transient
    @JsonProperty(value = "categoryId")
    private Long categoryIds;

    public Space() {
    }

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

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getNumberOfRoom() {
        return numberOfRoom;
    }

    public void setNumberOfRoom(Integer numberOfRoom) {
        this.numberOfRoom = numberOfRoom;
    }

    public BigDecimal getAcreage() {
        return acreage;
    }

    public void setAcreage(BigDecimal acreage) {
        this.acreage = acreage;
    }

    public BigDecimal getElectricPrice() {
        return electricPrice;
    }

    public void setElectricPrice(BigDecimal electricPrice) {
        this.electricPrice = electricPrice;
    }

    public BigDecimal getWaterPrice() {
        return waterPrice;
    }

    public void setWaterPrice(BigDecimal waterPrice) {
        this.waterPrice = waterPrice;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public BigDecimal getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(BigDecimal ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<ServiceSpace> getServiceSpaces() { return serviceSpaces; }

    public void setServiceSpaces(Set<ServiceSpace> serviceSpaces) { this.serviceSpaces = serviceSpaces; }

    public Long getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Long categoryIds) {
        this.categoryIds = categoryIds;
    }

    public SpaceDescription getSpaceDescription() {
        return spaceDescription;
    }

    public void setSpaceDescription(SpaceDescription spaceDescription) {
        this.spaceDescription = spaceDescription;
    }

    public SpaceContact getSpaceContact() {
        return spaceContact;
    }

    public void setSpaceContact(SpaceContact spaceContact) {
        this.spaceContact = spaceContact;
    }

    public SpaceAmenity getSpaceAmenity() {
        return spaceAmenity;
    }

    public void setSpaceAmenity(SpaceAmenity spaceAmenity) {
        this.spaceAmenity = spaceAmenity;
    }

    public SpaceAddress getSpaceAddress() {
        return spaceAddress;
    }

    public void setSpaceAddress(SpaceAddress spaceAddress) {
        this.spaceAddress = spaceAddress;
    }

    public Set<SpaceOperationTime> getSpaceOperationTimes() {return spaceOperationTimes; }

    public void setSpaceOperationTimes(Set<SpaceOperationTime> spaceOperationTimes) {this.spaceOperationTimes = spaceOperationTimes; }
}
