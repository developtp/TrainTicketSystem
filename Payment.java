public class Payment {
    int paymentID;
    int ticketID;
    double amount;
    String paymentDate;
    String paymentMethod;

    public Payment(int paymentID, int ticketID, double amount, String paymentDate, String paymentMethod) {
        this.paymentID = paymentID;
        this.ticketID = ticketID;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }
}