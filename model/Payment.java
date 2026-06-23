package model;

import enums.PaymentMethod;
import interfaces.Displayable;
import interfaces.Payable;
import interfaces.Printable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Processes payment for a Booking.
 *
 * OOP concepts demonstrated here:
 *   - Encapsulation  : all fields private
 *   - Static         : nextPaymentId, paymentCount
 *   - Overloading    : three constructors
 *   - Overriding     : pay(), isPaid(), displayInfo(), print(), toString()
 *   - Interface      : implements Displayable, Payable, Printable
 *
 * Guards:
 *   1. Cannot pay a cancelled booking.
 *   2. Cannot pay twice (already paid).
 *   3. Amount must be > 0.
 *
 * On success: booking.confirmBooking() is called.
 * Ticket generation is handled by TrainTicketBookingSystem.processPayment().
 */
public class Payment implements Displayable, Payable, Printable {

    // ─── Static counters ─────────────────────────────────────────────────────────
    private static int nextPaymentId = 1;
    private static int paymentCount  = 0;

    // ─── Instance fields ─────────────────────────────────────────────────────────
    private int           paymentId;
    private Booking       booking;
    private double        amount;
    private LocalDate     paymentDate;
    private PaymentMethod paymentMethod;
    private String        paymentStatus;

    // ─── OVERLOAD 1 — amount auto-calculated from booking ────────────────────────
    public Payment(Booking booking, PaymentMethod paymentMethod) {
        this.paymentId     = nextPaymentId++;
        this.booking       = booking;
        this.amount        = (booking != null) ? booking.getPrice() : 0;
        this.paymentDate   = LocalDate.now();
        this.paymentMethod = (paymentMethod != null) ? paymentMethod : PaymentMethod.CASH;
        this.paymentStatus = "Unpaid";
        paymentCount++;
    }

    // ─── OVERLOAD 2 — legacy String-based method name (for backward compat) ──────
    // Old code: new Payment(booking, "ABA") still compiles by mapping to PaymentMethod
    public Payment(Booking booking, String paymentMethodStr) {
        this(booking, parseMethod(paymentMethodStr));
    }

    // ─── OVERLOAD 3 — apply a percentage discount off the booking price ──────────
    public Payment(Booking booking, PaymentMethod paymentMethod, int discountPercent) {
        this(booking, paymentMethod);
        if (discountPercent > 0 && discountPercent <= 100) {
            this.amount = this.amount * (1.0 - discountPercent / 100.0);
        }
    }

    // ─── Getters ─────────────────────────────────────────────────────────────────
    public int           getPaymentId()     { return paymentId; }
    public Booking       getBooking()       { return booking; }
    public double        getAmount()        { return amount; }
    public double        getTotalPrice()    { return amount; }
    public LocalDate     getPaymentDate()   { return paymentDate; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public String        getPaymentStatus() { return paymentStatus; }

    // ─── Payable interface — OVERRIDE ─────────────────────────────────────────────
    @Override
    public boolean pay() {
        if (booking == null) {
            System.out.println("Payment failed: no booking connected.");
            return false;
        }
        if (isPaid()) {
            System.out.println("Payment failed: this booking has already been paid.");
            return false;
        }
        if (booking.isCancelled()) {
            System.out.println("Payment failed: booking " + booking.getBookingId() + " is cancelled.");
            return false;
        }
        if (amount <= 0) {
            System.out.println("Payment failed: amount must be greater than $0.");
            return false;
        }
        if (!booking.confirmBooking()) {
            System.out.println("Payment failed: booking could not be confirmed.");
            return false;
        }
        paymentStatus = "Paid";
        System.out.println("Payment successful. Booking #" + booking.getBookingId() + " is now CONFIRMED.");
        return true;
    }

    /** OVERRIDE — Payable interface */
    @Override
    public boolean isPaid() {
        return "Paid".equalsIgnoreCase(paymentStatus);
    }

    // ─── Displayable interface — OVERRIDE ─────────────────────────────────────────
    @Override
    public void displayInfo() {
        System.out.println("\n========== Payment Detail ==========");
        System.out.println("Payment ID     : " + paymentId);
        if (booking != null) {
            System.out.println("Booking ID     : " + booking.getBookingId());
            if (booking.getUser()  != null) System.out.println("Passenger      : " + booking.getUser().getName());
            if (booking.getTrain() != null) System.out.println("Train          : " + booking.getTrain().getTrainName());
        }
        System.out.printf ("Amount         : $%.2f%n", amount);
        System.out.println("Payment Date   : " + paymentDate);
        System.out.println("Payment Method : " + paymentMethod.getLabel());
        System.out.println("Payment Status : " + paymentStatus);
        System.out.println("====================================");
    }

    // ─── Printable interface — OVERRIDE ───────────────────────────────────────────
    @Override
    public void print() {
        System.out.println("\n========== PAYMENT RECEIPT ==========");
        if (booking != null && booking.getUser() != null) {
            System.out.println("Passenger      : " + booking.getUser().getName());
        }
        System.out.println("Receipt No     : " + paymentId);
        System.out.printf ("Amount Paid    : $%.2f%n", amount);
        System.out.println("Method         : " + paymentMethod.getLabel());
        System.out.println("Payment Date   : " + paymentDate);
        System.out.println("Status         : " + paymentStatus);
        System.out.println("=====================================");
    }

    // ─── Standard overrides ───────────────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("Payment{id=%d, amount=$%.2f, method='%s', status='%s'}",
                paymentId, amount, paymentMethod.getLabel(), paymentStatus);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Payment)) return false;
        Payment other = (Payment) obj;
        return this.paymentId == other.paymentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId);
    }

    public static int getPaymentCount() { return paymentCount; }

    // ─── Private helpers ──────────────────────────────────────────────────────────
    /** Maps a raw String to a PaymentMethod enum (used for backward compat). */
    private static PaymentMethod parseMethod(String raw) {
        if (raw == null) return PaymentMethod.CASH;
        switch (raw.trim().toUpperCase()) {
            case "ABA":   return PaymentMethod.ABA;
            case "WING":  return PaymentMethod.WING;
            case "KHQR":  return PaymentMethod.KHQR;
            default:      return PaymentMethod.CASH;
        }
    }
}