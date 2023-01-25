package microapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IssueEmployeeAssignmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueEmployeeAssignmentDTO.class);
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO1 = new IssueEmployeeAssignmentDTO();
        issueEmployeeAssignmentDTO1.setId(1L);
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO2 = new IssueEmployeeAssignmentDTO();
        assertThat(issueEmployeeAssignmentDTO1).isNotEqualTo(issueEmployeeAssignmentDTO2);
        issueEmployeeAssignmentDTO2.setId(issueEmployeeAssignmentDTO1.getId());
        assertThat(issueEmployeeAssignmentDTO1).isEqualTo(issueEmployeeAssignmentDTO2);
        issueEmployeeAssignmentDTO2.setId(2L);
        assertThat(issueEmployeeAssignmentDTO1).isNotEqualTo(issueEmployeeAssignmentDTO2);
        issueEmployeeAssignmentDTO1.setId(null);
        assertThat(issueEmployeeAssignmentDTO1).isNotEqualTo(issueEmployeeAssignmentDTO2);
    }
}
