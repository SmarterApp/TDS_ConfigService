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

        SqlParameterSource parameters = new MapSqlParameterSource("dateentered", ResultSetMapperUtility.mapInstantToTimestamp(now));

        jdbcTemplate.update(testModeInsertSQL, parameters);
        jdbcTemplate.update(segmentTestToolTypeInsertSQL, parameters);
        jdbcTemplate.update(segmentTestToolInsertSQL, parameters);
        jdbcTemplate.update(segmentPropertiesInsertSQL, parameters);
        jdbcTemplate.update(defaultToolTypeInsertSQL, parameters);
        jdbcTemplate.update(defaultTestToolInsertSQL, parameters);
    }

    @Test
    public void shouldFindAccommodationsForSegmentAndDefault() {
        List<Accommodation> accommodationList = repository.findAssessmentAccommodations("(SBAC_PT)SBAC-Mathematics-11-Spring-2013-2015", true, new HashSet<>());

        assertThat(accommodationList).hasSize(2);
    }
}
