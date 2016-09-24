package tds.config.repositories.impl.mappers;

import org.springframework.jdbc.core.RowMapper;
import tds.config.ClientTestProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Map a {@link ClientTestProperty} from the database to a POJO.
 */
public class ClientTestPropertyRowMapper implements RowMapper<ClientTestProperty> {
    @Override
    public ClientTestProperty mapRow(ResultSet rs, int i) throws SQLException {
        return new ClientTestProperty.Builder()
                .withClientName(rs.getString("clientName"))
                .withAssessmentId(rs.getString("assessmentId"))
                .withMaxOpportunities((Integer)rs.getObject("maxOpportunities"))
                .withPrefetch(rs.getInt("prefetch"))
                .withIsSelectable(rs.getBoolean("isSelectable"))
                .withLabel(rs.getString("label"))
                .withSubjectName(rs.getString("subjectName"))
                .withInitialAbilityBySubject(rs.getBoolean("initialAbilityBySubject"))
                .withAccommodationFamily(rs.getString("accommodationFamily"))
                .withSortOrder((Integer)rs.getObject("sortOrder"))
                .withRtsFormField(rs.getString("rtsFormField"))
                .withRequireRtsWindow(rs.getBoolean("requireRtsWindow"))
                .withTideId(rs.getString("tideId"))
                .withRtsModeField(rs.getString("rtsModeField"))
                .withRequireRtsMode(rs.getBoolean("requireRtsMode"))
                .withRequireRtsModeWindow(rs.getBoolean("requireRtsModeWindow"))
                .withDeleteUnansweredItems(rs.getBoolean("deleteUnansweredItems"))
                .withAbilitySlope(rs.getDouble("abilitySlope"))
                .withAbilityIntercept(rs.getDouble("abilityIntercept"))
                .withValidateCompleteness(rs.getBoolean("validateCompleteness"))
                .withGradeText(rs.getString("gradeText"))
                .build();
    }
}
