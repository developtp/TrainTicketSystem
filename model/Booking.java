package model;

import interfaces.Displayable;
import java.time.LocalDate;

public class Booking implements Displayable {
    private static int nextBookingId = 1;
    private static int bookingCount = 0;

    private int bookingId;
    private User user;
    private Train train;
    private LocalDate travelDate;
    private String status;

    public Booking(User user, Train train, String travelDate) {
        this.bookingId = nextBookingId++;
        this.user = user;
        this.train = train;
        setTravelDate(travelDate);
        this.status = "Pending";
        bookingCount++;
    }

    // Keeps compatibility with your old constructor.
    public Booking(User user, Train train, String travelDate, String status) {
        this(user, train, travelDate);
        setStatus(status);
    }

    private String cleanText(String value, String defaultValue) {
        if (value == null || value.trim().isEmpty()) return defaultValue;
        return value.trim();
    }

    public int getBookingId() { return bookingId; }
    public User getUser() { return user; }
    public Train getTrain() { return train; }
    public LocalDate getTravelDate() { return travelDate; }
    public String getStatus() { return status; }

    public void setTravelDate(String travelDate) {
        try {
            LocalDate date = LocalDate.parse(cleanText(travelDate, LocalDate.now().toString()));
            if (date.isBefore(LocalDate.now())) {
                System.out.println("Invalid travel date. Defaulting to today.");
                this.travelDate = LocalDate.now();
            } else {
                this.travelDate = date;
            }
        } catch (Exception e) {
            System.out.println("Invalid travel date format. Defaulting to today.");
            this.travelDate = LocalDate.now();
        }
    }

    public void setStatus(String status) {
        String cleanedStatus = cleanText(status, "Pending");
        if (cleanedStatus.equalsIgnoreCase("Pending") ||
            cleanedStatus.equalsIgnoreCase("Confirmed") ||
            cleanedStatus.equalsIgnoreCase("Cancelled")) {
            this.status = cleanedStatus.substring(0, 1).toUpperCase() + cleanedStatus.substring(1).toLowerCase();
        } else {
            this.status = "Pending";
        }
    }

    public boolean isPending() { return "Pending".equalsIgnoreCase(status); }
    public boolean isConfirmed() { return "Confirmed".equalsIgnoreCase(status); }
    public boolean isCancelled() { return "Cancelled".equalsIgnoreCase(status); }

    public double calculateAmount() {
        return train == null ? 0 : train.getTicketPrice();
    }

    public boolean confirm() {
        if (isCancelled()) {
            System.out.println("Booking " + bookingId + " is cancelled and cannot be confirmed.");
            return false;
        }
        if (user == null) {
            System.out.println("Booking cannot be confirmed without a user.");
            return false;
        }
        if (train == null) {
            System.out.println("Booking cannot be confirmed without a train.");
            return false;
        }
        if (!train.hasAvailableSeat()) {
            System.out.println("Booking cannot be confirmed. No available seats.");
            return false;
        }
        status = "Confirmed";
        return true;
    }

    public boolean cancel() {
        if (isConfirmed()) {
            System.out.println("Confirmed booking cannot be cancelled in this simple version.");
            return false;
        }
        status = "Cancelled";
        return true;
    }

    @Override
    public void displayInfo() {
        System.out.println("\n========== Booking Detail ==========");
        System.out.println("Booking ID  : " + bookingId);
        System.out.println("Travel Date : " + travelDate);
        System.out.println("Status      : " + status);
        if (user != null) {
            System.out.println("User        : " + user.getName());
        }
        if (train != null) {
            System.out.println("Train       : " + train.getTrainName());
            System.out.println("Route       : " + train.getSource() + " -> " + train.getDestination());
            System.out.println("Amount      : $" + calculateAmount());
        }
        System.out.println("====================================");
    }

    public static int getBookingCount() {
        return bookingCount;
    }
}
