package model;

import java.time.LocalDate;

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
    public int getPaymentID() {
        return paymentID;
    }

    public int getTicketID() {
        return ticketID;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }

    // Setter
    public void setPaymentID(int paymentID) {
        if (paymentID > 0) {
            this.paymentID = paymentID;
        } else {
            throw new IllegalArgumentException("Payment ID must be a positive integer.");
        }
    }
 
    public void setTicket(Ticket ticket) {
        if (ticket != null) {
            this.ticket = ticket;
        } else {
            throw new IllegalArgumentException("Ticket cannot be null.");
        }
    }
 
    public void setTotalPrice(double totalPrice) {
        if (totalPrice >= 0) {
            this.totalPrice = totalPrice;
        } else {
            throw new IllegalArgumentException("Total price cannot be negative.");
        }
    }
 
    public void setPaymentDate(LocalDate paymentDate) {
        if (paymentDate != null) {
            this.paymentDate = paymentDate;
        } else {
            throw new IllegalArgumentException("Payment date cannot be null.");
        }
    }
 
    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethod != null && !paymentMethod.trim().isEmpty()) {
            this.paymentMethod = paymentMethod;
        } else {
            throw new IllegalArgumentException("Payment method cannot be null or empty.");
        }
    }
}