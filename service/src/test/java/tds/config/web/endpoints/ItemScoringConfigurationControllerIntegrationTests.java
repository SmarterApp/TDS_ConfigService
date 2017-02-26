package tds.config.web.endpoints;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;
import tds.config.services.ItemScoringConfigurationService;
import tds.student.sql.data.ItemScoringConfig;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemScoringConfigurationController.class)
@Import({ExceptionAdvice.class, SecurityConfiguration.class})
public class ItemScoringConfigurationControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private ItemScoringConfigurationService mockItemScoringConfigurationService;

    @Test
    public void shouldReturnItemScoringConfig() throws Exception {
        ItemScoringConfig config = new ItemScoringConfig();
        config.setServerUrl("http://localhost:8080");
        config.setPriority(1);
        config.setEnabled(true);
        config.setContext("Contexting");
        config.setItemType("Box");

        when(mockItemScoringConfigurationService.findItemScoringConfigs("SBAC", "site","server")).thenReturn(Collections.singletonList(config));

        URI requestUri = UriComponentsBuilder.fromUriString("/config/SBAC/scoring?site=site&server=server")
            .build()
            .toUri();


        http.perform(get(requestUri)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].ServerUrl", is("http://localhost:8080")))
            .andExpect(jsonPath("[0].Priority", is(1)))
            .andExpect(jsonPath("[0].Enabled", is(true)))
            .andExpect(jsonPath("[0].Context", is("Contexting")))
            .andExpect(jsonPath("[0].ItemType", is("Box")));
    }
}
