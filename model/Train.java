package model;
public class Train {

    private static int trainCounter = 1; 
    private int trainID;
    private String trainName;
    private String source;
    private String destination;
    private int totalSeats;
    private double ticketPrice;

    // Constructor
        public Train( String trainName, String source,
                 String destination, int totalSeats, double ticketPrice) {

        this.trainID = trainCounter++;
        setTrainName(trainName);
        setSource(source);
        setDestination(destination);
        setTotalSeats(totalSeats);
        setTicketPrice(ticketPrice);
    }

    // Getter
    public String getTrainName() {
        return trainName;
    }

    public int getTrainID() {
        return trainID;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }
    
    // Setter
 
    public void setTrainName(String trainName) {
        if (trainName != null && !trainName.trim().isEmpty()) {
            this.trainName = trainName;
        } else {
            throw new IllegalArgumentException("Train name cannot be null or empty.");
        }
    }
 
    public void setSource(String source) {
        if (source != null && !source.trim().isEmpty()) {
            this.source = source;
        } else {
            throw new IllegalArgumentException("Source cannot be null or empty.");
        }
    }
 
    public void setDestination(String destination) {
        if (destination != null && !destination.trim().isEmpty()) {
            this.destination = destination;
        } else {
            throw new IllegalArgumentException("Destination cannot be null or empty.");
        }
    }
 
    public void setTotalSeats(int totalSeats) {
        if (totalSeats > 0) {
            this.totalSeats = totalSeats;
        } else {
            throw new IllegalArgumentException("Total seats must be a positive integer.");
        }
    }
 
    public void setTicketPrice(double ticketPrice) {
        if (ticketPrice >= 0) {
            this.ticketPrice = ticketPrice;
        } else {
            throw new IllegalArgumentException("Ticket price cannot be negative.");
        }
    }
}