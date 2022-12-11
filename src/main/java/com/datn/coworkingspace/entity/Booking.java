package com.datn.coworkingspace.entity;

import com.datn.coworkingspace.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name= "booking")
@JsonIgnoreProperties({"subSpace", "user"})
public class Booking extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "number_of_people")
    private Integer numberOfPeople;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "number_time_per_unit")
    private Integer numberTimePerUnit;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_space_id")
    @JsonIgnoreProperties("bookings")
    private SubSpace subSpace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("bookings")
    private User user;

    @Transient
    @JsonProperty(value = "userId")
    private Long userIds;

    @Transient
    @JsonProperty(value = "subSpaceId")
    private Long subSpaceIds;

    @Transient
    @JsonProperty(value = "subSpaceImage")
    private String subSpaceImage;

    @Transient
    @JsonProperty(value = "subSpaceTitle")
    private String subSpaceTitle;

    @Column(name = "space_id")
    private Long spaceId;

    @Column(name = "status")
    private BookingStatus status;

    @Column(name = "space_owner_id")
    private Long spaceOwnerId;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getCompanyName() { return companyName; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Integer getNumberOfPeople() { return numberOfPeople; }

    public void setNumberOfPeople(Integer numberOfPeople) { this.numberOfPeople = numberOfPeople; }

    public BigDecimal getTotalPrice() { return totalPrice; }

    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Integer getNumberTimePerUnit() { return numberTimePerUnit; }

    public void setNumberTimePerUnit(Integer numberTimePerUnit) { this.numberTimePerUnit = numberTimePerUnit; }

    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }

    public SubSpace getSubSpace() { return subSpace; }

    public void setSubSpace(SubSpace subSpace) { this.subSpace = subSpace; }

    public User getUser() { return user; }

    public void setUser(User user) {  this.user = user; }

    public Long getUserIds() { return userIds; }

    public void setUserIds(Long userIds) { this.userIds = userIds; }

    public Long getSubSpaceIds() { return subSpaceIds; }

    public void setSubSpaceIds(Long subSpaceIds) { this.subSpaceIds = subSpaceIds; }

    public Long getSpaceId() { return spaceId; }

    public void setSpaceId(Long spaceId) { this.spaceId = spaceId; }

    public BookingStatus getStatus() { return status; }

    public void setStatus(BookingStatus status) { this.status = status; }

    public String getSubSpaceImage() { return subSpaceImage; }

    public void setSubSpaceImage(String subSpaceImage) { this.subSpaceImage = subSpaceImage; }

    public String getSubSpaceTitle() { return subSpaceTitle; }

    public void setSubSpaceTitle(String subSpaceTitle) { this.subSpaceTitle = subSpaceTitle; }

    public Long getSpaceOwnerId() { return spaceOwnerId; }

    public void setSpaceOwnerId(Long spaceOwnerId) { this.spaceOwnerId = spaceOwnerId; }

    public Booking() {
    }
}
