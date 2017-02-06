package tds.config;

/**
 * Represents a record in the {@code configs.tds_coremessageobject} or {@code configs.client_messagetranslation} table.
 */
public class ClientSystemMessage {
    private int messageId;
    private String message;
    private String language;

    public ClientSystemMessage(int messageId, String message, String language) {
        this.messageId = messageId;
        this.message = message;
        this.language = language;
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
     * @return The language of the message for this {@link tds.config.ClientSystemMessage}
     */
    public String getLanguage() {
        return language;
    }
}
