package microapp.domain.enumeration;

/**
 * The IssuePriority enumeration.
 */
public enum IssuePriority {
    HIGHEST("Highest"),
    HIGHER("Higher"),
    HIGH("High"),
    NORMAL("Normal"),
    LOW("Low"),
    LOWER("Lower"),
    LOWERST("Lowest");

    private final String value;

    IssuePriority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
