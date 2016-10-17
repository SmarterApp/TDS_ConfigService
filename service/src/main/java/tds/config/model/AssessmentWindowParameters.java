package tds.config.model;

/**
 * Contains the parameters for the assessment window lookup
 */
public class AssessmentWindowParameters {
    private long studentId;
    private String clientName;
    private String assessmentId;
    private int sessionType;
    private int shiftWindowStart;
    private int shiftWindowEnd;
    private int shiftFormStart;
    private int shiftFormEnd;
    private String formList;

    private AssessmentWindowParameters(Builder builder){
        this.studentId = builder.studentId;
        this.clientName = builder.clientName;
        this.assessmentId = builder.assessmentId;
        this.shiftWindowStart = builder.shiftWindowStart;
        this.shiftWindowEnd = builder.shiftWindowEnd;
        this.shiftFormStart = builder.shiftFormStart;
        this.shiftFormEnd = builder.shiftFormEnd;
        this.formList = builder.formList;
        this.sessionType = builder.sessionType;
    }

    /**
     * @return The unique id for the student
     */
    public long getStudentId() {
        return studentId;
    }

    /**
     * @return the client name
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @return the id for the assessment
     */
    public String getAssessmentId() {
        return assessmentId;
    }

    /**
     * @return the session type
     */
    public int getSessionType() {
        return sessionType;
    }

    /**
     * @return the days to shift the assessment window start
     */
    public int getShiftWindowStart() {
        return shiftWindowStart;
    }

    /**
     * @return the days to shift the assessment window end
     */
    public int getShiftWindowEnd() {
        return shiftWindowEnd;
    }

    /**
     * @return the days to shift the assessment form start
     */
    public int getShiftFormStart() {
        return shiftFormStart;
    }

    /**
     * @return the days to shift the assessment form end
     */
    public int getShiftFormEnd() {
        return shiftFormEnd;
    }

    /**
     * @return the form list containing form and window information
     */
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

        public Builder withFormList(String formList) {
            this.formList = formList;
            return this;
        }

        public AssessmentWindowParameters build() {
            return new AssessmentWindowParameters(this);
        }
    }
}
