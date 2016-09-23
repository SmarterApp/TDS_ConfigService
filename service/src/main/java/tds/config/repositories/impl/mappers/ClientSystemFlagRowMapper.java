package tds.config.repositories.impl.mappers;

import org.springframework.jdbc.core.RowMapper;
import tds.config.ClientSystemFlag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneOffset;

/**
 * Map a {@link ClientSystemFlag} from the database to a POJO.
 */
public class ClientSystemFlagRowMapper implements RowMapper<ClientSystemFlag> {
    @Override
    public ClientSystemFlag mapRow(ResultSet rs, int i) throws SQLException {
        Instant dateChanged = rs.getTimestamp("dateChanged") == null
                ? null
                : rs.getTimestamp("dateChanged").toLocalDateTime().toInstant(ZoneOffset.UTC);

        Instant datePublished = rs.getTimestamp("datePublished") == null
                ? null
                : rs.getTimestamp("datePublished").toLocalDateTime().toInstant(ZoneOffset.UTC);

        return new ClientSystemFlag.Builder()
                .withAuditObject(rs.getString("auditObject"))
                .withClientName(rs.getString("clientName"))
                .withIsPracticeTest(rs.getBoolean("isPracticeTest"))
                .withIsOn(rs.getInt("isOn") == 1)
                .withDescription(rs.getString("description"))
                .withDateChanged(dateChanged)
                .withDatePublished(datePublished)
                .build();
    }
}
