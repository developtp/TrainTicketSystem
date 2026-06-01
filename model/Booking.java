package model;

import interfaces.Displayable;
import java.time.LocalDate;
import java.util.Objects;

public class Booking implements Displayable {

    private static int nextBookingId = 1;
    private static int bookingCount  = 0;

    private int       bookingId;
    private User      user;
    private Train     train;
    private LocalDate travelDate;
    private String    status;

    // ─── Constructors (OVERLOADED) ────────────────────────────────────────────────

    // VERSION 1 (EXISTING) — travel date as String e.g. "2026-06-10"
    // Parses and validates the string internally
    public Booking(User user, Train train, String travelDate) {
        this.bookingId = nextBookingId++;
        this.user      = user;
        this.train     = train;
        setTravelDate(travelDate);
        this.status = "Pending";
        bookingCount++;
    }

    // VERSION 2 (EXISTING) — same as above but caller also provides a status
    public Booking(User user, Train train, String travelDate, String status) {
        this(user, train, travelDate); // delegates to VERSION 1
        setStatus(status);
    }

    // VERSION 3 (NEW OVERLOAD) — travel date as LocalDate directly
    // Use this when you already have a LocalDate object — avoids string parsing
    // Example: Booking b = new Booking(user, train, LocalDate.of(2026, 6, 10));
    public Booking(User user, Train train, LocalDate travelDate) {
        this.bookingId = nextBookingId++;
        this.user      = user;
        this.train     = train;
        // Validate: cannot book in the past
        if (travelDate == null || travelDate.isBefore(LocalDate.now())) {
            System.out.println("Invalid travel date. Defaulting to today.");
            this.travelDate = LocalDate.now();
        } else {
            this.travelDate = travelDate;
        }
        this.status = "Pending";
        bookingCount++;
    }

    // ─── Helper ──────────────────────────────────────────────────────────────────
    private String cleanText(String value, String defaultValue) {
        if (value == null || value.trim().isEmpty()) return defaultValue;
        return value.trim();
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────
    public int       getBookingId()  { return bookingId; }
    public User      getUser()       { return user; }
    public Train     getTrain()      { return train; }
    public LocalDate getTravelDate() { return travelDate; }
    public String    getStatus()     { return status; }

    // ─── Setters ─────────────────────────────────────────────────────────────────
    public void setTravelDate(String travelDate) {
        try {
            LocalDate date = LocalDate.parse(cleanText(travelDate, LocalDate.now().toString()));
            if (date.isBefore(LocalDate.now())) {
                System.out.println("Invalid travel date. Defaulting to today.");
                this.travelDate = LocalDate.now();
            } else {
                this.travelDate = date;
            }
        } catch (Exception e) {
            System.out.println("Invalid travel date format. Defaulting to today.");
            this.travelDate = LocalDate.now();
        }
    }

    public void setStatus(String status) {
        String cleaned = cleanText(status, "Pending");
        if (cleaned.equalsIgnoreCase("Pending") ||
            cleaned.equalsIgnoreCase("Confirmed") ||
            cleaned.equalsIgnoreCase("Cancelled")) {
            this.status = cleaned.substring(0, 1).toUpperCase()
                        + cleaned.substring(1).toLowerCase();
        } else {
            this.status = "Pending";
        }
    }

    // ─── Status helpers ──────────────────────────────────────────────────────────
    public boolean isPending()   { return "Pending".equalsIgnoreCase(status); }
    public boolean isConfirmed() { return "Confirmed".equalsIgnoreCase(status); }
    public boolean isCancelled() { return "Cancelled".equalsIgnoreCase(status); }

    public double calculateAmount() {
        return (train == null) ? 0 : train.getTicketPrice();
    }

    // ─── Business logic ──────────────────────────────────────────────────────────
    public boolean confirm() {
        if (isCancelled()) {
            System.out.println("Booking " + bookingId + " is cancelled and cannot be confirmed.");
            return false;
        }
        if (user == null) {
            System.out.println("Booking cannot be confirmed without a user.");
            return false;
        }
        if (train == null) {
            System.out.println("Booking cannot be confirmed without a train.");
            return false;
        }
        if (!train.hasAvailableSeat()) {
            System.out.println("Booking cannot be confirmed. No available seats.");
            return false;
        }
        status = "Confirmed";
        return true;
    }

    public boolean cancel() {
        if (isConfirmed()) {
            System.out.println("Confirmed booking cannot be cancelled in this simple version.");
            return false;
        }
        status = "Cancelled";
        return true;
    }

    // ─── displayInfo() override (EXISTING) ───────────────────────────────────────
    @Override
    public void displayInfo() {
        System.out.println("\n========== Booking Detail ==========");
        System.out.println("Booking ID  : " + bookingId);
        System.out.println("Travel Date : " + travelDate);
        System.out.println("Status      : " + status);
        if (user  != null) System.out.println("User        : " + user.getName());
        if (train != null) {
            System.out.println("Train       : " + train.getTrainName());
            System.out.println("Route       : " + train.getSource() + " -> " + train.getDestination());
            System.out.println("Amount      : $" + calculateAmount());
        }
        System.out.println("====================================");
    }

    // ─── toString() override (NEW) ───────────────────────────────────────────────
    // Compact one-liner — useful for logging and list printing
    // e.g. System.out.println(booking1)  →  Booking{id=1, user='Dara', status='Confirmed', date=2026-06-10}
    @Override
    public String toString() {
        return String.format("Booking{id=%d, user='%s', status='%s', date=%s}",
                bookingId,
                (user  != null ? user.getName()        : "none"),
                status,
                travelDate);
    }

    // ─── equals() override (NEW) ─────────────────────────────────────────────────
    // Two bookings are equal if they share the same bookingId.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Booking)) return false;
        Booking other = (Booking) obj;
        return this.bookingId == other.bookingId;
    }

    // ─── hashCode() override (NEW) ───────────────────────────────────────────────
    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }

    public static int getBookingCount() { return bookingCount; }
}