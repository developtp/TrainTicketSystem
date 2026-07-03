package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class User extends Person {

    private int userId;
    private ArrayList<Booking> bookings;

    private static int userCount  = 0;
    private static int nextUserId = 1;

    public User(String name, int age, String gender, String phoneNumber) {
        super(name, age, gender, phoneNumber);
        this.userId   = nextUserId++;
        this.bookings = new ArrayList<>();
        userCount++;
    }

    public int getUserId() { return userId; }

    public ArrayList<Booking> getBookingsCopy() {
        return new ArrayList<>(bookings);
    }

    public void addBooking(Booking booking) {
        if (booking != null && !bookings.contains(booking)) {
            bookings.add(booking);
        }
    }

    // Shows all bookings, earliest travel date first — relies on Booking's Comparable.
    public void displayBookingHistory() {
        System.out.println("\nBooking History for " + name + ":");
        if (bookings.isEmpty()) {
            System.out.println("  No bookings yet.");
            return;
        }
        ArrayList<Booking> sorted = new ArrayList<>(bookings);
        Collections.sort(sorted);
        for (Booking booking : sorted) {
            booking.displayInfo();
        }
    }

    // OVERRIDE — adds userId and booking count around shared Person info
    @Override
    public void displayInfo() {
        System.out.println("User ID       : " + userId);
        super.displayInfo();
        System.out.println("Total Bookings: " + bookings.size());
    }

    // OVERRIDE — chains super.toString() so Person controls its own fields
    @Override
    public String toString() {
        return String.format("User{id=%d, bookings=%d, base=[%s]}",
                userId, bookings.size(), super.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return this.userId == other.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String getRoleDescription() {
        return "Registered User";
    }

    public static int getUserCount() { return userCount; }
}