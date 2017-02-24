package tds.config;

import org.joda.time.Instant;
import org.joda.time.Minutes;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientSystemFlagTest {
    @Test
    public void shouldBuildAClientSystemFlag() {
        Instant utcNowMinusFiveMinutes = Instant.now().minus(Minutes.minutes(5).toStandardDuration());

        ClientSystemFlag clientSystemFlag = new ClientSystemFlag.Builder()
                .withClientName("SBAC_PT")
                .withIsPracticeTest(true)
                .withEnabled(true)
                .withDescription("Unit test description")
                .withAuditObject("Unit test audit object")
                .withDateChanged(utcNowMinusFiveMinutes)
                .withDatePublished(utcNowMinusFiveMinutes)
                .build();

        assertThat(clientSystemFlag.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientSystemFlag.getIsPracticeTest()).isTrue();
        assertThat(clientSystemFlag.isEnabled()).isTrue();
        assertThat(clientSystemFlag.getDescription()).isEqualTo("Unit test description");
        assertThat(clientSystemFlag.getAuditObject()).isEqualTo("Unit test audit object");
        assertThat(clientSystemFlag.getDateChanged()).isEqualTo(utcNowMinusFiveMinutes);
        assertThat(clientSystemFlag.getDatePublished()).isEqualTo(utcNowMinusFiveMinutes);
    }
}
