package tds.config.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tds.config.Accommodation;
import tds.config.repositories.AccommodationsQueryRepository;

@Repository
public class AccommodationsQueryRepositoryImpl implements AccommodationsQueryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SEGMENTED_ASSESSMENT_BY_KEY_SQL = "SELECT \n" +
        "  distinct SegmentPosition as segment,\n" +
        "  SEG.modekey as segmentKey, \n" +
        "  TType.DisableOnGuestSession as disableOnGuestSession, \n" +
        "  TType.SortOrder as toolTypeSortOrder , \n" +
        "  TT.SortOrder as toolValueSortOrder, \n" +
        "  TType.TestMode as typeMode, \n" +
        "  TT.TestMode as toolMode, \n" +
        "  TT.Type as accType, \n" +
        "  TT.Value as accValue, \n" +
        "  TT.Code as accCode, \n" +
        "  TT.IsDefault as isDefault, \n" +
        "  TT.AllowCombine as allowCombine, \n" +
        "  TType.IsFunctional as isFunctional, \n" +
        "  TType.AllowChange as allowChange,\n" +
        "  TType.IsSelectable as isSelectable, \n" +
        "  TType.IsVisible as isVisible, \n" +
        "  TType.studentControl as studentControl, \n" +
        "  null as dependsOnToolType, \n" +
        "  IsEntryControl as isEntryControl\n" +
        "FROM \n" +
        "  configs.client_testmode MODE \n" +
        "  JOIN configs.client_testtooltype TType ON \n" +
        "    MODE.clientname = TType.clientname\n" +
        "  JOIN configs.client_testtool TT ON \n" +
        "    TT.Type = TType.Toolname AND \n" +
        "    MODE.clientname = TT.clientname\n" +
        "  JOIN configs.client_segmentproperties SEG ON \n" +
        "    MODE.testkey = SEG.modekey AND \n" +
        "    TType.Context = SEG.segmentID AND\n" +
        "    TT.Context = SEG.segmentId\n" +
        "WHERE \n" +
        "  SEG.parentTest = MODE.testID \n" +
        "  and MODE.testkey = :testKey \n" +
        "  and TType.ContextType = 'SEGMENT' \n" +
        "  and TT.ContextType = 'SEGMENT' \n" +
        "  and (TType.TestMode = 'ALL' AND TT.TestMode = 'ALL') \n" +
        "  or (TType.TestMode = MODE.mode and TT.TestMode = MODE.mode) \n";

    private static final String NON_SEGMENTED_ASSESSMENT_BY_KEY_SQL = "SELECT \n" +
        "  distinct 0 as segment, \n" +
        "  null as segmentKey, \n" +
        "  TType.DisableOnGuestSession as disableOnGuestSession, \n" +
        "  TType.SortOrder as toolTypeSortOrder, \n" +
        "  TT.SortOrder as toolValueSortOrder, \n" +
        "  TType.TestMode as typeMode,\n" +
        "  TT.TestMode as toolMode, \n" +
        "  TT.Type as accType, \n" +
        "  TT.Value as accValue, \n" +
        "  TT.Code as accCode, \n" +
        "  TT.IsDefault as isDefault, \n" +
        "  TT.AllowCombine as allowCombine, \n" +
        "  TType.IsFunctional as isFunctional, \n" +
        "  TType.AllowChange as allowChange, \n" +
        "  TType.IsSelectable as isSelectable, \n" +
        "  TType.IsVisible as isVisible, \n" +
        "  TType.studentControl as studentControl,\n" +
        "  TType.DependsOnToolType as dependsOnToolType, \n" +
        "  TType.IsEntryControl as isEntryControl\n" +
        "FROM \n" +
        "  configs.client_testtooltype TType \n" +
        "  JOIN configs.client_testtool TT ON \n" +
        "    TT.Type = TType.Toolname AND \n" +
        "    TT.ClientName = TType.clientname\n" +
        "  JOIN configs.client_testmode MODE ON \n" +
        "    TType.Context = MODE.testid AND \n" +
        "    TT.ClientName = MODE.clientname AND\n" +
        "    TType.ClientName = MODE.clientname\n" +
        "WHERE\n" +
        "  MODE.testkey = :testKey  \n" +
        "  and TType.ContextType = 'TEST' \n" +
        "  and TT.Context = MODE.testid \n" +
        "  and TT.ContextType = 'TEST' \n" +
        "  and (TT.Type <> 'Language' or TT.Code in (:languages)) \n" +
        "  and (TType.TestMode = 'ALL' AND TT.TestMode = 'ALL') \n" +
        "  or (TType.TestMode = MODE.mode and TT.TestMode = MODE.mode) \n";

    @Autowired
    public AccommodationsQueryRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Accommodation> findAccommodationsForSegmentedAssessmentByKey(String assessmentKey) {
        return findAssessmentAccommodationsByKey(assessmentKey, true, new HashSet<>());
    }

    @Override
    public List<Accommodation> findAccommodationsForNonSegmentedAssessmentByKey(String assessmentKey, Set<String> languageCodes) {
        return findAssessmentAccommodationsByKey(assessmentKey, false, languageCodes);
    }

    @Override
    public List<Accommodation> findAssessmentAccommodationsById(String clientName, String assessmentId) {
        SqlParameterSource parameters = new MapSqlParameterSource("assessmentId", assessmentId)
            .addValue("clientName", clientName);

        String SQL = "(\n" +
            "SELECT\n" +
            "  DISTINCT 0 AS segment, \n" +
            "  NULL AS segmentKey, \n" +
            "  TType.DisableOnGuestSession AS disableOnGuestSession, \n" +
            "  TType.SortOrder AS toolTypeSortOrder, \n" +
            "  TT.SortOrder AS toolValueSortOrder, \n" +
            "  TType.TestMode AS typeMode,\n" +
            "  TT.TestMode AS toolMode, \n" +
            "  TT.Type AS accType, \n" +
            "  TT.Value AS accValue, \n" +
            "  TT.Code AS accCode, \n" +
            "  TT.IsDefault AS isDefault, \n" +
            "  TT.AllowCombine AS allowCombine, \n" +
            "  TType.IsFunctional AS isFunctional, \n" +
            "  TType.AllowChange AS allowChange, \n" +
            "  TType.IsSelectable AS isSelectable, \n" +
            "  TType.IsVisible AS isVisible, \n" +
            "  TType.studentControl AS studentControl,\n" +
            "  TType.DependsOnToolType AS dependsOnToolType, \n" +
            "  (SELECT count(1) FROM client_testtool TOOL WHERE TOOL.ContextType = 'TEST' AND TOOL.Context = :assessmentId AND TOOL.clientname = :clientName AND TOOL.Type = TT.Type) AS ValCount,    \n" +
            "  TType.IsEntryControl AS isEntryControl\n" +
            "FROM\n" +
            "  configs.client_testtooltype TType\n" +
            "JOIN configs.client_testtool TT \n" +
            "  ON TType.context = TT.context\n" +
            "  AND TType.clientname = TT.clientname\n" +
            "  AND TType.contexttype = TT.contexttype\n" +
            "  AND TType.toolname = TT.type\n" +
            "WHERE\n" +
            "  TType.contexttype = 'TEST'\n" +
            "  AND TType.context = :assessmentId\n" +
            "  AND TType.clientname = :clientName\n" +
            ") UNION ALL (\n" +
            "SELECT\n" +
            "  DISTINCT SegmentPosition AS segment, \n" +
            "  SEG.modekey AS segmentKey,\n" +
            "  TType.DisableOnGuestSession AS disableOnGuestSession, \n" +
            "  TType.SortOrder AS toolTypeSortOrder, \n" +
            "  TT.SortOrder AS toolValueSortOrder, \n" +
            "  TType.TestMode AS typeMode, \n" +
            "  TT.TestMode AS toolMode, \n" +
            "  TT.Type AS accType, \n" +
            "  TT.Value AS accValue, \n" +
            "  TT.Code AS accCode, \n" +
            "  TT.IsDefault AS isDefault, \n" +
            "  TT.AllowCombine AS allowCombine, \n" +
            "  TType.IsFunctional AS isFunctional, \n" +
            "  TType.AllowChange AS allowChange, \n" +
            "  TType.IsSelectable AS isSelectable, \n" +
            "  TType.IsVisible AS isVisible, \n" +
            "  TType.studentControl AS studentControl, \n" +
            "  NULL AS dependsOnToolType, \n" +
            "  (SELECT count(1) FROM client_testtool TOOL WHERE TOOL.ContextType = 'TEST' AND TOOL.Context = :assessmentId AND TOOL.clientname = :clientName AND TOOL.Type = TT.Type) AS ValCount,  \n" +
            "  IsEntryControl AS isEntryControl \n" +
            "FROM\n" +
            "  configs.client_testtooltype TType\n" +
            "JOIN configs.client_testtool TT \n" +
            "  ON TType.context = TT.context\n" +
            "  AND TType.clientname = TT.clientname\n" +
            "  AND TType.contexttype = TT.contexttype\n" +
            "  AND TType.toolname = TT.type\n" +
            "JOIN \n" +
            "  configs.client_segmentproperties SEG\n" +
            "  ON TType.context = SEG.segmentid\n" +
            "WHERE\n" +
            "  SEG.parenttest = :assessmentId\n" +
            "  AND TType.contexttype = 'SEGMENT'\n" +
            "  AND TType.clientname = :clientName\n" +
            ") UNION ALL (\n" +
            "SELECT \n" +
            "  DISTINCT 0 AS segment,\n" +
            "  NULL AS segmentKey, \n" +
            "  TType.DisableOnGuestSession AS disableOnGuestSession,  \n" +
            "  TType.SortOrder AS toolTypeSortOrder, \n" +
            "  TT.SortOrder AS toolValueSortOrder, \n" +
            "  TType.TestMode AS typeMode, \n" +
            "  TT.TestMode AS toolMode, \n" +
            "  TT.Type AS accType, \n" +
            "  TT.Value AS accValue, \n" +
            "  TT.Code AS accCode, \n" +
            "  TT.IsDefault AS isDefault, \n" +
            "  TT.AllowCombine AS allowCombine, \n" +
            "  TType.IsFunctional AS isFunctional, \n" +
            "  TType.AllowChange AS allowChange, \n" +
            "  TType.IsSelectable AS isSelectable, \n" +
            "  TType.IsVisible AS isVisible, \n" +
            "  TType.studentControl AS studentControl, \n" +
            "  TType.DependsOnToolType AS dependsOnToolType, \n" +
            "  (SELECT count(1) FROM client_testtool TOOL WHERE TOOL.ContextType = 'TEST' AND TOOL.Context = '*' AND TOOL.clientname = :clientName AND TOOL.Type = TT.Type) AS ValCount, \n" +
            "  TType.IsEntryControl AS isEntryControl\n" +
            "FROM\n" +
            "  configs.client_testtooltype TType\n" +
            "JOIN configs.client_testtool TT \n" +
            "  ON TType.context = TT.context\n" +
            "  AND TType.clientname = TT.clientname\n" +
            "  AND TType.contexttype = TT.contexttype\n" +
            "  AND TType.toolname = TT.type\n" +
            "WHERE\n" +
            "  TType.contexttype = 'TEST'\n" +
            "  AND TType.context = '*'\n" +
            "  AND TType.clientname = :clientName\n" +
            "  AND NOT EXISTS \n" +
            "    (\n" +
            "      SELECT \n" +
            "        toolname \n" +
            "      FROM configs.client_testtooltype Tool \n" +
            "      WHERE Tool.ContextType = 'TEST' \n" +
            "        AND Tool.Context = :assessmentId \n" +
            "        AND Tool.Toolname = TType.Toolname \n" +
            "        AND Tool.Clientname = :clientName\n" +
            "    )\n" +
            ")";

        return jdbcTemplate.query(SQL, parameters, new ResultSetRowMapper());
    }

    private List<Accommodation> findAssessmentAccommodationsByKey(String assessmentKey, boolean segmented, Set<String> languageCodes) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("testKey", assessmentKey)
            .addValue("languages", languageCodes);

        String SQL;
        if (segmented) {
            SQL = "(" + SEGMENTED_ASSESSMENT_BY_KEY_SQL + ")";
        } else {
            SQL = "(" + NON_SEGMENTED_ASSESSMENT_BY_KEY_SQL + ")";
        }

        SQL += "UNION ALL \n " +
            "(\n" +
            "SELECT \n" +
            "  distinct 0 as segment,\n" +
            "  null as segmentKey, \n" +
            "  TType.DisableOnGuestSession as disableOnGuestSession,  \n" +
            "  TType.SortOrder as toolTypeSortOrder, \n" +
            "  TT.SortOrder as toolValueSortOrder, \n" +
            "  TType.TestMode as typeMode, \n" +
            "  TT.TestMode as toolMode, \n" +
            "  TT.Type as accType, \n" +
            "  TT.Value as accValue, \n" +
            "  TT.Code as accCode, \n" +
            "  TT.IsDefault as isDefault, \n" +
            "  TT.AllowCombine as allowCombine, \n" +
            "  TType.IsFunctional as isFunctional, \n" +
            "  TType.AllowChange as allowChange, \n" +
            "  TType.IsSelectable as isSelectable, \n" +
            "  TType.IsVisible as isVisible, \n" +
            "  TType.studentControl as studentControl, \n" +
            "  TType.DependsOnToolType as dependsOnToolType, \n" +
            "  TType.IsEntryControl as isEntryControl\n" +
            "FROM  \n" +
            "  configs.client_testmode MODE\n" +
            "  JOIN configs.client_testtooltype TType ON\n" +
            "    TType.clientname = MODE.clientname\n" +
            "  JOIN configs.client_testtool TT ON\n" +
            "    TT.clientname = TType.clientname AND\n" +
            "    TT.Type = TType.Toolname\n" +
            "WHERE \n" +
            "  MODE.testkey = :testKey \n" +
            "  and TType.ContextType = 'TEST' \n" +
            "  and TType.Context = '*' \n" +
            "  and TT.ContextType = 'TEST' \n" +
            "  and TT.Context = '*' \n" +
            "  and (TType.TestMode = 'ALL' AND TT.TestMode = 'ALL') \n" +
            "  or (TType.TestMode = MODE.mode and TT.TestMode = MODE.mode) \n" +
            "  and not exists (select * from configs.client_testtooltype Tool where Tool.ContextType = 'TEST' and Tool.Context = MODE.testID and Tool.Toolname = TType.Toolname and Tool.Clientname = MODE.clientname)\n" +
            ")";

        return jdbcTemplate.query(SQL, parameters, new ResultSetRowMapper());
    }

    private static class ResultSetRowMapper implements RowMapper<Accommodation> {
        @Override
        public Accommodation mapRow(ResultSet rs, int i) throws SQLException {
            return new tds.config.Accommodation.Builder()
                .withSegmentPosition(rs.getInt("segment"))
                .withDisableOnGuestSession(rs.getBoolean("disableOnGuestSession"))
                .withToolTypeSortOrder(rs.getInt("toolTypeSortOrder"))
                .withToolValueSortOrder(rs.getInt("toolValueSortOrder"))
                .withTypeMode(rs.getString("typeMode"))
                .withToolMode(rs.getString("toolMode"))
                .withAccommodationType(rs.getString("accType"))
                .withAccommodationValue(rs.getString("accValue"))
                .withAccommodationCode(rs.getString("accCode"))
                .withDefaultAccommodation(rs.getBoolean("isDefault"))
                .withAllowCombine(rs.getBoolean("allowCombine"))
                .withFunctional(rs.getBoolean("isFunctional"))
                .withAllowChange(rs.getBoolean("allowChange"))
                .withSelectable(rs.getBoolean("isSelectable"))
                .withVisible(rs.getBoolean("isVisible"))
                .withStudentControl(rs.getBoolean("studentControl"))
                .withDependsOnToolType(rs.getString("dependsOnToolType"))
                .withEntryControl(rs.getBoolean("isEntryControl"))
                .withSegmentKey(rs.getString("segmentKey"))
                .build();
        }
    }
}
