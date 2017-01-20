package tds.config;

/**
 * Represents time limit configuration settings for the client and/or assessment.
 * <p>
 *     Time limits can be configured at two levels:
 *     <ul>
 *         <li>
 *             <strong>Assessment:</strong> The {@code session.timelimits.clientname} and
 *             {@code session.timelimits._efk_testid} will both have values.  With one exception, these time limit
 *             configuration values should override the settings configured at the Client level.<br/>
 *             <strong>Exception: </strong> The TA Check-in value should always come from the Client level, even if
 *             there are time limit settings at the Assessment level.
 *         </li>
 *         <li>
 *             <strong>Client:</strong> The {@code session.timelimits.clientname} has a value but the
 *             {@code session.timelimits._efk_testid} is NULL.  These time limit configuration values will be assigned
 *             to any assessment owned by the client name.
 *         </li>
 *     </ul>
 * </p>
 * <p>
 *     While the data for instances of this class is pulled from the the {@code timelimits} view {@code session}
 *     database, it resides in the Config service because almost all of the fields for the {@code session.timelimits}
 *     view actually come from the {@code configs.client_timelimits} table.  The only fields that come from the
 *     {@code session._externs} table are the client name and enviroment.  Both of these fields are also stored in the
 *     {@code configs.client_timelimits} table, although they store different values:
 *
 *     <pre>
 *         {@code
 *              SELECT clientname, _efk_testid, environment FROM configs.client_timelimits;
 *
 *              # clientname, _efk_testid, environment
 *              'SBAC_PT', NULL, NULL
 *              'SBAC', NULL, NULL
 *
 *              SELECT clientname, _efk_testid, environment FROM session.timelimits;
 *
 *              # clientname, _efk_testid, environment
 *              'SBAC', NULL, 'Development'
 *              'SBAC_PT', NULL, 'Development'
 *          }
 *     <pre>
 *
 *     For reasons that are not entirely clear, The legacy code specifically references the {@code session._externs}
 *     table for the client name and environment fields, so that behavior has been carried forward.
 * </p>
 */
public class TimeLimitConfiguration {
    private String clientName;
    private String environment;
    private String assessmentId;
    private int examRestartWindowMinutes;
    private int examDelayDays;
    private Integer interfaceTimeoutMinutes;
    private int requestInterfaceTimeoutMinutes;
    private Integer taCheckinTimeMinutes;

    public static class Builder {
        private String clientName;
        private String environment;
        private String assessmentId;
        private int examRestartWindowMinutes;
        private int examDelayDays;
        private Integer interfaceTimeoutMinutes;
        private int requestInterfaceTimeoutMinutes;
        private Integer taCheckinTimeMinutes;

        public Builder withClientName(String clientName) {
            this.clientName = clientName;
            return this;
        }

        public Builder withEnvironment(String environment) {
            this.environment = environment;
            return this;
        }

        public Builder withAssessmentId(String assessmentId) {
            this.assessmentId = assessmentId;
            return this;
        }

        public Builder withExamRestartWindowMinutes(int examRestartWindowMinutes) {
            this.examRestartWindowMinutes = examRestartWindowMinutes;
            return this;
        }

        public Builder withExamDelayDays(int newExamDelayDays) {
            this.examDelayDays = newExamDelayDays;
            return this;
        }

        public Builder withInterfaceTimeoutMinutes(Integer interfaceTimeoutMinutes) {
            this.interfaceTimeoutMinutes = interfaceTimeoutMinutes;
            return this;
        }

        public Builder withRequestInterfaceTimeoutMinutes(int requestInterfaceTimeoutMinutes) {
            this.requestInterfaceTimeoutMinutes = requestInterfaceTimeoutMinutes;
            return this;
        }

        public Builder withTaCheckinTimeMinutes(Integer taCheckinTimeMinutes) {
            this.taCheckinTimeMinutes = taCheckinTimeMinutes;
            return this;
        }

        public TimeLimitConfiguration build() {
            return new TimeLimitConfiguration(this);
        }
    }

    private TimeLimitConfiguration(Builder builder) {
        this.clientName = builder.clientName;
        this.environment = builder.environment;
        this.assessmentId = builder.assessmentId;
        this.examRestartWindowMinutes = builder.examRestartWindowMinutes;
        this.examDelayDays = builder.examDelayDays;
        this.interfaceTimeoutMinutes = builder.interfaceTimeoutMinutes;
        this.requestInterfaceTimeoutMinutes = builder.requestInterfaceTimeoutMinutes;
        this.taCheckinTimeMinutes = builder.taCheckinTimeMinutes;
    }

    private TimeLimitConfiguration() {
        //For frameworks
    }

    /**
     * @return The name of the client that owns these {@link TimeLimitConfiguration}.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @return The name of the environment that owns these {@link TimeLimitConfiguration}.
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * @return The identifier of the assessment (referred to as "test" in legacy code) to which these {@link TimeLimitConfiguration}
     * apply.
     */
    public String getAssessmentId() {
        return assessmentId;
    }

    /**
     * @return Number of minutes in which an exam can be restarted.
     */
    public int getExamRestartWindowMinutes() {
        return examRestartWindowMinutes;
    }

    /**
     * @return The number of days that must pass before a student can resume/restart the same exam.
     */
    public int getExamDelayDays() {
        return examDelayDays;
    }

    /**
     * @return The number of minutes a student can be idle before logging them out.
     */
    public Integer getInterfaceTimeoutMinutes() {
        return interfaceTimeoutMinutes;
    }

    /**
     * @return The number of minutes a student can be idle after making a print request before logging them out.
     */
    public int getRequestInterfaceTimeoutMinutes() {
        return requestInterfaceTimeoutMinutes;
    }

    /**
     * @return The number of minutes before the TA CheckIn time expires.
     */
    public Integer getTaCheckinTimeMinutes() {
        return taCheckinTimeMinutes;
    }
}
