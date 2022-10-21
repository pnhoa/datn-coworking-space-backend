package com.datn.coworkingspace.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class BookingDTO extends AbstractDTO {

    @NotNull(message = "Please input name")
    private String name;

    @NotNull(message = "Please input email")
    private String email;

    private String companyName;

    @NotNull(message = "Please input phone number")
    private String phoneNumber;

    @NotNull(message = "Please input number of people")
    private String numberOfPeople;

    @NotNull(message = "Please input total price")
    private String totalPrice;

    @NotNull(message = "Please input start date")
    private Date startDate;

    @NotNull(message = "Please input number time per unit")
    private Integer numberTimePerUnit;

    private String note;

    @NotNull(message = "Please input user id")
    private Long userId;

    @NotNull(message = "Please input sub space id")
    private Long subSpaceId;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getCompanyName() { return companyName; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getNumberOfPeople() { return numberOfPeople; }

    public void setNumberOfPeople(String numberOfPeople) { this.numberOfPeople = numberOfPeople; }

    public String getTotalPrice() { return totalPrice; }

    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Integer getNumberTimePerUnit() { return numberTimePerUnit; }

    public void setNumberTimePerUnit(Integer numberTimePerUnit) { this.numberTimePerUnit = numberTimePerUnit; }

    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public Long getSubSpaceId() { return subSpaceId; }

    public void setSubSpaceId(Long subSpaceId) { this.subSpaceId = subSpaceId; }

    public BookingDTO() {
    }
}
