package model;

import java.time.LocalDate;

public class Booking { 

    private static int bookingCounter = 1;
    private int bookingID;
    private User userID;
    private Train trainID;
    private LocalDate travelDate;
    private String status;

    // Constructor
    public Booking(User userID, Train trainID,
                   String travelDate, String status) {

        this.bookingID = bookingCounter++;
        setUser(userID);  
        setTrain(trainID);
        setStatus(status); 
        setTravelDate(LocalDate.parse(travelDate));
    }

    // Getter
    public User getUserID() {
        return userID;
    }
    
    public Train getTrainID() {
        return trainID;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }
    
    public String getStatus() {
        return status;
    }

    // Setter
 
    public void setUser(User user) {
        if (user != null) {
            this.userID = user;
        } else {
            throw new IllegalArgumentException("User cannot be null.");
        }
    }
 
    public void setTrain(Train train) {
        if (train != null) {
            this.trainID = train;
        } else {
            throw new IllegalArgumentException("Train cannot be null.");
        }
    }
 
    public void setTravelDate(LocalDate travelDate) {
        if (travelDate != null && !travelDate.isBefore(LocalDate.now())) {
            this.travelDate = travelDate;
        } else {
            throw new IllegalArgumentException("Travel date cannot be null or in the past.");
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