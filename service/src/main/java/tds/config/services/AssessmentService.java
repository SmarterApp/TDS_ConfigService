package tds.config.services;

import java.util.Optional;

import tds.assessment.Assessment;

public interface AssessmentService {
    Optional<Assessment> findAssessment(String assessmentId);
}
