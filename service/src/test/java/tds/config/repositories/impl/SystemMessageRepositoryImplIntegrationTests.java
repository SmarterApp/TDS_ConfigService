package tds.config.repositories.impl;


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

import java.util.Optional;

import tds.config.ClientLanguage;
import tds.config.ClientSystemMessage;
import tds.config.repositories.SystemMessageRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SystemMessageRepositoryImplIntegrationTests {

    @Autowired
    private SystemMessageRepository systemMessageRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        String clientInsertSQL = "INSERT INTO client VALUES ('SBAC_PT', NULL, 1, 'ENU');";
        jdbcTemplate.update(clientInsertSQL, new MapSqlParameterSource());

        String coreMessageUserInsertSQL = "INSERT INTO tds_coremessageuser VALUES (3146, 'Database');";
        jdbcTemplate.update(coreMessageUserInsertSQL, new MapSqlParameterSource());

        String coreMessageObjectInsertSQL = "INSERT INTO tds_coremessageobject VALUES ('_CanOpenTestOpportunity', 'database', 10211, 'Database', 'Your next test opportunity is not yet available.', 'You cannot take this test until {0}.', 'DATEPARAMETER', 3146, NULL, '2012-05-03 19:17:37 ', NULL, NULL);";
        jdbcTemplate.update(coreMessageObjectInsertSQL, new MapSqlParameterSource());

        String[] messageTranslationInsertSQL = new String[]{
            "INSERT INTO client_messagetranslation VALUES (3146, 'SBAC_PT', 'Ao hiki iā oe ke hana i kēia hōike a hiki i ka lā  {0}.', 'HAW', '--ANY--', '--ANY--', 0xB32C36BFDF094559A65E46DBDD5DF602, '2013-08-01 11:02:31');",
            "INSERT INTO client_messagetranslation VALUES (3146, 'SBAC_PT', 'No puede realizar esta prueba hasta {0}.', 'ESN', '--ANY--', '--ANY--', 0x46F548F386ED4A92B554C6FCC835C2AF, '2013-08-01 11:02:31');",
            "INSERT INTO client_messagetranslation VALUES (3146, 'SBAC', 'Ao hiki iā oe ke hana i kēia hōike a hiki i ka lā  {0}.', 'HAW', '--ANY--', '--ANY--', 0xC43A1E73EB7E11E68FF40A73DE62EC37, '2013-08-02 10:34:37');"
        };

        for (String insertSQL : messageTranslationInsertSQL) {
            jdbcTemplate.update(insertSQL, new MapSqlParameterSource());
        }
    }

    @After
    public void tearDown() {
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemMessage Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetOptionalEmptyClientSystemMessageForAnInvalidClientName() {
        final String clientName = "foo";

        Optional<ClientSystemMessage> result = systemMessageRepository.findClientSystemMessage(clientName, null, null, null, null, null, null);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shouldGetOptionalEmptyClientSystemMessageForAnInvalidMessageKey() {
        final String clientName = "SBAC_PT";
        final String messageKey = "some invalid message key";

        Optional<ClientSystemMessage> result = systemMessageRepository.findClientSystemMessage(clientName, messageKey, null, null, null, null, null);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shouldGetEnglishLanguageClientSystemMessage() {
        final String clientName = "SBAC_PT";
        final String messageKey = "Your next test opportunity is not yet available.";
        final String language = "ENU";
        final String clientDefaultLanguage = "ENU";
        final String context = "_CanOpenTestOpportunity";

        Optional<ClientSystemMessage> result = systemMessageRepository.findClientSystemMessage(clientName, messageKey, language, clientDefaultLanguage, context, null, null);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getMessageId()).isEqualTo(10211);
        assertThat(result.get().getMessage()).isEqualTo("You cannot take this test until {0}.");
        assertThat(result.get().getLanguageCode()).isEqualTo(language);
    }

    @Test
    public void shouldGetSpanishLanguageClientSystemMessage() {
        final String clientName = "SBAC_PT";
        final String messageKey = "Your next test opportunity is not yet available.";
        final String language = "ESN";
        final String clientDefaultLanguage = "ENU";
        final String context = "_CanOpenTestOpportunity";

        Optional<ClientSystemMessage> result = systemMessageRepository.findClientSystemMessage(clientName, messageKey, language, clientDefaultLanguage, context, null, null);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getMessageId()).isEqualTo(10211);
        assertThat(result.get().getMessage()).isEqualTo("No puede realizar esta prueba hasta {0}.");
        assertThat(result.get().getLanguageCode()).isEqualTo(language);
    }

    @Test
    public void shouldGetDefaultLanguageClientSystemMessageWhenMissingLanguage() {
        final String clientName = "SBAC_PT";
        final String messageKey = "Your next test opportunity is not yet available.";
        final String language = "NON";
        final String clientDefaultLanguage = "ENU";
        final String context = "_CanOpenTestOpportunity";

        Optional<ClientSystemMessage> result = systemMessageRepository.findClientSystemMessage(clientName, messageKey, language, clientDefaultLanguage, context, null, null);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getMessageId()).isEqualTo(10211);
        assertThat(result.get().getMessage()).isEqualTo("You cannot take this test until {0}.");
        assertThat(result.get().getLanguageCode()).isEqualTo(clientDefaultLanguage);
    }

    @Test
    public void shouldGetEnglishLanguageClientSystemMessageWhenMissingLanguageAndClientDefault() {
        final String clientName = "SBAC_PT";
        final String messageKey = "Your next test opportunity is not yet available.";
        final String language = "NON";
        final String clientDefaultLanguage = "NON";
        final String context = "_CanOpenTestOpportunity";

        Optional<ClientSystemMessage> result = systemMessageRepository.findClientSystemMessage(clientName, messageKey, language, clientDefaultLanguage, context, null, null);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getMessageId()).isEqualTo(10211);
        assertThat(result.get().getMessage()).isEqualTo("You cannot take this test until {0}.");
        assertThat(result.get().getLanguageCode()).isEqualTo("ENU");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientLanguage Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetOptionalEmptyClientLanguageForInvalidClient() {
        final String clientName = "invalid";

        Optional<ClientLanguage> result = systemMessageRepository.findClientLanguage(clientName);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shouldGetClientLanguage() {
        final String clientName = "SBAC_PT";

        Optional<ClientLanguage> result = systemMessageRepository.findClientLanguage(clientName);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getClientName()).isEqualTo(clientName);
        assertThat(result.get().getDefaultLanguageCode()).isEqualTo("ENU");
        assertThat(result.get().isInternationalize()).isTrue();
    }
}
