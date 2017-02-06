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