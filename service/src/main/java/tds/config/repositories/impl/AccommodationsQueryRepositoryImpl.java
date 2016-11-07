package tds.config.repositories.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;

import tds.config.Accommodation;
import tds.config.repositories.AccommodationsQueryRepository;

@Repository
public class AccommodationsQueryRepositoryImpl implements AccommodationsQueryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SEGMENTED_ASSESSMENT_SQL = "SELECT \n" +
        "  distinct SegmentPosition as segment,\n" +
        "  TType.DisableOnGuestSession as disableOnGuestSession, \n" +
        "  TType.SortOrder as toolTypeSortOrder , \n" +
        "  TT.SortOrder as toolValueSortOrder, \n" +
        "  TType.TestMode as typeMode, \n" +
        "  TT.TestMode as toolMode, \n" +
        "  Type as accType, \n" +
        "  Value as accValue, \n" +
        "  Code as accCode, \n" +
        "  IsDefault as isDefault, \n" +
        "  AllowCombine as allowCombine, \n" +
        "  IsFunctional as isFunctional, \n" +
        "  AllowChange as allowChange,\n" +
        "  IsSelectable as isSelectable, \n" +
        "  IsVisible as isVisible, \n" +
        "  studentControl as studentControl, \n" +
        "  (select count(*) from configs.client_testtool TOOL where TOOL.ContextType = 'TEST' and TOOL.Context = MODE.testID and TOOL.clientname = MODE.clientname and TOOL.Type = TT.Type) as valCount, \n" +
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
        "  and (TType.TestMode = 'ALL' or TType.TestMode = MODE.mode) \n" +
        "  and (TT.TestMode = 'ALL' or TT.TestMode = MODE.mode)";

    private static final String NON_SEGMENTED_ASSESSMENT_SQL = "SELECT \n" +
        "  distinct 0 as segment, \n" +
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
        "  (select count(*) from configs.client_testtool TOOL where TOOL.ContextType = 'TEST' and TOOL.Context = MODE.testID  and TOOL.clientname = MODE.clientname and TOOL.Type = TT.Type) as valCount,\n" +
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
        "  and (TType.TestMode = 'ALL' or TType.TestMode = MODE.mode) \n" +
        "  and (TT.TestMode = 'ALL' or TT.TestMode = MODE.mode)";

    public AccommodationsQueryRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Accommodation> findAssessmentAccommodations(String assessmentKey, boolean segmented, Set<String> languageCodes) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("testKey", assessmentKey)
            .addValue("languages", languageCodes);

        String SQL;
        if(segmented) {
            SQL = "(" + SEGMENTED_ASSESSMENT_SQL + ")";
        } else {
            SQL = "(" + NON_SEGMENTED_ASSESSMENT_SQL + ")";
        }

        SQL += "UNION ALL \n " +
            "(\n" +
            "SELECT \n" +
            "  distinct 0 as segment,\n" +
            "  TType.DisableOnGuestSession as disableOnGuestSession,  \n" +
            "  TType.SortOrder as toolTypeSortOrder, \n" +
            "  TT.SortOrder as toolValueSortOrder, \n" +
            "  TType.TestMode as typeMode, \n" +
            "  TT.TestMode as toolMode, \n" +
            "  Type as accType, \n" +
            "  Value as accValue, \n" +
            "  Code as accCode, \n" +
            "  IsDefault as isDefault, \n" +
            "  AllowCombine as allowCombine, \n" +
            "  IsFunctional as isFunctional, \n" +
            "  AllowChange as allowChange, \n" +
            "  IsSelectable as isSelectable, \n" +
            "  IsVisible as isVisible, \n" +
            "  studentControl as studentControl, \n" +
            "  (select count(*) from configs.client_testtool TOOL where TOOL.ContextType = 'TEST' and TOOL.Context = '*' and TOOL.clientname = MODE.clientname and TOOL.Type = TT.Type) as valCount, \n" +
            "  DependsOnToolType as dependsOnToolType, \n" +
            "  IsEntryControl as isEntryControl\n" +
            "FROM  \n" +
            "  configs.client_testmode MODE\n" +
            "  JOIN configs.client_testtooltype TType ON\n" +
            "    TType.clientname = MODE.clientname\n" +
            "  JOIN configs.client_testtool TT ON\n" +
            "    TT.clientname = TType.clientname AND\n" +
            "    TT.Type = TType.Toolname\n"  +
            "WHERE \n" +
            "  MODE.testkey = :testKey \n" +
            "  and TType.ContextType = 'TEST' \n" +
            "  and TType.Context = '*' \n" +
            "  and TT.ContextType = 'TEST' \n" +
            "  and TT.Context = '*' \n" +
            "  and (TType.TestMode = 'ALL' or TType.TestMode = MODE.mode) \n" +
            "  and (TT.TestMode = 'ALL' or TT.TestMode = MODE.mode) \n" +
            "  and not exists (select * from configs.client_testtooltype Tool where Tool.ContextType = 'TEST' and Tool.Context = MODE.testID and Tool.Toolname = TType.Toolname and Tool.Clientname = MODE.clientname)\n" +
            ")";

        return jdbcTemplate.query(SQL, parameters, (rs, rowNum) ->
            new Accommodation.Builder()
                .withSegmentPosition(rs.getInt("segment"))
                .withDisableOnGuestSession(rs.getBoolean("disableOnGuestSession"))
                .withToolTypeSortOrder(rs.getInt("toolTypeSortOrder"))
                .withToolValueSortOrder(rs.getInt("toolValueSortOrder"))
                .withTypeMode(rs.getString("typeMode"))
                .withToolMode(rs.getString("toolMode"))
                .withAccType(rs.getString("accType"))
                .withAccValue(rs.getString("accValue"))
                .withAccCode(rs.getString("accCode"))
                .withDefaultAccommodation(rs.getBoolean("isDefault"))
                .withAllowCombine(rs.getBoolean("allowCombine"))
                .withFunctional(rs.getBoolean("isFunctional"))
                .withAllowChange(rs.getBoolean("allowChange"))
                .withSelectable(rs.getBoolean("isSelectable"))
                .withVisible(rs.getBoolean("isVisible"))
                .withStudentControl(rs.getBoolean("studentControl"))
                .withValueCount(rs.getInt("valCount"))
                .withDependsOnToolType(rs.getString("dependsOnToolType"))
                .withEntryControl(rs.getBoolean("isEntryControl"))
                .build()
        );
    }
}
