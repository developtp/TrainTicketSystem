package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class User extends Person implements Comparable<User> {

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

    public int getUserId()             { return userId; }
    public int getBookingHistorySize() { return bookings.size(); }

    public ArrayList<Booking> getBookingsCopy() {
        return new ArrayList<>(bookings);
    }

    public void addBooking(Booking booking) {
        if (booking != null && !bookings.contains(booking)) {
            bookings.add(booking);
        }
    }

    // OVERLOAD 1 — show ALL bookings (sorted by travel date)
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

    // OVERLOAD 2 — filter by status: "Pending", "Confirmed", "Cancelled"
    public void displayBookingHistory(String statusFilter) {
        System.out.println("\nBooking History for " + name
                + " [status = " + statusFilter + "]:");
        boolean found = false;
        for (Booking booking : bookings) {
            if (booking.getStatus().equalsIgnoreCase(statusFilter)) {
                booking.displayInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("  No bookings with status: " + statusFilter);
        }
    }

    // OVERLOAD 3 — show only the most recent N bookings
    public void displayBookingHistory(int limit) {
        System.out.println("\nLast " + limit + " booking(s) for " + name + ":");
        if (bookings.isEmpty()) {
            System.out.println("  No bookings yet.");
            return;
        }
        int start = Math.max(0, bookings.size() - limit);
        for (int i = start; i < bookings.size(); i++) {
            bookings.get(i).displayInfo();
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

    // OVERRIDE — sort Users alphabetically by name
    @Override
    public int compareTo(User other) {
        return this.name.compareTo(other.name);
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