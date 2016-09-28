package tds.config.repositories.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.ConfigServiceApplication;
import tds.config.repositories.ConfigRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConfigServiceApplication.class)
public class ConfigRepositoryImplIntegrationTests {

    @Autowired
    private ConfigRepository configRepository;

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemFlag Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientSystemFlagsList() {
        final String clientName = "SBAC_PT";

        List<ClientSystemFlag> result = configRepository.findClientSystemFlags(clientName);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(16);
        result.forEach(r -> {
            assertThat(r.getAuditObject()).isNotNull();
            assertThat(r.getClientName()).isEqualTo(clientName);
        });
    }

    @Test
    public void shouldGetOptionalEmptyClientSystemFlagsForAnInvalidClientName() {
        final String clientName = "foo";

        List<ClientSystemFlag> result = configRepository.findClientSystemFlags(clientName);

        assertThat(result.size()).isEqualTo(0);
    }
}