package tds.config.repositories.impl.mappers;

import org.springframework.jdbc.core.RowMapper;
import tds.config.TimeLimitConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Map a {@link TimeLimitConfiguration} from the database to a POJO.
 */
public class TimeLimitsRowMapper implements RowMapper<TimeLimitConfiguration> {
    @Override
    public TimeLimitConfiguration mapRow(ResultSet rs, int i) throws SQLException {
        return new TimeLimitConfiguration.Builder()
                .withClientName(rs.getString("clientName"))
                .withAssessmentId(rs.getString("assessmentId"))
                .withEnvironment(rs.getString("environment"))
                .withExamRestartWindowMinutes(rs.getInt("examRestartWindowMinutes"))
                .withExamDelayDays(rs.getInt("examDelayDays"))
                .withInterfaceTimeoutMinutes((Integer)rs.getObject("interfaceTimeoutMinutes")) // can be null in db
                .withRequestInterfaceTimeoutMinutes(rs.getInt("requestInterfaceTimeoutMinutes"))
                .withTaCheckinTimeMinutes((Integer)rs.getObject("taCheckinTimeMinutes")) // can be null in db
                .build();
    }
}
