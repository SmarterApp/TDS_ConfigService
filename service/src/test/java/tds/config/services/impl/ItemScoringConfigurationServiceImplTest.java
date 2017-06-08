package tds.config.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import tds.config.repositories.ItemScoringConfigurationRepository;
import tds.config.services.ItemScoringConfigurationService;
import tds.student.sql.data.ItemScoringConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemScoringConfigurationServiceImplTest {
    private ItemScoringConfigurationService itemScoringConfigurationService;

    @Mock
    private ItemScoringConfigurationRepository mockItemScoringConfigurationRepository;

    @Before
    public void setUp() {
        itemScoringConfigurationService = new ItemScoringConfigurationServiceImpl(mockItemScoringConfigurationRepository);
    }

    @Test
    public void shouldFindItemScoringConfiguration(){
        ItemScoringConfig config = new ItemScoringConfig();

        when(mockItemScoringConfigurationRepository.findItemScoringConfigs("SBAC_PT", "site", "server"))
            .thenReturn(Collections.singletonList(config));

        assertThat(itemScoringConfigurationService.findItemScoringConfigs("SBAC_PT", "site", "server")).containsExactly(config);

        verify(mockItemScoringConfigurationRepository).findItemScoringConfigs("SBAC_PT", "site", "server");
    }
}
