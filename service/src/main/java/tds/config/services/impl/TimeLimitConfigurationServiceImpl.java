package tds.config.services.impl;

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
    private final TimeLimitConfigurationRepository timeLimitConfigurationRepository;

    @Autowired
    public TimeLimitConfigurationServiceImpl(final TimeLimitConfigurationRepository timeLimitConfigurationRepository) {
        this.timeLimitConfigurationRepository = timeLimitConfigurationRepository;
    }

    @Override
    @Cacheable(CacheType.MEDIUM_TERM)
    public Optional<TimeLimitConfiguration> findTimeLimitConfiguration(final String clientName) {
        return timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName);
    }

    @Override
    @Cacheable(CacheType.MEDIUM_TERM)
    public Optional<TimeLimitConfiguration> findTimeLimitConfiguration(final String clientName, final String assessmentId) {
        Optional<TimeLimitConfiguration> maybeTimeLimitConfig;

        if (assessmentId == null) {
            return findTimeLimitConfiguration(clientName);
        }

        // RULE:  If time limit configuration cannot be found for clientName + assessmentId, get time limit configuration
        // for clientName.
        maybeTimeLimitConfig = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName, assessmentId);
        if (!maybeTimeLimitConfig.isPresent()) {
            maybeTimeLimitConfig = findTimeLimitConfiguration(clientName);
        }

        return maybeTimeLimitConfig;
    }
}
