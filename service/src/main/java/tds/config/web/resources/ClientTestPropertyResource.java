package tds.config.web.resources;

import org.springframework.hateoas.ResourceSupport;
import tds.config.ClientTestProperty;
import tds.config.web.endpoints.ConfigController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS representation of the {@link ClientTestProperty} for return to the consumer.
 */
public class ClientTestPropertyResource extends ResourceSupport {
    private final ClientTestProperty clientTestProperty;

    public ClientTestPropertyResource(ClientTestProperty clientTestProperty) {
        this.clientTestProperty = clientTestProperty;
        this.add(linkTo(
                methodOn(ConfigController.class)
                        .getClientTestProperty(clientTestProperty.getClientName(), clientTestProperty.getAssessmentId()))
                .withSelfRel());
    }

    public ClientTestProperty getClientTestProperty() {
        return clientTestProperty;
    }
}
