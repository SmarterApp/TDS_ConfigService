package tds.config.web.resources;

/**
 * A HATEOAS representation of the TA Check-In Time value.
 */
public class TaCheckinTimeResource {
    private final int taCheckinTimeMinutes;

    public TaCheckinTimeResource(int taCheckinTimeMinutes) {
        this.taCheckinTimeMinutes = taCheckinTimeMinutes;
    }

    public int getTaCheckinTimeMinutes() {
        return taCheckinTimeMinutes;
    }
}
