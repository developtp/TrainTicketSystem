package enums;

/**
 * Supported payment methods in the Train Ticket System.
 */
public enum PaymentMethod {
    CASH,
    ABA,
    WING,
    KHQR;

    /** Human-readable label for receipts and menus. */
    public String getLabel() {
        switch (this) {
            case CASH:  return "Cash";
            case ABA:   return "ABA Bank";
            case WING:  return "Wing";
            case KHQR:  return "KHQR";
            default:    return name();
        }
    }
}
