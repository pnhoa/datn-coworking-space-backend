package com.datn.coworkingspace.dto;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class MatchSubSpaceDTO {

    @NotNull(message = "Please input number of people")
    private Integer numberOfPeople;

    @NotNull(message = "Please input start date")
    private Date startDate;

    @NotNull(message = "Please input end date")
    private Date endDate;

    @NotNull(message = "Please input space id")
    private Long spaceId;

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getSpaceId() { return spaceId; }

    public void setSpaceId(Long spaceId) { this.spaceId = spaceId; }

    public MatchSubSpaceDTO() {
    }
}
