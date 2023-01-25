package microapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IssueTagMapperTest {

    private IssueTagMapper issueTagMapper;

    @BeforeEach
    public void setUp() {
        issueTagMapper = new IssueTagMapperImpl();
    }
}
