package tds.config.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.model.CurrentExamWindow;
import tds.config.model.ExamWindowProperties;
import tds.config.repositories.ClientTestFormPropertiesQueryRepository;
import tds.config.repositories.ClientTestPropertyQueryRepository;
import tds.config.repositories.ConfigRepository;
import tds.config.repositories.ExamWindowQueryRepository;
import tds.config.services.ConfigService;
import tds.config.services.StudentService;
import tds.student.RtsStudentPackageAttribute;

@Service
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository configRepository;
    private final ClientTestPropertyQueryRepository clientTestPropertyQueryRepository;
    private final ExamWindowQueryRepository examWindowQueryRepository;
    private final ClientTestFormPropertiesQueryRepository clientTestFormPropertiesQueryRepository;
    private final StudentService studentService;

    @Autowired
    public ConfigServiceImpl(ConfigRepository configRepository,
                             ClientTestPropertyQueryRepository clientTestPropertyQueryRepository,
                             ExamWindowQueryRepository examWindowQueryRepository,
                             ClientTestFormPropertiesQueryRepository clientTestFormPropertiesQueryRepository,
                             StudentService studentService) {
        this.configRepository = configRepository;
        this.clientTestPropertyQueryRepository = clientTestPropertyQueryRepository;
        this.examWindowQueryRepository = examWindowQueryRepository;
        this.clientTestFormPropertiesQueryRepository = clientTestFormPropertiesQueryRepository;
        this.studentService = studentService;

    }

    @Override
    public Optional<ClientTestProperty> findClientTestProperty(final String clientName, final String assessmentId) {
        return clientTestPropertyQueryRepository.findClientTestProperty(clientName, assessmentId);
    }

    @Override
    public Optional<ClientSystemFlag> findClientSystemFlag(String clientName, String auditObject) {
        List<ClientSystemFlag> clientSystemFlags = configRepository.findClientSystemFlags(clientName);

        return clientSystemFlags.stream()
            .filter(f -> f.getAuditObject().equals(auditObject))
            .findFirst();
    }

    @Override
    public Optional<CurrentExamWindow> getExamWindow(ExamWindowProperties examWindowProperties) {
        long studentId = examWindowProperties.getStudentId();
        String clientName = examWindowProperties.getClientName();
        String assessmentId = examWindowProperties.getAssessmentId();
        String windowList = examWindowProperties.getWindowList();

        if (studentId < 0) {
            //Lines 5871-5880 StudentDLL._GetTestteeTestWindows_SP()
            return examWindowQueryRepository.findCurrentTestWindowsForGuest(clientName, assessmentId, examWindowProperties.getShiftWindowStart(), examWindowProperties.getShiftWindowEnd());
        }

        //Lines 5881-5893 StudentDLL._GetTestteeTestWindows_SP()
        boolean requireWindow = false;
        String windowField = null;
        String tideId = null;

        Optional<ClientTestProperty> maybeClientTestPropertyProperties = clientTestPropertyQueryRepository.findClientTestProperty(clientName, assessmentId);
        if (maybeClientTestPropertyProperties.isPresent()) {
            ClientTestProperty clientTestProperty = maybeClientTestPropertyProperties.get();
            requireWindow = clientTestProperty.getRequireRtsWindow();
            windowField = clientTestProperty.getRtsWindowField();
            tideId = clientTestProperty.getTideId();
        }

        //Lines 5894-5898 StudentDLL._GetTestteeTestWindows_SP()
        boolean isFormTest = clientTestFormPropertiesQueryRepository.findClientTestFormProperty(clientName, assessmentId).isPresent();

        //Lines 5908-5911 StudentDLL._GetTestteeTestWindows_SP()
        requireWindow = windowList != null || windowField != null;

        if (requireWindow) {
            if (windowList == null) {
                Optional<RtsStudentPackageAttribute> maybeAttribute = studentService.findRtsStudentPackageAttribute(clientName, studentId, windowField);
                if (maybeAttribute.isPresent()) {
                    windowList = maybeAttribute.get().getValue();
                }
            }

            List<Map<String, String>> windows = new ArrayList<>();
            if(tideId != null && windowList != null) {
                windows = createWindowsList(windowList, tideId);
            }

        }



        Optional<Float> op = Optional.of(1F);



        return Optional.empty();
    }

    //StudentDLL._GetTesteeTestWindows_SP Lines 5926 - 5951
    private List<Map<String, String>> createWindowsList(String windowList, String tideId) {
        String[] windowNames = windowList.split(";");
        List<Map<String, String>> windows = new ArrayList<>();
        for (String windowName : windowNames) {
            String likeRec1 = String.format ("%s:", tideId);
            Map<String, String> record = new HashMap<>();
            int idx;
            if (windowName.startsWith (likeRec1) && (idx = windowName.indexOf (':')) != -1) {
                String win = windowName.substring (idx + 1);
                record.put ("win", win);
                windows.add(record);
            }
        }

        return windows;
    }
}
