package tds.config.web.resources;

import org.springframework.hateoas.ResourceSupport;
import tds.config.TimeLimitConfiguration;
import tds.config.web.endpoints.TimeLimitConfigurationController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS representation of the {@link tds.config.TimeLimitConfiguration} for return to the consumer.
 */
public class TimeLimitConfigurationResource extends ResourceSupport {
    private final TimeLimitConfiguration timeLimitConfiguration;

    public TimeLimitConfigurationResource(TimeLimitConfiguration timeLimitConfiguration) {
        this.timeLimitConfiguration = timeLimitConfiguration;
        if (timeLimitConfiguration.getAssessmentId() == null) {
            this.add(linkTo(
                    methodOn(TimeLimitConfigurationController.class).getTimeLimitConfiguration(
                            timeLimitConfiguration.getClientName()))
                    .withSelfRel());
        } else {
            this.add(linkTo(
                    methodOn(TimeLimitConfigurationController.class).getTimeLimitConfiguration(
                            timeLimitConfiguration.getClientName(),
                            timeLimitConfiguration.getAssessmentId()))
                    .withSelfRel());
        }
    }

    public TimeLimitConfiguration getTimeLimitConfiguration() {
        return timeLimitConfiguration;
    }
}
