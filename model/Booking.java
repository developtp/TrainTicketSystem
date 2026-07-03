package model;

import enums.BookingStatus;
import interfaces.Displayable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Central entity of the Train Ticket System.
 *
 * Booking lifecycle:
 *   PENDING → CONFIRMED (after successful payment)
 *   PENDING → CANCELLED
 *   CONFIRMED → CANCELLED (seat is released back to the train)
 */
public class Booking implements Displayable, Comparable<Booking> {

    // ─── Static counters ─────────────────────────────────────────────────────────
    private static int nextBookingId = 1;
    private static int bookingCount  = 0;

    // ─── Instance fields ─────────────────────────────────────────────────────────
    private int           bookingId;
    private User          user;
    private Train         train;
    private String        seatNumber;
    private double        price;          // auto-calculated at creation via PriceCalculator
    private LocalDate     travelDate;
    private BookingStatus status;

    public Booking(User user, Train train, String seatNumber) {
        this.bookingId   = nextBookingId++;
        this.user        = user;
        this.train       = train;
        this.seatNumber  = (seatNumber  != null) ? seatNumber.trim().toUpperCase() : "N/A";
        this.travelDate  = LocalDate.now();
        this.status      = BookingStatus.PENDING;
        // Price is business logic — caller never sets it directly
        this.price       = service.PriceCalculator.calculatePrice(
                               (train != null) ? train.getTrainType() : null);
        bookingCount++;
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────
    public int           getBookingId()  { return bookingId; }
    public User          getUser()       { return user; }
    public Train         getTrain()      { return train; }
    public String        getSeatNumber() { return seatNumber; }
    public double        getPrice()      { return price; }
    public LocalDate     getTravelDate() { return travelDate; }
    public BookingStatus getStatus()     { return status; }

    // ─── Status helpers ───────────────────────────────────────────────────────────
    public boolean isPending()   { return status == BookingStatus.PENDING; }
    public boolean isConfirmed() { return status == BookingStatus.CONFIRMED; }
    public boolean isCancelled() { return status == BookingStatus.CANCELLED; }

    // ─── Lifecycle methods ────────────────────────────────────────────────────────

    /**
     * Confirms the booking (called by Payment after successful payment).
     * Requires the booking to be in PENDING state.
     */
    public boolean confirmBooking() {
        if (isCancelled()) {
            System.out.println("Booking " + bookingId + " is cancelled and cannot be confirmed.");
            return false;
        }
        if (isConfirmed()) {
            System.out.println("Booking " + bookingId + " is already confirmed.");
            return false;
        }
        if (user == null || train == null) {
            System.out.println("Booking cannot be confirmed without a user and train.");
            return false;
        }
        status = BookingStatus.CONFIRMED;
        return true;
    }

    /**
     * Cancels the booking from PENDING or CONFIRMED state.
     * Releases the seat back to the train.
     * Cannot cancel an already-cancelled booking.
     */
    public boolean cancelBooking() {
        if (isCancelled()) {
            System.out.println("Booking " + bookingId + " is already cancelled.");
            return false;
        }
        status = BookingStatus.CANCELLED;
        // Release seat back to the train
        if (train != null && seatNumber != null && !seatNumber.equals("N/A")) {
            train.releaseSeat(seatNumber);
        }
        return true;
    }

    // ─── Displayable interface ────────────────────────────────────────────────────
    @Override
    public void displayInfo() {
        System.out.println("\n========== Booking Detail ==========");
        System.out.println("Booking ID  : " + bookingId);
        if (user  != null) System.out.println("Passenger   : " + user.getName());
        if (train != null) {
            System.out.println("Train       : " + train.getTrainName());
            System.out.println("Route       : " + train.getRoute());
        }
        System.out.println("Seat        : " + seatNumber);
        System.out.printf ("Price       : $%.2f%n", price);
        System.out.println("Travel Date : " + travelDate);
        System.out.println("Status      : " + status.name());
        System.out.println("====================================");
    }

    // Comparable: lets User.displayBookingHistory() sort bookings by travel date (earliest first).
    @Override
    public int compareTo(Booking other) {
        return this.travelDate.compareTo(other.travelDate);
    }

    // ─── Standard overrides ───────────────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("Booking{id=%d, user='%s', seat='%s', price=$%.2f, status=%s, date=%s}",
                bookingId,
                (user != null ? user.getName() : "none"),
                seatNumber,
                price,
                status.name(),
                travelDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Booking)) return false;
        Booking other = (Booking) obj;
        return this.bookingId == other.bookingId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }

    public static int getBookingCount() { return bookingCount; }
}