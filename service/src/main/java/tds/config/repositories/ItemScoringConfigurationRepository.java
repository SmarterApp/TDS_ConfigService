package tds.config.repositories;

import java.util.List;

import tds.student.sql.data.ItemScoringConfig;

/**
 * Repository for item scoring configurations
 */
public interface ItemScoringConfigurationRepository {
    /**
     * Finds the item scoring configurations for the client
     *
     * @param clientName the client name for scoring
     * @param siteName   the site name
     * @param serverName the server name
     * @return List of {@link tds.student.sql.data.ItemScoringConfig} for client
     */
    List<ItemScoringConfig> findItemScoringConfigs(final String clientName, final String siteName, final String serverName);
}
