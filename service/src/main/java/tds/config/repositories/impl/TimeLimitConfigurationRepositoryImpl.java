package tds.config.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

import tds.config.TimeLimitConfiguration;
import tds.config.repositories.TimeLimitConfigurationRepository;
import tds.config.repositories.impl.mappers.TimeLimitsRowMapper;

@Repository
public class TimeLimitConfigurationRepositoryImpl implements TimeLimitConfigurationRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public TimeLimitConfigurationRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
                "   client_timelimits \n" +
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
                "   clientname, \n" +
                "   _efk_testid AS assessmentId, \n" +
                "   environment, \n" +
                "   opprestart AS examRestartWindowMinutes, \n" +
                "   oppdelay AS examDelayDays, \n" +
                "   interfacetimeout AS interfaceTimeoutMinutes, \n" +
                "   requestinterfacetimeout AS requestInterfaceTimeoutMinutes, \n" +
                "   tacheckintime AS taCheckinTimeMinutes \n" +
                "FROM \n" +
                "   client_timelimits \n" +
                "WHERE \n" +
                "   clientname = :clientName" +
                "   AND _efk_testid = :assessmentId";

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
