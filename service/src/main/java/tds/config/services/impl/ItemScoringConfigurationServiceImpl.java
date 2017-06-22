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

package tds.config.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import tds.common.cache.CacheType;
import tds.config.repositories.ItemScoringConfigurationRepository;
import tds.config.services.ItemScoringConfigurationService;
import tds.student.sql.data.ItemScoringConfig;

@Service
public class ItemScoringConfigurationServiceImpl implements ItemScoringConfigurationService {
    private final ItemScoringConfigurationRepository itemScoringConfigurationRepository;

    @Autowired
    public ItemScoringConfigurationServiceImpl(final ItemScoringConfigurationRepository itemScoringConfigurationRepository) {
        this.itemScoringConfigurationRepository = itemScoringConfigurationRepository;
    }

    @Override
    @Cacheable(CacheType.LONG_TERM)
    public List<ItemScoringConfig> findItemScoringConfigs(final String clientName, final String siteName, final String serverName) {
        return itemScoringConfigurationRepository.findItemScoringConfigs(clientName, siteName, serverName);
    }
}
