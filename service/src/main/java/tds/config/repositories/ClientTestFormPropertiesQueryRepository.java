package tds.config.repositories;

import java.util.Optional;

import tds.config.model.ClientTestFormProperty;

/**
 *
 */
public interface ClientTestFormPropertiesQueryRepository {
    /**
     * Finds a single {@link tds.config.model.ClientTestFormProperty} for client name and assessment
     *
     * @param clientName client name
     * @param assessmentId assessment id
     * @return {@link tds.config.model.ClientTestFormProperty} if found otherwise empty
     */
    Optional<ClientTestFormProperty> findClientTestFormProperty(String clientName, String assessmentId);
}
