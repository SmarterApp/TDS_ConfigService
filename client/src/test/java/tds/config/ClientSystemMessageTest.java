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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientSystemMessageTest {
    @Test
    public void shouldCreateAClientSystemMessage() {
        ClientSystemMessage clientSystemMessage = new ClientSystemMessage(1234, "Test message {0}", "ENU");

        assertThat(clientSystemMessage.getMessageId()).isEqualTo(1234);
        assertThat(clientSystemMessage.getMessage()).isEqualTo("Test message {0}");
        assertThat(clientSystemMessage.getLanguageCode()).isEqualTo("ENU");
    }
}