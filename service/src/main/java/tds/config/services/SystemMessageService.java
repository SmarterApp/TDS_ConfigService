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

package tds.config.services;

/**
 * Provide an interface for interacting with the {@code configs} database and providing translated messages.
 */
public interface SystemMessageService {
    /**
     * Get the message string for this client that is translated for the language specified.
     *
     * @param clientName   The client name
     * @param messageKey   The message key found in the {@code configs.tds_coremessageobject} table in the {@code appkey} column
     * @param languageCode The language code to translate the message into
     * @param context      The context for the message found in the {@code configs.tds_coremessageobject} table in the {@code context} column
     * @param subject      A subject code used to find a more specific message.  NULL will match on all
     * @param grade        A grade level used to find a more specific message.  NULL will match on all
     * @return The message string with placeholders included
     */
    String getSystemMessage(final String clientName, final String messageKey, final String languageCode, final String context, final String subject, final String grade);
}
