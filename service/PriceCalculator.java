package service;

import enums.TicketClass;
import enums.TrainType;

/**
 * Stateless utility class for ticket price calculation.
 *
 * OOP concepts demonstrated here:
 *   - Static     : calculatePrice() is a static method (no instance needed)
 *   - Abstraction: hides pricing formula behind a clean API
 *   - Encapsulation: BASE_PRICE is a private constant
 *
 * Formula:
 *   price = BASE_PRICE * trainTypeMultiplier * ticketClassMultiplier
 *   rounded to 2 decimal places.
 *
 * Multipliers:
 *   TrainType  : REGULAR=1.0, EXPRESS=1.5, LUXURY=2.0
 *   TicketClass: ECONOMY=1.0, BUSINESS=1.75, FIRST_CLASS=2.5
 */
public class PriceCalculator {

    private static final double BASE_PRICE = 10.0;

    /** Private constructor — this class is not meant to be instantiated. */
    private PriceCalculator() {}

    /**
     * Calculates the ticket price for the given train type and ticket class.
     *
     * @param trainType   the type of train (null defaults to REGULAR)
     * @param ticketClass the class of service (null defaults to ECONOMY)
     * @return price rounded to 2 decimal places
     */
    public static double calculatePrice(TrainType trainType, TicketClass ticketClass) {
        double trainMultiplier = (trainType != null)
                ? trainType.getPriceMultiplier()
                : TrainType.REGULAR.getPriceMultiplier();

        double classMultiplier = getClassMultiplier(ticketClass);

        double raw = BASE_PRICE * trainMultiplier * classMultiplier;
        return Math.round(raw * 100.0) / 100.0;
    }

    // ─── Private helpers ──────────────────────────────────────────────────────────
    private static double getClassMultiplier(TicketClass ticketClass) {
        if (ticketClass == null) return 1.0;
        switch (ticketClass) {
            case ECONOMY:     return 1.0;
            case BUSINESS:    return 1.75;
            case FIRST_CLASS: return 2.5;
            default:          return 1.0;
        }
    }
}
