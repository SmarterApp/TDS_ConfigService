package tds.config.model;

import java.time.Instant;

public class CurrentExamWindow {
    private String windowId;
    private int windowMaxAttempts;
    private String mode;
    private int modeMaxAttempts;
    private Instant startTime;
    private Instant endTime;
    private int windowSessionType;
    private int modeSessionType;

    /**
     * Empty constructor for frameworks
     */
    private CurrentExamWindow(){}

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

    public static class Builder {
        private CurrentExamWindow window = new CurrentExamWindow();

        public Builder withWindowId(String windowId) {
            window.windowId = windowId;
            return this;
        }

        public Builder withWindowMaxAttempts(int windowMaxAttempts) {
            window.windowMaxAttempts = windowMaxAttempts;
            return this;
        }

        public Builder withMode(String mode) {
            window.mode = mode;
            return this;
        }

        public Builder withModeMaxAttempts(int modeMaxAttempts) {
            window.modeMaxAttempts = modeMaxAttempts;
            return this;
        }

        public Builder withStartTime(Instant startTime) {
            window.startTime = startTime;
            return this;
        }

        public Builder withEndTime(Instant endTime) {
            window.endTime = endTime;
            return this;
        }

        public Builder withWindowSessionType(int windowSessionType) {
            window.windowSessionType = windowSessionType;
            return this;
        }

        public Builder withModeSessionType(int modeSessionType) {
            window.modeSessionType = modeSessionType;
            return this;
        }

        public CurrentExamWindow build() {
            return window;
        }
    }
}
