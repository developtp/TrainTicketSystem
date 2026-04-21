public class Train {

    private int trainID;
    private String trainName;
    private String source;
    private String destination;
    private int totalSeats;
    private double ticketPrice;

    // Constructor
    public Train(int trainID, String trainName,
                 String source, String destination,
                 int totalSeats, double ticketPrice) {

        this.trainID = trainID;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.ticketPrice = ticketPrice;
    }

    // Getter
    public String getTrainName() {
        return trainName;
    }

    // Setter
    public void setTicketPrice(double ticketPrice) {
        if (ticketPrice >= 0) {
            this.ticketPrice = ticketPrice;
        }
    }
}