/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.config.repositories.impl.mappers;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import tds.config.TimeLimitConfiguration;

/**
 * Map a {@link TimeLimitConfiguration} from the database to a POJO.
 */
public class TimeLimitsRowMapper implements RowMapper<TimeLimitConfiguration> {
    @Override
    public TimeLimitConfiguration mapRow(final ResultSet rs, final int i) throws SQLException {
        return new TimeLimitConfiguration.Builder()
            .withClientName(rs.getString("clientName"))
            .withAssessmentId(rs.getString("assessmentId"))
            .withEnvironment(rs.getString("environment"))
            .withExamRestartWindowMinutes(rs.getInt("examRestartWindowMinutes"))
            .withExamDelayDays(rs.getInt("examDelayDays"))
            .withInterfaceTimeoutMinutes((Integer) rs.getObject("interfaceTimeoutMinutes")) // can be null in db
            .withRequestInterfaceTimeoutMinutes(rs.getInt("requestInterfaceTimeoutMinutes"))
            .withTaCheckinTimeMinutes((Integer) rs.getObject("taCheckinTimeMinutes")) // can be null in db
            .build();
    }
}
