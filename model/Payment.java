package model;

import java.time.LocalDate;

public class Payment {

    private static int paymentCounter = 1; 
    private int paymentID;
    private Ticket ticket;
    private double totalPrice;
    private LocalDate paymentDate;
    private String paymentMethod;

    // Constructor
    public Payment(int ticketID,
                   double totalPrice,
                   String paymentDate,
                   String paymentMethod) {

        this.paymentID = paymentCounter++;
        setTicket(ticket);
        setTotalPrice(totalPrice);
        setPaymentDate(LocalDate.parse(paymentDate));
        setPaymentMethod(paymentMethod);
    }

    // Getter
    public int getPaymentID() {
        return paymentID;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }

    // Setter
 
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