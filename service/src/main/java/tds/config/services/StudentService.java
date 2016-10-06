package tds.config.services;

import java.util.Optional;

import tds.student.RtsStudentPackageAttribute;

/**
 * Process interactions with student data
 */
public interface StudentService {

    /**
     * Finds the rts attribute in the student package
     *
     * @param clientName    client name for the installation
     * @param studentId     the student's id
     * @param attributeName the attribute name to find
     * @return {@link tds.student.RtsStudentPackageAttribute} if found otherwise empty
     */
    Optional<RtsStudentPackageAttribute> findRtsStudentPackageAttribute(String clientName, long studentId, String attributeName);
}
