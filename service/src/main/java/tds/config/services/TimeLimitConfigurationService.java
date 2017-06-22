/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.config.services;

import java.util.Optional;

import tds.config.TimeLimitConfiguration;

/**
 * Provide an interface for interacting with the {@code session} database and providing time limit configuration.
 * <p>
 * The logic for getting time limit configuration is as follows:
 * <ul>
 * <li>Attempt to find a record in {@code session.timelimits} for the specified assessment id and client name.</li>
 * <li>
 * If there is no such record, find a record in {@code session.timelimits} for the specified client name
 * where the assessment id is null.
 * </li>
 * </ul>
 * <p>
 * The assumption for this logic is that the program attempts to find time limit values that are specific to the
 * assessment being taken, otherwise it falls back the client-wide time limit settings.
 * </p>
 * <p>
 * When the {@code session} database is loaded with seed data, all records returned by the {@code timelimits}
 * view have a null assessment id (the {@code _efk_testid} column).
 * </p>
 * <p>
 * Because the results of these methods will rarely change, it is a strong candidate for caching.
 * </p>
 */
public interface TimeLimitConfigurationService {
    /**
     * Find the {@link TimeLimitConfiguration} configuration values from the {@code session.timelimits} view for the
     * specified client name and assessment id.
     * <p>
     * In the event this method does not find a record for the client name and assessment id, a record for the
     * specified client name and NULL assessment id should be returned.
     * </p>
     *
     * @param clientName   the name of the client for which the {@code TestSessionTimelimitConfiguration} should be
     *                     fetched.
     * @param assessmentId The Id of the assessment for which the {@code TestSessionTimelimitConfiguration} should be
     *                     fetched.
     * @return A {@link Optional <TimeLimitConfiguration>} for the assessment id and client name.
     */
    Optional<TimeLimitConfiguration> findTimeLimitConfiguration(final String clientName, final String assessmentId);

    /**
     * Find the {@link TimeLimitConfiguration} configuration values from the {@code session.timelimits} view for the
     * specified client name.
     *
     * @param clientName the name of the client for which the {@code TestSessionTimelimitConfiguration} should be
     *                   fetched.
     * @return A {@link Optional <TimeLimitConfiguration>} for the client name.
     */
    Optional<TimeLimitConfiguration> findTimeLimitConfiguration(final String clientName);
}
