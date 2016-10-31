package tds.config.services;

import java.util.List;
import java.util.Optional;

import tds.config.AssessmentWindow;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.model.AssessmentWindowParameters;

/**
 * Provide an interface for interacting with the {@code configs} database and providing configuration information.
 */
public interface ConfigService {
    /**
     * Get all the {@link ClientTestProperty} records from the {@code configs.client_testproperties} table for the
     * client and test.
     *
     * @param clientName   The name of the client (typically SBAC or SBAC_PT)
     * @param assessmentId The assessment identifier
     * @return A {@link Optional<ClientTestProperty>} for the specified client name and assessment id.
     */
    Optional<ClientTestProperty> findClientTestProperty(String clientName, String assessmentId);

    /**
     * Get the {@link ClientSystemFlag} for the specified client and audit name.
     *
     * @param clientName  The name of the client (typically SBAC or SBAC_PT)
     * @param type The type of the desired {@link ClientSystemFlag}
     * @return The {@link Optional<ClientSystemFlag>} that matches the client and audit name.
     */
    Optional<ClientSystemFlag> findClientSystemFlag(String clientName, String type);

    /**
     * Finds the {@link tds.config.AssessmentWindow AssessmentWindow} for the given {@link tds.config.model.AssessmentWindowParameters}
     *
     * @param assessmentWindowParameters {@link tds.config.model.AssessmentWindowParameters} propeties for the assessment window
     * @return list of {@link tds.config.AssessmentWindow} that fit the parameters
     */
    List<AssessmentWindow> findAssessmentWindows(AssessmentWindowParameters assessmentWindowParameters);
}
