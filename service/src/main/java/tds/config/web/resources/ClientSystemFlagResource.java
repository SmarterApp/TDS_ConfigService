package tds.config.web.resources;

import org.springframework.hateoas.ResourceSupport;
import tds.config.ClientSystemFlag;
import tds.config.endpoints.ConfigController;
import tds.config.exceptions.NotFoundException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS representation of the {@link tds.config.ClientSystemFlag} for return to the consumer.
 */
public class ClientSystemFlagResource extends ResourceSupport {
    private final ClientSystemFlag clientSystemFlag;

    // TODO:  Having to have a "throws" here is a code smell (its from the methodOn().getClientSystemFlag call, which throws a checked exception).  Investigate another way to build the self link.
    public ClientSystemFlagResource(ClientSystemFlag clientSystemFlag) throws NotFoundException{
        this.clientSystemFlag = clientSystemFlag;
        this.add(linkTo(
                methodOn(ConfigController.class)
                        .getClientSystemFlag(clientSystemFlag.getClientName(), clientSystemFlag.getAuditObject()))
                .withSelfRel());
    }

    public ClientSystemFlag getClientSystemFlag() {
        return clientSystemFlag;
    }
}
