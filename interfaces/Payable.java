package interfaces;

public interface Payable {
    boolean pay();
    boolean isPaid();

    // Keeps compatibility with the older Week 5 method name.
    default void processPayment() {
        pay();
    }
}
