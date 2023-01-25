package microapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IssueEmployeeMapperTest {

    private IssueEmployeeMapper issueEmployeeMapper;

    @BeforeEach
    public void setUp() {
        issueEmployeeMapper = new IssueEmployeeMapperImpl();
    }
}
