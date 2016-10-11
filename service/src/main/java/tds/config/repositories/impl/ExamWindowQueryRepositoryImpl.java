package tds.config.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import tds.config.model.CurrentExamWindow;
import tds.config.model.ExamFormWindow;
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
        final MapSqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
            .addValue("assessmentId", assessmentId)
            .addValue("shiftWindowStart", shiftWindowStart)
            .addValue("shiftWindowEnd", shiftWindowEnd);

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

    @Override
    public List<ExamFormWindow> findExamFormWindows(String clientName, String assessmentId, int sessionType, int shiftWindowStart, int shiftWindowEnd, int shiftFormStart, int shiftFormEnd) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
            .addValue("assessmentId", assessmentId)
            .addValue("sessionType", sessionType)
            .addValue("shiftWindowStart", shiftWindowStart)
            .addValue("shiftWindowEnd", shiftWindowEnd)
            .addValue("shiftFormStart", shiftFormStart)
            .addValue("shiftFormEnd", shiftFormEnd);

        String SQL = "SELECT\n" +
            "   windowID, \n" +
            "   W.numopps as windowMax, \n" +
            "   M.maxopps as modeMax, \n" +
            "   CASE WHEN W.startDate is null THEN NOW() ELSE (W.startDate + INTERVAL :shiftWindowsStart DAY) end as startDate,\n" +
            "   CASE WHEN W.endDate is null THEN NOW() ELSE (W.endDate + INTERVAL :shiftWindowsEnd DAY) end  as endDate,\n" +
            "   CASE WHEN F.startDate is null THEN NOW() ELSE ( F.startdate + INTERVAL :shiftFormStart DAY) end as formStart,\n" +
            "   CASE WHEN F.enddate is null THEN NOW() ELSE (F.enddate + INTERVAL :shiftFormEnd DAY ) end as formEnd,\n" +
            "   _efk_TestForm as formKey, \n" +
            "   FormID, \n" +
            "   F.Language, \n" +
            "   M.mode,\n" +
            "   M.testkey, \n" +
            "   W.sessionType as windowSession, \n" +
            "   M.sessionType as modeSession\n" +
            "FROM configs.client_testwindow W\n" +
            "JOIN configs.client_testmode M ON\n" +
            "   M.testid = W.testid AND\n" +
            "   M.clientname = W.clientname AND\n" +
            "   (M.sessionType = -1 or M.sessionType = :sessionType) \n" +
            "JOIN configs.client_testformproperties F ON \n" +
            "   M.testkey = F.testkey AND\n" +
            "   F.testid = W.testid AND\n" +
            "   F.clientname = W.clientname AND\n" +
            "   NOW() BETWEEN CASE WHEN F.startDate is null THEN NOW() ELSE (F.startdate + INTERVAL :shiftFormStart DAY) END\n" +
            "   AND CASE WHEN F.enddate is null THEN NOW() ELSE (F.enddate + INTERVAL :shiftFormEnd DAY) END      \n" +
            "WHERE W.clientname = :clientName AND \n" +
            "      W.testid = :assessmentId AND\n" +
            "      (W.sessionType = -1 or W.sessionType = :sessionType) AND \n" +
            "      NOW() BETWEEN CASE WHEN W.startDate is null THEN NOW() ELSE (W.startDate + INTERVAL :shiftWindowStart DAY) END\n" +
            "    AND CASE WHEN W.endDate is null THEN NOW() ELSE (W.endDate + INTERVAL :shiftWindowEnd DAY ) END;\n" +
            "UNION (\n" +
            "SELECT\n" +
            "   windowID, \n" +
            "   W.numopps, \n" +
            "   M.maxopps,\n" +
            "   CASE WHEN W.startDate is null THEN NOW() ELSE  (W.startDate + INTERVAL :shiftWindowStart DAY) end  as startDate ,\n" +
            "   CASE WHEN W.endDate is null THEN NOW() ELSE (W.endDate + INTERVAL :shiftWindowEnd DAY) end  as endDate ,\n" +
            "   CASE WHEN F.startDate is null THEN NOW() ELSE ( F.startdate + INTERVAL :shiftFormStart DAY) end as formStart ,\n" +
            "   CASE WHEN F.enddate is null THEN NOW() ELSE (F.enddate + INTERVAL :shiftFormEnd DAY ) end as formEnd,\n" +
            "   _efk_TestForm as formKey, \n" +
            "   FormID, \n" +
            "   F.Language, \n" +
            "   M.mode, \n" +
            "   M.testkey as TestKey, \n" +
            "   W.sessionType , \n" +
            "   M.sessionType\n" +
            "FROM \n" +
            "   configs.client_segmentproperties S\n" +
            "   JOIN configs.client_testformproperties F ON \n" +
            "            S.clientname = :clientName\n" +
            "            AND S.parentTest = :assessmentId \n" +
            "            AND S.clientname = F.clientname\n" +
            "            AND NOW() between \n" +
            "               ( CASE WHEN F.startDate is null THEN NOW() ELSE (F.startdate + INTERVAL :shiftFormStart DAY) end )\n" +
            "               AND ( CASE WHEN F.enddate is null THEN NOW() ELSE (F.enddate + INTERVAL :shiftFormEnd DAY) end )\n" +
            "            AND S.segmentid = F.testid\n" +
            "   JOIN configs.client_testmode M ON\n" +
            "            F.clientname = M.clientname\n" +
            "            AND S.parentTest = M.testID \n" +
            "            AND (M.sessionType = -1 or M.sessionType = :sessionType) \n" +
            "            AND S.modekey = M.testkey\n" +
            "   JOIN configs.client_testwindow W ON\n" +
            "            M.clientname = W.clientname\n" +
            "            AND W.testID = S.parentTest \n" +
            "            AND (W.sessionType = -1 or W.sessionType = :sessionType)\n" +
            "            AND NOW() between\n" +
            "               ( CASE WHEN W.startDate is null THEN NOW() ELSE (W.startDate + INTERVAL :shiftWindowStart DAY) end )\n" +
            "               AND ( CASE WHEN W.endDate is null THEN NOW() ELSE (W.endDate + INTERVAL :shiftWindowEnd DAY ) end )\n" +
            ");";


        return jdbcTemplate.query(SQL, parameters, (rs, rowNum) -> new ExamFormWindow(
            rs.getString("windowId"),
            rs.getInt("windowMax"),
            rs.getInt("modeMax")
        ));
    }
}
