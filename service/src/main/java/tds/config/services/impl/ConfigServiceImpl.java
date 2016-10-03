package tds.config.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.model.CurrentExamWindow;
import tds.config.repositories.ClientTestPropertyQueryRepository;
import tds.config.repositories.ConfigRepository;
import tds.config.repositories.ExamWindowQueryRepository;
import tds.config.services.ConfigService;

@Service
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository configRepository;
    private final ClientTestPropertyQueryRepository clientTestPropertyQueryRepository;
    private final ExamWindowQueryRepository examWindowQueryRepository;

    @Autowired
    public ConfigServiceImpl(ConfigRepository configRepository,
                             ClientTestPropertyQueryRepository clientTestPropertyQueryRepository,
                             ExamWindowQueryRepository examWindowQueryRepository) {
        this.configRepository = configRepository;
        this.clientTestPropertyQueryRepository = clientTestPropertyQueryRepository;
        this.examWindowQueryRepository = examWindowQueryRepository;
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
    public Optional<CurrentExamWindow> getExamWindow(long studentId, String clientName, String assessmentId, int shiftWindowStart, int shiftWindowEnd) {
        if (studentId < 0) {
            return examWindowQueryRepository.findCurrentTestWindowsForGuest(clientName, assessmentId, shiftWindowStart, shiftWindowEnd);
        }

        boolean requireWindow = false;
        boolean isFormTest = false;
        String windowField;
        String tideId;

        Optional<ClientTestProperty> maybeProperties = clientTestPropertyQueryRepository.findClientTestProperty(clientName, assessmentId);
        if (maybeProperties.isPresent()) {
            ClientTestProperty property = maybeProperties.get();
            requireWindow = property.getRequireRtsWindow();
            windowField = property.getRtsWindowField();
            tideId = property.getTideId();
        }



        return Optional.empty();
    }
}
