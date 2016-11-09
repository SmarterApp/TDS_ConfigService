package tds.config;

import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientSystemFlagUnitTests {
    @Test
    public void shouldBuildAClientSystemFlag() {
        Instant utcNow = Instant.now();

        ClientSystemFlag clientSystemFlag = new ClientSystemFlag.Builder()
                .withClientName("SBAC_PT")
                .withIsPracticeTest(true)
                .withEnabled(true)
                .withDescription("Unit test description")
                .withAuditObject("Unit test audit object")
                .withDateChanged(utcNow.minus(5, ChronoUnit.MINUTES))
                .withDatePublished(utcNow.plus(5, ChronoUnit.MINUTES))
                .build();

        assertThat(clientSystemFlag.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientSystemFlag.getIsPracticeTest()).isTrue();
        assertThat(clientSystemFlag.isEnabled()).isTrue();
        assertThat(clientSystemFlag.getDescription()).isEqualTo("Unit test description");
        assertThat(clientSystemFlag.getAuditObject()).isEqualTo("Unit test audit object");
        assertThat(clientSystemFlag.getDateChanged()).isEqualTo(utcNow.minus(5, ChronoUnit.MINUTES));
        assertThat(clientSystemFlag.getDatePublished()).isEqualTo(utcNow.plus(5, ChronoUnit.MINUTES));
    }
}
