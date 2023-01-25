package microapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IssueEmployeeAssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueEmployeeAssignment.class);
        IssueEmployeeAssignment issueEmployeeAssignment1 = new IssueEmployeeAssignment();
        issueEmployeeAssignment1.setId(1L);
        IssueEmployeeAssignment issueEmployeeAssignment2 = new IssueEmployeeAssignment();
        issueEmployeeAssignment2.setId(issueEmployeeAssignment1.getId());
        assertThat(issueEmployeeAssignment1).isEqualTo(issueEmployeeAssignment2);
        issueEmployeeAssignment2.setId(2L);
        assertThat(issueEmployeeAssignment1).isNotEqualTo(issueEmployeeAssignment2);
        issueEmployeeAssignment1.setId(null);
        assertThat(issueEmployeeAssignment1).isNotEqualTo(issueEmployeeAssignment2);
    }
}
