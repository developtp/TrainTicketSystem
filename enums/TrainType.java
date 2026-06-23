package enums;

/**
 * Represents the service tier of a train.
 * Affects pricing via PriceCalculator.
 */
public enum TrainType {
    REGULAR,
    EXPRESS,
    LUXURY;

    /** Price multiplier applied by PriceCalculator. */
    public double getPriceMultiplier() {
        switch (this) {
            case REGULAR: return 1.0;
            case EXPRESS: return 1.5;
            case LUXURY:  return 2.0;
            default:      return 1.0;
        }
    }

    /** Human-readable label for display. */
    public String getLabel() {
        switch (this) {
            case REGULAR: return "Regular";
            case EXPRESS: return "Express";
            case LUXURY:  return "Luxury";
            default:      return name();
        }
    }
}
