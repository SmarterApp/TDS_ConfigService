package tds.config.model;

import java.time.Instant;

public class ExamFormWindow {
    private String windowId;
    private int windowMax;
    private int modeMax;
    private String formKey;
    private Instant startDate;
    private Instant endDate;
    private String mode;
    private String assessmentKey;

    private ExamFormWindow(Builder builder) {
        this.windowId = builder.windowId;
        this.windowMax = builder.windowMax;
        this.modeMax = builder.modeMax;
        this.formKey = builder.formKey;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.mode = builder.mode;
        this.assessmentKey = builder.assessmentKey;
    }

    public String getWindowId() {
        return windowId;
    }

    public int getWindowMax() {
        return windowMax;
    }

    public int getModeMax() {
        return modeMax;
    }

    public String getFormKey() {
        return formKey;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public String getMode() {
        return mode;
    }

    public String getAssessmentKey() {
        return assessmentKey;
    }

    public static class Builder {
        private String windowId;
        private int windowMax;
        private int modeMax;
        private String formKey;
        private Instant startDate;
        private Instant endDate;
        private String mode;
        private String assessmentKey;

        public Builder(String windowId, String assessmentKey, String formKey) {
            this.windowId = windowId;
            this.assessmentKey = assessmentKey;
            this.formKey = formKey;
        }

        public Builder withWindowMax(int windowMax) {
            this.windowMax = windowMax;
            return this;
        }

        public Builder withModeMax(int modeMax) {
            this.modeMax = modeMax;
            return this;
        }

        public Builder withStartDate(Instant startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(Instant endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder withMode(String mode) {
            this.mode = mode;
            return this;
        }

        public ExamFormWindow build() {
            return new ExamFormWindow(this);
        }
    }
}
