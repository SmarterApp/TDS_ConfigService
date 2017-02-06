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

    /**
     * Finds the {@link tds.config.ClientSystemMessage} for the given client, message and context.  The {@link tds.config.ClientSystemMessage}
     * contains the parameterized message and message ID.
     *
     * @param clientName            The client name
     * @param messageKey            The message key found in the {@code configs.tds_coremessageobject} table in the {@code appkey} column
     * @param languageCode              The language code to translate the message into
     * @param clientDefaultLanguage The default language to fallback to if the specific {@code language} parameter is not available
     * @param context               The context for the message found in the {@code configs.tds_coremessageobject} table in the {@code context} column
     * @param subject               A subject code used to find a more specific message.  NULL will match on all
     * @param grade                 A grade level used to find a more specific message.  NULL will match on all
     * @return A {@link tds.config.ClientSystemMessage} if found, empty otherwise
     */
    Optional<ClientSystemMessage> findClientSystemMessage(String clientName, String messageKey, String languageCode, String clientDefaultLanguage, String context, String subject, String grade);

    /**
     * Finds the {@link tds.config.ClientLanguage} by the client name which contains the default language setting for this client.
     *
     * @param clientName The client name
     * @return {@link tds.config.ClientLanguage} if found, otherwise empty
     */
    Optional<ClientLanguage> findClientLanguage(String clientName);
}
