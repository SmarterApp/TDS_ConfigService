package tds.config.services;

import java.util.Optional;

import tds.config.ClientTestProperty;

public interface ClientTestPropertyService {
    /**
     * Get all the {@link tds.config.ClientTestProperty} records from the {@code configs.client_testproperties} table for the
     * client and test.
     *
     * @param clientName The name of the client (typically SBAC or SBAC_PT)
     * @param assessmentId The assessment identifier
     * @return A {@link java.util.Optional < tds.config.ClientTestProperty >} for the specified client name and assessment id.
     */
    Optional<ClientTestProperty> findClientTestProperty(String clientName, String assessmentId);
}
