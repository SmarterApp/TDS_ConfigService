package tds.config.repositories;

import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;

import java.util.List;
import java.util.Optional;

/**
 * Data Access for interacting with the {@code configs} database.
 */
public interface ConfigRepository {
    /**
     * Get the {@link ClientTestProperty} record from {@code configs.client_testproperties} for the specified client name
     * and test id.
     * <p>
     *     Because the {@link ClientTestProperty} values will rarely change, they are strong candidates for caching.
     * </p>
     *
     * @param clientName The client name for which the {@link ClientTestProperty} records should be fetched.
     * @param assessmentId The ID (which is the name) of the test for which the {@link ClientTestProperty} records
     *                     should be fetched.
     * @return A {@link ClientTestProperty} for the specified client name and test id.
     */
    Optional<ClientTestProperty> findClientTestProperty(String clientName, String assessmentId);

    /**
     * Get all the {@link ClientSystemFlag} records from the {@code configs.client_systemflags} database for the
     * specified client name.
     * <p>
     *     The implementation of this method should account for the The {@code session.externs} view.  The {@code JOIN}
     *     to the {@code session.externs} view came from looking at the SQL contained in the
     *     {@code CommonDLL.selectIsOnByAuditObject} method.
     * </p>
     * <p>
     *     Because the {@link ClientSystemFlag} values will rarely change, they are strong candidates for caching.
     * </p>
     *
     * @param clientName The client name for which the {@link ClientSystemFlag} records should be fetched.
     * @return A collection of {@link ClientSystemFlag} records for the specified client name.
     */
    List<ClientSystemFlag> findClientSystemFlags(String clientName);
}
