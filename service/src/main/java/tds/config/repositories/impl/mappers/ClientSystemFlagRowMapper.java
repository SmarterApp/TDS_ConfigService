package tds.config.repositories.impl.mappers;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import tds.config.ClientSystemFlag;

import static tds.common.data.mapping.ResultSetMapperUtility.mapTimeStampToInstant;

/**
 * Map a {@link ClientSystemFlag} from the database to a POJO.
 */
public class ClientSystemFlagRowMapper implements RowMapper<ClientSystemFlag> {
    @Override
    public ClientSystemFlag mapRow(ResultSet rs, int i) throws SQLException {
        return new ClientSystemFlag.Builder()
                .withAuditObject(rs.getString("auditObject"))
                .withClientName(rs.getString("clientName"))
                .withIsPracticeTest(rs.getBoolean("isPracticeTest"))
                .withIsOn(rs.getInt("isOn") == 1)
                .withDescription(rs.getString("description"))
                .withDateChanged(mapTimeStampToInstant(rs, "dateChanged"))
                .withDatePublished(mapTimeStampToInstant(rs, "datePublished"))
                .build();
    }
}
