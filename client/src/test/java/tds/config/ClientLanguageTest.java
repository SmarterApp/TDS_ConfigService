package tds.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientLanguageTest {
    @Test
    public void shouldCreateAClientDefaultLanguage() {
        ClientLanguage clientLanguage = new ClientLanguage("SBAC_PT", "ENU", true);

        assertThat(clientLanguage.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientLanguage.getDefaultLanguage()).isEqualTo("ENU");
        assertThat(clientLanguage.isInternationalize()).isTrue();
    }
}