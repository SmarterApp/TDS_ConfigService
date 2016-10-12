package tds.config.model;

public class AssessmentProperties {
    private boolean requireForm;
    private boolean requireIfFormExists;
    private String formField;
    private boolean requireFormWindow;
    private String tideId;

    public AssessmentProperties(boolean requireForm,
                                boolean requireIfFormExists,
                                String formField,
                                boolean requireFormWindow,
                                String tideId) {
        this.requireForm = requireForm;
        this.requireIfFormExists = requireIfFormExists;
        this.formField = formField;
        this.requireFormWindow = requireFormWindow;
        this.tideId = tideId;
    }

    public boolean isRequireForm() {
        return requireForm;
    }

    public boolean isRequireIfFormExists() {
        return requireIfFormExists;
    }

    public String getFormField() {
        return formField;
    }

    public boolean isRequireFormWindow() {
        return requireFormWindow;
    }

    public String getTideId() {
        return tideId;
    }
}
