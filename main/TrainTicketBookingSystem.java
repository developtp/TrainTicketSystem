package main;

import interfaces.BookingSearchable;
import interfaces.Displayable;
import interfaces.TrainSearchable;
import interfaces.UserSearchable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import model.Booking;
import model.Payment;
import model.Ticket;
import model.Train;
import model.User;

public class TrainTicketBookingSystem implements Displayable, UserSearchable, TrainSearchable, BookingSearchable {

    private String             systemName;
    private HashMap<Integer, User> users;
    private ArrayList<Train>   trains;
    private ArrayList<Booking> bookings;
    private ArrayList<Payment> payments;
    private ArrayList<Ticket>  tickets;
    private HashSet<String>    destinations;

    // ─── Constructor ─────────────────────────────────────────────────────────────
    public TrainTicketBookingSystem(String systemName) {
        this.systemName   = (systemName == null || systemName.trim().isEmpty())
                          ? "Train Ticket Booking System"
                          : systemName.trim();
        this.users        = new HashMap<>();
        this.trains       = new ArrayList<>();
        this.bookings     = new ArrayList<>();
        this.payments     = new ArrayList<>();
        this.tickets      = new ArrayList<>();
        this.destinations = new HashSet<>();
    }

    // ─── Add methods ─────────────────────────────────────────────────────────────
    public boolean addUser(User user) {
        if (user == null) return false;
        if (users.containsKey(user.getUserId())) {
            System.out.println("User ID already exists: " + user.getUserId());
            return false;
        }
        users.put(user.getUserId(), user);
        return true;
    }

    public boolean addTrain(Train train) {
        if (train == null) return false;
        if (searchTrainById(train.getTrainId()) != null) {
            System.out.println("Train ID already exists: " + train.getTrainId());
            return false;
        }
        trains.add(train);
        destinations.add(train.getDestination());
        return true;
    }

    // ─── Search methods (interface overrides) ────────────────────────────────────

    // EXISTING — search by numeric ID (from UserSearchable)
    @Override
    public User searchUserById(int userId) {
        return users.get(userId);
    }

    // NEW OVERLOAD — search by name (case-insensitive)
    // Returns first match found, or null if no user has that name.
    // Example: system.searchUserByName("dara")  →  finds "Dara"
    public User searchUserByName(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        for (User user : users.values()) {
            if (user.getName().equalsIgnoreCase(name.trim())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Train searchTrainById(int trainId) {
        for (Train train : trains) {
            if (train.getTrainId() == trainId) return train;
        }
        return null;
    }

    @Override
    public Booking searchBookingById(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId) return booking;
        }
        return null;
    }

    // ─── Core operations ─────────────────────────────────────────────────────────
    public boolean createBooking(Booking booking) {
        if (booking == null) {
            System.out.println("Cannot create a null booking.");
            return false;
        }
        if (searchBookingById(booking.getBookingId()) != null) {
            System.out.println("Booking ID already exists: " + booking.getBookingId());
            return false;
        }
        if (booking.getUser() == null || booking.getTrain() == null) {
            System.out.println("Cannot create booking without user and train.");
            return false;
        }
        if (!users.containsValue(booking.getUser()))   addUser(booking.getUser());
        if (!trains.contains(booking.getTrain()))       addTrain(booking.getTrain());
        bookings.add(booking);
        booking.getUser().addBooking(booking);
        booking.getTrain().addBooking(booking);
        System.out.println("Booking " + booking.getBookingId() + " created successfully.");
        return true;
    }

    public boolean processPayment(Payment payment) {
        if (payment == null) {
            System.out.println("Cannot process a null payment.");
            return false;
        }
        boolean paid = payment.pay();
        if (paid) {
            payments.add(payment);
            System.out.println("Payment " + payment.getPaymentId() + " processed successfully.");
            return true;
        }
        System.out.println("Payment " + payment.getPaymentId() + " failed.");
        return false;
    }

    public Ticket issueTicket(Booking booking, Payment payment, String seatNumber) {
        Ticket ticket = Ticket.createTicket(booking, payment, seatNumber);
        if (ticket != null) {
            tickets.add(ticket);
            System.out.println("Ticket " + ticket.getTicketId() + " issued successfully.");
        }
        return ticket;
    }

    // ─── Display helpers ─────────────────────────────────────────────────────────
    public void displayAllUsers() {
        System.out.println("\n========== All Users ==========");
        if (users.isEmpty()) { System.out.println("No users yet."); return; }
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            entry.getValue().displayInfo();
            System.out.println();
        }
    }

    // NEW — display all trains (symmetric to displayAllUsers)
    public void displayAllTrains() {
        System.out.println("\n========== All Trains ==========");
        if (trains.isEmpty()) { System.out.println("No trains yet."); return; }
        for (Train train : trains) {
            train.displayInfo();
            System.out.println();
        }
    }

    public void displayDestinations() {
        System.out.println("\nAvailable Destinations:");
        if (destinations.isEmpty()) { System.out.println("No destinations yet."); return; }
        for (String destination : destinations) {
            System.out.println("- " + destination);
        }
    }

    // ─── displayInfo() override (FIXED — was missing, caused compile error) ───────
    // Required because this class implements Displayable.
    // Without this method the compiler throws:
    //   "TrainTicketBookingSystem must implement the inherited abstract method Displayable.displayInfo()"
    @Override
    public void displayInfo() {
        System.out.println("\n========== System Info ==========");
        System.out.println("System Name : " + systemName);
        System.out.println("Users       : " + users.size());
        System.out.println("Trains      : " + trains.size());
        System.out.println("Bookings    : " + bookings.size());
        System.out.println("Payments    : " + payments.size());
        System.out.println("Tickets     : " + tickets.size());
        System.out.println("=================================");
    }

    // ─── toString() override (NEW) ───────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("TrainTicketBookingSystem{name='%s', users=%d, trains=%d, bookings=%d}",
                systemName, users.size(), trains.size(), bookings.size());
    }

    // ─── Size getters ─────────────────────────────────────────────────────────────
    public int getUserMapSize()      { return users.size(); }
    public int getTrainListSize()    { return trains.size(); }
    public int getBookingListSize()  { return bookings.size(); }
    public int getPaymentListSize()  { return payments.size(); }
    public int getTicketListSize()   { return tickets.size(); }
    public int getDestinationSetSize(){ return destinations.size(); }
}