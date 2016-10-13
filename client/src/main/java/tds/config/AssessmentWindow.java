package tds.config;

import java.time.Instant;

public class AssessmentWindow {
    private String windowId;
    private int windowMaxAttempts;
    private String mode;
    private int modeMaxAttempts;
    private Instant startTime;
    private Instant endTime;
    private int windowSessionType;
    private int modeSessionType;
    private String formKey;
    private String assessmentId;

    private AssessmentWindow(AssessmentWindow.Builder builder) {
        windowId = builder.windowId;
        windowMaxAttempts = builder.windowMaxAttempts;
        mode = builder.mode;
        modeMaxAttempts = builder.modeMaxAttempts;
        startTime = builder.startTime;
        endTime = builder.endTime;
        windowSessionType = builder.windowSessionType;
        modeSessionType = builder.modeSessionType;
        formKey = builder.formKey;
        assessmentId = builder.assessmentId;
    }

    /**
     * Empty constructor for frameworks
     */
    private AssessmentWindow(){}

    public String getWindowId() {
        return windowId;
    }

    public int getWindowMaxAttempts() {
        return windowMaxAttempts;
    }

    public String getMode() {
        return mode;
    }

    public int getModeMaxAttempts() {
        return modeMaxAttempts;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public int getWindowSessionType() {
        return windowSessionType;
    }

    public int getModeSessionType() {
        return modeSessionType;
    }

    public String getFormKey() {
        return formKey;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public static class Builder {
        private String windowId;
        private int windowMaxAttempts;
        private String mode;
        private int modeMaxAttempts;
        private Instant startTime;
        private Instant endTime;
        private int windowSessionType;
        private int modeSessionType;
        private String formKey;
        private String assessmentId;

        public Builder withWindowId(String windowId) {
            this.windowId = windowId;
            return this;
        }

        public Builder withWindowMaxAttempts(int windowMaxAttempts) {
            this.windowMaxAttempts = windowMaxAttempts;
            return this;
        }

        public Builder withMode(String mode) {
            this.mode = mode;
            return this;
        }

        public Builder withModeMaxAttempts(int modeMaxAttempts) {
            this.modeMaxAttempts = modeMaxAttempts;
            return this;
        }

        public Builder withStartTime(Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder withEndTime(Instant endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder withWindowSessionType(int windowSessionType) {
            this.windowSessionType = windowSessionType;
            return this;
        }

        public Builder withModeSessionType(int modeSessionType) {
            this.modeSessionType = modeSessionType;
            return this;
        }

        public Builder withFormKey(String formKey) {
            this.formKey = formKey;
            return this;
        }

        public Builder withAssessmentId(String assessmentId) {
            this.assessmentId = assessmentId;
            return this;
        }

        public AssessmentWindow build() {
            return new AssessmentWindow(this);
        }
    }
}
