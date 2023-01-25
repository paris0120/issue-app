package microapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IssueTagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueTagDTO.class);
        IssueTagDTO issueTagDTO1 = new IssueTagDTO();
        issueTagDTO1.setId(1L);
        IssueTagDTO issueTagDTO2 = new IssueTagDTO();
        assertThat(issueTagDTO1).isNotEqualTo(issueTagDTO2);
        issueTagDTO2.setId(issueTagDTO1.getId());
        assertThat(issueTagDTO1).isEqualTo(issueTagDTO2);
        issueTagDTO2.setId(2L);
        assertThat(issueTagDTO1).isNotEqualTo(issueTagDTO2);
        issueTagDTO1.setId(null);
        assertThat(issueTagDTO1).isNotEqualTo(issueTagDTO2);
    }
}
