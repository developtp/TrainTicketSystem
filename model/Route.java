package model;

import interfaces.Displayable;
import java.util.Objects;

/**
 * Represents a point-to-point train route.
 * Stored inside Train to avoid duplicating source/destination strings.
 */
public class Route implements Displayable {

    private String departureStation;
    private String destinationStation;

    // ─── Constructor ──────────────────────────────────────────────────────────────
    public Route(String departureStation, String destinationStation) {
        setDepartureStation(departureStation);
        setDestinationStation(destinationStation);
    }

    // ─── Getters ──────────────────────────────────────────────────────────────────
    public String getDepartureStation()   { return departureStation; }
    public String getDestinationStation() { return destinationStation; }

    // ─── Setters (with null-safety) ───────────────────────────────────────────────
    public void setDepartureStation(String departureStation) {
        String cleaned = (departureStation == null) ? "" : departureStation.trim();
        this.departureStation = cleaned.isEmpty() ? "Unknown Station" : cleaned;
    }

    public void setDestinationStation(String destinationStation) {
        String cleaned = (destinationStation == null) ? "" : destinationStation.trim();
        if (cleaned.isEmpty()) {
            this.destinationStation = "Unknown Destination";
        } else if (cleaned.equalsIgnoreCase(this.departureStation)) {
            this.destinationStation = "Invalid Destination";
        } else {
            this.destinationStation = cleaned;
        }
    }

    // ─── Displayable interface ────────────────────────────────────────────────────
    @Override
    public void displayInfo() {
        System.out.println("Departure   : " + departureStation);
        System.out.println("Destination : " + destinationStation);
    }

    // ─── Standard overrides ───────────────────────────────────────────────────────
    @Override
    public String toString() {
        return departureStation + " -> " + destinationStation;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Route)) return false;
        Route other = (Route) obj;
        return Objects.equals(this.departureStation,   other.departureStation) &&
               Objects.equals(this.destinationStation, other.destinationStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departureStation, destinationStation);
    }
}
