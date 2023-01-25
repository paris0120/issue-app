package microapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IssueEmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueEmployee.class);
        IssueEmployee issueEmployee1 = new IssueEmployee();
        issueEmployee1.setId(1L);
        IssueEmployee issueEmployee2 = new IssueEmployee();
        issueEmployee2.setId(issueEmployee1.getId());
        assertThat(issueEmployee1).isEqualTo(issueEmployee2);
        issueEmployee2.setId(2L);
        assertThat(issueEmployee1).isNotEqualTo(issueEmployee2);
        issueEmployee1.setId(null);
        assertThat(issueEmployee1).isNotEqualTo(issueEmployee2);
    }
}
