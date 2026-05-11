package model;

public class Train implements Displayable {

    private static int trainIdCounter = 1;

    private int trainId;
    private String trainName;
    private String source;
    private String destination;
    private int totalSeats;
    private int reservedSeats; // tracks how many seats have been booked
    private double ticketPrice;

    public Train(String trainName, String source, String destination, int totalSeats, double ticketPrice) {
        this.trainId = trainIdCounter++;
        setTrainName(trainName);
        setSource(source);
        setDestination(destination);
        setTotalSeats(totalSeats);
        setTicketPrice(ticketPrice);
        this.reservedSeats = 0; // starts with no seats reserved
    }

    public int getTrainId() {
        return trainId;
    }

    public static int getTrainCount() {
        return trainIdCounter - 1;
    }

    public String getTrainName() {
        return trainName;
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

    public int getReservedSeats() {
        return reservedSeats;
    }

    // Returns how many seats are still available for booking
    public int getAvailableSeats() {
        return totalSeats - reservedSeats;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    // Attempts to reserve one seat. Returns true if successful, false if train is full.
    // This prevents issuing more tickets than the train has seats.
    public boolean reserveSeat() {
        if (reservedSeats < totalSeats) {
            reservedSeats++;
            return true;
        } else {
            System.out.println("No seats available on " + trainName + ".");
            return false;
        }
    }

    public void setTrainName(String trainName) {
        if (trainName != null && !trainName.trim().isEmpty()) {
            this.trainName = trainName;
        } else {
            System.out.println("Invalid train name.");
        }
    }

    public void setSource(String source) {
        if (source != null && !source.trim().isEmpty()) {
            this.source = source;
        } else {
            System.out.println("Invalid source.");
        }
    }

    public void setDestination(String destination) {
        if (destination != null && !destination.trim().isEmpty()) {
            this.destination = destination;
        } else {
            System.out.println("Invalid destination.");
        }
    }

    public void setTotalSeats(int totalSeats) {
        if (totalSeats > 0) {
            this.totalSeats = totalSeats;
        } else {
            System.out.println("Invalid total seats. Seats must be greater than 0.");
        }
    }

    public void setTicketPrice(double ticketPrice) {
        if (ticketPrice > 0) {
            this.ticketPrice = ticketPrice;
        } else {
            System.out.println("Invalid ticket price. Price must be greater than 0.");
        }
    }

    @Override
    public void displayInfo() {
        System.out.println("Train ID         : " + trainId);
        System.out.println("Train Name       : " + trainName);
        System.out.println("Route            : " + source + " -> " + destination);
        System.out.println("Total Seats      : " + totalSeats);
        System.out.println("Available Seats  : " + getAvailableSeats());
        System.out.println("Price            : $" + ticketPrice);
    }
}
