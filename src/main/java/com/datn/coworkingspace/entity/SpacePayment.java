package com.datn.coworkingspace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name= "space_payment")
@JsonIgnoreProperties({"space"})
public class SpacePayment extends BaseEntity {

    private String servicePackName;

    private BigDecimal price;

    private Date expiredTime;

    private boolean outOfDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "space_id")
    @JsonIgnoreProperties("spacePayments")
    private Space space;


    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price;  }

    public String getServicePackName() { return servicePackName; }

    public void setServicePackName(String servicePackName) { this.servicePackName = servicePackName; }

    public Date getExpiredTime() { return expiredTime; }

    public void setExpiredTime(Date expiredTime) { this.expiredTime = expiredTime; }

    public Space getSpace() { return space; }

    public void setSpace(Space space) { this.space = space; }

    public boolean isOutOfDate() { return outOfDate; }

    public void setOutOfDate(boolean outOfDate) { this.outOfDate = outOfDate; }

    public SpacePayment() {
        super();
    }

}
