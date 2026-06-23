package main;

import enums.BookingStatus;
import enums.PaymentMethod;
import enums.TicketClass;
import enums.TrainType;
import exceptions.TicketIssuanceException;
import interfaces.BookingSearchable;
import interfaces.Displayable;
import interfaces.TrainSearchable;
import interfaces.UserSearchable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import model.Booking;
import model.Payment;
import model.Ticket;
import model.Train;
import model.User;

/**
 * Central service layer for the Train Ticket System.
 *
 * Responsibilities:
 *   - Manages collections of Users, Trains, Bookings, Payments, Tickets
 *   - Implements all search, filter, sort, booking lifecycle, and payment flows
 *   - Keeps business logic out of model classes
 *
 * OOP concepts demonstrated here:
 *   - Interface      : implements Displayable, UserSearchable, TrainSearchable, BookingSearchable
 *   - Encapsulation  : all collections are private
 *   - Static         : Train.getTrainCount(), User.getUserCount() (static counters)
 *   - Polymorphism   : search methods return polymorphic results
 */
public class TrainTicketBookingSystem implements Displayable, UserSearchable, TrainSearchable, BookingSearchable {

    private String              systemName;
    private HashMap<Integer, User> users;
    private ArrayList<Train>    trains;
    private ArrayList<Booking>  bookings;
    private ArrayList<Payment>  payments;
    private ArrayList<Ticket>   tickets;
    private HashSet<String>     destinations;

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

    // ═══════════════════════════════════════════════════════════════════════════════
    // ADD METHODS
    // ═══════════════════════════════════════════════════════════════════════════════

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

    // ═══════════════════════════════════════════════════════════════════════════════
    // SEARCH METHODS  (interface overrides)
    // ═══════════════════════════════════════════════════════════════════════════════

    /** OVERRIDE — UserSearchable */
    @Override
    public User searchUserById(int userId) {
        return users.get(userId);
    }

    /** OVERLOAD — search by name (case-insensitive) */
    public User searchUserByName(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        for (User user : users.values()) {
            if (user.getName().equalsIgnoreCase(name.trim())) return user;
        }
        return null;
    }

    /** OVERRIDE — TrainSearchable */
    @Override
    public Train searchTrainById(int trainId) {
        for (Train train : trains) {
            if (train.getTrainId() == trainId) return train;
        }
        return null;
    }

    /** Search trains by departure and destination (case-insensitive, partial match). */
    public List<Train> searchTrainByRoute(String departure, String destination) {
        List<Train> result = new ArrayList<>();
        if (departure == null || destination == null) return result;
        String dep = departure.trim().toLowerCase();
        String dst = destination.trim().toLowerCase();
        for (Train train : trains) {
            boolean depMatch = train.getSource().toLowerCase().contains(dep);
            boolean dstMatch = train.getDestination().toLowerCase().contains(dst);
            if (depMatch && dstMatch) result.add(train);
        }
        return result;
    }

    /** OVERRIDE — BookingSearchable */
    @Override
    public Booking searchBookingById(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId) return booking;
        }
        return null;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // FILTER METHODS
    // ═══════════════════════════════════════════════════════════════════════════════

    /** Filter trains by TrainType. */
    public List<Train> filterTrainsByType(TrainType type) {
        List<Train> result = new ArrayList<>();
        if (type == null) return result;
        for (Train train : trains) {
            if (train.getTrainType() == type) result.add(train);
        }
        return result;
    }

    /** Filter trains that still have at least one available seat in the given class. */
    public List<Train> filterTrainsByClassAvailability(TicketClass ticketClass) {
        List<Train> result = new ArrayList<>();
        if (ticketClass == null) return result;
        for (Train train : trains) {
            if (train.hasAvailableSeat(ticketClass)) result.add(train);
        }
        return result;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // SORT METHODS  — use Comparator + Collections.sort()
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * Returns a copy of the train list sorted by Economy base price (ascending).
     * Demonstrates: Comparator, Collections.sort()
     */
    public List<Train> sortTrainsByPrice() {
        List<Train> sorted = new ArrayList<>(trains);
        Collections.sort(sorted, new Comparator<Train>() {
            @Override
            public int compare(Train a, Train b) {
                return Double.compare(a.getTicketPrice(), b.getTicketPrice());
            }
        });
        return sorted;
    }

    /**
     * Returns a copy sorted by total available seats (most available first).
     */
    public List<Train> sortTrainsByAvailableSeats() {
        List<Train> sorted = new ArrayList<>(trains);
        Collections.sort(sorted, new Comparator<Train>() {
            @Override
            public int compare(Train a, Train b) {
                int seatsA = a.getTotalSeats() - a.getReservedSeatCount();
                int seatsB = b.getTotalSeats() - b.getReservedSeatCount();
                return Integer.compare(seatsB, seatsA); // descending
            }
        });
        return sorted;
    }

    /**
     * Returns a copy sorted by TrainType ordinal (REGULAR → EXPRESS → LUXURY).
     */
    public List<Train> sortTrainsByTrainType() {
        List<Train> sorted = new ArrayList<>(trains);
        Collections.sort(sorted, new Comparator<Train>() {
            @Override
            public int compare(Train a, Train b) {
                return Integer.compare(a.getTrainType().ordinal(), b.getTrainType().ordinal());
            }
        });
        return sorted;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // BOOKING LIFECYCLE
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * Registers a booking and links it to the user and train.
     * The booking's seat must already be reserved on the train before calling this.
     */
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
        if (!users.containsValue(booking.getUser()))  addUser(booking.getUser());
        if (!trains.contains(booking.getTrain()))      addTrain(booking.getTrain());
        bookings.add(booking);
        booking.getUser().addBooking(booking);
        booking.getTrain().addBooking(booking);
        System.out.println("Booking #" + booking.getBookingId() + " created successfully (PENDING).");
        return true;
    }

    /**
     * Cancels a booking by ID.
     * Works from PENDING or CONFIRMED state.
     * The booking's cancelBooking() method releases the seat automatically.
     */
    public boolean cancelBooking(int bookingId) {
        Booking booking = searchBookingById(bookingId);
        if (booking == null) {
            System.out.println("Booking #" + bookingId + " not found.");
            return false;
        }
        if (booking.isCancelled()) {
            System.out.println("Booking #" + bookingId + " is already cancelled.");
            return false;
        }
        boolean cancelled = booking.cancelBooking();
        if (cancelled) {
            System.out.println("Booking #" + bookingId + " has been cancelled. Seat released.");
        }
        return cancelled;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // PAYMENT  — centralized flow: pay → confirm booking → auto-issue ticket
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * Processes payment for a booking.
     * On success:
     *   1. Booking status → CONFIRMED
     *   2. Ticket is automatically generated and registered
     */
    public boolean processPayment(Payment payment) {
        if (payment == null) {
            System.out.println("Cannot process a null payment.");
            return false;
        }
        boolean paid = payment.pay();
        if (!paid) {
            System.out.println("Payment #" + payment.getPaymentId() + " failed.");
            return false;
        }
        payments.add(payment);
        System.out.println("Payment #" + payment.getPaymentId() + " processed successfully.");

        // Auto-issue ticket
        Booking booking = payment.getBooking();
        if (booking != null) {
            try {
                Ticket ticket = issueTicket(booking, payment, booking.getSeatNumber());
                System.out.println("Ticket #" + ticket.getTicketId() + " issued automatically.");
            } catch (TicketIssuanceException e) {
                System.out.println("Warning: payment succeeded but ticket issuance failed: " + e.getMessage());
            }
        }
        return true;
    }

    /**
     * Issues a ticket after validating all preconditions.
     * Throws TicketIssuanceException on any validation failure.
     * Demonstrates: Exception Handling
     */
    public Ticket issueTicket(Booking booking, Payment payment, String seatNumber)
            throws TicketIssuanceException {
        if (booking == null)
            throw new TicketIssuanceException("Ticket issuance failed: booking cannot be null.");
        if (payment == null)
            throw new TicketIssuanceException("Ticket issuance failed: payment cannot be null.");
        if (!payment.isPaid())
            throw new TicketIssuanceException("Ticket issuance failed: payment #"
                    + payment.getPaymentId() + " is not completed.");
        if (payment.getBooking() != booking)
            throw new TicketIssuanceException("Ticket issuance failed: payment does not belong to this booking.");
        if (!booking.isConfirmed())
            throw new TicketIssuanceException("Ticket issuance failed: booking #"
                    + booking.getBookingId() + " is not confirmed.");
        if (booking.getTrain() == null)
            throw new TicketIssuanceException("Ticket issuance failed: booking has no associated train.");

        Ticket ticket = Ticket.createTicket(booking, payment, seatNumber);
        if (ticket == null)
            throw new TicketIssuanceException("Ticket issuance failed: internal ticket creation error.");
        tickets.add(ticket);
        return ticket;
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // VIEW HELPERS
    // ═══════════════════════════════════════════════════════════════════════════════

    public void displayAllUsers() {
        System.out.println("\n========== All Users ==========");
        if (users.isEmpty()) { System.out.println("No users registered."); return; }
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            entry.getValue().displayInfo();
            System.out.println();
        }
    }

    public void displayAllTrains() {
        System.out.println("\n========== All Trains ==========");
        if (trains.isEmpty()) { System.out.println("No trains available."); return; }
        for (Train train : trains) {
            train.displayInfo();
            System.out.println();
        }
    }

    public void displayTrainList(List<Train> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("  No trains found.");
            return;
        }
        for (Train train : list) {
            train.displayInfo();
            System.out.println();
        }
    }

    public void displayAllBookings() {
        System.out.println("\n========== All Bookings ==========");
        if (bookings.isEmpty()) { System.out.println("No bookings yet."); return; }
        for (Booking booking : bookings) {
            booking.displayInfo();
        }
    }

    /** Returns all bookings for a given user (including cancelled). */
    public List<Booking> getBookingsForUser(int userId) {
        List<Booking> result = new ArrayList<>();
        User user = searchUserById(userId);
        if (user == null) return result;
        return user.getBookingsCopy();
    }

    /** Returns the most recent ticket for a booking ID. */
    public Ticket getTicketForBooking(int bookingId) {
        for (int i = tickets.size() - 1; i >= 0; i--) {
            Ticket t = tickets.get(i);
            if (t.getBooking() != null && t.getBooking().getBookingId() == bookingId) {
                return t;
            }
        }
        return null;
    }

    public void displayDestinations() {
        System.out.println("\nAvailable Destinations:");
        if (destinations.isEmpty()) { System.out.println("No destinations yet."); return; }
        for (String dest : destinations) System.out.println("  - " + dest);
    }

    /** Returns a copy of all trains for menu display. */
    public List<Train> getAllTrains() { return new ArrayList<>(trains); }

    /** Returns a copy of all users (as list) for menu display. */
    public List<User> getAllUsers() { return new ArrayList<>(users.values()); }

    /** Returns a copy of all tickets. */
    public List<Ticket> getAllTickets() { return new ArrayList<>(tickets); }

    // ═══════════════════════════════════════════════════════════════════════════════
    // Displayable interface — OVERRIDE
    // ═══════════════════════════════════════════════════════════════════════════════
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

    @Override
    public String toString() {
        return String.format(
            "TrainTicketBookingSystem{name='%s', users=%d, trains=%d, bookings=%d}",
            systemName, users.size(), trains.size(), bookings.size());
    }

    // ─── Size getters ─────────────────────────────────────────────────────────────
    public int getUserMapSize()       { return users.size(); }
    public int getTrainListSize()     { return trains.size(); }
    public int getBookingListSize()   { return bookings.size(); }
    public int getPaymentListSize()   { return payments.size(); }
    public int getTicketListSize()    { return tickets.size(); }
    public int getDestinationSetSize(){ return destinations.size(); }
}