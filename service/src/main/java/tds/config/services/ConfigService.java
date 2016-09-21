package tds.config.services;

import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.TimeLimits;

import java.util.NoSuchElementException;
import java.util.Optional;

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
     * @return A {@link Optional<ClientTestProperty>} for the specified client name and assessment id.
     */
    Optional<ClientTestProperty> getClientTestProperty(String clientName, String assessmentId);

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
    Optional<ClientSystemFlag> getClientSystemFlag(String clientName, String auditObject);

    /**
     * Get the TA Check-In time limit for the specified client.
     * <p>
     *     This is a special method to fetch the {@code tacheckintime} from the {@code session.timelimits} view.  Unlike
     *     the other timeout values, the code in {@code StudentDLL._ValidateTesteeAccessProc_SP} on line 1117
     *     specifically queries the {@code session.timelimits} view for the client name where the assessment id is null.
     *     The assumption here is that the TA Check-In time is always configured at the client level, where other time
     *     limits can be configured at the assessment level.  Furthermore, the TA Check-In time setting at the client
     *     level supercedes any setting at the test level.  This is the only value in {@code session.timelimits} that
     *     exhibits this behavior.
     * </p>
     * <p>
     *     Because of the logic in the legacy Student code, the TA Check-In time limit can never be null.  If a check in
     *     value is not found in the database, it is set to 0.
     * </p>
     * <p>
     *     Because these values will rarely change, it is a strong candidate for caching.
     * </p>
     *
     * @param clientName The name of the client for which the TA Check-In Time should be fetched.
     * @return An {@code int} representing the length of the TA Check-In timeout window in minutes.  If no value is
     *         present for the specified client name, 0 will be returned.
     */
    Optional<Integer> getTaCheckInTimeLimit(String clientName);

    /**
     * Get the {@link TimeLimits} configuration values from the {@code session.timelimits} view.
     * <p>
     *     The logic in {@code StudentDLL.T_StartTestOpportunity_SP} starting at line 3668 is as follows:
     *     -- Attempt to find a record in {@code session.timelimits} for the specified assessment id and client name.
     *     -- If there is no such record, find a record in {@code session.timelimits} for the specified client name
     *        where the assessment id is null.
     *
     *     The assumption for this logic is that the program attempts to find time limit values that are specific to the
     *     assessment being taken, otherwise it falls back the client-wide time limit settings.
     * </p>
     * <p>
     *     When the {@code session} database is loaded with seed data, all records returned by the {@code timelimits}
     *     view have a null assessment id (the {@code _efk_testid} column).
     * </p>
     * <p>
     *     Because these values will rarely change, it is a strong candidate for caching.
     * </p>
     *
     * @param clientName the name of the client for which the {@code TestSessionTimelimitConfiguration} should be
     *                   fetched.
     * @param assessmentId The Id of the assessment for which the {@code TestSessionTimelimitConfiguration} should be
     *                     fetched.
     * @return A {@link TimeLimits} for the assessment id and client name.
     */
    Optional<TimeLimits> getTimeLimits(String clientName, String assessmentId);
}
