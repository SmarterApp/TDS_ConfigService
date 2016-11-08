package tds.config.repositories.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tds.common.data.mapping.ResultSetMapperUtility;
import tds.config.Accommodation;
import tds.config.repositories.AccommodationsQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AccommodationsQueryRepositoryImplIntegrationTests {
    @Autowired
    private AccommodationsQueryRepository repository;

    @Autowired
    private DataSource dataSource;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        Instant now = Instant.now();

        String testModeInsertSQL = "INSERT INTO client_testmode VALUES ('SBAC_PT','SBAC-Mathematics-11','online','virtual',0,1,50,0,0,1,0,'(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015',X'0431F6515F2D11E6B2C80243FCF25EAB');";
        String segmentTestToolTypeInsertSQL = "INSERT INTO client_testtooltype (clientname, toolname, allowchange, rtsfieldname, isrequired, isselectable, dateentered, contexttype, context, testmode) VALUES ('SBAC_PT', 'toolTypeSegmented', 0, 'rts', 1, 0, :dateentered, 'SEGMENT', 'SBAC-SEG1-MATH-11', 'ALL');";
        String segmentTestToolInsertSQL = "INSERT INTO client_testtool (clientname, type, code, value, isdefault, allowcombine, context, contexttype, testmode) VALUES ('SBAC_PT', 'toolTypeSegmented', 'toolTypeSegmented', 'segmentValue', 1, 0, 'SBAC-SEG1-MATH-11', 'SEGMENT', 'ALL');";
        String segmentPropertiesInsertSQL = "INSERT INTO client_segmentproperties (ispermeable, clientname, entryapproval, exitapproval, segmentid, segmentposition, parenttest, modekey) VALUES (1, 'SBAC_PT', 1, 1, 'SBAC-SEG1-MATH-11', 99, 'SBAC-Mathematics-11', '(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015');";
        String defaultToolTypeInsertSQL = "INSERT INTO client_testtooltype (clientname, toolname, allowchange, rtsfieldname, isrequired, isselectable, dateentered, contexttype, context, testmode) VALUES ('SBAC_PT', 'toolTypeDefault', 0, 'rts', 1, 0, :dateentered, 'TEST', '*', 'ALL')  ";
        String defaultTestToolInsertSQL = "INSERT INTO client_testtool (clientname, type, code, value, isdefault, allowcombine, context, contexttype, testmode) VALUES ('SBAC_PT', 'toolTypeDefault', 'toolTypeDefault', 'defaultTool', 1, 0, '*', 'TEST', 'ALL');";

        String nonSegmentTestToolTypeInsertSQL = "INSERT INTO client_testtooltype (clientname, context, contexttype, toolname, allowchange, rtsfieldname, isrequired, isselectable, dateentered, testmode) " +
            "VALUES ('SBAC_PT', 'SBAC-Mathematics-11', 'TEST', 'Language', 0, 'rts', 1, 0, :dateentered, 'ALL');";
        String noSegmentTestToolInsertEnglishSQL = "INSERT INTO client_testtool (clientname, context, contexttype, type, code, value, isdefault, allowcombine, testmode) " +
            "VALUES ('SBAC_PT', 'SBAC-Mathematics-11', 'TEST', 'Language', 'ENU', 'ENU', 1, 0, 'ALL');";
        String noSegmentTestToolInsertFrenchSQL = "INSERT INTO client_testtool (clientname, context, contexttype, type, code, value, isdefault, allowcombine, testmode) " +
            "VALUES ('SBAC_PT', 'SBAC-Mathematics-11', 'TEST', 'Language', 'FRN', 'FRN', 1, 0, 'ALL');";

        SqlParameterSource parameters = new MapSqlParameterSource("dateentered", ResultSetMapperUtility.mapInstantToTimestamp(now));

        jdbcTemplate.update(testModeInsertSQL, parameters);
        jdbcTemplate.update(segmentTestToolTypeInsertSQL, parameters);
        jdbcTemplate.update(segmentTestToolInsertSQL, parameters);
        jdbcTemplate.update(segmentPropertiesInsertSQL, parameters);
        jdbcTemplate.update(defaultToolTypeInsertSQL, parameters);
        jdbcTemplate.update(defaultTestToolInsertSQL, parameters);
        jdbcTemplate.update(nonSegmentTestToolTypeInsertSQL, parameters);
        jdbcTemplate.update(noSegmentTestToolInsertEnglishSQL, parameters);
        jdbcTemplate.update(noSegmentTestToolInsertFrenchSQL, parameters);
    }

    @Test
    public void shouldFindAccommodationsForSegmentAndDefault() {
        List<Accommodation> accommodationList = repository.findAccommodationsForSegmentedAssessment("(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015");

        assertThat(accommodationList).hasSize(2);

        Accommodation segmentAccommodation = accommodationList.get(0);
        Accommodation defaultAccommodation = accommodationList.get(1);

        assertThat(segmentAccommodation.getAccommodationCode()).isEqualTo("toolTypeSegmented");
        assertThat(segmentAccommodation.getAccommodationType()).isEqualTo("toolTypeSegmented");
        assertThat(segmentAccommodation.getAccommodationValue()).isEqualTo("segmentValue");
        assertThat(segmentAccommodation.getDependsOnToolType()).isNull();
        assertThat(segmentAccommodation.getSegmentPosition()).isEqualTo(99);
        assertThat(segmentAccommodation.getToolMode()).isEqualTo("ALL");
        assertThat(segmentAccommodation.getToolTypeSortOrder()).isEqualTo(0);
        assertThat(segmentAccommodation.getToolValueSortOrder()).isEqualTo(0);
        assertThat(segmentAccommodation.getTypeMode()).isEqualTo("ALL");
        assertThat(segmentAccommodation.getValueCount()).isEqualTo(0);
        assertThat(segmentAccommodation.isAllowChange()).isFalse();
        assertThat(segmentAccommodation.isAllowCombine()).isFalse();
        assertThat(segmentAccommodation.isDefaultAccommodation()).isTrue();
        assertThat(segmentAccommodation.isDisableOnGuestSession()).isFalse();
        assertThat(segmentAccommodation.isEntryControl()).isFalse();
        assertThat(segmentAccommodation.isFunctional()).isTrue();
        assertThat(segmentAccommodation.isSelectable()).isFalse();
        assertThat(segmentAccommodation.isVisible()).isTrue();

        assertThat(defaultAccommodation.getAccommodationCode()).isEqualTo("toolTypeDefault");
        assertThat(defaultAccommodation.getAccommodationType()).isEqualTo("toolTypeDefault");
        assertThat(defaultAccommodation.getAccommodationValue()).isEqualTo("defaultTool");
        assertThat(defaultAccommodation.getDependsOnToolType()).isNull();
        assertThat(defaultAccommodation.getSegmentPosition()).isEqualTo(0);
        assertThat(defaultAccommodation.getToolMode()).isEqualTo("ALL");
        assertThat(defaultAccommodation.getToolTypeSortOrder()).isEqualTo(0);
        assertThat(defaultAccommodation.getToolValueSortOrder()).isEqualTo(0);
        assertThat(defaultAccommodation.getTypeMode()).isEqualTo("ALL");
        assertThat(defaultAccommodation.getValueCount()).isEqualTo(1);
        assertThat(defaultAccommodation.isAllowChange()).isFalse();
        assertThat(defaultAccommodation.isAllowCombine()).isFalse();
        assertThat(defaultAccommodation.isDefaultAccommodation()).isTrue();
        assertThat(defaultAccommodation.isDisableOnGuestSession()).isFalse();
        assertThat(defaultAccommodation.isEntryControl()).isFalse();
        assertThat(defaultAccommodation.isFunctional()).isTrue();
        assertThat(defaultAccommodation.isSelectable()).isFalse();
        assertThat(defaultAccommodation.isVisible()).isTrue();
    }

    @Test
    public void shouldFindAccommodationsForNonSegmentAndDefault() {
        Set<String> languages = new HashSet<>();
        languages.add("ENU");
        List<Accommodation> accommodationList = repository.findAccommodationsForNonSegmentedAssessment("(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015", languages);

        assertThat(accommodationList).hasSize(2);

        Accommodation englishAccommodation = accommodationList.get(0);
        Accommodation defaultAccommodation = accommodationList.get(1);

        assertThat(englishAccommodation.getAccommodationCode()).isEqualTo("ENU");
        assertThat(englishAccommodation.getAccommodationType()).isEqualTo("Language");
        assertThat(englishAccommodation.getAccommodationValue()).isEqualTo("ENU");
        assertThat(englishAccommodation.getDependsOnToolType()).isNull();
        assertThat(englishAccommodation.getSegmentPosition()).isEqualTo(0);
        assertThat(englishAccommodation.getToolMode()).isEqualTo("ALL");
        assertThat(englishAccommodation.getToolTypeSortOrder()).isEqualTo(0);
        assertThat(englishAccommodation.getToolValueSortOrder()).isEqualTo(0);
        assertThat(englishAccommodation.getTypeMode()).isEqualTo("ALL");
        assertThat(englishAccommodation.getValueCount()).isEqualTo(2);
        assertThat(englishAccommodation.isAllowChange()).isFalse();
        assertThat(englishAccommodation.isAllowCombine()).isFalse();
        assertThat(englishAccommodation.isDefaultAccommodation()).isTrue();
        assertThat(englishAccommodation.isDisableOnGuestSession()).isFalse();
        assertThat(englishAccommodation.isEntryControl()).isFalse();
        assertThat(englishAccommodation.isFunctional()).isTrue();
        assertThat(englishAccommodation.isSelectable()).isFalse();
        assertThat(englishAccommodation.isVisible()).isTrue();

        assertThat(defaultAccommodation.getAccommodationCode()).isEqualTo("toolTypeDefault");
        assertThat(defaultAccommodation.getAccommodationType()).isEqualTo("toolTypeDefault");
        assertThat(defaultAccommodation.getAccommodationValue()).isEqualTo("defaultTool");
        assertThat(defaultAccommodation.getDependsOnToolType()).isNull();
        assertThat(defaultAccommodation.getSegmentPosition()).isEqualTo(0);
        assertThat(defaultAccommodation.getToolMode()).isEqualTo("ALL");
        assertThat(defaultAccommodation.getToolTypeSortOrder()).isEqualTo(0);
        assertThat(defaultAccommodation.getToolValueSortOrder()).isEqualTo(0);
        assertThat(defaultAccommodation.getTypeMode()).isEqualTo("ALL");
        assertThat(defaultAccommodation.getValueCount()).isEqualTo(1);
        assertThat(defaultAccommodation.isAllowChange()).isFalse();
        assertThat(defaultAccommodation.isAllowCombine()).isFalse();
        assertThat(defaultAccommodation.isDefaultAccommodation()).isTrue();
        assertThat(defaultAccommodation.isDisableOnGuestSession()).isFalse();
        assertThat(defaultAccommodation.isEntryControl()).isFalse();
        assertThat(defaultAccommodation.isFunctional()).isTrue();
        assertThat(defaultAccommodation.isSelectable()).isFalse();
        assertThat(defaultAccommodation.isVisible()).isTrue();
    }
}
