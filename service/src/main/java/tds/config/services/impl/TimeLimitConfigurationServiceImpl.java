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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import tds.common.cache.CacheType;
import tds.config.TimeLimitConfiguration;
import tds.config.repositories.TimeLimitConfigurationRepository;
import tds.config.services.TimeLimitConfigurationService;

@Service
public class TimeLimitConfigurationServiceImpl implements TimeLimitConfigurationService {
    private static final Logger LOG = LoggerFactory.getLogger(TimeLimitConfigurationServiceImpl.class);

    private final TimeLimitConfigurationRepository timeLimitConfigurationRepository;

    @Autowired
    public TimeLimitConfigurationServiceImpl(final TimeLimitConfigurationRepository timeLimitConfigurationRepository) {
        this.timeLimitConfigurationRepository = timeLimitConfigurationRepository;
    }

    @Override
    @Cacheable(CacheType.MEDIUM_TERM)
    public Optional<TimeLimitConfiguration> findTimeLimitConfiguration(final String clientName) {
        LOG.debug("Fetching assessment for client: {}", clientName);
        return timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName);
    }

    @Override
    @Cacheable(CacheType.MEDIUM_TERM)
    public Optional<TimeLimitConfiguration> findTimeLimitConfiguration(final String clientName, final String assessmentId) {
        Optional<TimeLimitConfiguration> maybeTimeLimitConfig;

        if (assessmentId == null) {
            LOG.debug("Assessment Id is null, calling method without assessment id");
            return timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName);
        }

        // RULE:  If time limit configuration cannot be found for clientName + assessmentId, get time limit configuration
        // for clientName.
        LOG.debug("Fetching assessment for client: {} assessment: {}", clientName, assessmentId);
        maybeTimeLimitConfig = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName, assessmentId);
        if (!maybeTimeLimitConfig.isPresent()) {
            LOG.debug("Time limit is null, calling method without assessment id");
            maybeTimeLimitConfig = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName);
        }

        return maybeTimeLimitConfig;
    }
}
