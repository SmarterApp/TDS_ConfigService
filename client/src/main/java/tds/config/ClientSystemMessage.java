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

package tds.config;

/**
 * Represents a record in the {@code configs.tds_coremessageobject} or {@code configs.client_messagetranslation} table.
 */
public class ClientSystemMessage {
    private final int messageId;
    private final String message;
    private final String languageCode;

    public ClientSystemMessage(final int messageId, final String message, final String languageCode) {
        this.messageId = messageId;
        this.message = message;
        this.languageCode = languageCode;
    }

    /**
     * @return The messageId for this {@link tds.config.ClientSystemMessage}
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * @return The message template for this {@link tds.config.ClientSystemMessage}
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return The language code of the message for this {@link tds.config.ClientSystemMessage}
     */
    public String getLanguageCode() {
        return languageCode;
    }
}
