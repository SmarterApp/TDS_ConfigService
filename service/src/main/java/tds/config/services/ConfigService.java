package tds.config.services;

import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;

import java.util.NoSuchElementException;

/**
 * Provide an interface for interacting with the {#code configs} database and providing configuration information.
 */
public interface ConfigService {
    /**
     * Get all the {@link ClientTestProperty} records from the {@code configs.client_testproperties} table for the
     * client and test.
     *
     * @param clientName The name of the client (typically SBAC or SBAC_PT)
     * @param assessmentId The assessment identifier
     * @return A {@code ClientTestProperty} for the specified client name and assessment id.
     * @throws NoSuchElementException if there is no {@code ClientTestProperty} record for the specified client name and
     * assessment id.
     */
    ClientTestProperty getClientTestProperty(String clientName, String assessmentId) throws NoSuchElementException;

    /**
     * Get the {@link ClientSystemFlag} for the specified client and audit name.
     * <p>
     *     The audit name is the name/description of the ClientSystemFlag.  Ideally the source of data for this method
     *     will be an in-memory collection; the {@code ClientSystemFlag} values rarely change, making them a strong
     *     candidate for caching.
     * </p>
     *
     * @param clientName  The name of the client (typically SBAC or SBAC_PT)
     * @param auditObject The name of the desired {@code ClientSystemFlag}
     * @return The {@code ClientSystemFlag} that matches the client and audit name.
     * @throws NoSuchElementException if the {@code ClientSystemFlag} with the specified audit name does not exist
     */
    ClientSystemFlag getClientSystemFlag(String clientName, String auditObject) throws NoSuchElementException;
}
