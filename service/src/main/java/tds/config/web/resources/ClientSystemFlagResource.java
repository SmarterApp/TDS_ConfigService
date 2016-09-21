package tds.config.web.resources;

import org.springframework.hateoas.ResourceSupport;
import tds.config.ClientSystemFlag;
import tds.config.web.endpoints.ConfigController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A HATEOAS representation of the {@link tds.config.ClientSystemFlag} for return to the consumer.
 */
public class ClientSystemFlagResource extends ResourceSupport {
    private final ClientSystemFlag clientSystemFlag;

    public ClientSystemFlagResource(ClientSystemFlag clientSystemFlag) {
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
