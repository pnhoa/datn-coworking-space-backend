package com.datn.coworkingspace.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name= "service_pack", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class ServicePack extends BaseEntity {

    private String name;

    private BigDecimal price;

    private Integer period;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price;  }

    public Integer getPeriod() { return period; }

    public void setPeriod(Integer period) { this.period = period; }

    public ServicePack() {
        super();
    }

}
