package tds.config.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.repositories.ConfigRepository;
import tds.config.services.ConfigService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ConfigServiceImpl implements ConfigService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigServiceImpl.class);
    private final ConfigRepository configRepository;

    @Autowired
    public ConfigServiceImpl(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public ClientTestProperty getClientTestProperty(final String clientName, final String assessmentId) throws NoSuchElementException {
        ClientTestProperty clientTestProperty = configRepository.getClientTestProperty(clientName, assessmentId);

        if (clientTestProperty == null) {
            String message = "Could not find ClientTestProperty for client name " + clientName + " and assessment id " + assessmentId;
            throw new NoSuchElementException(message);
        }

        return clientTestProperty;
    }

    @Override
    public ClientSystemFlag getClientSystemFlag(String clientName, String auditObject) throws NoSuchElementException {
        List<ClientSystemFlag> clientSystemFlags = configRepository.getClientSystemFlags(clientName);

        return clientSystemFlags.stream().filter(f -> f.getAuditObject().equals(auditObject))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Could not find ClientSystemFlag for client name " + clientName + " and audit object " + auditObject));
    }
}
