package model;

import java.util.ArrayList;

public class User extends Person {
    private int userId;
    private ArrayList<Booking> bookings;

    private static int userCount = 0;
    private static int nextUserId = 1;

    public User(String name, int age, String gender, String phoneNumber) {
        super(name, age, gender, phoneNumber);
        this.userId = nextUserId;
        nextUserId++;
        userCount++;
        this.bookings = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public void addBooking(Booking booking) {
        if (booking != null && !bookings.contains(booking)) {
            bookings.add(booking);
        }
    }

    public ArrayList<Booking> getBookingsCopy() {
        return new ArrayList<>(bookings);
    }

    public int getBookingHistorySize() {
        return bookings.size();
    }

    public void displayBookingHistory() {
        System.out.println("\nBooking History for " + name + ":");
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
            return;
        }

        for (Booking booking : bookings) {
            booking.displayInfo();
        }
    }

    @Override
    public void displayInfo() {
        System.out.println("User ID: " + userId);
        super.displayInfo();
        System.out.println("Total Bookings: " + bookings.size());
    }

    public static int getUserCount() {
        return userCount;
    }
}

