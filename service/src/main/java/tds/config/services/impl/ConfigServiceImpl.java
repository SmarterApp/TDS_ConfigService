package tds.config.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.TimeLimits;
import tds.config.repositories.ConfigRepository;
import tds.config.services.ConfigService;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigServiceImpl implements ConfigService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigServiceImpl.class);
    private final ConfigRepository configRepository;

    @Autowired
    public ConfigServiceImpl(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public Optional<ClientTestProperty> getClientTestProperty(final String clientName, final String assessmentId) {
        return configRepository.getClientTestProperty(clientName, assessmentId);
    }

    @Override
    public Optional<ClientSystemFlag> getClientSystemFlag(String clientName, String auditObject) {
        List<ClientSystemFlag> clientSystemFlags = configRepository.getClientSystemFlags(clientName);

        return clientSystemFlags.stream()
                .filter(f -> f.getAuditObject().equals(auditObject))
                .findFirst();
    }

    @Override
    public Optional<Integer> getTaCheckInTimeLimit(final String clientName) {
        Optional<TimeLimits> timeLimits =  configRepository.getTimeLimits(clientName);

        if (timeLimits.isPresent()) {
            return Optional.of(
                    timeLimits.map(TimeLimits::getTaCheckinTimeMinutes)
                    .orElse(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TimeLimits> getTimeLimits(String clientName, String assessmentId) {
        return configRepository.getTimeLimits(clientName, assessmentId)
                .map(Optional::of)
                .orElseGet(() -> configRepository.getTimeLimits(clientName));
    }
}
