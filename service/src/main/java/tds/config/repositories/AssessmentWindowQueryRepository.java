package tds.config.repositories;

import java.util.List;
import java.util.Optional;

import tds.config.AssessmentWindow;
import tds.config.model.AssessmentFormWindowProperties;

/**
 * Handles data access dealing with assessment windows
 */
public interface AssessmentWindowQueryRepository {
    /**
     * Find the current active assessment windows
     *
     * @param clientName       the client name for the installation
     * @param assessmentId     assessment id
     * @param shiftWindowStart the days to prepend for the window
     * @param shiftWindowEnd   the days to append for the window
     * @param sessionType      the type of session for the window
     * @return {@link tds.config.AssessmentWindow}
     */
    List<AssessmentWindow> findCurrentAssessmentWindows(String clientName, String assessmentId, int shiftWindowStart, int shiftWindowEnd, int sessionType);

    /**
     * Finds the current active assessment form windows
     *
     * @param clientName       the client name for the installation
     * @param assessmentId     assessment id
     * @param sessionType      the type of session
     * @param shiftWindowStart the days to shift for the start of window
     * @param shiftWindowEnd   the days to shift for the end of window
     * @param shiftFormStart   the days to shift for the start of the form
     * @param shiftFormEnd     the days to shift for the end of the form
     * @return
     */
    List<AssessmentWindow> findCurrentAssessmentFormWindows(String clientName, String assessmentId, int sessionType, int shiftWindowStart, int shiftWindowEnd, int shiftFormStart, int shiftFormEnd);

    /**
     * Finds the assessment form window properties
     *
     * @param clientName   client name of the installation
     * @param assessmentId assessment id
     * @param sessionType  the type of session
     * @return {@link tds.config.model.AssessmentFormWindowProperties} if found otherwise empty
     */
    Optional<AssessmentFormWindowProperties> findAssessmentFormWindowProperties(String clientName, String assessmentId, int sessionType);
}
