package model;

import java.util.ArrayList;
import java.util.Objects;

public class User extends Person {

    private int userId;
    private ArrayList<Booking> bookings;

    private static int userCount  = 0;
    private static int nextUserId = 1;

    // ─── Constructor ─────────────────────────────────────────────────────────────
    public User(String name, int age, String gender, String phoneNumber) {
        super(name, age, gender, phoneNumber);
        this.userId   = nextUserId++;
        this.bookings = new ArrayList<>();
        userCount++;
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────
    public int getUserId()            { return userId; }
    public int getBookingHistorySize(){ return bookings.size(); }

    public ArrayList<Booking> getBookingsCopy() {
        return new ArrayList<>(bookings);
    }

    // ─── Booking management ──────────────────────────────────────────────────────
    public void addBooking(Booking booking) {
        if (booking != null && !bookings.contains(booking)) {
            bookings.add(booking);
        }
    }

    // ─── displayBookingHistory() — OVERLOADED (3 versions) ───────────────────────

    // VERSION 1 (EXISTING) — shows ALL bookings
    public void displayBookingHistory() {
        System.out.println("\nBooking History for " + name + ":");
        if (bookings.isEmpty()) {
            System.out.println("  No bookings yet.");
            return;
        }
        for (Booking booking : bookings) {
            booking.displayInfo();
        }
    }

    // VERSION 2 (NEW OVERLOAD) — filter by status: "Pending", "Confirmed", "Cancelled"
    // Example usage: user.displayBookingHistory("Confirmed")
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

    // VERSION 3 (NEW OVERLOAD) — show only the most recent N bookings
    // Example usage: user.displayBookingHistory(2)  → last 2 bookings
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

    // ─── displayInfo() override (EXISTING) ───────────────────────────────────────
    // Overrides Person.displayInfo() — adds userId and booking count on top/bottom
    @Override
    public void displayInfo() {
        System.out.println("User ID       : " + userId);
        super.displayInfo();   // calls Person.displayInfo() — prints name, age, gender, phone
        System.out.println("Total Bookings: " + bookings.size());
    }

    // ─── toString() override (NEW) ───────────────────────────────────────────────
    // Chains with super.toString() from Person so we don't repeat name/phone logic
    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', age=%d, bookings=%d}",
                userId, name, age, bookings.size());
    }

    // ─── equals() override (NEW) ─────────────────────────────────────────────────
    // Two User objects are equal only if they have the same userId.
    // This fixes the contains() check in addBooking() — previously it compared
    // memory addresses, so the same logical user could be added twice.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return this.userId == other.userId;
    }

    // ─── hashCode() override (NEW) ───────────────────────────────────────────────
    // Must match equals() — use userId as the hash key.
    // This makes User work correctly inside HashMap and HashSet.
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    public static int getUserCount() { return userCount; }
}