package tds.config.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import tds.common.cache.CacheType;
import tds.config.ClientLanguage;
import tds.config.ClientSystemFlag;
import tds.config.ClientSystemMessage;
import tds.config.repositories.ConfigRepository;
import tds.config.repositories.impl.ConfigRepositoryImpl;
import tds.config.services.ConfigService;

@Service
public class ConfigServiceImpl implements ConfigService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigServiceImpl.class);
    private final ConfigRepository configRepository;

    @Autowired
    public ConfigServiceImpl(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    @Cacheable(CacheType.LONG_TERM)
    public Optional<ClientSystemFlag> findClientSystemFlag(String clientName, String type) {
        /*
            The type is the name of the ClientSystemFlag.  Ideally the source of data for this
            method will be an in-memory collection; ClientSystemFlag values rarely change, making them a
            strong candidate for caching.
        */

        List<ClientSystemFlag> clientSystemFlags = configRepository.findClientSystemFlags(clientName);

        return clientSystemFlags.stream()
            .filter(f -> f.getAuditObject().equals(type))
            .findFirst();
    }

    @Override
    /**
     * This method is meant to replace CommonDLL._FormatMessage_SP (line 1975)
     */
    public String getSystemMessage(String clientName, String messageKey, String language, String context, String subject, String grade) {
        String clientDefaultLanguage = null;
        Optional<ClientLanguage> maybeClientLanguage = configRepository.findClientLanguage(clientName);

        /**
         * CommonDLL Lines 2080 - 2092
         * Get the default language for the client which acts as a fallback if there is no message in the language of this particular student
         * The internationzlize flag allows this fallback, and if it is false then the student always sees the default language of the client
         */
        if (maybeClientLanguage.isPresent()) {
            clientDefaultLanguage = maybeClientLanguage.get().getDefaultLanguage();

            if (!maybeClientLanguage.get().isInternationalize()) {
                language = maybeClientLanguage.get().getDefaultLanguage();
            }
        }

        // get translation message key (maybe these can be combined into a single query?)
        Optional<ClientSystemMessage> maybeSystemMessage = configRepository.findClientSystemMessage(clientName, messageKey, language, clientDefaultLanguage, context, subject, grade);

        if (!maybeSystemMessage.isPresent()) {
            LOG.info("Message missing for key {} for client {}", messageKey, clientName);
            return String.format ("%s [-----]", messageKey);
        }

        ClientSystemMessage systemMessage = maybeSystemMessage.get();
        return String.format ("%s [%d]", systemMessage.getMessage(), systemMessage.getMessageId());
    }
}
