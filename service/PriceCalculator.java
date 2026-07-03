package service;

import enums.TrainType;

/**
 * Stateless utility class for ticket price calculation.
 *
 * Formula:
 *   price = BASE_PRICE * trainTypeMultiplier
 *   rounded to 2 decimal places.
 *
 * Multipliers:
 *   TrainType : REGULAR=1.0, EXPRESS=1.5, LUXURY=2.0
 */
public class PriceCalculator {

    private static final double BASE_PRICE = 10.0;

    /** Private constructor — this class is not meant to be instantiated. */
    private PriceCalculator() {}

    /**
     * Calculates the ticket price for the given train type.
     *
     * @param trainType the type of train (null defaults to REGULAR)
     * @return price rounded to 2 decimal places
     */
    public static double calculatePrice(TrainType trainType) {
        double trainMultiplier = (trainType != null)
                ? trainType.getPriceMultiplier()
                : TrainType.REGULAR.getPriceMultiplier();

        double raw = BASE_PRICE * trainMultiplier;
        return Math.round(raw * 100.0) / 100.0;
    }
}
