package model;

import interfaces.Displayable;
import interfaces.Payable;
import interfaces.Printable;
import java.time.LocalDate;

public class Payment implements Displayable, Payable, Printable {
    private static int nextPaymentId = 1;
    private static int paymentCount = 0;

    private int paymentId;
    private Booking booking;
    private double amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String paymentStatus;

    public Payment(Booking booking, String paymentMethod) {
        this.paymentId = nextPaymentId++;
        this.booking = booking;
        this.amount = calculateAmountFromBooking();
        this.paymentDate = LocalDate.now();
        this.paymentMethod = cleanText(paymentMethod, "Unknown Method");
        this.paymentStatus = "Unpaid";
        paymentCount++;
    }

    private String cleanText(String value, String defaultValue) {
        if (value == null || value.trim().isEmpty()) return defaultValue;
        return value.trim();
    }

    private double calculateAmountFromBooking() {
        return booking == null ? 0 : booking.calculateAmount();
    }

    public int getPaymentId() { return paymentId; }
    public Booking getBooking() { return booking; }
    public double getAmount() { return amount; }

    // Keeps compatibility with your older code name.
    public double getTotalPrice() { return amount; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }

    @Override
    public boolean pay() {
        if (booking == null) {
            System.out.println("Payment failed: no booking connected.");
            return false;
        }
        if (booking.isCancelled()) {
            System.out.println("Payment failed: booking is cancelled.");
            return false;
        }

        amount = calculateAmountFromBooking();
        if (amount <= 0) {
            System.out.println("Payment failed: amount must be greater than 0.");
            return false;
        }

        boolean confirmed = booking.confirm();
        if (!confirmed) {
            System.out.println("Payment failed: booking cannot be confirmed.");
            return false;
        }

        paymentStatus = "Paid";
        System.out.println("Payment successful. Booking is now confirmed.");
        return true;
    }

    @Override
    public boolean isPaid() {
        return "Paid".equalsIgnoreCase(paymentStatus);
    }

    @Override
    public void displayInfo() {
        System.out.println("\n========== Payment Detail ==========");
        System.out.println("Payment ID     : " + paymentId);
        if (booking != null) {
            System.out.println("Booking ID     : " + booking.getBookingId());
            if (booking.getUser() != null) {
                System.out.println("Passenger      : " + booking.getUser().getName());
            }
            if (booking.getTrain() != null) {
                System.out.println("Train          : " + booking.getTrain().getTrainName());
            }
        }
        System.out.println("Amount         : $" + amount);
        System.out.println("Payment Date   : " + paymentDate);
        System.out.println("Payment Method : " + paymentMethod);
        System.out.println("Payment Status : " + paymentStatus);
        System.out.println("====================================");
    }

    @Override
    public void print() {
        System.out.println("\n========== PAYMENT RECEIPT ==========");
        if (booking != null && booking.getUser() != null) {
            System.out.println("Passenger      : " + booking.getUser().getName());
        }
        System.out.println("Receipt No     : " + paymentId);
        System.out.println("Amount Paid    : $" + amount);
        System.out.println("Method         : " + paymentMethod);
        System.out.println("Payment Date   : " + paymentDate);
        System.out.println("Status         : " + paymentStatus);
        System.out.println("=====================================");
    }

    public static int getPaymentCount() {
        return paymentCount;
    }
}
