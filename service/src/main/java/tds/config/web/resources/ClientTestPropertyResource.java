package tds.config.web.resources;

import org.springframework.hateoas.ResourceSupport;
import tds.config.ClientTestProperty;
import tds.config.endpoints.ConfigController;
import tds.config.exceptions.NotFoundException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS representation of the {@link ClientTestProperty} for return to the consumer.
 */
public class ClientTestPropertyResource extends ResourceSupport {
    private final ClientTestProperty clientTestProperty;

    // TODO:  Having to have a "throws" here is a code smell (its from the methodOn().getClientTestProperty call, which throws a checked exception).  Investigate another way to build the self link.
    public ClientTestPropertyResource(ClientTestProperty clientTestProperty) throws NotFoundException {
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
