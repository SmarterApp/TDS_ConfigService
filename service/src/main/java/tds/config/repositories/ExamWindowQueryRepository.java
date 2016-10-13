package tds.config.repositories;

import java.util.List;
import java.util.Optional;

import tds.config.model.AssessmentProperties;
import tds.config.model.AssessmentWindow;

public interface ExamWindowQueryRepository {
    List<AssessmentWindow> findCurrentExamWindows(String clientName, String assessmentId, int shiftWindowStart, int shiftWindowEnd, int sessionType);

    List<AssessmentWindow> findCurrentExamFormWindows(String clientName, String assessmentId, int sessionType, int shiftWindowStart, int shiftWindowEnd, int shiftFormStart, int shiftFormEnd);

    Optional<AssessmentProperties> findExamFormWindowProperties(String clientName, String assessmentId, int sessionType);
}
