public class Train {
    int trainID;
    String trainName;
    String source;
    String destination;
    int totalSeats;
    double ticketPrice;

    public Train(int trainID, String trainName, String source, String destination, int totalSeats, double ticketPrice) {
        this.trainID = trainID;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.ticketPrice = ticketPrice;
    }
}