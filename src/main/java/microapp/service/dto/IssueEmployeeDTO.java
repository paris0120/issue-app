package microapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link microapp.domain.IssueEmployee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IssueEmployeeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String username;

    @NotNull(message = "must not be null")
    private String issueAssignmentTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIssueAssignmentTitle() {
        return issueAssignmentTitle;
    }

    public void setIssueAssignmentTitle(String issueAssignmentTitle) {
        this.issueAssignmentTitle = issueAssignmentTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IssueEmployeeDTO)) {
            return false;
        }

        IssueEmployeeDTO issueEmployeeDTO = (IssueEmployeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, issueEmployeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IssueEmployeeDTO{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", issueAssignmentTitle='" + getIssueAssignmentTitle() + "'" +
            "}";
    }
}
