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
import tds.config.model.AssessmentProperties;
import tds.config.model.CurrentExamWindow;
import tds.config.model.ExamFormWindow;
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
    public Optional<CurrentExamWindow> getExamWindow(ExamWindowProperties examWindowProperties)  {
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

        //Lines StudentDLL 5955 - 5975
        List<ExamFormWindow> formWindows = examWindowQueryRepository.findExamFormWindows(clientName,
            assessmentId,
            examWindowProperties.getSessionType(),
            examWindowProperties.getShiftWindowStart(),
            examWindowProperties.getShiftWindowEnd(),
            examWindowProperties.getShiftFormStart(),
            examWindowProperties.getShiftFormEnd()
        );

        if (!formWindows.isEmpty()) {
            //Logic in StudentDLL lines 5963
            //The first call in the StudentDLL._GetTesteeTestForms_SP is to get the window for the guest which is duplication from earlier code

            return findCurrentExamWindowFromFormWindows(examWindowProperties, formWindows);
        }



        return Optional.empty();
    }

    private Optional<CurrentExamWindow> findCurrentExamWindowFromFormWindows(ExamWindowProperties examWindowProperties, List<ExamFormWindow> formWindows) {
        String tideId = null, formField = null;
        boolean requireFormWindow = false, requireForm = false, ifExists = false;

        //Lines 3712 - 3730 in StudentDLL._GetTesteeTestForms_SP
        Optional<AssessmentProperties> maybeAsessmentProperties = examWindowQueryRepository.findExamFormWindowProperties(examWindowProperties.getClientName(), examWindowProperties.getAssessmentId(), examWindowProperties.getSessionType());

        if (maybeAsessmentProperties.isPresent()) {
            AssessmentProperties properties = maybeAsessmentProperties.get();
            tideId = properties.getTideId();
            formField = properties.getFormField();
            ifExists = properties.isRequireIfFormExists();
        }

        String formList = examWindowProperties.getFormList();
        if (formList != null) {
            if (formList.indexOf (':') > -1)
                requireFormWindow = true;
            else {
                requireForm = true;
                requireFormWindow = false;
            }
        } else if (tideId != null && formField != null) {
            Optional<RtsStudentPackageAttribute> maybeAttribute = studentService.findRtsStudentPackageAttribute(
                examWindowProperties.getClientName(),
                examWindowProperties.getStudentId(),
                formField);

            if(maybeAttribute.isPresent()) {
                formList = maybeAttribute.get().getValue();
            }
        }

        //Key is a the form key and the value is the list of window ids associated with the form key
        Map<String, List<String>> studentPackageForms = new HashMap<>();

        //Lines 3753 - 3781 in StudentDLL._GetTesteeTestForms_SP
        String[] forms = formList.split(";");
        for (String formValue : forms) {
            String wid;
            String form;

            int idx;
            if ((idx = formValue.indexOf (":")) > -1) {// i.e. found
                wid = formValue.substring (0, idx);
                form = formValue.substring (idx + 1);
            } else {
                form = formValue;
                wid = null;
            }

            if(!studentPackageForms.containsKey(form)) {
                studentPackageForms.put(form, new ArrayList<>());
            }

            if (wid != null) {
                studentPackageForms.get(form).add(wid);
            }
        }

        //Lines 2786 - 2815 in StudentDLL._GetTesteeTestForms_SP
        if(requireFormWindow) {
            for (ExamFormWindow formWindow : formWindows) {
                String formKey = formWindow.getFormKey();
                if (studentPackageForms.containsKey(formKey) &&
                    studentPackageForms.get(formKey).contains(formWindow.getWindowId())) {

                    return Optional.of(convert(formWindow));
                }
            }
        } else if (requireForm || (ifExists && !studentPackageForms.isEmpty())) {
            for (ExamFormWindow formWindow : formWindows) {
                String formKey = formWindow.getFormKey();
                if (studentPackageForms.containsKey(formKey)) {
                    return Optional.of(convert(formWindow));
                }
            }
        } else {
            return Optional.of(convert(formWindows.get(0)));
        }

        return Optional.empty();
    }

    //StudentDLL._GetTesteeTestWindows_SP Lines 5926 - 5951
    private List<Map<String, String>> createWindowsList(String windowList, String tideId) {
        String[] windowNames = windowList.split(";");
        List<Map<String, String>> windows = new ArrayList<>();
        for (String windowName : windowNames) {
            String likeRec1 = String.format("%s:", tideId);
            Map<String, String> record = new HashMap<>();
            int idx;
            if (windowName.startsWith(likeRec1) && (idx = windowName.indexOf(':')) != -1) {
                String win = windowName.substring(idx + 1);
                record.put("win", win);
                windows.add(record);
            }
        }

        return windows;
    }

    private CurrentExamWindow convert(ExamFormWindow formWindow) {
        return new CurrentExamWindow.Builder()
            .withWindowId(formWindow.getWindowId())
            .withWindowMaxAttempts(formWindow.getWindowMax())
            .withStartTime(formWindow.getStartDate())
            .withEndTime(formWindow.getEndDate())
            .withFormKey(formWindow.getFormKey())
            .withMode(formWindow.getMode())
            //TODO - look into assessment id instead of test key
            .withAssessmentId(formWindow.getAssessmentKey())
            .build();
    }
}
