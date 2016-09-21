package tds.config;

import java.sql.Timestamp;

/**
 Represents a record in the {@code configs.client_systemflags} table.
 */
public class ClientSystemFlag {
    private String auditObject = "";
    private String clientName = "";
    private Boolean isPracticeTest;
    private Boolean isOn;
    private String description;
    private Timestamp dateChanged;
    private Timestamp datePublished;

    public ClientSystemFlag() { }

    public ClientSystemFlag(String auditObject, String clientName) {
        this.setAuditObject(auditObject);
        this.setClientName(clientName);
    }

    /**
     * Examples of audit object names: AnonymousTestee, latencies, SuppressScores.
     *
     * @return The type of thing this {@link ClientSystemFlag} represents/is related to.
     */
    public String getAuditObject() {
        return auditObject;
    }

    public void setAuditObject(String auditObject) {
        this.auditObject = auditObject;
    }

    /**
     * This client name corresponds to one of the values stored in the {@code configs.client_externs} table or the
     * {@code session._externs} table.  Typically this value will be "SBAC" or "SBAC_PT".
     *
     * @return The name of the client that owns this {@link ClientTestProperty}.
     */
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return Identify if the {@link ClientSystemFlag} is associated/relevant to practice assessments.
     */
    public Boolean getIsPracticeTest() {
        return isPracticeTest;
    }

    public void setIsPracticeTest(Boolean practiceTest) {
        isPracticeTest = practiceTest;
    }

    /**
     * @return {@code True} if the {@link ClientSystemFlag} is enabled; otherwise {@code False}.
     */
    public Boolean getIsOn() {
        return isOn;
    }

    public void setIsOn(Boolean on) {
        isOn = on;
    }

    /**
     * @return Text describing the affect of the {@link ClientSystemFlag}.
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The most recent date and time when the {@link ClientSystemFlag} was changed.
     */
    public Timestamp getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Timestamp dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Timestamp getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Timestamp datePublished) {
        this.datePublished = datePublished;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!(other instanceof ClientSystemFlag)) {
            return false;
        }

        if (this == other) {
            return true;
        }

        ClientSystemFlag that = (ClientSystemFlag)other;
        return this.getAuditObject().equals(that.getAuditObject())
                && this.getClientName().equals(that.getClientName());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(this);
    }
}
