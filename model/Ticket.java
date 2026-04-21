package model;
public class Ticket {

    private int ticketID;
    private Train train;
    private int userID;
    private String seatNumber;
    private String travelDate;
    private String status;

    // Constructor
    public Ticket(int ticketID, Train train,
                  int userID, String seatNumber,
                  String travelDate, String status) {

        this.ticketID = ticketID;
        this.train = train;
        this.userID = userID;
        this.seatNumber = seatNumber;
        this.travelDate = travelDate;
        this.status = status;
    }

    // Getter
    public int getTicketID() {
        return ticketID;
    }
    
    public Train getTrain() {
        return train;
    }

    public int getUserID() {
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
    public void setStatus(String status) {
        this.status = status;
    }
}