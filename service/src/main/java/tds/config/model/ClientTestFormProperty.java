package tds.config.model;

/**
 * Client Fixed Form test configuration
 */
public class ClientTestFormProperty {
    private String clientName;
    private String assessmentId;
    private String clientFormId;

    /**
     * @param clientName client name associated with the test form
     * @param assessmentId assessment id for the assessment
     * @param clientFormId client form id
     */
    public ClientTestFormProperty(String clientName, String assessmentId, String clientFormId) {
        this.clientName = clientName;
        this.assessmentId = assessmentId;
        this.clientFormId = clientFormId;
    }

    /**
     * For frameworks
     */
    private ClientTestFormProperty(){}

    /**
     * @return associated client name
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @return assessment id
     */
    public String getAssessmentId() {
        return assessmentId;
    }

    /**
     * @return client form id
     */
    public String getClientFormId() {
        return clientFormId;
    }
}
