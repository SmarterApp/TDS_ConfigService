package tds.config.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import tds.config.ClientLanguage;
import tds.config.ClientSystemMessage;
import tds.config.repositories.SystemMessageRepository;

@Repository
public class SystemMessageRepositoryImpl implements SystemMessageRepository {
    private static final Logger LOG = LoggerFactory.getLogger(SystemMessageRepositoryImpl.class);
    private static final String MATCH_ANY = "--ANY--";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public SystemMessageRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ClientSystemMessage> findClientSystemMessage(String clientName, String messageKey, String languageCode, String clientDefaultLanguage, String context, String subject, String grade) {
        List<String> langaugeCodes = Arrays.asList(languageCode, clientDefaultLanguage);
        List<String> grades = new ArrayList<>(Arrays.asList(MATCH_ANY));
        List<String> subjects = new ArrayList<>(Arrays.asList(MATCH_ANY));

        if (grade != null && !grade.equals(MATCH_ANY)) {
            grades.add(grade);
        }
        if (subject != null && !subject.equals(MATCH_ANY)) {
            subjects.add(subject);
        }

        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
            .addValue("messageKey", messageKey)
            .addValue("languageCodes", langaugeCodes)
            .addValue("languageCode", languageCode)
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
                "   ON mu._fk_coremessageobject = mo._key \n" +
                "JOIN \n" +
                "   client_messagetranslation mt \n" +
                "   ON mo._Key = mt._fk_coremessageobject \n" +
                "WHERE \n" +
                "   mu.systemid = 'database' \n" +
                "   and mo.context = :context \n" +
                "   and mo.contexttype = 'database' \n" +
                "   and mo.appkey = :messageKey \n" +
                "   and mt.language in (:languageCodes) \n" +
                "   and mt.client = :clientName \n" +
                "   and mt.grade in (:grades) \n" +
                "   and mt.subject in (:subjects) \n" +
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
                "   ON mu._fk_coremessageobject = mo._Key \n" +
                "WHERE \n" +
                "   mu.systemid = 'database' \n" +
                "   and mo.context = :context \n" +
                "   and mo.contexttype = 'database' \n" +
                "   and mo.appkey = :messageKey \n" +
                "ORDER BY \n" +
                "   rank, \n" +
                "   CASE WHEN language = :languageCode THEN 1 ELSE 2 END \n" +
                "LIMIT 1";

        /*
            The first part of the UNION is for message translations
            The second part is the default message (ENU) that is used if no translation is found
            Order by explanation:
                Rank: This makes sure that any translation found is used, then fallback to the default message in ENU
                CASE: Since the student language preference and the client default is passed in, if it matches both then we want the student preference to be used
         */

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
            LOG.debug("Missing Message: {} did not return results for clientName = {}, messageKey = {}, language = {}, context = {}, subject = {}, grade = {}", SQL, clientName);
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

        Optional<ClientLanguage> clientLanguage;
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
