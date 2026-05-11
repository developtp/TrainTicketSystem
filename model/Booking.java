package model;

import java.time.LocalDate;

public class Booking implements Displayable {

    private static int bookingCounter = 1;

    private int bookingId;
    private User user;
    private Train train;
    private LocalDate travelDate;
    private String status;

    public Booking(User user, Train train, String travelDate, String status) {
        this.bookingId = bookingCounter++;
        setUser(user);
        setTrain(train);
        setTravelDate(LocalDate.parse(travelDate));
        setStatus(status);
    }

    public int getBookingId() {
        return bookingId;
    }

    public static int getBookingCount() {
        return bookingCounter - 1;
    }

    public User getUser() {
        return user;
    }

    public Train getTrain() {
        return train;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public String getStatus() {
        return status;
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
        } else {
            System.out.println("User cannot be null.");
        }
    }

    public void setTrain(Train train) {
        if (train != null) {
            this.train = train;
        } else {
            System.out.println("Train cannot be null.");
        }
    }

    public void setTravelDate(LocalDate travelDate) {
        if (travelDate != null && !travelDate.isBefore(LocalDate.now())) {
            this.travelDate = travelDate;
        } else {
            System.out.println("Invalid travel date.");
        }
    }

    public void setStatus(String status) {
        if (status != null &&
           (status.equals("Pending") || status.equals("Confirmed") || status.equals("Cancelled"))) {
            this.status = status;
        } else {
            System.out.println("Invalid booking status.");
        }
    }

    public boolean isConfirmed() {
        return status.equals("Confirmed");
    }

    @Override
    public void displayInfo() {
        System.out.println("Booking ID  : " + bookingId);
        System.out.println("Passenger   : " + user.getName());
        System.out.println("Train       : " + train.getTrainName());
        System.out.println("Route       : " + train.getSource() + " -> " + train.getDestination());
        System.out.println("Travel Date : " + travelDate);
        System.out.println("Status      : " + status);
        System.out.println("Price       : $" + train.getTicketPrice());
    }
}