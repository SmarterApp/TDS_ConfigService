package tds.config.web.endpoints;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import tds.config.services.ItemScoringConfigurationService;
import tds.student.sql.data.ItemScoringConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemScoringConfigurationControllerTest {
    @Mock
    private ItemScoringConfigurationService mockItemScoringConfigurationService;

    private ItemScoringConfigurationController controller;

    @Before
    public void setUp() {
        controller = new ItemScoringConfigurationController(mockItemScoringConfigurationService);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void itShouldReturnConfigs() {
        ItemScoringConfig config = new ItemScoringConfig();

        when(mockItemScoringConfigurationService.findItemScoringConfigs("SBAC", "site","server")).thenReturn(Collections.singletonList(config));

        ResponseEntity<List<ItemScoringConfig>> response = controller.findScoringConfigurations("SBAC", "site", "server");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(config);
    }

    @Test
    public void itShouldReturnNoContentOnEmptyList() {
        when(mockItemScoringConfigurationService.findItemScoringConfigs("SBAC", "site","server")).thenReturn(Collections.emptyList());

        ResponseEntity<List<ItemScoringConfig>> response = controller.findScoringConfigurations("SBAC", "site", "server");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isEmpty();
    }
}