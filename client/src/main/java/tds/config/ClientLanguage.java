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
 Represents a record in the {@code configs.client}  table.
 */
public class ClientLanguage {
    private final String clientName;
    private final String defaultLanguageCode;
    private final boolean internationalize;

    public ClientLanguage(final String clientName,
                          final String defaultLanguageCode,
                          final boolean internationalize) {
        this.clientName = clientName;
        this.defaultLanguageCode = defaultLanguageCode;
        this.internationalize = internationalize;
    }

    /**
     * @return The client for this {@link ClientLanguage}
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @return The default language code for this client
     */
    public String getDefaultLanguageCode() {
        return defaultLanguageCode;
    }

    /**
     * @return Determines if language will fallback to the default language
     */
    public boolean isInternationalize() {
        return internationalize;
    }
}
