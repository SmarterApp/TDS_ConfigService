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

package tds.config.repositories;

import java.util.Optional;

import tds.config.ClientLanguage;
import tds.config.ClientSystemMessage;

/**
 * Data Access for interacting with the {@code configs} database to get message data.
 */
public interface SystemMessageRepository {
    /**
     * Finds the {@link tds.config.ClientSystemMessage} for the given client, message and context.  The {@link tds.config.ClientSystemMessage}
     * contains the parameterized message and message ID.
     *
     * @param clientName            The client name
     * @param messageKey            The message key found in the {@code configs.tds_coremessageobject} table in the {@code appkey} column
     * @param languageCode              The language code to translate the message into
     * @param clientDefaultLanguage The default language to fallback to if the specific {@code language} parameter is not available
     * @param context               The context for the message found in the {@code configs.tds_coremessageobject} table in the {@code context} column
     * @param subject               A subject code used to find a more specific message.  NULL will match on all
     * @param grade                 A grade level used to find a more specific message.  NULL will match on all
     * @return A {@link tds.config.ClientSystemMessage} if found, empty otherwise
     */
    Optional<ClientSystemMessage> findClientSystemMessage(final String clientName, final String messageKey, String languageCode, String clientDefaultLanguage, String context, String subject, String grade);

    /**
     * Finds the {@link tds.config.ClientLanguage} by the client name which contains the default language setting for this client.
     *
     * @param clientName The client name
     * @return {@link tds.config.ClientLanguage} if found, otherwise empty
     */
    Optional<ClientLanguage> findClientLanguage(final String clientName);
}
