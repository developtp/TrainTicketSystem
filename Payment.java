public class Payment {

    private int paymentID;
    private int ticketID;
    private double totalPrice;
    private String paymentDate;
    private String paymentMethod;

    // Constructor
    public Payment(int paymentID, int ticketID,
                   double totalPrice,
                   String paymentDate,
                   String paymentMethod) {

        this.paymentID = paymentID;
        this.ticketID = ticketID;
        this.totalPrice = totalPrice;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    // Getter
    public double getTotalPrice() {
        return totalPrice;
    }

    // Setter
    public void setTotalPrice(double totalPrice) {
        if (totalPrice >= 0) {
            this.totalPrice = totalPrice;
        }
    }
}