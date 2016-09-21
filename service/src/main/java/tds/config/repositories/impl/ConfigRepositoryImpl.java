package tds.config.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.TimeLimits;
import tds.config.repositories.ConfigRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class ConfigRepositoryImpl implements ConfigRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String TIMELIMITS_BASE_SQL =
            "SELECT \n" +
            "   clientname, \n" +
            "   _efk_testid AS assessmentId, \n" +
            "   environment, \n" +
            "   opprestart AS examRestartWindowMinutes, \n" +
            "   oppdelay AS examDelayDays, \n" +
            "   interfacetimeout AS interfaceTimeoutMinutes, \n" +
            "   requestinterfacetimeout AS requestInterfaceTimeoutMinutes, \n" +
            "   tacheckintime AS taCheckinTimeMinutes \n" +
            "FROM \n" +
            "   session.timelimits \n" +
            "WHERE \n" +
            "   clientname = :clientName \n";

    @Autowired
    public ConfigRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<ClientSystemFlag> getClientSystemFlags(final String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        final String SQL =
                "SELECT\n" +
                "   s.auditobject AS auditObject,\n" +
                "   s.clientname AS clientName,\n" +
                "   s.ispracticetest AS isPracticeTest,\n" +
                "   s.ison AS isOn,\n" +
                "   s.description AS description,\n" +
                "   s.datechanged AS dateChanged,\n" +
                "   s.datepublished AS datePublished\n" +
                "FROM\n" +
                "   configs.client_systemflags s\n" +
                "JOIN\n" +
                "   session.externs e\n" +
                "   ON (e.clientname = s.clientname\n" +
                "   AND e.ispracticetest = s.ispracticetest)\n" +
                "WHERE\n" +
                "   e.clientname = :clientName";

        List<ClientSystemFlag> clientSystemFlags;
        try {
            clientSystemFlags =
                    jdbcTemplate.query(
                            SQL,
                            parameters,
                            new BeanPropertyRowMapper<>(ClientSystemFlag.class));
        } catch(DataAccessException e) {
            LOG.debug("{} did not return results for clientName = {}", SQL, clientName);
            throw e;
        }

        return clientSystemFlags;
    }

    @Override
    public Optional<ClientTestProperty> getClientTestProperty(final String clientName, final String assessmentId) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
                .addValue("assessmentId", assessmentId);

        final String SQL =
                "SELECT\n" +
                "   clientname AS clientName,\n" +
                "   testid AS assessmentId,\n" +
                "   maxopportunities AS maxOpportunities,\n" +
                "   handscoreproject AS handScoreProject,\n" +
                "   prefetch AS prefetch,\n" +
                "   datechanged AS dateChanged,\n" +
                "   isprintable AS isPrintable,\n" +
                "   isselectable AS isSelectable,\n" +
                "   label AS label,\n" +
                "   printitemtypes AS printItemTypes,\n" +
                "   scorebytds AS scoreByTds,\n" +
                "   batchmodereport AS batchModeReport,\n" +
                "   subjectname AS subjectName,\n" +
                "   origin AS origin,\n" +
                "   source AS source,\n" +
                "   maskitemsbysubject AS maskItemsBySubject,\n" +
                "   initialabilitybysubject AS initialAbilityBySubject,\n" +
                "   startdate AS startDate,\n" +
                "   enddate AS endDate,\n" +
                "   ftstartdate AS ftStartDate,\n" +
                "   ftenddate AS ftEndDate,\n" +
                "   accommodationfamily AS accommodationFamily,\n" +
                "   sortorder AS sortOrder,\n" +
                "   rtsformfield AS rtsFormField,\n" +
                "   rtswindowfield AS rtsWindowField,\n" +
                "   windowtideselectable AS windowTideSelectable,\n" +
                "   requirertswindow AS requireRtsWindow,\n" +
                "   reportinginstrument AS reportingInstrument,\n" +
                "   tide_id AS tideId,\n" +
                "   forcecomplete AS forceComplete,\n" +
                "   rtsmodefield AS rtsModeField,\n" +
                "   modetideselectable AS modeTideSelectable,\n" +
                "   requirertsmode AS requireRtsMode,\n" +
                "   requirertsmodewindow AS requireRtsModeWindow,\n" +
                "   deleteunanswereditems AS deleteUnansweredItems,\n" +
                "   abilityslope AS abilitySlope,\n" +
                "   abilityintercept AS abilityIntercept,\n" +
                "   validatecompleteness AS validateCompleteness,\n" +
                "   gradetext AS gradeText,\n" +
                "   initialabilitytestid AS initialAbilityTestId,\n" +
                "   proctoreligibility AS proctorEligibility,\n" +
                "   category AS category\n" +
                "FROM\n" +
                "   configs.client_testproperties\n" +
                "WHERE\n" +
                "   clientname = :clientName\n" +
                "   AND testid = :assessmentId";

        Optional<ClientTestProperty> clientTestPropertyOptional;
        try {
            clientTestPropertyOptional = Optional.of(
                    jdbcTemplate.queryForObject(
                            SQL,
                            parameters,
                            new BeanPropertyRowMapper<>(ClientTestProperty.class)));
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("{} did not return results for clientName = {}, assessmentId = {}", SQL, clientName, assessmentId);
            clientTestPropertyOptional = Optional.empty();
        }

        return clientTestPropertyOptional;
    }

    @Override
    public Optional<TimeLimits> getTimeLimits(final String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        final String SQL = TIMELIMITS_BASE_SQL + "   AND _efk_testid IS NULL";

        Optional<TimeLimits> timeLimitsOptional;
        try {
            timeLimitsOptional = Optional.of(
                    jdbcTemplate.queryForObject(
                            SQL,
                            parameters,
                            new BeanPropertyRowMapper<>(TimeLimits.class)));
        } catch(IncorrectResultSizeDataAccessException e) {
            timeLimitsOptional = Optional.empty();
        }

        return timeLimitsOptional;
    }

    @Override
    public Optional<TimeLimits> getTimeLimits(final String clientName, final String assessmentId) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
                .addValue("assessmentId", assessmentId);

        final String SQL = TIMELIMITS_BASE_SQL + "   AND _efk_testid = :assessmentId";

        Optional<TimeLimits> timeLimitsOptional;
        try {
            timeLimitsOptional = Optional.of(
                    jdbcTemplate.queryForObject(
                            SQL,
                            parameters,
                            new BeanPropertyRowMapper<>(TimeLimits.class)));
        } catch(IncorrectResultSizeDataAccessException e) {
            timeLimitsOptional = Optional.empty();
        }

        return timeLimitsOptional;
    }
}
