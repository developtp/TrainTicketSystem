package model;

import interfaces.Displayable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Train implements Displayable {

    private static int nextTrainId = 1;
    private static int trainCount  = 0;

    private int    trainId;
    private String trainName;
    private String source;
    private String destination;
    private double ticketPrice;
    private int    totalSeats;

    private ArrayList<Booking> bookings;
    private ArrayList<Ticket>  tickets;
    private HashSet<String>    reservedSeats;

    // ─── Constructor ─────────────────────────────────────────────────────────────
    public Train(String trainName, String source, String destination,
                 int totalSeats, double ticketPrice) {
        this.trainId      = nextTrainId++;
        this.bookings     = new ArrayList<>();
        this.tickets      = new ArrayList<>();
        this.reservedSeats = new HashSet<>();
        trainCount++;
        setTrainName(trainName);
        setSource(source);
        setDestination(destination);
        setTotalSeats(totalSeats);
        setTicketPrice(ticketPrice);
    }

    // ─── Helper ──────────────────────────────────────────────────────────────────
    private String cleanText(String value, String defaultValue) {
        if (value == null || value.trim().isEmpty()) return defaultValue;
        return value.trim();
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────
    public int    getTrainId()    { return trainId; }
    public String getTrainName()  { return trainName; }
    public String getSource()     { return source; }
    public String getDestination(){ return destination; }
    public double getTicketPrice(){ return ticketPrice; }
    public int    getTotalSeats() { return totalSeats; }
    public int    getReservedSeatCount() { return reservedSeats.size(); }

    public ArrayList<Booking> getBookingsCopy()     { return new ArrayList<>(bookings); }
    public ArrayList<Ticket>  getTicketsCopy()      { return new ArrayList<>(tickets); }
    public HashSet<String>    getReservedSeatsCopy(){ return new HashSet<>(reservedSeats); }

    // ─── Setters ─────────────────────────────────────────────────────────────────
    public void setTrainName(String trainName) {
        this.trainName = cleanText(trainName, "Unknown Train");
    }

    public void setSource(String source) {
        this.source = cleanText(source, "Unknown Source");
    }

    public void setDestination(String destination) {
        this.destination = cleanText(destination, "Unknown Destination");
        if (this.destination.equalsIgnoreCase(this.source)) {
            this.destination = "Invalid Destination";
        }
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = (ticketPrice > 0) ? ticketPrice : 0;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = (totalSeats > 0) ? totalSeats : 0;
    }

    // ─── Booking / Ticket management ─────────────────────────────────────────────
    public boolean addBooking(Booking booking) {
        if (booking == null || bookings.contains(booking)) return false;
        bookings.add(booking);
        return true;
    }

    public boolean addTicket(Ticket ticket) {
        if (ticket == null || tickets.contains(ticket)) return false;
        tickets.add(ticket);
        return true;
    }

    // ─── Seat management (OVERLOADED) ────────────────────────────────────────────

    // VERSION 1 (EXISTING) — caller provides a specific seat label e.g. "A1"
    public boolean reserveSeat(String seatNumber) {
        if (!isSeatAvailable(seatNumber)) return false;
        reservedSeats.add(seatNumber.trim().toUpperCase());
        return true;
    }

    // VERSION 2 (NEW OVERLOAD) — auto-assigns next available seat label
    // Use when the passenger doesn't care which seat — system picks for them.
    // Returns the seat label that was assigned, or null if train is full.
    // Example: String seat = train.reserveSeat();  →  "S1", "S2", ...
    public String reserveSeat() {
        if (!hasAvailableSeat()) {
            System.out.println("Train is full. No available seats.");
            return null;
        }
        // Generate seat labels S1, S2, S3 … until we find one not yet taken
        int attempt = reservedSeats.size() + 1;
        while (attempt <= totalSeats + 1) {          // +1 safety buffer
            String autoSeat = "S" + attempt;
            if (!reservedSeats.contains(autoSeat)) {
                reservedSeats.add(autoSeat);
                return autoSeat;                     // return assigned label to caller
            }
            attempt++;
        }
        return null; // should not reach here, but safe fallback
    }

    public boolean hasAvailableSeat() {
        return reservedSeats.size() < totalSeats;
    }

    public boolean isSeatAvailable(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) return false;
        return hasAvailableSeat() && !reservedSeats.contains(seatNumber.trim().toUpperCase());
    }

    // ─── displayInfo() override (EXISTING) ───────────────────────────────────────
    @Override
    public void displayInfo() {
        System.out.println("Train ID       : " + trainId);
        System.out.println("Train Name     : " + trainName);
        System.out.println("Route          : " + source + " -> " + destination);
        System.out.println("Ticket Price   : $" + ticketPrice);
        System.out.println("Total Seats    : " + totalSeats);
        System.out.println("Reserved Seats : " + reservedSeats.size());
        System.out.println("Bookings       : " + bookings.size());
        System.out.println("Tickets        : " + tickets.size());
    }

    // ─── toString() override (NEW) ───────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("Train{id=%d, name='%s', route='%s->%s', price=$%.2f, seats=%d/%d}",
                trainId, trainName, source, destination,
                ticketPrice, reservedSeats.size(), totalSeats);
    }

    // ─── equals() override (NEW) ─────────────────────────────────────────────────
    // Two Train objects are equal if they share the same trainId.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Train)) return false;
        Train other = (Train) obj;
        return this.trainId == other.trainId;
    }

    // ─── hashCode() override (NEW) ───────────────────────────────────────────────
    @Override
    public int hashCode() {
        return Objects.hash(trainId);
    }

    public static int getTrainCount() { return trainCount; }
}