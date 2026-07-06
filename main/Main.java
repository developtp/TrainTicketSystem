package main;

import enums.PaymentMethod;
import enums.TrainType;
import java.util.List;
import java.util.Scanner;
import model.Booking;
import model.Payment;
import model.Route;
import model.Ticket;
import model.Train;
import model.User;

public class Main {

    // ─── Shared state 
    private static TrainTicketBookingSystem system;
    private static Scanner                  sc;

    // ENTRY POINT
    public static void main(String[] args) {
        system = new TrainTicketBookingSystem("Train Booking");
        sc     = new Scanner(System.in);

        seedData();       // pre-load sample trains
        runMenu();        // start interactive loop
        sc.close();
    }

    // SEED DATA — pre-loaded trains so the menu is useful immediately
    private static void seedData() {
        system.addTrain(new Train("Star Express",
                new Route("Phnom Penh", "Siem Reap"),
                TrainType.EXPRESS, 45));

        system.addTrain(new Train("Kingdom Rail",
                new Route("Phnom Penh", "Battambang"),
                TrainType.REGULAR, 60));

        system.addTrain(new Train("Royal Luxury",
                new Route("Siem Reap", "Sihanoukville"),
                TrainType.LUXURY, 35));

        system.addTrain(new Train("Mekong Express",
                new Route("Phnom Penh", "Kampot"),
                TrainType.EXPRESS, 40));

        system.addTrain(new Train("Local Shuttle",
                new Route("Battambang", "Poipet"),
                TrainType.REGULAR, 50));

        System.out.println("System ready. " + Train.getTrainCount() + " trains loaded.\n");
    }

    // MAIN MENU LOOP
    private static void runMenu() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");
            System.out.println();
            switch (choice) {
                case  1: handleRegisterUser();    break;
                case  2: handleViewAllTrains();   break;
                case  3: handleSearchByRoute();   break;
                case  4: handleFilterTrains();    break;
                case  5: handleSortTrains();      break;
                case  6: handleCreateBooking();   break;
                case  7: handleMakePayment();     break;
                case  8: handleCancelBooking();   break;
                case  9: handleViewMyBookings();  break;
                case 10: handleViewTicket();      break;
                case 11: running = false;
                         System.out.println("Thank you for using CAM Train Booking. Goodbye!");
                         break;
                default: System.out.println("Invalid choice. Please enter 1-11.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println();
        System.out.println("===========================================");
        System.out.println("          CAM TRAIN TICKET SYSTEM         ");
        System.out.println("===========================================");
        System.out.println("  1.  Register User");
        System.out.println("  2.  View All Trains");
        System.out.println("  3.  Search Train By Route");
        System.out.println("  4.  Filter Trains");
        System.out.println("  5.  Sort Trains");
        System.out.println("  6.  Create Booking");
        System.out.println("  7.  Make Payment");
        System.out.println("  8.  Cancel Booking");
        System.out.println("  9.  View My Bookings");
        System.out.println("  10. View Ticket");
        System.out.println("  11. Exit");
        System.out.println("===========================================");
    }

    // HANDLER 1 — Register User
    private static void handleRegisterUser() {
        System.out.println("--- Register New User ---");
        System.out.print("Full Name   : "); String name   = sc.nextLine().trim();
        System.out.print("Age         : "); int    age    = readInt("");
        System.out.print("Gender      : "); String gender = sc.nextLine().trim();
        System.out.print("Phone       : "); String phone  = sc.nextLine().trim();

        if (name.isEmpty()) { System.out.println("Name cannot be empty."); return; }

        User user = new User(name, age, gender, phone);
        boolean added = system.addUser(user);
        if (added) {
            System.out.println("User registered successfully!");
            System.out.println("User ID: " + user.getUserId() + "  |  Name: " + user.getName());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // HANDLER 2 — View All Trains
    // ═══════════════════════════════════════════════════════════════════════════════
    private static void handleViewAllTrains() {
        System.out.println("--- All Available Trains ---");
        system.displayAllTrains();
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // HANDLER 3 — Search Train By Route
    // ═══════════════════════════════════════════════════════════════════════════════
    private static void handleSearchByRoute() {
        System.out.println("--- Search Train By Route ---");
        System.out.print("Departure station   : "); String dep = sc.nextLine().trim();
        System.out.print("Destination station : "); String dst = sc.nextLine().trim();

        if (dep.isEmpty() || dst.isEmpty()) {
            System.out.println("Both departure and destination are required.");
            return;
        }

        List<Train> results = system.searchTrainByRoute(dep, dst);
        if (results.isEmpty()) {
            System.out.println("No trains found for route: " + dep + " -> " + dst);
        } else {
            System.out.println(results.size() + " train(s) found:");
            system.displayTrainList(results);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // HANDLER 4 — Filter Trains
    // ═══════════════════════════════════════════════════════════════════════════════
    private static void handleFilterTrains() {
        System.out.println("--- Filter Trains By Type ---");
        TrainType type = selectTrainType();
        if (type == null) return;
        List<Train> results = system.filterTrainsByType(type);
        System.out.println("\n" + type.getLabel() + " trains (" + results.size() + " found):");
        system.displayTrainList(results);
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // HANDLER 5 — Sort Trains
    // ═══════════════════════════════════════════════════════════════════════════════
    private static void handleSortTrains() {
        System.out.println("--- Sort Trains ---");
        System.out.println("Sort by:");
        System.out.println("  1. Price (ascending)");
        System.out.println("  2. Available Seats (most first)");
        System.out.println("  3. Train Type  (REGULAR → EXPRESS → LUXURY)");
        int choice = readInt("Choice: ");

        List<Train> sorted;
        String label;
        switch (choice) {
            case 1: sorted = system.sortTrainsByPrice();         label = "Price (asc)";          break;
            case 2: sorted = system.sortTrainsByAvailableSeats();label = "Available Seats (desc)";break;
            case 3: sorted = system.sortTrainsByTrainType();     label = "Train Type";            break;
            default: System.out.println("Invalid sort choice."); return;
        }
        System.out.println("\nTrains sorted by " + label + ":");
        system.displayTrainList(sorted);
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // HANDLER 6 — Create Booking
    // ═══════════════════════════════════════════════════════════════════════════════
    private static void handleCreateBooking() {
        System.out.println("--- Create New Booking ---");

        // Step 1 — select user
        User user = selectUser();
        if (user == null) return;

        // Step 2 — select train
        system.displayAllTrains();
        int trainId = readInt("Enter Train ID: ");
        Train train = system.searchTrainById(trainId);
        if (train == null) { System.out.println("Train not found."); return; }

        if (!train.hasAvailableSeat()) {
            System.out.println("No available seats on this train.");
            return;
        }

        // Step 3 — choose seat
        System.out.println("\nSeat Selection:");
        System.out.println("  Available: " + train.getAvailableSeats() + "/" + train.getCapacity());
        System.out.println("  Enter '0' to auto-assign a seat.");
        System.out.print("Seat number : ");
        String seatInput = sc.nextLine().trim();

        String seatNumber;
        if (seatInput.equals("0") || seatInput.isEmpty()) {
            // Auto-assign
            seatNumber = train.reserveSeat();
            if (seatNumber == null) {
                System.out.println("Auto-assignment failed. Train may be full.");
                return;
            }
            System.out.println("Auto-assigned seat: " + seatNumber);
        } else {
            // Manual choice — validate and reserve
            seatNumber = seatInput.toUpperCase();
            boolean reserved = train.reserveSeat(seatNumber);
            if (!reserved) {
                System.out.println("Could not reserve seat '" + seatNumber + "'. Please try again.");
                return;
            }
        }

        // Step 4 — create booking (price auto-calculated inside constructor)
        Booking booking = new Booking(user, train, seatNumber);
        system.createBooking(booking);

        System.out.println("\n--- Booking Summary ---");
        booking.displayInfo();
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // HANDLER 7 — Make Payment
    // ═══════════════════════════════════════════════════════════════════════════════
    private static void handleMakePayment() {
        System.out.println("--- Make Payment ---");

        int bookingId = readInt("Enter Booking ID to pay: ");
        Booking booking = system.searchBookingById(bookingId);

        if (booking == null) {
            System.out.println("Booking #" + bookingId + " not found.");
            return;
        }
        if (booking.isCancelled()) {
            System.out.println("Booking #" + bookingId + " is cancelled. Cannot pay.");
            return;
        }
        if (booking.isConfirmed()) {
            System.out.println("Booking #" + bookingId + " is already confirmed and paid.");
            return;
        }

        System.out.println("\nBooking found:");
        booking.displayInfo();

        // Select payment method
        System.out.println("\nSelect Payment Method:");
        System.out.println("  1. Cash");
        System.out.println("  2. ABA Bank");
        System.out.println("  3. Wing");
        System.out.println("  4. KHQR");
        int methodChoice = readInt("Choice: ");

        PaymentMethod method;
        switch (methodChoice) {
            case 1:  method = PaymentMethod.CASH;  break;
            case 2:  method = PaymentMethod.ABA;   break;
            case 3:  method = PaymentMethod.WING;  break;
            case 4:  method = PaymentMethod.KHQR;  break;
            default: System.out.println("Invalid choice. Defaulting to Cash.");
                     method = PaymentMethod.CASH;
        }

        Payment payment = new Payment(booking, method);
        boolean success = system.processPayment(payment);

        if (success) {
            System.out.println("\nPayment & Ticket summary:");
            payment.print();
            // Show ticket
            Ticket ticket = system.getTicketForBooking(bookingId);
            if (ticket != null) ticket.print();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // HANDLER 8 — Cancel Booking
    // ═══════════════════════════════════════════════════════════════════════════════
    private static void handleCancelBooking() {
        System.out.println("--- Cancel Booking ---");
        int bookingId = readInt("Enter Booking ID to cancel: ");
        Booking booking = system.searchBookingById(bookingId);

        if (booking == null) {
            System.out.println("Booking #" + bookingId + " not found.");
            return;
        }

        System.out.println("\nBooking to cancel:");
        booking.displayInfo();

        System.out.print("\nAre you sure you want to cancel? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (!confirm.equals("yes")) {
            System.out.println("Cancellation aborted.");
            return;
        }

        system.cancelBooking(bookingId);
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // HANDLER 9 — View My Bookings
    // ═══════════════════════════════════════════════════════════════════════════════
    private static void handleViewMyBookings() {
        System.out.println("--- View My Bookings ---");
        User user = selectUser();
        if (user == null) return;

        // Sorted earliest-to-latest by travel date (Booking's Comparable).
        user.displayBookingHistory();
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // HANDLER 10 — View Ticket
    // ═══════════════════════════════════════════════════════════════════════════════
    private static void handleViewTicket() {
        System.out.println("--- View Ticket ---");
        int bookingId = readInt("Enter Booking ID: ");

        Ticket ticket = system.getTicketForBooking(bookingId);
        if (ticket == null) {
            System.out.println("No ticket found for Booking #" + bookingId
                    + ". Make sure payment has been completed.");
            return;
        }
        ticket.print();
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // SHARED UI HELPERS
    // ═══════════════════════════════════════════════════════════════════════════════

    /** Prompts the user to enter a User ID or name and returns the found User. */
    private static User selectUser() {
        List<User> allUsers = system.getAllUsers();
        if (allUsers.isEmpty()) {
            System.out.println("No users registered. Please register a user first (option 1).");
            return null;
        }
        System.out.println("Registered users:");
        for (User u : allUsers) {
            System.out.println("  [" + u.getUserId() + "] " + u.getName());
        }
        int userId = readInt("Enter User ID: ");
        User user = system.searchUserById(userId);
        if (user == null) System.out.println("User ID " + userId + " not found.");
        return user;
    }

    /** Prompts the user to select a TrainType from a numbered list. */
    private static TrainType selectTrainType() {
        System.out.println("Train types:");
        System.out.println("  1. Regular");
        System.out.println("  2. Express");
        System.out.println("  3. Luxury");
        int choice = readInt("Choice: ");
        switch (choice) {
            case 1: return TrainType.REGULAR;
            case 2: return TrainType.EXPRESS;
            case 3: return TrainType.LUXURY;
            default: System.out.println("Invalid choice."); return null;
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            if (!prompt.isEmpty()) System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}