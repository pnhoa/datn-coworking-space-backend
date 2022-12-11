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
@JsonIgnoreProperties({"category", "user"})
public class Space extends BaseEntity {

    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "unit")
    private String unit;

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

    private boolean notApproved;

    private boolean paid;

    private boolean expired;

    private double xCoordinate;

    private double yCoordinate;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "rating_average")
    private BigDecimal ratingAverage;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "space_description_id")
    private SpaceDescription spaceDescription;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "space_contact_id")
    private SpaceContact spaceContact;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "space_amenity_id")
    private SpaceAmenity spaceAmenity;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "space_address_id")
    private SpaceAddress spaceAddress;

    @OneToMany(mappedBy = "space", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("space")
    private Set<SpaceOperationTime> spaceOperationTimes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("spaces")
    private Category category;

    @OneToMany(mappedBy = "space", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("space")
    private Set<ServiceSpace> serviceSpaces = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
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

    @OneToMany(mappedBy = "space", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("space")
    private Set<SpacePayment> spacePayments;

    @Transient
    @JsonProperty(value = "categoryId")
    private Long categoryIds;

    @Transient
    @JsonProperty(value = "userId")
    private Long userIds;

    @Transient
    @JsonProperty(value = "spaceDescriptionId")
    private Long spaceDescriptionIds;

    @Transient
    @JsonProperty(value = "spaceContactId")
    private Long spaceContactIds;

    @Transient
    @JsonProperty(value = "spaceAddressId")
    private Long spaceAddressIds;

    @Transient
    @JsonProperty(value = "spaceAmenityId")
    private Long spaceAmenityIds;

    @Transient
    @JsonProperty(value = "spaceOperationTimeIds")
    private List<Long> spaceOperationTimeIds;

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

    public Long getSpaceDescriptionIds() { return spaceDescriptionIds; }

    public void setSpaceDescriptionIds(Long spaceDescriptionIds) { this.spaceDescriptionIds = spaceDescriptionIds; }

    public Long getSpaceContactIds() { return spaceContactIds; }

    public void setSpaceContactIds(Long spaceContactIds) { this.spaceContactIds = spaceContactIds; }

    public Long getSpaceAddressIds() { return spaceAddressIds; }

    public void setSpaceAddressIds(Long spaceAddressIds) { this.spaceAddressIds = spaceAddressIds; }

    public Long getSpaceAmenityIds() { return spaceAmenityIds; }

    public void setSpaceAmenityIds(Long spaceAmenityIds) { this.spaceAmenityIds = spaceAmenityIds; }

    public List<Long> getSpaceOperationTimeIds() { return spaceOperationTimeIds; }

    public void setSpaceOperationTimeIds(List<Long> spaceOperationTimeIds) { this.spaceOperationTimeIds = spaceOperationTimeIds; }

    public Long getUserIds() { return userIds; }

    public void setUserIds(Long userIds) { this.userIds = userIds; }

    public boolean isNotApproved() { return notApproved; }

    public void setNotApproved(boolean notApproved) { this.notApproved = notApproved; }

    public String getUnit() { return unit; }

    public void setUnit(String unit) { this.unit = unit; }

    public boolean isPaid() { return paid; }

    public void setPaid(boolean paid) { this.paid = paid; }

    public boolean isExpired() { return expired; }

    public void setExpired(boolean expired) { this.expired = expired; }

    public Set<SpacePayment> getSpacePayments() { return spacePayments; }

    public void setSpacePayments(Set<SpacePayment> spacePayments) { this.spacePayments = spacePayments; }
}
