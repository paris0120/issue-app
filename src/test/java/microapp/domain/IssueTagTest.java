package microapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import microapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IssueTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueTag.class);
        IssueTag issueTag1 = new IssueTag();
        issueTag1.setId(1L);
        IssueTag issueTag2 = new IssueTag();
        issueTag2.setId(issueTag1.getId());
        assertThat(issueTag1).isEqualTo(issueTag2);
        issueTag2.setId(2L);
        assertThat(issueTag1).isNotEqualTo(issueTag2);
        issueTag1.setId(null);
        assertThat(issueTag1).isNotEqualTo(issueTag2);
    }
}
