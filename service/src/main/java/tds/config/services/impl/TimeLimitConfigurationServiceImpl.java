package tds.config.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tds.config.TimeLimitConfiguration;
import tds.config.repositories.TimeLimitConfigurationRepository;
import tds.config.services.TimeLimitConfigurationService;

import java.util.Optional;

@Service
public class TimeLimitConfigurationServiceImpl implements TimeLimitConfigurationService {
    private final TimeLimitConfigurationRepository timeLimitConfigurationRepository;

    @Autowired
    public TimeLimitConfigurationServiceImpl(TimeLimitConfigurationRepository timeLimitConfigurationRepository) {
        this.timeLimitConfigurationRepository = timeLimitConfigurationRepository;
    }

    @Override
    public Optional<TimeLimitConfiguration> findTimeLimitConfiguration(String clientName) {
        return timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName);
    }

    @Override
    public Optional<TimeLimitConfiguration> findTimeLimitConfiguration(String clientName, String assessmentId) {
        return timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName, assessmentId)
                .map(Optional::of)
                .orElseGet(() -> timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName));
    }
}
