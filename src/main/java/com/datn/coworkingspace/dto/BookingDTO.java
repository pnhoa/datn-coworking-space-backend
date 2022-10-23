package com.datn.coworkingspace.dto;

import com.datn.coworkingspace.enums.BookingStatus;
import com.datn.coworkingspace.validation.EnumNamePattern;
import com.datn.coworkingspace.validationgroups.OnUpdate;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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
    private Integer numberOfPeople;

    @NotNull(message = "Please input total price")
    private BigDecimal totalPrice;

    @NotNull(message = "Please input start date")
    private Date startDate;

    @NotNull(message = "Please input number time per unit")
    private Integer numberTimePerUnit;

    private String note;

    @NotNull(message = "Please input user id")
    private Long userId;

    @NotNull(message = "Please input sub space id")
    private Long subSpaceId;

    @NotNull(message = "Please input space id")
    private Long spaceId;

    @NotNull(message = "is required", groups = {OnUpdate.class})
    @EnumNamePattern(regexp = "CANCELLED|COMPLETED|BOOKED|PENDING")
    private BookingStatus status;

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

    public Integer getNumberTimePerUnit() { return numberTimePerUnit; }

    public void setNumberTimePerUnit(Integer numberTimePerUnit) { this.numberTimePerUnit = numberTimePerUnit; }

    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public Long getSubSpaceId() { return subSpaceId; }

    public void setSubSpaceId(Long subSpaceId) { this.subSpaceId = subSpaceId; }

    public Long getSpaceId() { return spaceId; }

    public void setSpaceId(Long spaceId) { this.spaceId = spaceId; }

    public BookingStatus getStatus() { return status; }

    public void setStatus(BookingStatus status) { this.status = status; }

    public BookingDTO() {
    }
}
