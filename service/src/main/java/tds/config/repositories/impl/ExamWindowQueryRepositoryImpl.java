package tds.config.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

import tds.config.model.CurrentExamWindow;
import tds.config.repositories.ExamWindowQueryRepository;

import static tds.common.data.mapping.ResultSetMapperUtility.mapTimeStampToInstant;

@Repository
public class ExamWindowQueryRepositoryImpl implements ExamWindowQueryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ExamWindowQueryRepositoryImpl(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<CurrentExamWindow> findCurrentTestWindowsForGuest(String clientName, String assessmentId, int shiftWindowStart, int shiftWindowEnd) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);
        parameters.addValue("assessmentId", assessmentId);
        parameters.addValue("shiftWindowStart", shiftWindowStart);
        parameters.addValue("shiftWindowEnd", shiftWindowEnd);

        String SQL = "SELECT \n" +
            "      distinct W.numopps as windowMax, \n" +
            "      W.windowID,\n" +
            "      case when W.startDate is null then NOW() else ( W.startDate + INTERVAL :shiftWindowStart DAY) end as startDate,\n" +
            "      case when W.endDate is null then NOW() else ( W.endDate + INTERVAL :shiftWindowEnd DAY) end as endDate,\n" +
            "      M.mode, \n" +
            "      M.testkey, \n" +
            "      M.maxopps as modeMax,\n" +
            "      W.sessionType as windowSession, \n" +
            "      M.sessionType as modeSession \n" +
            "FROM \n" +
            "    configs.client_testwindow W \n" +
            "JOIN configs.client_testmode M \n" +
            "ON M.clientname = W.clientname AND W.testID = M.testID\n" +
            "WHERE \n" +
            "    W.clientname = :clientName and W.testID = :assessmentId \n" +
            "AND \n" +
            "    NOW() BETWEEN\n" +
            "    CASE\n" +
            "        WHEN W.startDate is null \n" +
            "        THEN NOW() \n" +
            "        ELSE ( W.startDate + INTERVAL :shiftWindowStart DAY)\n" +
            "    END\n" +
            "AND \n" +
            "    CASE \n" +
            "        WHEN W.endDate is null \n" +
            "        THEN NOW() \n" +
            "        ELSE ( W.endDate + INTERVAL :shiftWindowEnd DAY) \n" +
            "    END    \n" +
            "AND (M.sessionType = -1 or M.sessionType = 0) \n" +
            "AND (W.sessionType = -1 OR W.sessionType = 0);";

        Optional<CurrentExamWindow> maybeCurrentExamWindow;
        try {
            CurrentExamWindow cew = jdbcTemplate.queryForObject(SQL, parameters, (rs, rowNum) ->
                new CurrentExamWindow.Builder()
                    .withWindowId(rs.getString("windowID"))
                    .withMode(rs.getString("mode"))
                    .withWindowMaxAttempts(rs.getInt("windowMax"))
                    .withStartTime(mapTimeStampToInstant(rs, "startDate"))
                    .withEndTime(mapTimeStampToInstant(rs, "endDate"))
                    .withMode(rs.getString("mode"))
                    .withModeMaxAttempts(rs.getInt("modeMax"))
                    .withWindowSessionType(rs.getInt("windowSession"))
                    .withModeSessionType(rs.getInt("modeSession"))
                    .build()
            );

            maybeCurrentExamWindow = Optional.of(cew);
        } catch (EmptyResultDataAccessException erd) {
            maybeCurrentExamWindow = Optional.empty();
        }

        return maybeCurrentExamWindow;
    }
}
