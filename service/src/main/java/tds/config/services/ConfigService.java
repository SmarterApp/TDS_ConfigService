package tds.config.services;

import java.util.Optional;

import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.model.CurrentExamWindow;

/**
 * Provide an interface for interacting with the {@code configs} database and providing configuration information.
 */
public interface ConfigService {
    /**
     * Get all the {@link ClientTestProperty} records from the {@code configs.client_testproperties} table for the
     * client and test.
     *
     * @param clientName The name of the client (typically SBAC or SBAC_PT)
     * @param assessmentId The assessment identifier
     * @return A {@link Optional<ClientTestProperty>} for the specified client name and assessment id.
     */
    Optional<ClientTestProperty> findClientTestProperty(String clientName, String assessmentId);

    /**
     * Get the {@link ClientSystemFlag} for the specified client and audit name.
     * <p>
     *     The audit name is the name/description of the {@link ClientSystemFlag}.  Ideally the source of data for this
     *     method will be an in-memory collection; the {@link ClientSystemFlag} values rarely change, making them a
     *     strong candidate for caching.
     * </p>
     *
     * @param clientName  The name of the client (typically SBAC or SBAC_PT)
     * @param auditObject The name of the desired {@link ClientSystemFlag}
     * @return The {@link Optional<ClientSystemFlag>} that matches the client and audit name.
     */
    Optional<ClientSystemFlag> findClientSystemFlag(String clientName, String auditObject);

    Optional<CurrentExamWindow> getExamWindow(long studentId, String clientName, String assessmentId, int shiftWindowStart, int shiftWindowEnd);
}
