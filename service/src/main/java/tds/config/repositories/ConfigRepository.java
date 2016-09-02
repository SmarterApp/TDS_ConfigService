package tds.config.repositories;

import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;

import java.util.List;

/**
 * Data Access Object for interacting with the {@code configs} database.
 */
public interface ConfigRepository {
    /**
     * Get the {@link ClientTestProperty} record from {@code configs.client_testproperties} for the specified client name
     * and test id.
     * <p>
     *     Because these values will rarely change, they are strong candidates for caching.
     * </p>
     *
     * @param clientName The client name for which the {@code ClientTestProperty} records should be fetched.
     * @param assessmentId The ID (which is the name) of the test for which the {@code ClientTestProperty} records should be fetched.
     * @return A {@code ClientTestProperty} for the specified client name and test id.
     */
    ClientTestProperty getClientTestProperty(String clientName, String assessmentId);

    /**
     * Get all the {@link ClientSystemFlag} records from the {@code configs.client_systemflags} database for the
     * specified client name.
     * <p>
     *     The implementation of this method should account for the The {@code session.externs} view.  The {@code JOIN}
     *     to the {@code session.externs} view came from looking at the SQL contained in the
     *     {@code CommonDLL.selectIsOnByAuditObject} method.
     * </p>
     * <p>
     *     Because these values will rarely change, they are strong candidates for caching.
     * </p>
     *
     * @param clientName The client name for which the {@code ClientSystemFlag} records should be fetched.
     * @return A collection of {@code ClientSystemFlag} records for the specified client name.
     */
    List<ClientSystemFlag> getClientSystemFlags(String clientName);
}
