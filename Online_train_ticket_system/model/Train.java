package model;

import interfaces.Displayable;
import java.util.ArrayList;
import java.util.HashSet;

public class Train implements Displayable {
    private static int nextTrainId = 1;
    private static int trainCount = 0;

    private int trainId;
    private String trainName;
    private String source;
    private String destination;
    private double ticketPrice;
    private int totalSeats;
    private ArrayList<Booking> bookings;
    private ArrayList<Ticket> tickets;
    private HashSet<String> reservedSeats;

    public Train(String trainName, String source, String destination, int totalSeats, double ticketPrice) {
        this.trainId = nextTrainId++;
        trainCount++;
        this.bookings = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.reservedSeats = new HashSet<>();
        setTrainName(trainName);
        setSource(source);
        setDestination(destination);
        setTotalSeats(totalSeats);
        setTicketPrice(ticketPrice);
    }

    private String cleanText(String value, String defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    public int getTrainId() { return trainId; }
    public String getTrainName() { return trainName; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public double getTicketPrice() { return ticketPrice; }
    public int getTotalSeats() { return totalSeats; }

    public ArrayList<Booking> getBookingsCopy() { return new ArrayList<>(bookings); }
    public ArrayList<Ticket> getTicketsCopy() { return new ArrayList<>(tickets); }
    public HashSet<String> getReservedSeatsCopy() { return new HashSet<>(reservedSeats); }

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
        this.ticketPrice = ticketPrice > 0 ? ticketPrice : 0;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats > 0 ? totalSeats : 0;
    }

    public boolean addBooking(Booking booking) {
        if (booking == null) return false;
        if (!bookings.contains(booking)) {
            bookings.add(booking);
            return true;
        }
        return false;
    }

    public boolean addTicket(Ticket ticket) {
        if (ticket == null) return false;
        if (!tickets.contains(ticket)) {
            tickets.add(ticket);
            return true;
        }
        return false;
    }

    public boolean hasAvailableSeat() {
        return reservedSeats.size() < totalSeats;
    }

    public boolean isSeatAvailable(String seatNumber) {
        if (seatNumber == null || seatNumber.trim().isEmpty()) return false;
        return hasAvailableSeat() && !reservedSeats.contains(seatNumber.trim().toUpperCase());
    }

    public boolean reserveSeat(String seatNumber) {
        if (!isSeatAvailable(seatNumber)) return false;
        reservedSeats.add(seatNumber.trim().toUpperCase());
        return true;
    }

    public int getReservedSeatCount() {
        return reservedSeats.size();
    }

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

    public static int getTrainCount() {
        return trainCount;
    }
}
