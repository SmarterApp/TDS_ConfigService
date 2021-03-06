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

package tds.config.repositories;

import java.util.Optional;

import tds.config.TimeLimitConfiguration;

/**
 * Data Access for interacting with the time limit-related configuration stored within the {@code session} database.
 */
public interface TimeLimitConfigurationRepository {
    /**
     * Get the time limits from the {@code session.timelimits} view for the specified client name.
     * <p>
     * Because the {@link TimeLimitConfiguration} values will rarely change, they are strong candidates for caching.
     * </p>
     *
     * @param clientName The client name for which the {@link TimeLimitConfiguration} record should be fetched.
     * @return An {@link Optional <TimeLimitConfiguration>} representing the time limit configuration values; otherwise
     * {@code Optional.empty()}.
     */
    Optional<TimeLimitConfiguration> findTimeLimitConfiguration(final String clientName);

    /**
     * Get the time limits from the {@code session.timelimits} view for the specified client name and assessment id.
     * <p>
     * The TA check-in value should always come from the client level, even if a record is found for the client
     * name/assessment id combination.  To get the TA check-in value at the "client name" level, look in the
     * {@code session.timelimits} view for the specified client name where the assessment id is NULL.
     * </p>
     * <p>
     * Because the {@link TimeLimitConfiguration} values will rarely change, they are strong candidates for caching.
     * </p>
     *
     * @param clientName   The client name for which the {@link TimeLimitConfiguration} record should be fetched.
     * @param assessmentId The identifier of the assessment for which the {@link TimeLimitConfiguration} record should be fetched.
     * @return An {@link Optional<Integer>} representing the TA Check-In time limit in minutes; otherwise
     * {@code Optional.empty()}.
     */
    Optional<TimeLimitConfiguration> findTimeLimitConfiguration(final String clientName, final String assessmentId);
}
