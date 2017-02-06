package tds.config.repositories;

import java.util.List;
import java.util.Optional;

import tds.config.ClientLanguage;
import tds.config.ClientSystemFlag;
import tds.config.ClientSystemMessage;

/**
 * Data Access for interacting with the {@code configs} database.
 */
public interface ConfigRepository {
    /**
     * Get all the {@link tds.config.ClientSystemFlag} records from the {@code configs.client_systemflags} database for the
     * specified client name.
     * <p>
     * The implementation of this method should account for the The {@code session.externs} view.  The {@code JOIN}
     * to the {@code session.externs} view came from looking at the SQL contained in the
     * {@code CommonDLL.selectIsOnByAuditObject} method.
     * </p>
     * <p>
     * Because the {@link tds.config.ClientSystemFlag} values will rarely change, they are strong candidates for caching.
     * </p>
     *
     * @param clientName The client name for which the {@link tds.config.ClientSystemFlag} records should be fetched.
     * @return A collection of {@link tds.config.ClientSystemFlag} records for the specified client name.
     */
    List<ClientSystemFlag> findClientSystemFlags(String clientName);
}
