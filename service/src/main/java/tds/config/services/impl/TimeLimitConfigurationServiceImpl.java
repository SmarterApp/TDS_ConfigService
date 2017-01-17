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
    public TimeLimitConfigurationServiceImpl(TimeLimitConfigurationRepository timeLimitConfigurationRepository) {
        this.timeLimitConfigurationRepository = timeLimitConfigurationRepository;
    }

    @Override
    @Cacheable(CacheType.MEDIUM_TERM)
    public Optional<TimeLimitConfiguration> findTimeLimitConfiguration(String clientName) {
        return this.findTimeLimitConfiguration(clientName, null);
    }

    @Override
    @Cacheable(CacheType.MEDIUM_TERM)
    public Optional<TimeLimitConfiguration> findTimeLimitConfiguration(String clientName, String assessmentId) {
        Optional<TimeLimitConfiguration> maybeTimeLimitConfig;

        if (assessmentId == null) {
            return timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName);
        }

        // RULE:  If time limit configuration cannot be found for clientName + assessmentId, get time limit configuration
        // for clientName.
        maybeTimeLimitConfig = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName, assessmentId);
        if (!maybeTimeLimitConfig.isPresent()) {
            maybeTimeLimitConfig = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName);
        }

        return maybeTimeLimitConfig;
    }
}
