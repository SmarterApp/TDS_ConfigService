package tds.config.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import tds.config.model.AssessmentFormWindowProperties;
import tds.config.model.AssessmentWindowParameters;
import tds.config.repositories.AssessmentWindowQueryRepository;
import tds.config.repositories.ClientTestPropertyQueryRepository;
import tds.config.repositories.ConfigRepository;
import tds.config.services.ConfigService;

@Service
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository configRepository;
    private final ClientTestPropertyQueryRepository clientTestPropertyQueryRepository;
    private final AssessmentWindowQueryRepository assessmentWindowQueryRepository;

    @Autowired
    public ConfigServiceImpl(ConfigRepository configRepository,
                             ClientTestPropertyQueryRepository clientTestPropertyQueryRepository,
                             AssessmentWindowQueryRepository assessmentWindowQueryRepository) {
        this.configRepository = configRepository;
        this.clientTestPropertyQueryRepository = clientTestPropertyQueryRepository;
        this.assessmentWindowQueryRepository = assessmentWindowQueryRepository;
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
    public List<AssessmentWindow> findAssessmentWindows(AssessmentWindowParameters assessmentWindowParameters) {
        long studentId = assessmentWindowParameters.getStudentId();
        String clientName = assessmentWindowParameters.getClientName();
        String assessmentId = assessmentWindowParameters.getAssessmentId();

        //Lines StudentDLL 5955 - 5975
        List<AssessmentWindow> examFormWindows = assessmentWindowQueryRepository.findCurrentAssessmentFormWindows(clientName,
            assessmentId,
            assessmentWindowParameters.getSessionType(),
            assessmentWindowParameters.getShiftWindowStart(),
            assessmentWindowParameters.getShiftWindowEnd(),
            assessmentWindowParameters.getShiftFormStart(),
            assessmentWindowParameters.getShiftFormEnd()
        );

        if (!examFormWindows.isEmpty()) {
            //Logic in StudentDLL lines 5963
            //The first call in the StudentDLL._GetTesteeTestForms_SP is to get the window for the guest which is duplication from earlier code
            return findCurrentExamWindowFromFormWindows(assessmentWindowParameters, examFormWindows);
        }

        //Lines 5871-5880 StudentDLL._GetTestteeTestWindows_SP()
        List<AssessmentWindow> assessmentWindows = assessmentWindowQueryRepository.findCurrentAssessmentWindows(clientName,
            assessmentId,
            assessmentWindowParameters.getShiftWindowStart(),
            assessmentWindowParameters.getShiftWindowEnd(),
            assessmentWindowParameters.getSessionType());

        if (studentId < 0) {
            return assessmentWindows;
        }

        //Lines 6001-6011 in StudentDLL._GetTesteeTestWindows_SP
        return assessmentWindows.stream()
            .filter(distinctByKey(AssessmentWindow::getWindowId))
            .collect(Collectors.toList());
    }

    private List<AssessmentWindow> findCurrentExamWindowFromFormWindows(AssessmentWindowParameters assessmentWindowParameters, List<AssessmentWindow> formWindows) {
        boolean requireFormWindow = false, requireForm = false;

        //Lines 3703 - 3710 in StudentDLL._GetTesteeTestForms_SP
        if (assessmentWindowParameters.getStudentId() < 0) {
            return formWindows;
        }

        //Lines 3712 - 3730 in StudentDLL._GetTesteeTestForms_SP
        Optional<AssessmentFormWindowProperties> maybeAssessmentProperties = assessmentWindowQueryRepository.findAssessmentFormWindowProperties(assessmentWindowParameters.getClientName(), assessmentWindowParameters.getAssessmentId(), assessmentWindowParameters.getSessionType());
        if (maybeAssessmentProperties.isPresent()) {
            AssessmentFormWindowProperties properties = maybeAssessmentProperties.get();
        }

        String formList = assessmentWindowParameters.getFormList();
        if (formList != null) {
            if (formList.indexOf(':') > -1)
                requireFormWindow = true;
            else {
                requireForm = true;
                requireFormWindow = false;
            }
        }

        //Key is a the form key and the value is the list of window ids associated with the form key
        Map<String, Set<String>> studentPackageForms = new HashMap<>();

        if(formList != null) {
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
                    studentPackageForms.put(form, new HashSet<>());
                }

                if (wid != null) {
                    studentPackageForms.get(form).add(wid);
                }
            }
        }

        //Lines 3786 - 3815 in StudentDLL._GetTesteeTestForms_SP
        if (requireFormWindow) {
            return formWindows.stream().filter(assessmentWindow -> {
                String formKey = assessmentWindow.getFormKey();
                return studentPackageForms.containsKey(formKey) &&
                    studentPackageForms.get(formKey).contains(assessmentWindow.getWindowId());
            }).filter(distinctByKey(AssessmentWindow::getWindowId)).collect(Collectors.toList());
        } else if (requireForm) {
            return formWindows.stream().filter(assessmentWindow ->
                studentPackageForms.containsKey(assessmentWindow.getFormKey()))
                .filter(distinctByKey(AssessmentWindow::getWindowId))
                .collect(Collectors.toList());
        }

        return formWindows;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
