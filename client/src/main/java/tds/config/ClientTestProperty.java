package tds.config;

/**
 * Represents a record from the {#code configs.client_testproperties} table.
 */
public class ClientTestProperty {
    private String clientName;
    private String assessmentId;
    private Integer maxOpportunities;
    private int prefetch;
    private boolean isSelectable;
    private String label;
    private String subjectName;
    private boolean initialAbilityBySubject;
    private String accommodationFamily;
    private Integer sortOrder;
    private String rtsFormField;
    private boolean requireRtsWindow;
    private String rtsModeField;
    private boolean requireRtsMode;
    private boolean requireRtsModeWindow;
    private boolean deleteUnansweredItems;
    private Double abilitySlope;
    private Double abilityIntercept;
    private boolean validateCompleteness;
    private String gradeText;
    private String rtsWindowField;

    public static class Builder {
        private String clientName;
        private String assessmentId;
        private Integer maxOpportunities;
        private int prefetch;
        private boolean isSelectable;
        private String label;
        private String subjectName;
        private boolean initialAbilityBySubject;
        private String accommodationFamily;
        private Integer sortOrder;
        private String rtsFormField;
        private boolean requireRtsWindow;
        private String rtsModeField;
        private boolean requireRtsMode;
        private boolean requireRtsModeWindow;
        private boolean deleteUnansweredItems;
        private Double abilitySlope;
        private Double abilityIntercept;
        private boolean validateCompleteness;
        private String gradeText;
        private String rtsWindowField;
        
        public Builder withClientName(String newClientName) {
            this.clientName = newClientName;
            return this;
        }
        
        public Builder withAssessmentId(String newAssessmentId) {
            this.assessmentId = newAssessmentId;
            return this;
        }
        
        public Builder withMaxOpportunities(Integer newMaxOpportunities) {
            this.maxOpportunities = newMaxOpportunities;
            return this;
        }
        
        public Builder withPrefetch(int newPrefetch) {
            this.prefetch = newPrefetch;
            return this;
        }
        
        public Builder withIsSelectable(boolean newIsSelectable) {
            this.isSelectable = newIsSelectable;
            return this;
        }
        
        public Builder withLabel(String newLabel) {
            this.label = newLabel;
            return this;
        }
        
        public Builder withSubjectName(String newSubjectName) {
            this.subjectName = newSubjectName;
            return this;
        }
        
        public Builder withInitialAbilityBySubject(boolean newInitialAbilityBySubject) {
            this.initialAbilityBySubject = newInitialAbilityBySubject;
            return this;
        }
        
        public Builder withAccommodationFamily(String newAccommodationFamily) {
            this.accommodationFamily = newAccommodationFamily;
            return this;
        }
        
        public Builder withSortOrder(Integer newSortOrder) {
            this.sortOrder = newSortOrder;
            return this;
        }
        
        public Builder withRtsFormField(String newRtsFormField) {
            this.rtsFormField = newRtsFormField;
            return this;
        }
        
        public Builder withRequireRtsWindow(boolean newRequireRtsWindow) {
            this.requireRtsWindow = newRequireRtsWindow;
            return this;
        }
        
        public Builder withRtsModeField(String newRtsModeField) {
            this.rtsModeField = newRtsModeField;
            return this;
        }
        
        public Builder withRequireRtsMode(boolean newRequireRtsMode) {
            this.requireRtsMode = newRequireRtsMode;
            return this;
        }
        
        public Builder withRequireRtsModeWindow(boolean newRequireRtsModeWindow) {
            this.requireRtsModeWindow = newRequireRtsModeWindow;
            return this;
        }
        
        public Builder withDeleteUnansweredItems(boolean newDeleteUnansweredItems) {
            this.deleteUnansweredItems = newDeleteUnansweredItems;
            return this;
        }
        
        public Builder withAbilitySlope(Double newAbilitySlope) {
            this.abilitySlope = newAbilitySlope;
            return this;
        }
        
        public Builder withAbilityIntercept(Double newAbilityIntercept) {
            this.abilityIntercept = newAbilityIntercept;
            return this;
        }
        
        public Builder withValidateCompleteness(boolean newValidateCompleteness) {
            this.validateCompleteness = newValidateCompleteness;
            return this;
        }
        
        public Builder withGradeText(String newGradeText) {
            this.gradeText = newGradeText;
            return this;
        }

        public Builder withRtsWindowField(String rtsWindowField) {
            this.rtsWindowField = rtsWindowField;
            return this;
        }
        
        public ClientTestProperty build() { return new ClientTestProperty(this); }
    }
    
    private ClientTestProperty(Builder builder) {
        this.clientName = builder.clientName;
        this.assessmentId = builder.assessmentId;
        this.maxOpportunities = builder.maxOpportunities;
        this.prefetch = builder.prefetch;
        this.isSelectable = builder.isSelectable;
        this.label = builder.label;
        this.subjectName = builder.subjectName;
        this.initialAbilityBySubject = builder.initialAbilityBySubject;
        this.accommodationFamily = builder.accommodationFamily;
        this.sortOrder = builder.sortOrder;
        this.rtsFormField = builder.rtsFormField;
        this.requireRtsWindow = builder.requireRtsWindow;
        this.rtsModeField = builder.rtsModeField;
        this.requireRtsMode = builder.requireRtsMode;
        this.requireRtsModeWindow = builder.requireRtsModeWindow;
        this.deleteUnansweredItems = builder.deleteUnansweredItems;
        this.abilitySlope = builder.abilitySlope;
        this.abilityIntercept = builder.abilityIntercept;
        this.validateCompleteness = builder.validateCompleteness;
        this.gradeText = builder.gradeText;
        this.rtsWindowField = builder.rtsWindowField;
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

    /**
     * The value for this getter/setter is typically referred to as "testid" in the database.
     *
     * @return The unique identifier of the assessment to which this {@link ClientTestProperty} is associated.
     */
    public String getAssessmentId() {
        return assessmentId;
    }

    /**
     * @return The maximum number of times a Student may take this Assessment.
     */
    public Integer getMaxOpportunities() {
        return maxOpportunities;
    }

    public int getPrefetch() {
        return prefetch;
    }

    public boolean getIsSelectable() {
        return isSelectable;
    }

    public String getLabel() {
        return label;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public boolean getInitialAbilityBySubject() {
        return initialAbilityBySubject;
    }

    public String getAccommodationFamily() {
        return accommodationFamily;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public String getRtsFormField() {
        return rtsFormField;
    }

    public boolean getRequireRtsWindow() {
        return requireRtsWindow;
    }

    public String getRtsModeField() {
        return rtsModeField;
    }

    public boolean getRequireRtsMode() {
        return requireRtsMode;
    }

    public boolean getRequireRtsModeWindow() {
        return requireRtsModeWindow;
    }

    public boolean getDeleteUnansweredItems() {
        return deleteUnansweredItems;
    }

    public Double getAbilitySlope() {
        return abilitySlope;
    }

    public Double getAbilityIntercept() {
        return abilityIntercept;
    }

    public boolean getValidateCompleteness() {
        return validateCompleteness;
    }

    public String getGradeText() {
        return gradeText;
    }

    public String getRtsWindowField() {
        return rtsWindowField;
    }
}

