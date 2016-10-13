package tds.config.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import tds.config.AssessmentWindow;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.model.AssessmentProperties;
import tds.config.model.ExamWindowProperties;
import tds.config.repositories.AssessmentWindowQueryRepository;
import tds.config.repositories.ClientTestPropertyQueryRepository;
import tds.config.repositories.ConfigRepository;
import tds.config.services.ConfigService;
import tds.config.services.StudentService;
import tds.student.RtsStudentPackageAttribute;

@Service
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository configRepository;
    private final ClientTestPropertyQueryRepository clientTestPropertyQueryRepository;
    private final AssessmentWindowQueryRepository assessmentWindowQueryRepository;
    private final StudentService studentService;

    @Autowired
    public ConfigServiceImpl(ConfigRepository configRepository,
                             ClientTestPropertyQueryRepository clientTestPropertyQueryRepository,
                             AssessmentWindowQueryRepository assessmentWindowQueryRepository,
                             StudentService studentService) {
        this.configRepository = configRepository;
        this.clientTestPropertyQueryRepository = clientTestPropertyQueryRepository;
        this.assessmentWindowQueryRepository = assessmentWindowQueryRepository;
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
    public List<AssessmentWindow> getExamWindow(ExamWindowProperties examWindowProperties) {
        long studentId = examWindowProperties.getStudentId();
        String clientName = examWindowProperties.getClientName();
        String assessmentId = examWindowProperties.getAssessmentId();
        String windowList = examWindowProperties.getWindowList();

        //Lines StudentDLL 5955 - 5975
        List<AssessmentWindow> examFormWindows = assessmentWindowQueryRepository.findCurrentAssessmentFormWindows(clientName,
            assessmentId,
            examWindowProperties.getSessionType(),
            examWindowProperties.getShiftWindowStart(),
            examWindowProperties.getShiftWindowEnd(),
            examWindowProperties.getShiftFormStart(),
            examWindowProperties.getShiftFormEnd()
        );

        if (!examFormWindows.isEmpty()) {
            //Logic in StudentDLL lines 5963
            //The first call in the StudentDLL._GetTesteeTestForms_SP is to get the window for the guest which is duplication from earlier code
            return findCurrentExamWindowFromFormWindows(examWindowProperties, examFormWindows);
        }

        //Lines 5871-5880 StudentDLL._GetTestteeTestWindows_SP()
        List<AssessmentWindow> assessmentWindows = assessmentWindowQueryRepository.findCurrentAssessmentWindows(clientName,
            assessmentId,
            examWindowProperties.getShiftWindowStart(),
            examWindowProperties.getShiftWindowEnd(),
            examWindowProperties.getSessionType());

        if (studentId < 0) {
            return assessmentWindows;
        }

        //Lines 5881-5893 StudentDLL._GetTestteeTestWindows_SP()
        boolean requireWindow = true;
        String windowField = null;
        String tideId = null;

        Optional<ClientTestProperty> maybeClientTestPropertyProperties = clientTestPropertyQueryRepository.findClientTestProperty(clientName, assessmentId);
        if (maybeClientTestPropertyProperties.isPresent()) {
            ClientTestProperty clientTestProperty = maybeClientTestPropertyProperties.get();
            windowField = clientTestProperty.getRtsWindowField();
            tideId = clientTestProperty.getTideId();
        }

        //Lines 5908-5911 StudentDLL._GetTestteeTestWindows_SP()

        if (windowList != null) {
            requireWindow = true;
        } else if (windowField == null) {
            requireWindow = false;
        }

        Set<String> windowListIds = new HashSet<>();
        if (requireWindow && tideId != null) {
            if (windowList == null) {
                Optional<RtsStudentPackageAttribute> maybeAttribute = studentService.findRtsStudentPackageAttribute(clientName, studentId, windowField);
                if (maybeAttribute.isPresent()) {
                    windowList = maybeAttribute.get().getValue();
                }
            }

            if (windowList != null) {
                windowListIds = createWindowsSet(windowList, tideId);
            }

            //Lines 5989 - 6011 in StudentDLL._GetTesteeTestWindows_SP - Comments below from original code
            //NOT A FIXED FORM TEST. Determine if the WINDOW has been assigned to the student
            //test windows are recorded by TIDE_ID (in lieu of testID), which is not necessarily unique.
            final Set<String> windowIdJoinSet = windowListIds;
            return assessmentWindows.stream().filter(assessmentWindow -> windowIdJoinSet.contains(assessmentWindow.getWindowId()))
                .filter(distinctByKey(AssessmentWindow::getWindowId))
                .collect(Collectors.toList());
        }

        //Lines 6001-6011 in StudentDLL._GetTesteeTestWindows_SP
        return assessmentWindows.stream()
            .filter(distinctByKey(AssessmentWindow::getWindowId))
            .collect(Collectors.toList());
    }

    private List<AssessmentWindow> findCurrentExamWindowFromFormWindows(ExamWindowProperties examWindowProperties, List<AssessmentWindow> formWindows) {
        String tideId = null, formField = null;
        boolean requireFormWindow = false, requireForm = false, ifExists = false;

        //Lines 3712 - 3730 in StudentDLL._GetTesteeTestForms_SP
        Optional<AssessmentProperties> maybeAssessmentProperties = assessmentWindowQueryRepository.findAssessmentFormWindowProperties(examWindowProperties.getClientName(), examWindowProperties.getAssessmentId(), examWindowProperties.getSessionType());

        if (maybeAssessmentProperties.isPresent()) {
            AssessmentProperties properties = maybeAssessmentProperties.get();
            tideId = properties.getTideId();
            formField = properties.getFormField();
            ifExists = properties.isRequireIfFormExists();
        }

        String formList = examWindowProperties.getFormList();
        if (formList != null) {
            if (formList.indexOf(':') > -1)
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

            if (maybeAttribute.isPresent()) {
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
            if ((idx = formValue.indexOf(":")) > -1) {// i.e. found
                wid = formValue.substring(0, idx);
                form = formValue.substring(idx + 1);
            } else {
                form = formValue;
                wid = null;
            }

            if (!studentPackageForms.containsKey(form)) {
                studentPackageForms.put(form, new ArrayList<>());
            }

            if (wid != null) {
                studentPackageForms.get(form).add(wid);
            }
        }

        //Lines 2786 - 2815 in StudentDLL._GetTesteeTestForms_SP
        if (requireFormWindow) {
            return formWindows.stream().filter(assessmentWindow -> {
                String formKey = assessmentWindow.getFormKey();
                return studentPackageForms.containsKey(formKey) &&
                    studentPackageForms.get(formKey).contains(assessmentWindow.getWindowId());
            }).filter(distinctByKey(AssessmentWindow::getWindowId)).collect(Collectors.toList());
        } else if (requireForm || (ifExists && !studentPackageForms.isEmpty())) {
            return formWindows.stream().filter(assessmentWindow ->
                studentPackageForms.containsKey(assessmentWindow.getFormKey()))
                .filter(distinctByKey(AssessmentWindow::getWindowId))
                .collect(Collectors.toList());
        }

        return formWindows.stream()
            .filter(distinctByKey(AssessmentWindow::getWindowId))
            .collect(Collectors.toList());
    }

    //StudentDLL._GetTesteeTestWindows_SP Lines 5926 - 5951
    private Set<String> createWindowsSet(String windowList, String tideId) {
        String[] windowNames = windowList.split(";");
        Set<String> windows = new HashSet<>();
        for (String windowName : windowNames) {
            String tideIdFormat = String.format("%s:", tideId);
            int idx;
            if (windowName.startsWith(tideIdFormat) && (idx = windowName.indexOf(':')) != -1) {
                windows.add(windowName.substring(idx + 1));
            }
        }

        return windows;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
