package microapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IssueEmployeeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueEmployeeDTO.class);
        IssueEmployeeDTO issueEmployeeDTO1 = new IssueEmployeeDTO();
        issueEmployeeDTO1.setId(1L);
        IssueEmployeeDTO issueEmployeeDTO2 = new IssueEmployeeDTO();
        assertThat(issueEmployeeDTO1).isNotEqualTo(issueEmployeeDTO2);
        issueEmployeeDTO2.setId(issueEmployeeDTO1.getId());
        assertThat(issueEmployeeDTO1).isEqualTo(issueEmployeeDTO2);
        issueEmployeeDTO2.setId(2L);
        assertThat(issueEmployeeDTO1).isNotEqualTo(issueEmployeeDTO2);
        issueEmployeeDTO1.setId(null);
        assertThat(issueEmployeeDTO1).isNotEqualTo(issueEmployeeDTO2);
    }
}
