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

    /**
     * This method is meant to replace CommonDLL._FormatMessage_SP (line 1975)
     * <p>
     * Instead of following the existing pattern in the code of getting the _key (int) from tds_coremessageobject for default translation
     * or getting the _key (varbinary) from client_messagetranslation and then using that to get the actual message text.
     * The repository takes care of that in a single database query.
     * <p>
     * NOTE: This method differs from _FormatMessage_SP in that it does not take in arguments to replace placeholder text in the messages.
     * The consumer of the endpoint is expected to handle this.
     */
    @Override
    @Cacheable(CacheType.LONG_TERM)
    public String getSystemMessage(String clientName, String messageKey, String languageCode, String context, String subject, String grade) {
        String clientDefaultLanguage = null;
        Optional<ClientLanguage> maybeClientLanguage = configRepository.findClientLanguage(clientName);

        /**
         * CommonDLL Lines 2080 - 2092
         * Get the default language for the client which acts as a fallback if there is no message in the language of this particular student
         * The internationalize flag allows this fallback, and if it is false then the student always sees the default language of the client
         */
        if (maybeClientLanguage.isPresent()) {
            clientDefaultLanguage = maybeClientLanguage.get().getDefaultLanguageCode();

            if (!maybeClientLanguage.get().isInternationalize()) {
                languageCode = maybeClientLanguage.get().getDefaultLanguageCode();
            }
        }

        /** This repository call replaces calling TDS_GetMessagekey_FN at CommonDLL Line 1987 and then conditionally getting
         *  the message text lines 2008 - 2037
         */
        Optional<ClientSystemMessage> maybeSystemMessage = configRepository.findClientSystemMessage(clientName, messageKey, languageCode, clientDefaultLanguage, context, subject, grade);

        if (!maybeSystemMessage.isPresent()) {
            LOG.info("Message missing for key {} for client {}", messageKey, clientName);
            return String.format("%s [-----]", messageKey);
        }

        return String.format("%s [%d]", maybeSystemMessage.get().getMessage(), maybeSystemMessage.get().getMessageId());
    }
}
