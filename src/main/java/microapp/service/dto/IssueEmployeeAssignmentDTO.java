package microapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link microapp.domain.IssueEmployeeAssignment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IssueEmployeeAssignmentDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Long issueId;

    @NotNull(message = "must not be null")
    private UUID issueUuid;

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

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public UUID getIssueUuid() {
        return issueUuid;
    }

    public void setIssueUuid(UUID issueUuid) {
        this.issueUuid = issueUuid;
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
        if (!(o instanceof IssueEmployeeAssignmentDTO)) {
            return false;
        }

        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = (IssueEmployeeAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, issueEmployeeAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IssueEmployeeAssignmentDTO{" +
            "id=" + getId() +
            ", issueId=" + getIssueId() +
            ", issueUuid='" + getIssueUuid() + "'" +
            ", username='" + getUsername() + "'" +
            ", issueAssignmentTitle='" + getIssueAssignmentTitle() + "'" +
            "}";
    }
}
