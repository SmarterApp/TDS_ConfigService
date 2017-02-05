package tds.config;

/**
 Represents a record in the {@code configs.client}  table.
 */
public class ClientLanguage {
    private String clientName;
    private String defaultLanguage;
    private boolean internationalize;

    public ClientLanguage(String clientName, String defaultLanguage, boolean internationalize) {
        this.clientName = clientName;
        this.defaultLanguage = defaultLanguage;
        this.internationalize = internationalize;
    }

    /**
     * @return The client for this {@link ClientLanguage}
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @return The default language for this client
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * @return Determines if language will fallback to the default language
     */
    public boolean isInternationalize() {
        return internationalize;
    }
}
