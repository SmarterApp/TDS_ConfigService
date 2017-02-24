package tds.config.repositories.impl;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import tds.config.ClientLanguage;
import tds.config.ClientSystemFlag;
import tds.config.ClientSystemMessage;
import tds.config.repositories.ConfigRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ConfigRepositoryImplIntegrationTests {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        String clientFlagInsertSQL = "INSERT INTO client_systemflags (auditobject,ison,description,clientname,ispracticetest,datechanged,datepublished) " +
            "VALUES ('accommodations',1,'keeps an audit trail of various changes to accommodations settings','SBAC_PT',1,'2011-06-01 11:27:47.980',NULL);";

        jdbcTemplate.update(clientFlagInsertSQL, new MapSqlParameterSource());
    }

    @After
    public void tearDown() {
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemFlag Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientSystemFlagsList() {
        final String clientName = "SBAC_PT";

        List<ClientSystemFlag> result = configRepository.findClientSystemFlags(clientName);

        Assertions.assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        result.forEach(r -> {
            assertThat(r.getAuditObject()).isNotNull();
            assertThat(r.getClientName()).isEqualTo(clientName);
        });
    }

    @Test
    public void shouldGetEmptyListClientSystemFlagsForAnInvalidClientName() {
        final String clientName = "foo";

        List<ClientSystemFlag> result = configRepository.findClientSystemFlags(clientName);

        assertThat(result.size()).isEqualTo(0);
    }
}