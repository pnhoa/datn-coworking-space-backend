package com.datn.coworkingspace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "space_operation_time")
@JsonIgnoreProperties({"space"})
public class SpaceOperationTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Integer day;

    @Column(nullable= false, precision=4, scale=0)
    private BigDecimal openTime;

    @Column(nullable= false, precision=4, scale=0)
    private BigDecimal closeTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "space_id")
    @JsonIgnoreProperties("spaceOperationTimes")
    private Space space;

    public SpaceOperationTime() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }
}
