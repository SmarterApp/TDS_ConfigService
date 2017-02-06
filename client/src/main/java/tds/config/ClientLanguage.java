package tds.config;

/**
 Represents a record in the {@code configs.client}  table.
 */
public class ClientLanguage {
    private String clientName;
    private String defaultLanguageCode;
    private boolean internationalize;

    public ClientLanguage(String clientName, String defaultLanguageCode, boolean internationalize) {
        this.clientName = clientName;
        this.defaultLanguageCode = defaultLanguageCode;
        this.internationalize = internationalize;
    }

    /**
     * @return The client for this {@link ClientLanguage}
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @return The default language code for this client
     */
    public String getDefaultLanguageCode() {
        return defaultLanguageCode;
    }

    /**
     * @return Determines if language will fallback to the default language
     */
    public boolean isInternationalize() {
        return internationalize;
    }
}
