package tds.config.repositories.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Optional;

import tds.config.ClientTestProperty;
import tds.config.repositories.ClientTestPropertyQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ClientTestPropertyQueryRepositoryImplIntegrationTests {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private ClientTestPropertyQueryRepository repository;

    @Before
    public void setUp() {
        String clientTestPropertyInsertSQL = "INSERT INTO client_testproperties (clientname,testid,maxopportunities,handscoreproject,prefetch,datechanged,isprintable,isselectable,label,printitemtypes,scorebytds,batchmodereport,subjectname,origin,source,maskitemsbysubject,initialabilitybysubject,startdate,enddate,ftstartdate,ftenddate,accommodationfamily,sortorder,rtsformfield,rtswindowfield,windowtideselectable,requirertswindow,reportinginstrument,tide_id,forcecomplete,rtsmodefield,modetideselectable,requirertsmode,requirertsmodewindow,deleteunanswereditems,abilityslope,abilityintercept,validatecompleteness,gradetext,initialabilitytestid,proctoreligibility,category) \n" +
            "VALUES ('SBAC_PT','IRP-Perf-ELA-11',3,NULL,2,NULL,0,1,'Grade 11 ELA','',1,0,'ELA',NULL,NULL,1,1,NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow',0,1,NULL,NULL,1,'tds-testmode',0,0,0,0,1,0,0,'Grade 11',NULL,0,NULL);";

        jdbcTemplate.update(clientTestPropertyInsertSQL, new MapSqlParameterSource());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldReturnEmptyWhenClientTestPropertyCannotBeFound() {
        Optional<ClientTestProperty> maybeClientTestProperty = repository.findClientTestProperty("SBAC_PT", "BOGUS");
        assertThat(maybeClientTestProperty).isNotPresent();
    }

    @Test
    public void shouldReturnClientTestProperty() {
        Optional<ClientTestProperty> maybeClientTestProperty = repository.findClientTestProperty("SBAC_PT", "IRP-Perf-ELA-11");
        assertThat(maybeClientTestProperty).isPresent();

        ClientTestProperty property = maybeClientTestProperty.get();

        assertThat(property.getClientName()).isEqualTo("SBAC_PT");
        assertThat(property.getAssessmentId()).isEqualTo("IRP-Perf-ELA-11");
        assertThat(property.getMaxOpportunities()).isEqualTo(3);
    }
}
