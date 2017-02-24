package tds.config.services;

/**
 * Provide an interface for interacting with the {@code configs} database and providing translated messages.
 */
public interface SystemMessageService {
    /**
     * Get the message string for this client that is translated for the language specified.
     *
     * @param clientName   The client name
     * @param messageKey   The message key found in the {@code configs.tds_coremessageobject} table in the {@code appkey} column
     * @param languageCode The language code to translate the message into
     * @param context      The context for the message found in the {@code configs.tds_coremessageobject} table in the {@code context} column
     * @param subject      A subject code used to find a more specific message.  NULL will match on all
     * @param grade        A grade level used to find a more specific message.  NULL will match on all
     * @return The message string with placeholders included
     */
    String getSystemMessage(final String clientName, final String messageKey, final String languageCode, final String context, final String subject, final String grade);
}
