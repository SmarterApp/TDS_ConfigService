package tds.config.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import tds.config.TimeLimitConfiguration;
import tds.config.repositories.TimeLimitConfigurationRepository;
import tds.config.repositories.impl.mappers.TimeLimitsRowMapper;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class TimeLimitConfigurationRepositoryImpl implements TimeLimitConfigurationRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public TimeLimitConfigurationRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<TimeLimitConfiguration> findTimeLimitConfiguration(final String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        final String SQL =
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
                "   clientname = :clientName \n" +
                "   AND _efk_testid IS NULL";

        Optional<TimeLimitConfiguration> maybeTimeLimitConfig;
        try {
            maybeTimeLimitConfig = Optional.of(
                    jdbcTemplate.queryForObject(
                            SQL,
                            parameters,
                            new TimeLimitsRowMapper()));
        } catch(IncorrectResultSizeDataAccessException e) {
            LOG.debug("{} did not return results for clientName = {}", SQL, clientName);
            maybeTimeLimitConfig = Optional.empty();
        }

        return maybeTimeLimitConfig;
    }

    @Override
    public Optional<TimeLimitConfiguration> findTimeLimitConfiguration(final String clientName, final String assessmentId) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
                .addValue("assessmentId", assessmentId);

        // The TA Checkin time should always come from the timelimits record that has a NULL assessmentId value (that is
        // it should always come from the Client Name level).
        final String SQL =
                "SELECT \n" +
                "   t.clientname, \n" +
                "   t._efk_testid AS assessmentId, \n" +
                "   t.environment, \n" +
                "   t.opprestart AS examRestartWindowMinutes, \n" +
                "   t.oppdelay AS examDelayDays, \n" +
                "   t.interfacetimeout AS interfaceTimeoutMinutes, \n" +
                "   t.requestinterfacetimeout AS requestInterfaceTimeoutMinutes, \n" +
                "   c.tacheckintime AS taCheckinTimeMinutes \n" +
                "FROM \n" +
                "   session.timelimits t \n" +
                "JOIN" +
                "   session.timelimits c \n" +
                "   ON (t.clientname = c.clientname \n" +
                "       AND c._efk_testid IS NULL) \n" +
                "WHERE \n" +
                "   t.clientname = :clientName \n" +
                "   AND t._efk_testid = :assessmentId";

        Optional<TimeLimitConfiguration> maybeTimeLimitConfig;
        try {
            maybeTimeLimitConfig = Optional.of(
                    jdbcTemplate.queryForObject(
                            SQL,
                            parameters,
                            new TimeLimitsRowMapper()));
        } catch(IncorrectResultSizeDataAccessException e) {
            LOG.debug("{} did not return results for clientName = {}, assessmentId = {}", SQL, clientName, assessmentId);
            maybeTimeLimitConfig = Optional.empty();
        }

        return maybeTimeLimitConfig;
    }
}
