package tds.config.model;

public class ExamWindowProperties {
    private long studentId;
    private String clientName;
    private String assessmentId;
    private int sessionType;
    private int shiftWindowStart;
    private int shiftWindowEnd;
    private int shiftFormStart;
    private int shiftFormEnd;
    private String windowList;
    private String formList;

    private ExamWindowProperties(Builder builder){
        this.studentId = builder.studentId;
        this.clientName = builder.clientName;
        this.assessmentId = builder.assessmentId;
        this.shiftWindowStart = builder.shiftWindowStart;
        this.shiftWindowEnd = builder.shiftWindowEnd;
        this.shiftFormStart = builder.shiftFormStart;
        this.shiftFormEnd = builder.shiftFormEnd;
        this.windowList = builder.windowList;
        this.formList = builder.formList;
        this.sessionType = builder.sessionType;
    }

    public long getStudentId() {
        return studentId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public int getSessionType() {
        return sessionType;
    }

    public int getShiftWindowStart() {
        return shiftWindowStart;
    }

    public int getShiftWindowEnd() {
        return shiftWindowEnd;
    }

    public int getShiftFormStart() {
        return shiftFormStart;
    }

    public int getShiftFormEnd() {
        return shiftFormEnd;
    }

    public String getFormList() {
        return formList;
    }

    public static class Builder {
        private long studentId;
        private String clientName;
        private String assessmentId;
        private int sessionType;
        private int shiftWindowStart = 0;
        private int shiftWindowEnd = 0;
        private int shiftFormStart = 0;
        private int shiftFormEnd = 0;
        private String windowList;
        private String formList;

        public Builder(long studentId, String clientName, String assessmentId, int sessionType) {
            this.studentId = studentId;
            this.clientName = clientName;
            this.assessmentId = assessmentId;
            this.sessionType = sessionType;
        }

        public Builder withShiftWindowStart(int shiftWindowStart) {
            this.shiftWindowStart = shiftWindowStart;
            return this;
        }

        public Builder withShiftWindowEnd(int shiftWindowEnd) {
            this.shiftWindowEnd = shiftWindowEnd;
            return this;
        }

        public Builder withShiftFormStart(int shiftFormStart) {
            this.shiftFormStart = shiftFormStart;
            return this;
        }

        public Builder withShiftFormEnd(int shiftFormEnd) {
            this.shiftFormEnd = shiftFormEnd;
            return this;
        }

        public Builder withWindowList(String windowList) {
            this.windowList = windowList;
            return this;
        }

        public Builder withFormList(String formList) {
            this.formList = formList;
            return this;
        }

        public ExamWindowProperties build() {
            return new ExamWindowProperties(this);
        }
    }
}
