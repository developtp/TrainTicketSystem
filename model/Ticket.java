package model;

import java.time.LocalDate;

public class Ticket { 

    private  static int ticketCounter = 1;
    private int ticketID;
    private Train train;
    private User userID;
    private String seatNumber;
    private LocalDate travelDate;
    private String status;

    // Constructor
    public Ticket(Train train,
                  User userID, String seatNumber,
                  String travelDate, String status) {

        this.ticketID = ticketCounter++;
        setTrain(train);
        setUser(userID);
        setSeatNumber(seatNumber);
        setTrain(train);
        setTravelDate(travelDate);
        setStatus(status);
    }

    // Getter
    public int getTicketID() {
        return ticketID;
    }
    
    public Train getTrain() {
        return train;
    }

    public User getUserID() {
        return userID;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public String getStatus() {
        return status;
    }

    // Setter
 
    public void setTrain(Train train) {
        if (train != null) {
            this.train = train;
        } else {
            throw new IllegalArgumentException("Train cannot be null.");
        }
    }
 
    public void setUser(User user) {
        if (user != null) {
            this.userID = user;
        } else {
            throw new IllegalArgumentException("User cannot be null.");
        }
    }
 
    public void setSeatNumber(String seatNumber) {
        if (seatNumber != null && !seatNumber.trim().isEmpty()) {
            this.seatNumber = seatNumber;
        } else {
            throw new IllegalArgumentException("Seat number cannot be null or empty.");
        }
    }
 
    public void setTravelDate(LocalDate travelDate) {
        if (travelDate != null && !travelDate.trim().isEmpty()) {
            this.travelDate = LocalDate.parse(travelDate)
        } else {
            throw new IllegalArgumentException("Travel date cannot be null or empty.");
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
