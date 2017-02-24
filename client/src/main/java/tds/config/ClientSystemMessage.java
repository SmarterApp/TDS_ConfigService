package tds.config;

/**
 * Represents a record in the {@code configs.tds_coremessageobject} or {@code configs.client_messagetranslation} table.
 */
public class ClientSystemMessage {
    private final int messageId;
    private final String message;
    private final String languageCode;

    public ClientSystemMessage(final int messageId, final String message, final String languageCode) {
        this.messageId = messageId;
        this.message = message;
        this.languageCode = languageCode;
    }

    /**
     * @return The messageId for this {@link tds.config.ClientSystemMessage}
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * @return The message template for this {@link tds.config.ClientSystemMessage}
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return The language code of the message for this {@link tds.config.ClientSystemMessage}
     */
    public String getLanguageCode() {
        return languageCode;
    }
}
