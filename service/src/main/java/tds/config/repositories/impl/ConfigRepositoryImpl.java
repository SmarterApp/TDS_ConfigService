package tds.config.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import tds.config.ClientLanguage;
import tds.config.ClientSystemFlag;
import tds.config.ClientSystemMessage;
import tds.config.repositories.ConfigRepository;
import tds.config.repositories.impl.mappers.ClientSystemFlagRowMapper;

@Repository
public class ConfigRepositoryImpl implements ConfigRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ConfigRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ClientSystemFlag> findClientSystemFlags(final String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        final String SQL =
            "SELECT\n" +
                "   auditobject AS auditObject,\n" +
                "   clientname AS clientName,\n" +
                "   ispracticetest AS isPracticeTest,\n" +
                "   ison AS isOn,\n" +
                "   description AS description,\n" +
                "   datechanged AS dateChanged,\n" +
                "   datepublished AS datePublished\n" +
                "FROM\n" +
                "   configs.client_systemflags\n" +
                "WHERE\n" +
                "   clientname = :clientName";

        List<ClientSystemFlag> clientSystemFlags;
        try {
            clientSystemFlags =
                jdbcTemplate.query(
                    SQL,
                    parameters,
                    new ClientSystemFlagRowMapper());
        } catch (DataAccessException e) {
            LOG.debug("{} did not return results for clientName = {}", SQL, clientName);
            clientSystemFlags = Collections.emptyList();
        }

        return clientSystemFlags;
    }

    @Override
    public Optional<ClientSystemMessage> findClientSystemMessage(String clientName, String messageKey, String language, String clientDefaultLanguage, String context, String subject, String grade) {
        List<String> langauges = Arrays.asList(language, clientDefaultLanguage);
        List<String> grades = new ArrayList<>(Arrays.asList("--ANY--"));
        List<String> subjects = new ArrayList<>(Arrays.asList("--ANY--"));

        if (grade != null && !grade.equals("--ANY--")) {
            grades.add(grade);
        }
        if (subject != null && !subject.equals("--ANY--")) {
            subjects.add(subject);
        }

        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
            .addValue("messageKey", messageKey)
            .addValue("languages", langauges)
            .addValue("language", language)
            .addValue("context", context)
            .addValue("subjects", subjects)
            .addValue("grades", grades);

        final String SQL =
            "SELECT \n" +
                "   mt.message, \n" +
                "   mo.messageID, \n" +
                "   mt.language, \n" +
                "   1 as rank \n" +
                "FROM  \n" +
                "   tds_coremessageuser mu \n" +
                "JOIN \n" +
                "   tds_coremessageobject mo \n" +
                "   ON mu._fk_CoreMessageObject = mo._Key \n" +
                "JOIN \n" +
                "   client_messagetranslation mt \n" +
                "   ON mo._Key = mt._fk_CoreMessageObject \n" +
                "WHERE \n" +
                "   mu.SystemID = 'database' \n" +
                "   and mo.Context = :context \n" +
                "   and mo.contextType = 'database' \n" +
                "   and mo.appkey = :messageKey \n" +
                "   and mt.language in (:languages) \n" +
                "   and mt.client = :clientName \n" +
                "   and mt.Grade in (:grades) \n" +
                "   and mt.Subject in (:subjects) \n" +
            "UNION \n" +
            "SELECT \n" +
                "   mo.message, \n" +
                "   mo.messageID, \n" +
                "   'ENU', \n" +
                "   2 as rank \n" +
                "FROM  \n" +
                "   tds_coremessageuser mu \n" +
                "JOIN \n" +
                "   tds_coremessageobject mo \n" +
                "   ON mu._fk_CoreMessageObject = mo._Key \n" +
                "WHERE \n" +
                "   mu.SystemID = 'database' \n" +
                "   and mo.Context = :context \n" +
                "   and mo.contextType = 'database' \n" +
                "   and mo.appkey = :messageKey \n" +
            "ORDER BY \n" +
            "   rank, \n" +
            "   CASE WHEN language = :language THEN 1 ELSE 2 END \n" +
            "LIMIT 1";

        Optional<ClientSystemMessage> maybeClientSystemMessage;
        try {
            maybeClientSystemMessage = Optional.of(
                jdbcTemplate.queryForObject(SQL, parameters, (resultSet, i) ->
                        new ClientSystemMessage(
                            resultSet.getInt("messageID"),
                            resultSet.getString("message"),
                            resultSet.getString("language")
                        )
                    ));
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("{} did not return results for clientName = {}, messageKey = {}, language = {}, context = {}, subject = {}, grade = {}", SQL, clientName);
            maybeClientSystemMessage = Optional.empty();
        }

        return maybeClientSystemMessage;
    }

    @Override
    public Optional<ClientLanguage> findClientLanguage(String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        final String SQL =
            "SELECT \n" +
                "   name AS clientName, \n" +
                "   defaultLanguage, \n" +
                "   internationalize \n" +
                "FROM \n" +
                "   client \n" +
                "WHERE \n" +
                "   name = :clientName";

        Optional <ClientLanguage> clientLanguage;
        try {
            clientLanguage = Optional.of(
                jdbcTemplate.queryForObject(SQL, parameters, (resultSet, i) ->
                    new ClientLanguage(
                        resultSet.getString("clientName"),
                        resultSet.getString("defaultLanguage"),
                        resultSet.getBoolean("internationalize")
                    )
                )
            );
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("{} did not return results for clientName = {}", SQL, clientName);
            clientLanguage = Optional.empty();
        }

        return clientLanguage;
    }
}
