package model;

import enums.TicketClass;
import enums.TrainType;
import interfaces.Displayable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a train in the booking system.
 *
 * OOP concepts demonstrated here:
 *   - Encapsulation  : all fields private, accessed via getters/setters
 *   - Static         : nextTrainId, trainCount, getTrainCount()
 *   - Overloading    : reserveSeat(String), reserveSeat(TicketClass),
 *                      displayInfo(), displayInfo(boolean)
 *   - Interface      : implements Displayable
 */
public class Train implements Displayable {

    // ─── Static counters ─────────────────────────────────────────────────────────
    private static int nextTrainId = 1;
    private static int trainCount  = 0;

    // ─── Instance fields ─────────────────────────────────────────────────────────
    private int       trainId;
    private String    trainName;
    private Route     route;          // single source of truth for departure/destination
    private TrainType trainType;

    // Per-class total capacities
    private int economyCapacity;
    private int businessCapacity;
    private int firstClassCapacity;

    // Per-class reserved seat sets (seat labels like "E1", "B3", "F2")
    private HashSet<String> reservedEconomy;
    private HashSet<String> reservedBusiness;
    private HashSet<String> reservedFirstClass;

    private ArrayList<Booking> bookings;
    private ArrayList<Ticket>  tickets;

    // ─── Constructor (new — Route-based) ─────────────────────────────────────────
    public Train(String trainName,
                 Route route,
                 TrainType trainType,
                 int economyCapacity,
                 int businessCapacity,
                 int firstClassCapacity) {
        this.trainId          = nextTrainId++;
        this.bookings         = new ArrayList<>();
        this.tickets          = new ArrayList<>();
        this.reservedEconomy    = new HashSet<>();
        this.reservedBusiness   = new HashSet<>();
        this.reservedFirstClass = new HashSet<>();
        trainCount++;
        setTrainName(trainName);
        setRoute(route);
        setTrainType(trainType);
        setEconomyCapacity(economyCapacity);
        setBusinessCapacity(businessCapacity);
        setFirstClassCapacity(firstClassCapacity);
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

    public void setEconomyCapacity(int capacity) {
        this.economyCapacity = (capacity > 0) ? capacity : 0;
    }

    public void setBusinessCapacity(int capacity) {
        this.businessCapacity = (capacity > 0) ? capacity : 0;
    }

    public void setFirstClassCapacity(int capacity) {
        this.firstClassCapacity = (capacity > 0) ? capacity : 0;
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────
    public int       getTrainId()            { return trainId; }
    public String    getTrainName()          { return trainName; }
    public Route     getRoute()              { return route; }
    public TrainType getTrainType()          { return trainType; }

    public int getEconomyCapacity()          { return economyCapacity; }
    public int getBusinessCapacity()         { return businessCapacity; }
    public int getFirstClassCapacity()       { return firstClassCapacity; }

    // Backward-compatible delegates — existing code calling getSource()/getDestination() still works
    public String getSource()                { return route.getDepartureStation(); }
    public String getDestination()           { return route.getDestinationStation(); }

    // Legacy price accessor — returns base price of ECONOMY on this train type
    public double getTicketPrice()           {
        return service.PriceCalculator.calculatePrice(trainType, TicketClass.ECONOMY);
    }

    // Total seats across all classes
    public int getTotalSeats() {
        return economyCapacity + businessCapacity + firstClassCapacity;
    }

    public int getReservedSeatCount() {
        return reservedEconomy.size() + reservedBusiness.size() + reservedFirstClass.size();
    }

    public ArrayList<Booking> getBookingsCopy()  { return new ArrayList<>(bookings); }
    public ArrayList<Ticket>  getTicketsCopy()   { return new ArrayList<>(tickets); }

    // ─── Capacity helpers ─────────────────────────────────────────────────────────
    public int getCapacity(TicketClass ticketClass) {
        switch (ticketClass) {
            case ECONOMY:     return economyCapacity;
            case BUSINESS:    return businessCapacity;
            case FIRST_CLASS: return firstClassCapacity;
            default:          return 0;
        }
    }

    public int getReservedCount(TicketClass ticketClass) {
        switch (ticketClass) {
            case ECONOMY:     return reservedEconomy.size();
            case BUSINESS:    return reservedBusiness.size();
            case FIRST_CLASS: return reservedFirstClass.size();
            default:          return 0;
        }
    }

    public int getAvailableSeats(TicketClass ticketClass) {
        return getCapacity(ticketClass) - getReservedCount(ticketClass);
    }

    public boolean hasAvailableSeat()                          { return getReservedSeatCount() < getTotalSeats(); }
    public boolean hasAvailableSeat(TicketClass ticketClass)   { return getAvailableSeats(ticketClass) > 0; }

    // ─── Seat reservation ─────────────────────────────────────────────────────────

    /**
     * OVERLOAD 1 — Reserve a specific named seat with class validation.
     * Seat label must begin with the correct prefix for its class (E/B/F).
     * Returns false if seat is already reserved, wrong class prefix, or class is full.
     */
    public boolean reserveSeat(String seatNumber, TicketClass ticketClass) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) return false;
        String seat = seatNumber.trim().toUpperCase();

        // Validate prefix matches the chosen class
        if (!seat.startsWith(ticketClass.getSeatPrefix())) {
            System.out.println("Seat '" + seat + "' does not belong to class "
                    + ticketClass.getLabel() + " (expected prefix '"
                    + ticketClass.getSeatPrefix() + "').");
            return false;
        }
        HashSet<String> reserved = getReservedSet(ticketClass);
        if (reserved.contains(seat)) {
            System.out.println("Seat '" + seat + "' is already reserved.");
            return false;
        }
        if (!hasAvailableSeat(ticketClass)) {
            System.out.println("No available seats in " + ticketClass.getLabel() + " class.");
            return false;
        }
        reserved.add(seat);
        return true;
    }

    /**
     * OVERLOAD 2 — Legacy single-argument overload (used by old Ticket.createTicket path).
     * Infers class from seat prefix (E/B/F). Falls back to no-prefix logic if prefix unknown.
     */
    public boolean reserveSeat(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) return false;
        String seat = seatNumber.trim().toUpperCase();
        TicketClass inferredClass = inferClassFromSeat(seat);
        if (inferredClass != null) {
            return reserveSeat(seat, inferredClass);
        }
        // Legacy fallback — treat as economy if prefix not recognised
        if (!hasAvailableSeat(TicketClass.ECONOMY)) return false;
        if (reservedEconomy.contains(seat)) return false;
        reservedEconomy.add(seat);
        return true;
    }

    /**
     * OVERLOAD 3 — Auto-assign the next available seat for a given class.
     * Returns the assigned seat label, or null if the class is full.
     */
    public String reserveSeat(TicketClass ticketClass) {
        if (!hasAvailableSeat(ticketClass)) {
            System.out.println(ticketClass.getLabel() + " class is full.");
            return null;
        }
        HashSet<String> reserved = getReservedSet(ticketClass);
        int capacity = getCapacity(ticketClass);
        for (int i = 1; i <= capacity + 1; i++) {
            String seat = ticketClass.getSeatPrefix() + i;
            if (!reserved.contains(seat)) {
                reserved.add(seat);
                return seat;
            }
        }
        return null;
    }

    /** Release a previously reserved seat (called on booking cancellation). */
    public boolean releaseSeat(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) return false;
        String seat = seatNumber.trim().toUpperCase();
        if (reservedEconomy.remove(seat))    return true;
        if (reservedBusiness.remove(seat))   return true;
        if (reservedFirstClass.remove(seat)) return true;
        return false;
    }

    public boolean isSeatAvailable(String seatNumber, TicketClass ticketClass) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) return false;
        String seat = seatNumber.trim().toUpperCase();
        if (!seat.startsWith(ticketClass.getSeatPrefix())) return false;
        return hasAvailableSeat(ticketClass) && !getReservedSet(ticketClass).contains(seat);
    }

    /** Legacy isSeatAvailable used by old Ticket.createTicket. */
    public boolean isSeatAvailable(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) return false;
        String seat = seatNumber.trim().toUpperCase();
        TicketClass cls = inferClassFromSeat(seat);
        if (cls != null) return isSeatAvailable(seat, cls);
        return hasAvailableSeat() && !reservedEconomy.contains(seat);
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

    /** OVERRIDE — summary view */
    @Override
    public void displayInfo() {
        System.out.println("Train ID    : " + trainId);
        System.out.println("Train Name  : " + trainName);
        System.out.println("Train Type  : " + trainType.getLabel());
        System.out.println("Route       : " + route);
        System.out.println("Economy     : " + reservedEconomy.size()    + "/" + economyCapacity    + " reserved");
        System.out.println("Business    : " + reservedBusiness.size()   + "/" + businessCapacity   + " reserved");
        System.out.println("First Class : " + reservedFirstClass.size() + "/" + firstClassCapacity + " reserved");
        System.out.println("Bookings    : " + bookings.size());
    }

    /** OVERLOAD — detailed view with optional reserved-seat labels */
    public void displayInfo(boolean showSeats) {
        displayInfo();
        if (showSeats) {
            System.out.println("Economy seats     : " + reservedEconomy);
            System.out.println("Business seats    : " + reservedBusiness);
            System.out.println("First Class seats : " + reservedFirstClass);
        }
    }

    // ─── Standard overrides ───────────────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format(
            "Train{id=%d, name='%s', type=%s, route='%s', seats=%d/%d}",
            trainId, trainName, trainType.getLabel(), route,
            getReservedSeatCount(), getTotalSeats());
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

    // ─── Private helpers ──────────────────────────────────────────────────────────
    private HashSet<String> getReservedSet(TicketClass ticketClass) {
        switch (ticketClass) {
            case ECONOMY:     return reservedEconomy;
            case BUSINESS:    return reservedBusiness;
            case FIRST_CLASS: return reservedFirstClass;
            default:          return reservedEconomy;
        }
    }

    private TicketClass inferClassFromSeat(String seat) {
        if (seat.startsWith("E")) return TicketClass.ECONOMY;
        if (seat.startsWith("B")) return TicketClass.BUSINESS;
        if (seat.startsWith("F")) return TicketClass.FIRST_CLASS;
        return null;
    }
}