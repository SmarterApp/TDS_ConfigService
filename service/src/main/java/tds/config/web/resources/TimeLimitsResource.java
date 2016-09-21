package tds.config.web.resources;

import org.springframework.hateoas.ResourceSupport;
import tds.config.TimeLimits;

/**
 * A HATEOAS representation of the {@link tds.config.TimeLimits} for return to the consumer.
 */
public class TimeLimitsResource extends ResourceSupport {
    private final TimeLimits timeLimits;

    public TimeLimitsResource(TimeLimits timeLimits) {
        this.timeLimits = timeLimits;
    }

    public TimeLimits getTimeLimits() {
        return timeLimits;
    }
}
