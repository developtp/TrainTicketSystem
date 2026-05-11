package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import model.*;

public class Main {

    public static void main(String[] args) {

        // ===== USERS =====
        User user1 = new User("Alice", 30, "Female", "1234567890");
        User user2 = new User("Bob", 25, "Male", "0987654321");
        User user3 = new User("Charlie", 40, "Male", "1122334455");

        // ===== TRAINS =====
        Train train1 = new Train("Express 101", "Phnom Penh", "Siem Reap", 100, 15.50);
        Train train2 = new Train("Night Rider", "Phnom Penh", "Battambang", 80, 12.00);
        Train train3 = new Train("City Link", "Siem Reap", "Kampot", 60, 9.75);

        // ===== DISPLAY TRAINS (Displayable) =====
        System.out.println("\n=== TRAINS ===");
        // displayInfo() shows raw train data including available seats
        train1.displayInfo();
        System.out.println("---");
        train2.displayInfo();
        System.out.println("---");
        train3.displayInfo();

        // ===== BOOKINGS =====
        Booking booking1 = new Booking(user1, train1, "2026-05-10", "Pending");
        Booking booking2 = new Booking(user2, train2, "2026-05-12", "Pending");
        Booking booking3 = new Booking(user3, train3, "2026-05-15", "Pending");

        // ===== DISPLAY BOOKINGS (Displayable) =====
        System.out.println("\n=== BOOKINGS (Before Payment) ===");
        // displayInfo() is called on Booking objects to show current booking state
        booking1.displayInfo();
        System.out.println("---");
        booking2.displayInfo();
        System.out.println("---");
        booking3.displayInfo();

        // ===== PAYMENTS =====
        // totalPrice is now derived automatically from booking -> train -> ticketPrice.
        // No manual amount is passed, so there is no risk of the payment amount
        // being different from the actual train ticket price.
        Payment payment1 = new Payment(booking1, LocalDate.now(), "Credit Card");
        Payment payment3 = new Payment(booking3, LocalDate.now(), "Online Transfer");

        // processPayment() is called from the Payable interface.
        // It marks the payment as Paid and sets the booking status to Confirmed.
        System.out.println("\n=== PROCESSING PAYMENTS (Payable) ===");
        payment1.processPayment();
        payment3.processPayment();
        // booking2 has no payment -> stays Pending -> cannot get a ticket

        // ===== DISPLAY PAYMENTS (Displayable) =====
        System.out.println("\n=== PAYMENTS - displayInfo() (Displayable) ===");
        // displayInfo() shows payment data in a plain format for internal use
        payment1.displayInfo();
        System.out.println("---");
        payment3.displayInfo();

        // ===== PRINT PAYMENT RECEIPTS (Printable) =====
        System.out.println("\n=== PAYMENT RECEIPTS - print() (Printable) ===");
        // print() produces a formal receipt layout — different from displayInfo().
        // displayInfo() = show object data; print() = format for the passenger/customer.
        payment1.print();
        System.out.println("---");
        payment3.print();

        // ===== TICKETS =====
        // Ticket.createTicket() checks that the booking is Confirmed before issuing.
        // It also calls reserveSeat() on the train to track seat usage.
        Ticket ticket1 = Ticket.createTicket(booking1, "A1"); // booking1 is Confirmed -> success
        Ticket ticket2 = Ticket.createTicket(booking2, "B2"); // booking2 is Pending  -> fails
        Ticket ticket3 = Ticket.createTicket(booking3, "C5"); // booking3 is Confirmed -> success

        // ===== PRINT TICKETS (Printable) =====
        System.out.println("\n=== TICKETS - print() (Printable) ===");
        // print() is the formal boarding-pass format for the passenger.
        // displayInfo() shows the same data without the header/footer decoration.
        ArrayList<Ticket> ticketList = new ArrayList<>();
        addTicketIfValid(ticketList, ticket1);
        addTicketIfValid(ticketList, ticket2); // null, will be skipped
        addTicketIfValid(ticketList, ticket3);

        for (Ticket ticket : ticketList) {
            ticket.print();
            System.out.println("---");
        }

        // ===== COLLECTIONS =====
        ArrayList<Train> trainList = new ArrayList<>();
        trainList.add(train1);
        trainList.add(train2);
        trainList.add(train3);

        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking1);
        bookingList.add(booking2);
        bookingList.add(booking3);

        ArrayList<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment1);
        paymentList.add(payment3);

        HashSet<String> destinations = new HashSet<>();
        destinations.add(train1.getDestination());
        destinations.add(train2.getDestination());
        destinations.add(train3.getDestination());

        HashMap<Integer, User> userMap = new HashMap<>();
        userMap.put(user1.getUserId(), user1);
        userMap.put(user2.getUserId(), user2);
        userMap.put(user3.getUserId(), user3);

        // ===== UNIQUE DESTINATIONS =====
        System.out.println("\n=== UNIQUE DESTINATIONS ===");
        for (String destination : destinations) {
            System.out.println("- " + destination);
        }

        // ===== USER LOOKUP =====
        System.out.println("\n=== USER LOOKUP BY ID ===");
        for (int id : userMap.keySet()) {
            System.out.println("ID " + id + " -> " + userMap.get(id).getName());
        }

        // ===== SEAT AVAILABILITY AFTER BOOKING =====
        System.out.println("\n=== SEAT AVAILABILITY AFTER TICKETING ===");
        for (Train train : trainList) {
            System.out.println(train.getTrainName() + " : "
                + train.getAvailableSeats() + " / " + train.getTotalSeats() + " seats available");
        }

        // ===== SYSTEM SUMMARY =====
        System.out.println("\n=== SYSTEM SUMMARY ===");
        System.out.println("Total Users    : " + User.getUserCount());
        System.out.println("Total Trains   : " + Train.getTrainCount());
        System.out.println("Total Bookings : " + Booking.getBookingCount());
        System.out.println("Total Payments : " + paymentList.size());
        System.out.println("Total Tickets  : " + Ticket.getTicketCount());
    }

    private static void addTicketIfValid(ArrayList<Ticket> list, Ticket ticket) {
        if (ticket != null) {
            list.add(ticket);
        }
    }
}
