/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.config.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import tds.common.cache.CacheType;
import tds.config.ClientLanguage;
import tds.config.ClientSystemMessage;
import tds.config.repositories.SystemMessageRepository;
import tds.config.services.SystemMessageService;

import static tds.common.util.Preconditions.checkNotNull;

@Service
public class SystemMessageServiceImpl implements SystemMessageService {
    private static final Logger LOG = LoggerFactory.getLogger(SystemMessageServiceImpl.class);
    private final SystemMessageRepository systemMessageRepository;

    @Autowired
    public SystemMessageServiceImpl(final SystemMessageRepository systemMessageRepository) {
        this.systemMessageRepository = systemMessageRepository;
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
    public String getSystemMessage(final String clientName,
                                   final String messageKey,
                                   final String languageCode,
                                   final String context,
                                   final String subject,
                                   final String grade) {
        String languageCodeForTranslation = checkNotNull(languageCode, "languageCodeForTranslation cannot be null");

        String clientDefaultLanguage = null;
        Optional<ClientLanguage> maybeClientLanguage = systemMessageRepository.findClientLanguage(clientName);

        /**
         * CommonDLL Lines 2080 - 2092
         * Get the default language for the client which acts as a fallback if there is no message in the language of this particular student
         * The internationalize flag allows this fallback, and if it is false then the student always sees the default language of the client
         */
        if (maybeClientLanguage.isPresent()) {
            clientDefaultLanguage = maybeClientLanguage.get().getDefaultLanguageCode();

            if (!maybeClientLanguage.get().isInternationalize()) {
                languageCodeForTranslation = maybeClientLanguage.get().getDefaultLanguageCode();
            }
        }

        /** This repository call replaces calling TDS_GetMessagekey_FN at CommonDLL Line 1987 and then conditionally getting
         *  the message text lines 2008 - 2037
         */
        Optional<ClientSystemMessage> maybeSystemMessage = systemMessageRepository.findClientSystemMessage(clientName, messageKey, languageCodeForTranslation, clientDefaultLanguage, context, subject, grade);

        if (!maybeSystemMessage.isPresent()) {
            LOG.info("Message missing for key {} for client {}", messageKey, clientName);
            return String.format("%s [-----]", messageKey);
        }

        return String.format("%s [%d]", maybeSystemMessage.get().getMessage(), maybeSystemMessage.get().getMessageId());
    }
}
