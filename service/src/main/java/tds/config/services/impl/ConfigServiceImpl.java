package tds.config.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import tds.common.cache.CacheType;
import tds.config.ClientSystemFlag;
import tds.config.repositories.ConfigRepository;
import tds.config.services.ConfigService;

@Service
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository configRepository;

    @Autowired
    public ConfigServiceImpl(final ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    @Cacheable(CacheType.LONG_TERM)
    public Optional<ClientSystemFlag> findClientSystemFlag(final String clientName, final String type) {
        /*
            The type is the name of the ClientSystemFlag.  Ideally the source of data for this
            method will be an in-memory collection; ClientSystemFlag values rarely change, making them a
            strong candidate for caching.
        */

        List<ClientSystemFlag> clientSystemFlags = configRepository.findClientSystemFlags(clientName);

        return clientSystemFlags.stream()
            .filter(f -> f.getAuditObject().equals(type))
            .findFirst();
    }
}
