package model;

import interfaces.Displayable;
import interfaces.Printable;
import java.util.Objects;

public class Ticket implements Displayable, Printable {

    private static int nextTicketId = 1;
    private static int ticketCount  = 0;

    private int     ticketId;
    private Booking booking;
    private String  seatNumber;
    private String  status;

    // ─── Constructor (private — use createTicket() factory method) ────────────────
    private Ticket(Booking booking, String seatNumber) {
        this.ticketId   = nextTicketId++;
        this.booking    = booking;
        this.seatNumber = cleanSeatNumber(seatNumber);
        this.status     = "Issued";
        ticketCount++;
    }

    // ─── Helper ──────────────────────────────────────────────────────────────────
    private static String cleanSeatNumber(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) return "No Seat";
        return seatNumber.trim().toUpperCase();
    }

    // ─── Factory method (EXISTING) ───────────────────────────────────────────────
    // Validates all preconditions before creating a Ticket.
    // Called via: Ticket.createTicket(booking, payment, "A1")
    public static Ticket createTicket(Booking booking, Payment payment, String seatNumber) {
        if (booking == null) {
            System.out.println("Ticket cannot be created. Booking is null.");
            return null;
        }
        if (payment == null || !payment.isPaid()) {
            System.out.println("Ticket cannot be created. Payment is not paid.");
            return null;
        }
        if (payment.getBooking() != booking) {
            System.out.println("Ticket cannot be created. Payment does not belong to this booking.");
            return null;
        }
        if (!booking.isConfirmed()) {
            System.out.println("Ticket cannot be created. Booking is not confirmed.");
            return null;
        }
        if (booking.getTrain() == null) {
            System.out.println("Ticket cannot be created. Booking has no train.");
            return null;
        }
        if (!booking.getTrain().reserveSeat(seatNumber)) {
            System.out.println("Ticket cannot be created. Seat is not available.");
            return null;
        }
        Ticket ticket = new Ticket(booking, seatNumber);
        booking.getTrain().addTicket(ticket);
        return ticket;
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────
    public int     getTicketId()   { return ticketId; }
    public Booking getBooking()    { return booking; }
    public String  getSeatNumber() { return seatNumber; }
    public String  getStatus()     { return status; }
    public boolean isIssued()      { return "Issued".equalsIgnoreCase(status); }

    // ─── displayInfo() override (EXISTING) ───────────────────────────────────────
    @Override
    public void displayInfo() {
        System.out.println("\n========== Ticket Detail ==========");
        System.out.println("Ticket ID   : " + ticketId);
        System.out.println("Seat Number : " + seatNumber);
        System.out.println("Status      : " + status);
        if (booking != null) {
            System.out.println("Booking ID  : " + booking.getBookingId());
            System.out.println("Travel Date : " + booking.getTravelDate());
            if (booking.getUser()  != null) System.out.println("Passenger   : " + booking.getUser().getName());
            if (booking.getTrain() != null) {
                System.out.println("Train       : " + booking.getTrain().getTrainName());
                System.out.println("Route       : " + booking.getTrain().getSource()
                        + " -> " + booking.getTrain().getDestination());
            }
        }
        System.out.println("===================================");
    }

    // ─── print() override — Printable interface (EXISTING) ───────────────────────
    @Override
    public void print() {
        System.out.println("\n========== FORMAL TRAIN TICKET ==========");
        System.out.println("Ticket No : " + ticketId);
        if (booking != null) {
            if (booking.getUser()  != null) System.out.println("Passenger : " + booking.getUser().getName());
            if (booking.getTrain() != null) {
                System.out.println("Train     : " + booking.getTrain().getTrainName());
                System.out.println("Route     : " + booking.getTrain().getSource()
                        + " -> " + booking.getTrain().getDestination());
            }
            System.out.println("Date      : " + booking.getTravelDate());
        }
        System.out.println("Seat      : " + seatNumber);
        System.out.println("Status    : " + status);
        System.out.println("=========================================");
    }

    // ─── toString() override (NEW) ───────────────────────────────────────────────
    // Quick one-liner for logging — e.g. System.out.println(ticket)
    @Override
    public String toString() {
        return String.format("Ticket{id=%d, seat='%s', status='%s', booking=%d}",
                ticketId,
                seatNumber,
                status,
                (booking != null ? booking.getBookingId() : -1));
    }

    // ─── equals() override (NEW) ─────────────────────────────────────────────────
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ticket)) return false;
        Ticket other = (Ticket) obj;
        return this.ticketId == other.ticketId;
    }

    // ─── hashCode() override (NEW) ───────────────────────────────────────────────
    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }

    public static int getTicketCount() { return ticketCount; }
}