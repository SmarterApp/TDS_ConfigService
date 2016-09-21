package tds.config;

/**
 * Represents a record from the {@code session.timelimits} view.
 * <p>
 *     While the data for instances of this class is pulled from the {@code session} database, it resides in the
 *     Config service because almost all of the fields for the {@code session.timelimits} view actually come from the
 *     {@code configs.client_timelimits} table.  The only fields that come from the {@code session._externs} table are
 *     the client name and enviroment.  Both of these fields are also stored in the {@code configs.client_timelimits}
 *     table, although they store different values:
 *
 *     SELECT clientname, _efk_testid, environment FROM configs.client_timelimits;
 *
 *     # clientname, _efk_testid, environment
 *       'SBAC_PT', NULL, NULL
 *       'SBAC', NULL, NULL
 *
 *     SELECT clientname, _efk_testid, environment FROM session.timelimits;
 *
 *     # clientname, _efk_testid, environment
 *       'SBAC', NULL, 'Development'
 *       'SBAC_PT', NULL, 'Development'
 *
 *     For reasons that are not entirely clear, The legacy code specifically references the {@code session._externs}
 *     table for the client name and environment fields, so that behavior has been carried forward.
 * </p>
 */
public class TimeLimits {
    private String clientName;
    private String environment;
    private String assessmentId;
    private Integer examRestartWindowMinutes;
    private Integer examDelayDays;
    private Integer interfaceTimeoutMinutes;
    private Integer requestInterfaceTimeoutMinutes;
    private Integer taCheckinTimeMinutes;

    /**
     * This client name corresponds to one of the values stored in the {@code configs.client_externs} table or the
     * {@code session._externs} table.  Typically this value will be "SBAC" or "SBAC_PT".
     *
     * @return The name of the client that owns these {@link TimeLimits}.
     */
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * In some sections of the legacy codebase, there is special handling for "Development" or "SIMULATION"
     * environments.
     *
     * @return The name of the environment that owns these {@link TimeLimits}.
     */
    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    /**
     * @return The identifier of the assessment (referred to as "test" in legacy code) to which these {@link TimeLimits}
     * apply.
     */
    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    /**
     * Referred to as "delay" in {@code StudentDLL.T_StartTestOpportunity_SP} @ line 5310.  Data for this field comes
     * from the {@code session.timelimits.opprestart} column.
     *
     * @return Number of minutes in which an exam can be restarted.
     */
    public Integer getExamRestartWindowMinutes() {
        return examRestartWindowMinutes;
    }

    /**
     * Referred to as "delay" in {@code StudentDLL.T_StartTestOpportunity_SP} @ line 5310.
     * <p>
     *     Logic @ line 3689 in {@code StudentDLL.T_StartTestOpportunity_SP} states that if
     *     {@code session.timeouts.opprestart} is null, it should be set to 1.
     * </p>
     *
     * @param examRestartWindowMinutes the value that {@code opportunityRestartMinutes} should be set to.
     */
    public void setExamRestartWindowMinutes(Integer examRestartWindowMinutes) {
        this.examRestartWindowMinutes = examRestartWindowMinutes == null
        ? 1
        : examRestartWindowMinutes;
    }

    /**
     * The number of days that must pass before a student can resume/restart the same exam.  Data for this field comes
     * from the {@code session.timelimits.oppdelay} column.
     *
     * @return The number of days before an opportunity can be retaken.
     */
    public Integer getExamDelayDays() {
        return examDelayDays;
    }

    public void setExamDelayDays(Integer examDelayDays) {
        this.examDelayDays = examDelayDays;
    }

    /**
     * This definition came from the {@code tds.student.sql.data.TestConfig} getter.  Data for this field comes from the
     * {@code session.timelimits.interfacetimeout} column.
     *
     * @return The number of minutes a student can be idle before logging them out.
     */
    public Integer getInterfaceTimeoutMinutes() {
        return interfaceTimeoutMinutes;
    }

    public void setInterfaceTimeoutMinutes(Integer interfaceTimeoutMinutes) {
        this.interfaceTimeoutMinutes = interfaceTimeoutMinutes;
    }

    /**
     * This definition came from the {@code tds.student.sql.data.TestConfig} getter.  Data for this field comes from the
     * {@code session.timelimits.requestinterfacetimeout} column.
     *
     * @return The number of minutes a student can be idle after making a print request before logging them out.
     */
    public Integer getRequestInterfaceTimeoutMinutes() {
        return requestInterfaceTimeoutMinutes;
    }

    public void setRequestInterfaceTimeoutMinutes(Integer requestInterfaceTimeoutMinutes) {
        this.requestInterfaceTimeoutMinutes = requestInterfaceTimeoutMinutes;
    }

    /**
     * Data for this field comes from the {@code session.timelimits.tacheckintime} column.
     *
     * @return The number of minutes before the TA CheckIn time expires.
     */
    public Integer getTaCheckinTimeMinutes() {
        return taCheckinTimeMinutes;
    }

    public void setTaCheckinTimeMinutes(Integer taCheckinTimeMinutes) {
        this.taCheckinTimeMinutes = taCheckinTimeMinutes;
    }
}
