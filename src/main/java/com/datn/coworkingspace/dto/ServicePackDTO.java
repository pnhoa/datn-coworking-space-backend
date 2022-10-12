package com.datn.coworkingspace.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ServicePackDTO extends AbstractDTO {

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "price is required")
    private BigDecimal price;

    @NotNull(message = "period is required")
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

    public ServicePackDTO() {
        super();
    }
}
