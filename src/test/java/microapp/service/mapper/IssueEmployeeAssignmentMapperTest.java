package microapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IssueEmployeeAssignmentMapperTest {

    private IssueEmployeeAssignmentMapper issueEmployeeAssignmentMapper;

    @BeforeEach
    public void setUp() {
        issueEmployeeAssignmentMapper = new IssueEmployeeAssignmentMapperImpl();
    }
}
