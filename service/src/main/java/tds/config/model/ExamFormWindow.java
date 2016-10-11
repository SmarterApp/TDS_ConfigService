package tds.config.model;

public class ExamFormWindow {
    private final String windowId;
    private final int windowMax;
    private final int modeMax;

    public ExamFormWindow(String windowId, int windowMax, int modeMax) {
        this.windowId = windowId;
        this.windowMax = windowMax;
        this.modeMax = modeMax;
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
}
