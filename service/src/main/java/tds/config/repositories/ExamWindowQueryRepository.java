package tds.config.repositories;

import java.util.List;
import java.util.Optional;

import tds.config.model.CurrentExamWindow;
import tds.config.model.ExamFormWindow;

public interface ExamWindowQueryRepository {
    Optional<CurrentExamWindow> findCurrentTestWindowsForGuest(String clientName, String assessmentId, int shiftWindowStart, int shiftWindowEnd);

    List<ExamFormWindow> findExamFormWindows(String clientName, String assessmentId, int sessionType, int shiftWindowStart, int shiftWindowEnd, int shiftFormStart, int shiftFormEnd);
}
