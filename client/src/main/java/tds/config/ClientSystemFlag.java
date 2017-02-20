package tds.config;

import org.joda.time.Instant;

/**
 Represents a record in the {@code configs.client_systemflags} table.
 */
public class ClientSystemFlag {
    public static final String ALLOW_ANONYMOUS_STUDENT_FLAG_TYPE = "AnonymousTestee";
    public static final String RESTORE_ACCOMMODATIONS_TYPE = "RestoreAccommodations";

    private String auditObject = "";
    private String clientName = "";
    private boolean isPracticeTest;
    private boolean enabled;
    private String description;
    private Instant dateChanged;
    private Instant datePublished;

    public static class Builder {
        private String auditObject = "";
        private String clientName = "";
        private boolean isPracticeTest;
        private boolean enabled;
        private String description;
        private Instant dateChanged;
        private Instant datePublished;

        public Builder withAuditObject(final String auditObject) {
            this.auditObject = auditObject;
            return this;
        }

        public Builder withClientName(final String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder withIsPracticeTest(final boolean isPracticeTest) {
            this.isPracticeTest = isPracticeTest;
            return this;
        }

        public Builder withEnabled(final boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public Builder withDateChanged(final Instant dateChanged) {
            this.dateChanged = dateChanged;
            return this;
        }

        public Builder withDatePublished(final Instant datePublished) {
            this.datePublished = datePublished;
            return this;
        }

        public ClientSystemFlag build() {
            return new ClientSystemFlag(this);
        }
    }

    private ClientSystemFlag(final Builder builder) {
        this.auditObject = builder.auditObject;
        this.clientName = builder.clientName;
        this.isPracticeTest = builder.isPracticeTest;
        this.enabled = builder.enabled;
        this.description = builder.description;
        this.dateChanged = builder.dateChanged;
        this.datePublished = builder.datePublished;
    }

    /**
     * only present for frameworks
     */
    private ClientSystemFlag() {
    }


    /**
     * @return The type of thing this {@link ClientSystemFlag} represents/is related to.
     */
    public String getAuditObject() {
        return auditObject;
    }

    /**
     *  @return The name of the client that owns this {@link ClientSystemFlag}.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @return Identify if the {@link ClientSystemFlag} is associated/relevant to practice assessments.
     */
    public boolean getIsPracticeTest() {
        return isPracticeTest;
    }

    /**
     * <p>
     *     Cannot be null in {@code configs.client_systemflags} table.  This value is an  {#code INT(11)} in the
     *     database, but is treated like a boolean value in the legacy code's logic.  An example of how this property is
     *     treated (edited for clarity):
     *     <pre>
     *         {@code
     *             select IsOn as flag from  ${ConfigDB}.client_systemflags F, externs E  where E.ClientName = ${clientname} and F.clientname = ${clientname} and E.IsPracticeTest = F.IsPracticeTest and AuditOBject = ${responses}
     *             // ... execute query...
     *             if (DbComparator.isEqual (flag, 0)) {
     *                 return 0;
     *             } else {
     *                 return 1;
     *             }
     *         }
     *     </pre>
     * </p>
     *
     * @return {@code True} if the {@link ClientSystemFlag} is enabled; otherwise {@code False}.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @return Text describing the affect of the {@link ClientSystemFlag}.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The most recent date and time when the {@link ClientSystemFlag} was changed.
     */
    public Instant getDateChanged() {
        return dateChanged;
    }

    /**
     * @return the date published for the flag
     */
    public Instant getDatePublished() {
        return datePublished;
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
                && this.getClientName().equals(that.getClientName())
                && this.getIsPracticeTest() == (that.getIsPracticeTest());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hashCode(this);
    }
}
