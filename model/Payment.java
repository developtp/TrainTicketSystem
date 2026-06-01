package model;

import interfaces.Displayable;
import interfaces.Payable;
import interfaces.Printable;
import java.time.LocalDate;
import java.util.Objects;

public class Payment implements Displayable, Payable, Printable {

    private static int nextPaymentId = 1;
    private static int paymentCount  = 0;

    private int       paymentId;
    private Booking   booking;
    private double    amount;
    private LocalDate paymentDate;
    private String    paymentMethod;
    private String    paymentStatus;

    // ─── Constructors (OVERLOADED) ────────────────────────────────────────────────

    // VERSION 1 (EXISTING) — amount is calculated automatically from the booking
    public Payment(Booking booking, String paymentMethod) {
        this.paymentId     = nextPaymentId++;
        this.booking       = booking;
        this.amount        = calculateAmountFromBooking();
        this.paymentDate   = LocalDate.now();
        this.paymentMethod = cleanText(paymentMethod, "Unknown Method");
        this.paymentStatus = "Unpaid";
        paymentCount++;
    }

    // VERSION 2 (NEW OVERLOAD) — caller can override the amount manually
    // Useful for discounts, partial payments, or adjusted fares
    // Example: new Payment(booking, "ABA", 8.00)  →  discounted amount
    public Payment(Booking booking, String paymentMethod, double customAmount) {
        this(booking, paymentMethod); // delegates to VERSION 1
        if (customAmount > 0) {
            this.amount = customAmount; // override the auto-calculated amount
        }
    }

    // ─── Helper ──────────────────────────────────────────────────────────────────
    private String cleanText(String value, String defaultValue) {
        if (value == null || value.trim().isEmpty()) return defaultValue;
        return value.trim();
    }

    private double calculateAmountFromBooking() {
        return (booking == null) ? 0 : booking.calculateAmount();
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────
    public int       getPaymentId()     { return paymentId; }
    public Booking   getBooking()       { return booking; }
    public double    getAmount()        { return amount; }
    public double    getTotalPrice()    { return amount; } // legacy alias
    public LocalDate getPaymentDate()   { return paymentDate; }
    public String    getPaymentMethod() { return paymentMethod; }
    public String    getPaymentStatus() { return paymentStatus; }

    // ─── Payable interface overrides (EXISTING) ───────────────────────────────────
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
        if (!booking.confirm()) {
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

    // ─── displayInfo() override (EXISTING) ───────────────────────────────────────
    @Override
    public void displayInfo() {
        System.out.println("\n========== Payment Detail ==========");
        System.out.println("Payment ID     : " + paymentId);
        if (booking != null) {
            System.out.println("Booking ID     : " + booking.getBookingId());
            if (booking.getUser()  != null) System.out.println("Passenger      : " + booking.getUser().getName());
            if (booking.getTrain() != null) System.out.println("Train          : " + booking.getTrain().getTrainName());
        }
        System.out.println("Amount         : $" + amount);
        System.out.println("Payment Date   : " + paymentDate);
        System.out.println("Payment Method : " + paymentMethod);
        System.out.println("Payment Status : " + paymentStatus);
        System.out.println("====================================");
    }

    // ─── print() override — Printable interface (EXISTING) ───────────────────────
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

    // ─── toString() override (NEW) ───────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("Payment{id=%d, amount=$%.2f, method='%s', status='%s'}",
                paymentId, amount, paymentMethod, paymentStatus);
    }

    // ─── equals() override (NEW) ─────────────────────────────────────────────────
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Payment)) return false;
        Payment other = (Payment) obj;
        return this.paymentId == other.paymentId;
    }

    // ─── hashCode() override (NEW) ───────────────────────────────────────────────
    @Override
    public int hashCode() {
        return Objects.hash(paymentId);
    }

    public static int getPaymentCount() { return paymentCount; }
}