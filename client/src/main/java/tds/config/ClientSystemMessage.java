package tds.config;

/**
 * Represents a record in the {@code configs.tds_coremessageobject} or {@code configs.client_messagetranslation} table.
 */
public class ClientSystemMessage {
    private int messageId;
    private String message;
    private String languageCode;

    public ClientSystemMessage(int messageId, String message, String languageCode) {
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
