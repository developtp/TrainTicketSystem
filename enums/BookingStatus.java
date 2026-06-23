package enums;

/**
 * Represents the lifecycle state of a Booking.
 *
 * Lifecycle:
 *   PENDING → CONFIRMED → CANCELLED
 *   PENDING → CANCELLED
 */
public enum BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED
}
