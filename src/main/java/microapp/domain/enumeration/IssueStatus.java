package microapp.domain.enumeration;

/**
 * The IssueStatus enumeration.
 */
public enum IssueStatus {
    OPEN("Open"),
    VERIFIED("Verified"),
    WAITING_FOR_RESPONSE("Waiting for Customer Response"),
    IN_PROGRESS("In Progress"),
    REOPENED("Reopened"),
    CANNOT_REPRODUCE("Cannot Reproduce"),
    DUPLICATE("Duplicate"),
    SOLVED("Solved"),
    WONT_IMPLEMENT("Won&#39;t Implement"),
    CANCELLED("Cancelled"),
    CLOSED("Closed");

    private final String value;

    IssueStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}