package tds.config.repositories;

import java.util.Optional;

import tds.config.ClientTestProperty;

public interface ClientTestPropertyQueryRepository {
    /**
     * Get the {@link tds.config.ClientTestProperty} record from {@code configs.client_testproperties} for the specified client name
     * and test id.
     * <p>
     *     Because the {@link tds.config.ClientTestProperty} values will rarely change, they are strong candidates for caching.
     * </p>
     *
     * @param clientName The client name for which the {@link tds.config.ClientTestProperty} records should be fetched.
     * @param assessmentId The ID (which is the name) of the test for which the {@link tds.config.ClientTestProperty} records
     *                     should be fetched.
     * @return A {@link tds.config.ClientTestProperty} for the specified client name and test id.
     */
    Optional<ClientTestProperty> findClientTestProperty(String clientName, String assessmentId);
}
