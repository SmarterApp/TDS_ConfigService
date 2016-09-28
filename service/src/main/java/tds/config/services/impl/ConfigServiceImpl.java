package tds.config.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.repositories.ClientTestPropertyQueryRepository;
import tds.config.repositories.ConfigRepository;
import tds.config.services.ConfigService;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository configRepository;
    private final ClientTestPropertyQueryRepository clientTestPropertyQueryRepository;

    @Autowired
    public ConfigServiceImpl(ConfigRepository configRepository, ClientTestPropertyQueryRepository clientTestPropertyQueryRepository) {
        this.configRepository = configRepository;
        this.clientTestPropertyQueryRepository = clientTestPropertyQueryRepository;
    }

    @Override
    public Optional<ClientTestProperty> findClientTestProperty(final String clientName, final String assessmentId) {
        return clientTestPropertyQueryRepository.findClientTestProperty(clientName, assessmentId);
    }

    @Override
    public Optional<ClientSystemFlag> findClientSystemFlag(String clientName, String auditObject) {
        List<ClientSystemFlag> clientSystemFlags = configRepository.findClientSystemFlags(clientName);

        return clientSystemFlags.stream()
                .filter(f -> f.getAuditObject().equals(auditObject))
                .findFirst();
    }
}
