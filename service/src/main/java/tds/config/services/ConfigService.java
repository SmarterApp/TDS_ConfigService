package tds.config.services;

import java.util.Optional;

import tds.config.ClientSystemFlag;

/**
 * Provide an interface for interacting with the {@code configs} database and providing configuration information.
 */
public interface ConfigService {
    /**
     * Get the {@link ClientSystemFlag} for the specified client and audit name.
     *
     * @param clientName The name of the client (typically SBAC or SBAC_PT)
     * @param type       The type of the desired {@link ClientSystemFlag}
     * @return The {@link Optional<ClientSystemFlag>} that matches the client and audit name.
     */
    Optional<ClientSystemFlag> findClientSystemFlag(String clientName, String type);

    /**
     * Get the message string for this client that is translated for the language specified.
     *
     * @param clientName The client name
     * @param messageKey The message key found in the {@code configs.tds_coremessageobject} table in the {@code appkey} column
     * @param languageCode   The language code to translate the message into
     * @param context    The context for the message found in the {@code configs.tds_coremessageobject} table in the {@code context} column
     * @param subject    A subject code used to find a more specific message.  NULL will match on all
     * @param grade      A grade level used to find a more specific message.  NULL will match on all
     * @return The message string with placeholders included
     */
    String getSystemMessage(String clientName, String messageKey, String languageCode, String context, String subject, String grade);
}
