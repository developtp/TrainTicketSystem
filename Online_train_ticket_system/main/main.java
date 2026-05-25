package main;

import interfaces.Displayable;
import interfaces.Payable;
import interfaces.Printable;
import java.util.ArrayList;
import model.*;

public class Main {
    public static void main(String[] args) {
        TrainTicketBookingSystem system = new TrainTicketBookingSystem("CAM Train Booking");

        User user1 = new User("Dara", 22, "Male", "012345678");
        User user2 = new User("Sokha", 21, "Female", "098765432");
        Staff staff1 = new Staff("Bopha", 30, "Female", "011111111", "Ticket Officer");

        system.addUser(user1);
        system.addUser(user2);

        Train train1 = new Train("Express A", "Phnom Penh", "Battambang", 3, 12.50);
        Train train2 = new Train("Express B", "Phnom Penh", "Sihanoukville", 2, 10.00);
        system.addTrain(train1);
        system.addTrain(train2);

        System.out.println("\n=== INHERITANCE TEST: Person -> User and Staff ===");
        ArrayList<Person> people = new ArrayList<>();
        people.add(user1);
        people.add(user2);
        people.add(staff1);
        for (Person person : people) {
            person.displayInfo();
            System.out.println("---");
        }

        System.out.println("\n=== BOOKING, PAYMENT, AND TICKET FLOW ===");
        User foundUser = system.searchUserById(user1.getUserId());
        Train foundTrain = system.searchTrainById(train1.getTrainId());

        if (foundUser != null && foundTrain != null) {
            Booking booking1 = new Booking(foundUser, foundTrain, "2026-06-10");
            system.createBooking(booking1);

            Payment payment1 = new Payment(booking1, "ABA");
            Payable payable = payment1;
            system.processPayment(payment1);
            System.out.println("Payable interface isPaid(): " + payable.isPaid());

            Ticket ticket1 = system.issueTicket(booking1, payment1, "A1");

            System.out.println("\n=== DISPLAYABLE INTERFACE TEST ===");
            ArrayList<Displayable> displayables = new ArrayList<>();
            displayables.add(system);
            displayables.add(user1);
            displayables.add(staff1);
            displayables.add(train1);
            displayables.add(booking1);
            displayables.add(payment1);
            if (ticket1 != null) {
                displayables.add(ticket1);
            }
            for (Displayable item : displayables) {
                item.displayInfo();
                System.out.println("---");
            }

            System.out.println("\n=== PRINTABLE INTERFACE TEST ===");
            ArrayList<Printable> printables = new ArrayList<>();
            printables.add(payment1);
            if (ticket1 != null) {
                printables.add(ticket1);
            }
            for (Printable printable : printables) {
                printable.print();
            }
        }

        System.out.println("\n=== INVALID SEARCH TEST ===");
        User wrongUser = system.searchUserById(99);
        if (wrongUser == null) {
            System.out.println("User ID 99 does not exist.");
        }

        System.out.println("\n=== DESTINATION HASHSET TEST ===");
        system.displayDestinations();
    }
}
