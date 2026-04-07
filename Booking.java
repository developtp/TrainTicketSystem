public class Booking {
    int bookingID;
    int passengerID;
    int trainID;
    String travelDate;
    String status;

    public Booking(int bookingID, int passengerID, int trainID, String travelDate, String status) {
        this.bookingID = bookingID;
        this.passengerID = passengerID;
        this.trainID = trainID;
        this.travelDate = travelDate;
        this.status = status;
    }
}