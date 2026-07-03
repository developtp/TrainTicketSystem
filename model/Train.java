package model;

import enums.TrainType;
import interfaces.Displayable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Train implements Displayable {

    // ─── Static counters ─────────────────────────────────────────────────────────
    private static int nextTrainId = 1;
    private static int trainCount  = 0;

    // ─── Instance fields ─────────────────────────────────────────────────────────
    private int       trainId;
    private String    trainName;
    private Route     route;          // single source of truth for departure/destination
    private TrainType trainType;

    private int capacity;                     // total seats on this train
    private HashSet<String> reservedSeats;    // reserved seat labels, e.g. "1", "2", ...

    private ArrayList<Booking> bookings;
    private ArrayList<Ticket>  tickets;

    // ─── Constructor ──────────────────────────────────────────────────────────────
    public Train(String trainName, Route route, TrainType trainType, int capacity) {
        this.trainId       = nextTrainId++;
        this.bookings      = new ArrayList<>();
        this.tickets       = new ArrayList<>();
        this.reservedSeats = new HashSet<>();
        trainCount++;
        setTrainName(trainName);
        setRoute(route);
        setTrainType(trainType);
        setCapacity(capacity);
    }

    // ─── Setters ─────────────────────────────────────────────────────────────────
    public void setTrainName(String trainName) {
        String cleaned = (trainName == null) ? "" : trainName.trim();
        this.trainName = cleaned.isEmpty() ? "Unknown Train" : cleaned;
    }

    public void setRoute(Route route) {
        this.route = (route != null) ? route : new Route("Unknown", "Unknown");
    }

    public void setTrainType(TrainType trainType) {
        this.trainType = (trainType != null) ? trainType : TrainType.REGULAR;
    }

    public void setCapacity(int capacity) {
        this.capacity = (capacity > 0) ? capacity : 0;
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────
    public int       getTrainId()   { return trainId; }
    public String    getTrainName() { return trainName; }
    public Route     getRoute()     { return route; }
    public TrainType getTrainType() { return trainType; }
    public int        getCapacity() { return capacity; }

    public String getSource()      { return route.getDepartureStation(); }
    public String getDestination() { return route.getDestinationStation(); }

    public double getTicketPrice() {
        return service.PriceCalculator.calculatePrice(trainType);
    }

    public int getReservedSeatCount()  { return reservedSeats.size(); }
    public int getAvailableSeats()     { return capacity - reservedSeats.size(); }
    public boolean hasAvailableSeat()  { return getAvailableSeats() > 0; }

    public ArrayList<Booking> getBookingsCopy() { return new ArrayList<>(bookings); }

    // ─── Seat reservation ─────────────────────────────────────────────────────────

    // Method overloading: reserve a specific seat, or auto-assign one (see below).
    public boolean reserveSeat(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) return false;
        String seat = seatNumber.trim().toUpperCase();
        if (reservedSeats.contains(seat)) {
            System.out.println("Seat '" + seat + "' is already reserved.");
            return false;
        }
        if (!hasAvailableSeat()) {
            System.out.println("No available seats on this train.");
            return false;
        }
        reservedSeats.add(seat);
        return true;
    }

    // Overload: auto-assign the next free seat instead of naming one.
    // Returns the assigned seat label, or null if the train is full.
    public String reserveSeat() {
        if (!hasAvailableSeat()) {
            System.out.println("Train is full.");
            return null;
        }
        for (int i = 1; i <= capacity; i++) {
            String seat = String.valueOf(i);
            if (!reservedSeats.contains(seat)) {
                reservedSeats.add(seat);
                return seat;
            }
        }
        return null;
    }

    /** Release a previously reserved seat (called on booking cancellation). */
    public boolean releaseSeat(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) return false;
        return reservedSeats.remove(seatNumber.trim().toUpperCase());
    }

    // ─── Booking / Ticket registration ───────────────────────────────────────────
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

    // ─── Displayable interface ────────────────────────────────────────────────────
    @Override
    public void displayInfo() {
        System.out.println("Train ID    : " + trainId);
        System.out.println("Train Name  : " + trainName);
        System.out.println("Train Type  : " + trainType.getLabel());
        System.out.println("Route       : " + route);
        System.out.println("Seats       : " + reservedSeats.size() + "/" + capacity + " reserved");
        System.out.println("Bookings    : " + bookings.size());
    }

    // ─── Standard overrides ───────────────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format(
            "Train{id=%d, name='%s', type=%s, route='%s', seats=%d/%d}",
            trainId, trainName, trainType.getLabel(), route,
            getReservedSeatCount(), capacity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Train)) return false;
        Train other = (Train) obj;
        return this.trainId == other.trainId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainId);
    }

    public static int getTrainCount() { return trainCount; }
}
