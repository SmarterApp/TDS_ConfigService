package tds.config.services;

import java.util.Optional;

import tds.config.ClientSystemFlag;

/**
 * Provide an interface for interacting with the {@code configs} database and providing configuration information.
 */
public interface ConfigService {
    /**
     * Get the {@link ClientSystemFlag} for the specified client and audit name.
     *
     * @param clientName The name of the client (typically SBAC or SBAC_PT)
     * @param type       The type of the desired {@link ClientSystemFlag}
     * @return The {@link Optional<ClientSystemFlag>} that matches the client and audit name.
     */
    Optional<ClientSystemFlag> findClientSystemFlag(final String clientName, final String type);
}
