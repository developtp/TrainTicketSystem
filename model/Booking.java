package model;

import java.time.LocalDate;

public class Booking { 

    private static int bookingCounter = 1; // Static counter for auto-incrementing booking IDs
    private int bookingID;
    private User userID;
    private Train trainID;
    private String travelDate;
    private String status;

    // Constructor
    public Booking(User userID, Train trainID,
                   String travelDate, String status) {

        this.bookingID = bookingCounter++;
        this.userID = userID;
        this.trainID = trainID;
        this.travelDate = travelDate;
        this.status = status;
    }

    // Getter
    public User getUserID() {
        return userID;
    }
    
    public Train getTrainID() {
        return trainID;
    }

    public String getTravelDate() {
        return travelDate;
    }
    
    public String getStatus() {
        return status;
    }

    // Setter
    public void setBookingID(int bookingID) {
        if (bookingID > 0) {
            this.bookingID = bookingID;
        } else {
            throw new IllegalArgumentException("Booking ID must be a positive integer.");
        }
    }
 
    public void setUser(User user) {
        if (user != null) {
            this.user = user;
        } else {
            throw new IllegalArgumentException("User cannot be null.");
        }
    }
 
    public void setTrain(Train train) {
        if (train != null) {
            this.train = train;
        } else {
            throw new IllegalArgumentException("Train cannot be null.");
        }
    }
 
    public void setTravelDate(LocalDate travelDate) {
        if (travelDate != null) {
            this.travelDate = travelDate;
        } else {
            throw new IllegalArgumentException("Travel date cannot be null.");
        }
    }
 
    public void setStatus(String status) {
        if (status.equals("Confirmed") || status.equals("Cancelled") || status.equals("Pending")) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid status. Allowed values are: Confirmed, Cancelled, Pending.");
        }
    }
}