package model;

import interfaces.Displayable;
import interfaces.Printable;
import java.time.LocalDate;
import java.util.Objects;

public class Ticket implements Displayable, Printable {

    // ─── Static counters ─────────────────────────────────────────────────────────
    private static int nextTicketId = 1;
    private static int ticketCount  = 0;

    // ─── Instance fields ─────────────────────────────────────────────────────────
    private int       ticketId;
    private Booking   booking;
    private String    seatNumber;   // seat label (e.g. "1", "2", "3")
    private LocalDate issueDate;    // date the ticket was generated
    private String    status;

    // ─── Private constructor — use createTicket() factory method ─────────────────
    private Ticket(Booking booking, String seatNumber) {
        this.ticketId   = nextTicketId++;
        this.booking    = booking;
        this.seatNumber = cleanSeat(seatNumber);
        this.issueDate  = LocalDate.now();
        this.status     = "Issued";
        ticketCount++;
    }

    // ─── Factory method ───────────────────────────────────────────────────────────
    /**
     * Creates and returns a Ticket after validating that:
     *   - Booking is non-null and CONFIRMED
     *   - Payment is non-null and paid
     *   - Payment belongs to this booking
     *   - Train is non-null
     *   - Seat is available for the booking's ticket class
     *
     * Returns null (with console message) if any guard fails.
     */
    public static Ticket createTicket(Booking booking, Payment payment, String seatNumber) {
        if (booking == null) {
            System.out.println("Ticket cannot be created: booking is null.");
            return null;
        }
        if (payment == null || !payment.isPaid()) {
            System.out.println("Ticket cannot be created: payment is not completed.");
            return null;
        }
        if (payment.getBooking() != booking) {
            System.out.println("Ticket cannot be created: payment does not belong to this booking.");
            return null;
        }
        if (!booking.isConfirmed()) {
            System.out.println("Ticket cannot be created: booking is not confirmed.");
            return null;
        }
        if (booking.getTrain() == null) {
            System.out.println("Ticket cannot be created: booking has no train.");
            return null;
        }

        // Use the seat that was already reserved during booking creation
        String seat = (seatNumber != null && !seatNumber.trim().isEmpty())
                    ? seatNumber.trim().toUpperCase()
                    : booking.getSeatNumber();

        Ticket ticket = new Ticket(booking, seat);
        booking.getTrain().addTicket(ticket);
        return ticket;
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────
    public int       getTicketId()   { return ticketId; }
    public Booking   getBooking()    { return booking; }
    public String    getSeatNumber() { return seatNumber; }
    public LocalDate getIssueDate()  { return issueDate; }
    public String    getStatus()     { return status; }
    public boolean   isIssued()      { return "Issued".equalsIgnoreCase(status); }

    // ─── Displayable interface — OVERRIDE ─────────────────────────────────────────
    @Override
    public void displayInfo() {
        System.out.println("\n========== Ticket Detail ==========");
        System.out.println("Ticket ID   : " + ticketId);
        System.out.println("Seat        : " + seatNumber);
        System.out.println("Issue Date  : " + issueDate);
        System.out.println("Status      : " + status);
        if (booking != null) {
            System.out.println("Booking ID  : " + booking.getBookingId());
            System.out.printf ("Price       : $%.2f%n", booking.getPrice());
            System.out.println("Travel Date : " + booking.getTravelDate());
            if (booking.getUser()  != null) System.out.println("Passenger   : " + booking.getUser().getName());
            if (booking.getTrain() != null) {
                System.out.println("Train       : " + booking.getTrain().getTrainName());
                System.out.println("Route       : " + booking.getTrain().getRoute());
            }
        }
        System.out.println("===================================");
    }

    // ─── Printable interface — OVERRIDE ───────────────────────────────────────────
    @Override
    public void print() {
        System.out.println("\n========== FORMAL TRAIN TICKET ==========");
        System.out.println("Ticket No   : " + ticketId);
        System.out.println("Seat        : " + seatNumber);
        System.out.println("Issue Date  : " + issueDate);
        if (booking != null) {
            if (booking.getUser() != null)
                System.out.println("Passenger   : " + booking.getUser().getName());
            if (booking.getTrain() != null) {
                System.out.println("Train       : " + booking.getTrain().getTrainName()
                        + " [" + booking.getTrain().getTrainType().getLabel() + "]");
                System.out.println("Route       : " + booking.getTrain().getRoute());
            }
            System.out.printf ("Price       : $%.2f%n", booking.getPrice());
            System.out.println("Travel Date : " + booking.getTravelDate());
        }
        System.out.println("Status      : " + status);
        System.out.println("=========================================");
    }

    // ─── Standard overrides ───────────────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("Ticket{id=%d, seat='%s', issued=%s, booking=%d}",
                ticketId, seatNumber, issueDate,
                (booking != null ? booking.getBookingId() : -1));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ticket)) return false;
        Ticket other = (Ticket) obj;
        return this.ticketId == other.ticketId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }

    public static int getTicketCount() { return ticketCount; }

    // ─── Private helpers ──────────────────────────────────────────────────────────
    private static String cleanSeat(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) return "N/A";
        return seatNumber.trim().toUpperCase();
    }
}