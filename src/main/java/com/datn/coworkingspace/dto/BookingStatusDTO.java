package com.datn.coworkingspace.dto;

import com.datn.coworkingspace.enums.BookingStatus;
import com.datn.coworkingspace.validation.EnumNamePattern;

import javax.validation.constraints.NotNull;

public class BookingStatusDTO {

    @NotNull(message = "is required")
    private Long userId;

    @NotNull(message = "is required")
    @EnumNamePattern(regexp = "CANCELLED|COMPLETED|BOOKED|PENDING")
    private BookingStatus status;

    public BookingStatusDTO() {
    }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public BookingStatus getStatus() { return status; }

    public void setStatus(BookingStatus status) { this.status = status; }
}
