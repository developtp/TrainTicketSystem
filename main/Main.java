package main;

import interfaces.Displayable;
import interfaces.Payable;
import interfaces.Printable;
import java.time.LocalDate;
import java.util.ArrayList;
import model.*;

public class Main {
    public static void main(String[] args) {

        TrainTicketBookingSystem system = new TrainTicketBookingSystem("CAM Train Booking");

        // ── Create people ───────────────────────────────────────────────────────
        User  user1  = new User("Dara",  22, "Male",   "012345678");
        User  user2  = new User("Sokha", 21, "Female", "098765432");
        Staff staff1 = new Staff("Bopha", 30, "Female", "011111111", "Ticket Officer");

        System.out.println("\n=== TEST: OVERRIDING getRoleDescription() ===");
        System.out.println(user1.getName() + " -> " + user1.getRoleDescription());
        System.out.println(user2.getName() + " -> " + user2.getRoleDescription());
        System.out.println(staff1.getName() + " -> " + staff1.getRoleDescription());

        system.addUser(user1);
        system.addUser(user2);   

        // ── Create trains ────────────────────────────────────────────────────────
        Train train1 = new Train("Express A", "Phnom Penh", "Battambang",    3, 12.50);
        Train train2 = new Train("Express B", "Phnom Penh", "Sihanoukville", 2, 10.00);
        system.addTrain(train1);
        system.addTrain(train2);

        // TEST 1 — toString() on all model objects (NEW)
        // Shows the one-liner output instead of ugly memory addresses
        System.out.println("\n=== TEST: toString() ON ALL OBJECTS ===");
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(staff1);
        System.out.println(train1);
        System.out.println(train2);
        System.out.println(system);

        // TEST 2 — OVERLOADED setPhoneNumber() (NEW)
        // Person.setPhoneNumber(String countryCode, String localNumber)
        System.out.println("\n=== TEST: OVERLOADED setPhoneNumber() ===");
        user1.setPhoneNumber("+855", "12345678"); // country code + local number
        System.out.println("Updated phone for " + user1.getName() + ": " + user1.getPhoneNumber());
        user1.setPhoneNumber("012345678");        // back to original single-string version
        System.out.println("Restored phone for " + user1.getName() + ": " + user1.getPhoneNumber());

        // ════════════════════════════════════════════════════════════════════════
        // TEST 3 — OVERLOADED Booking constructor: LocalDate version (NEW)
        // ════════════════════════════════════════════════════════════════════════
        System.out.println("\n=== TEST: OVERLOADED Booking constructor (LocalDate) ===");
        Booking bookingWithDate = new Booking(user2, train2, LocalDate.of(2026, 8, 20));
        System.out.println("Created booking with LocalDate: " + bookingWithDate);

        // ════════════════════════════════════════════════════════════════════════
        // TEST 4 — OVERLOADED Payment constructor: custom amount (NEW)
        // ════════════════════════════════════════════════════════════════════════
        System.out.println("\n=== TEST: OVERLOADED Payment constructor (custom amount) ===");
        Payment discountedPayment = new Payment(bookingWithDate, "Wing", 7.00);
        System.out.println("Discounted payment: " + discountedPayment);

        // ════════════════════════════════════════════════════════════════════════
        // TEST 5 — equals() and hashCode() (NEW)
        // ════════════════════════════════════════════════════════════════════════
        System.out.println("\n=== TEST: equals() and hashCode() ===");
        User sameUser = user1;  // same reference
        User copyUser = new User("Dara", 22, "Male", "012345678"); // different object, different ID
        System.out.println("user1 == user1 (same ref)   : " + user1.equals(sameUser));  // true
        System.out.println("user1 == copyUser (diff ID) : " + user1.equals(copyUser));  // false — different userId

        // ════════════════════════════════════════════════════════════════════════
        // TEST 6 — INHERITANCE: displayInfo() polymorphism (EXISTING, still works)
        // ════════════════════════════════════════════════════════════════════════
        System.out.println("\n=== TEST: INHERITANCE — Person -> User / Staff displayInfo() ===");
        ArrayList<Person> people = new ArrayList<>();
        people.add(user1);
        people.add(user2);
        people.add(staff1);
        for (Person person : people) {
            person.displayInfo(); // calls User.displayInfo() or Staff.displayInfo() at runtime
            System.out.println("---");
        }

        // ════════════════════════════════════════════════════════════════════════
        // TEST 7 — Full booking / payment / ticket flow (EXISTING)
        // ════════════════════════════════════════════════════════════════════════
        System.out.println("\n=== TEST: BOOKING, PAYMENT, AND TICKET FLOW ===");
        User  foundUser  = system.searchUserById(user1.getUserId());
        Train foundTrain = system.searchTrainById(train1.getTrainId());

        if (foundUser != null && foundTrain != null) {
            Booking booking1 = new Booking(foundUser, foundTrain, "2026-06-10"); // String version
            system.createBooking(booking1);

            Payment payment1 = new Payment(booking1, "ABA");
            Payable payable  = payment1;
            system.processPayment(payment1);
            System.out.println("isPaid(): " + payable.isPaid());

            Ticket ticket1 = system.issueTicket(booking1, payment1, "A1");
            System.out.println("Ticket toString: " + ticket1); // NEW — uses our toString()

            // ── OVERLOADED displayBookingHistory() tests (NEW) ──────────────────
            System.out.println("\n=== TEST: OVERLOADED displayBookingHistory() ===");

            // VERSION 1 — all bookings
            foundUser.displayBookingHistory();

            // VERSION 2 — filter by status
            foundUser.displayBookingHistory("Confirmed");
            foundUser.displayBookingHistory("Pending");   // should show none

            // VERSION 3 — last N bookings
            foundUser.displayBookingHistory(1);

            // ── OVERLOADED reserveSeat() auto-assign (NEW) ─────────────────────
            System.out.println("\n=== TEST: OVERLOADED reserveSeat() auto-assign ===");
            String autoSeat = train1.reserveSeat(); // no argument — system picks seat
            System.out.println("Auto-assigned seat on " + train1.getTrainName() + ": " + autoSeat);

            // ── Displayable interface test ──────────────────────────────────────
            System.out.println("\n=== TEST: DISPLAYABLE INTERFACE ===");
            ArrayList<Displayable> displayables = new ArrayList<>();
            displayables.add(system);
            displayables.add(user1);
            displayables.add(staff1);
            displayables.add(train1);
            displayables.add(booking1);
            displayables.add(payment1);
            if (ticket1 != null) displayables.add(ticket1);
            for (Displayable item : displayables) {
                item.displayInfo();
                System.out.println("---");
            }

            // ── Printable interface test ────────────────────────────────────────
            System.out.println("\n=== TEST: PRINTABLE INTERFACE ===");
            ArrayList<Printable> printables = new ArrayList<>();
            printables.add(payment1);
            if (ticket1 != null) printables.add(ticket1);
            for (Printable printable : printables) {
                printable.print();
            }
        }

        // ════════════════════════════════════════════════════════════════════════
        // TEST 8 — searchUserByName() — new overload on the system (NEW)
        // ════════════════════════════════════════════════════════════════════════
        System.out.println("\n=== TEST: searchUserByName() OVERLOAD ===");
        User foundByName = system.searchUserByName("sokha");
        System.out.println("Search 'sokha' → " + (foundByName != null ? foundByName.toString() : "Not found"));
        User notFound = system.searchUserByName("nobody");
        System.out.println("Search 'nobody' → " + (notFound != null ? notFound.toString() : "Not found"));

        // ════════════════════════════════════════════════════════════════════════
        // TEST 9 — displayAllTrains() (NEW)
        // ════════════════════════════════════════════════════════════════════════
        System.out.println("\n=== TEST: displayAllTrains() ===");
        system.displayAllTrains();

        // ════════════════════════════════════════════════════════════════════════
        // TEST 10 — Destination HashSet (EXISTING)
        // ════════════════════════════════════════════════════════════════════════
        System.out.println("\n=== TEST: DESTINATION HASHSET ===");
        system.displayDestinations();

        // ════════════════════════════════════════════════════════════════════════
        // TEST 11 — Invalid search (EXISTING)
        // ════════════════════════════════════════════════════════════════════════
        System.out.println("\n=== TEST: INVALID SEARCH ===");
        User wrongUser = system.searchUserById(99);
        System.out.println("Search ID 99 → " + (wrongUser == null ? "Not found (correct)" : wrongUser));
    }
}