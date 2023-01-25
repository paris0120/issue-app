package microapp.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String CUSTOMERSERVICE = "ROLE_ISSUE_CUSTOMER_SERVICE";

    public static final String MANAGER = "ROLE_ISSUE_MANAGER";

    public static final String TECH = "ROLE_ISSUE_TECHNICIAN";

    private AuthoritiesConstants() {}
}
