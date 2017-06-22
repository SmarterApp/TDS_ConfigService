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

import java.util.List;

import tds.config.ClientSystemFlag;

/**
 * Data Access for interacting with the {@code configs} database.
 */
public interface ConfigRepository {
    /**
     * Get all the {@link tds.config.ClientSystemFlag} records from the {@code configs.client_systemflags} database for the
     * specified client name.
     * <p>
     * The implementation of this method should account for the The {@code session.externs} view.  The {@code JOIN}
     * to the {@code session.externs} view came from looking at the SQL contained in the
     * {@code CommonDLL.selectIsOnByAuditObject} method.
     * </p>
     * <p>
     * Because the {@link tds.config.ClientSystemFlag} values will rarely change, they are strong candidates for caching.
     * </p>
     *
     * @param clientName The client name for which the {@link tds.config.ClientSystemFlag} records should be fetched.
     * @return A collection of {@link tds.config.ClientSystemFlag} records for the specified client name.
     */
    List<ClientSystemFlag> findClientSystemFlags(final String clientName);
}
