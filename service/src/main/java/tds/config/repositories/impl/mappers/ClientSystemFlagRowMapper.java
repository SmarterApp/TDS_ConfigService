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

import tds.config.ClientSystemFlag;

import static tds.common.data.mapping.ResultSetMapperUtility.mapTimestampToJodaInstant;


/**
 * Map a {@link ClientSystemFlag} from the database to a POJO.
 */
public class ClientSystemFlagRowMapper implements RowMapper<ClientSystemFlag> {
    @Override
    public ClientSystemFlag mapRow(final ResultSet rs, final int i) throws SQLException {
        return new ClientSystemFlag.Builder()
            .withAuditObject(rs.getString("auditObject"))
            .withClientName(rs.getString("clientName"))
            .withIsPracticeTest(rs.getBoolean("isPracticeTest"))
            .withEnabled(rs.getInt("isOn") == 1)
            .withDescription(rs.getString("description"))
            .withDateChanged(mapTimestampToJodaInstant(rs, "dateChanged"))
            .withDatePublished(mapTimestampToJodaInstant(rs, "datePublished"))
            .build();
    }
}
