package ru.practicum.shareit.booking.model;

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CURRENT,
    PAST,
    FUTURE,
    ALL,
    UNSUPPORTED_STATUS;

    public static BookingStatus checkEnum(String status) {
        for (BookingStatus bookingStatus : BookingStatus.values()) {
            if (bookingStatus.name().equals(status)) {
                return bookingStatus;
            }
        }
        return UNSUPPORTED_STATUS;
    }
}
