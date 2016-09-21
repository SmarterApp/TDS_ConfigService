package tds.config.repositories;

import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.TimeLimits;

import java.security.cert.PKIXRevocationChecker;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for interacting with the {@code configs} database.
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
    Optional<ClientTestProperty> getClientTestProperty(String clientName, String assessmentId);

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
    List<ClientSystemFlag> getClientSystemFlags(String clientName);

    /**
     * Get the time limits from the {@code session.timelimits} view for the specified client name.
     * <p>
     *     Because the {@link TimeLimits} values will rarely change, they are strong candidates for caching.
     * </p>
     *
     * @param clientName The client name for which the {@link TimeLimits} record should be fetched.
     * @return An {@link Optional<TimeLimits>} representing the time limit configuration values; otherwise
     *         {@code Optional.empty()}.
     */
    Optional<TimeLimits> getTimeLimits(String clientName);

    /**
     * Get the time limits from the {@code session.timelimits} view for the specified client name and assessment id.
     * <p>
     *     Because the {@link TimeLimits} values will rarely change, they are strong candidates for caching.
     * </p>
     *
     * @param clientName The client name for which the {@link TimeLimits} record should be fetched.
     * @param assessmentId The identifier of the assessment for which the {@link TimeLimits} record should be fetched.
     * @return An {@link Optional<Integer>} representing the TA Check-In time limit in minutes; otherwise
     *         {@code Optional.empty()}.
     */
    Optional<TimeLimits> getTimeLimits(String clientName, String assessmentId);
}
