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
