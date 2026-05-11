package model;

public class Ticket implements Displayable, Printable {

    private static int ticketCounter = 1;

    private int ticketId;
    private Booking booking;
    private String seatNumber;
    private String status;

    private Ticket(Booking booking, String seatNumber) {
        this.ticketId = ticketCounter++;
        this.booking = booking;
        setSeatNumber(seatNumber);
        this.status = "Confirmed";
    }

    // Factory method: validates all conditions before creating a Ticket.
    // Also calls reserveSeat() on the train to track seat availability.
    public static Ticket createTicket(Booking booking, String seatNumber) {
        if (booking == null) {
            System.out.println("Ticket cannot be created. Booking is null.");
            return null;
        }

        if (!booking.isConfirmed()) {
            System.out.println("Ticket cannot be created. Booking is not confirmed.");
            return null;
        }

        if (seatNumber == null || seatNumber.trim().isEmpty()) {
            System.out.println("Ticket cannot be created. Seat number is invalid.");
            return null;
        }

        // Reserve the seat on the train. If the train is full, ticket creation fails.
        boolean reserved = booking.getTrain().reserveSeat();
        if (!reserved) {
            System.out.println("Ticket cannot be created. Train is fully booked.");
            return null;
        }

        return new Ticket(booking, seatNumber);
    }

    public int getTicketId() {
        return ticketId;
    }

    public static int getTicketCount() {
        return ticketCounter - 1;
    }

    public Booking getBooking() {
        return booking;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setSeatNumber(String seatNumber) {
        if (seatNumber != null && !seatNumber.trim().isEmpty()) {
            this.seatNumber = seatNumber;
        } else {
            System.out.println("Invalid seat number.");
        }
    }

    public boolean isValidTicket() {
        return booking != null;
    }

    // From Displayable interface:
    // displayInfo() shows the ticket's data in a plain format — used for system-level display.
    @Override
    public void displayInfo() {
        if (!isValidTicket()) {
            System.out.println("Invalid ticket.");
            return;
        }

        System.out.println("Ticket ID   : " + ticketId);
        System.out.println("Passenger   : " + booking.getUser().getName());
        System.out.println("Train       : " + booking.getTrain().getTrainName());
        System.out.println("Route       : " + booking.getTrain().getSource() + " -> " + booking.getTrain().getDestination());
        System.out.println("Seat        : " + seatNumber);
        System.out.println("Travel Date : " + booking.getTravelDate());
        System.out.println("Status      : " + status);
        System.out.println("Price       : $" + booking.getTrain().getTicketPrice());
    }

    // From Printable interface:
    // print() produces a formal ticket layout — suitable for the passenger to present at boarding.
    // It wraps displayInfo() with a header and footer to create a complete ticket document.
    @Override
    public void print() {
        System.out.println("===== TRAIN TICKET =====");
        displayInfo();
        System.out.println("========================");
    }
}
