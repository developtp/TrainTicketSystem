public class Booking {

    private int userID;
    private int trainID;
    private String travelDate;
    private String status;

    // Constructor
    public Booking(int userID, int trainID,
                   String travelDate, String status) {

        this.userID = userID;
        this.trainID = trainID;
        this.travelDate = travelDate;
        this.status = status;
    }

    // Getter
    public String getStatus() {
        return status;
    }

    // Setter
    public void setStatus(String status) {
        this.status = status;
    }
}