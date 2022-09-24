package com.datn.coworkingspace.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class SpaceOperationTimeDTO {

    private Long id;

    @Min(0)
    @Max(6)
    private Integer day;

    private BigDecimal openTime;

    private BigDecimal closeTime;

    public SpaceOperationTimeDTO() {
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public BigDecimal getOpenTime() {
        return openTime;
    }

    public void setOpenTime(BigDecimal openTime) {
        this.openTime = openTime;
    }

    public BigDecimal getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(BigDecimal closeTime) {
        this.closeTime = closeTime;
    }

}
