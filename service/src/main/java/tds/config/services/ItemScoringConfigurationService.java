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

import java.util.List;

import tds.student.sql.data.ItemScoringConfig;

/**
 * Service handling the item scoring configuration
 */
public interface ItemScoringConfigurationService {
    /**
     * Finds the item scoring configurations for the client
     *
     * @param clientName the client name for scoring
     * @param siteName   the site name
     * @param serverName the server name
     * @return List of {@link tds.student.sql.data.ItemScoringConfig} for client
     */
    List<ItemScoringConfig> findItemScoringConfigs(final String clientName, final String siteName, final String serverName);
}
