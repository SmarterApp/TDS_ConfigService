package tds.config.model;

public class AssessmentProperties {
    private boolean requireForm;
    private boolean requireIfFormExists;
    private String formField;
    private boolean requireFormWindow;

    public AssessmentProperties(boolean requireForm,
                                boolean requireIfFormExists,
                                String formField,
                                boolean requireFormWindow) {
        this.requireForm = requireForm;
        this.requireIfFormExists = requireIfFormExists;
        this.formField = formField;
        this.requireFormWindow = requireFormWindow;
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
}
