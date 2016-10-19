package tds.config.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

import tds.config.ClientTestProperty;
import tds.config.repositories.ClientTestPropertyQueryRepository;
import tds.config.repositories.impl.mappers.ClientTestPropertyRowMapper;

@Repository
public class ClientTestPropertyQueryRepositoryImpl implements ClientTestPropertyQueryRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ClientTestPropertyQueryRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<ClientTestProperty> findClientTestProperty(final String clientName, final String assessmentId) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
            .addValue("assessmentId", assessmentId);

        final String SQL =
            "SELECT\n" +
                "   clientname AS clientName,\n" +
                "   testid AS assessmentId,\n" +
                "   maxopportunities AS maxOpportunities,\n" +
                "   prefetch AS prefetch,\n" +
                "   isselectable AS isSelectable,\n" +
                "   label AS label,\n" +
                "   subjectname AS subjectName,\n" +
                "   initialabilitybysubject AS initialAbilityBySubject,\n" +
                "   accommodationfamily AS accommodationFamily,\n" +
                "   sortorder AS sortOrder,\n" +
                "   rtsformfield AS rtsFormField,\n" +
                "   requirertswindow AS requireRtsWindow,\n" +
                "   rtsmodefield AS rtsModeField,\n" +
                "   requirertsmode AS requireRtsMode,\n" +
                "   requirertsmodewindow AS requireRtsModeWindow,\n" +
                "   deleteunanswereditems AS deleteUnansweredItems,\n" +
                "   abilityslope AS abilitySlope,\n" +
                "   abilityintercept AS abilityIntercept,\n" +
                "   validatecompleteness AS validateCompleteness,\n" +
                "   gradetext AS gradeText,\n" +
                "   rtswindowfield \n" +
                "FROM\n" +
                "   configs.client_testproperties\n" +
                "WHERE\n" +
                "   clientname = :clientName\n" +
                "   AND testid = :assessmentId";

        Optional<ClientTestProperty> maybeClientTestProperty;
        try {
            maybeClientTestProperty = Optional.of(
                jdbcTemplate.queryForObject(
                    SQL,
                    parameters,
                    new ClientTestPropertyRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("{} did not return results for clientName = {}, assessmentId = {}", SQL, clientName, assessmentId);
            maybeClientTestProperty = Optional.empty();
        }

        return maybeClientTestProperty;
    }
}
