package microapp.domain.enumeration;

/**
 * The IssueType enumeration.
 */
public enum IssueType {
    BUG("Bug"),
    FEATURE("Feature");

    private final String value;

    IssueType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
