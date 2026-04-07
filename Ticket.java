public class Ticket {
    int ticketID;
    int trainID;
    int passengerID;
    String seatNumber;
    String travelDate;
    String status;

    public Ticket(int ticketID, int trainID, int passengerID, String seatNumber, String travelDate, String status) {
        this.ticketID = ticketID;
        this.trainID = trainID;
        this.passengerID = passengerID;
        this.seatNumber = seatNumber;
        this.travelDate = travelDate;
        this.status = status;
    }
}