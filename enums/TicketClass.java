package enums;

/**
 * Represents the class of service for a train ticket.
 *
 * Seat prefix conventions:
 *   ECONOMY    → E1, E2, ...
 *   BUSINESS   → B1, B2, ...
 *   FIRST_CLASS → F1, F2, ...
 */
public enum TicketClass {
    ECONOMY,
    BUSINESS,
    FIRST_CLASS;

    /** Returns the seat prefix letter used for validation (e.g. "E", "B", "F"). */
    public String getSeatPrefix() {
        switch (this) {
            case ECONOMY:     return "E";
            case BUSINESS:    return "B";
            case FIRST_CLASS: return "F";
            default:          return "?";
        }
    }

    /** Human-readable label for display. */
    public String getLabel() {
        switch (this) {
            case ECONOMY:     return "Economy";
            case BUSINESS:    return "Business";
            case FIRST_CLASS: return "First Class";
            default:          return name();
        }
    }
}
