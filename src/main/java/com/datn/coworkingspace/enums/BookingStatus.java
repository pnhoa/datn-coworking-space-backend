package com.datn.coworkingspace.enums;

public enum BookingStatus {
    COMPLETED(1),
    CANCELLED(2),
    PENDING(3),
    BOOKED(4);
    private int status;

    BookingStatus(int status) {
        this.status = status;
    }
}
