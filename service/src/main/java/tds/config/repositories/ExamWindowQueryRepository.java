package tds.config.repositories;

import java.util.Optional;

import tds.config.model.CurrentExamWindow;

public interface ExamWindowQueryRepository {
    Optional<CurrentExamWindow> findCurrentTestWindowsForGuest(String clientName, String assessmentId, int shiftWindowStart, int shiftWindowEnd);
}
