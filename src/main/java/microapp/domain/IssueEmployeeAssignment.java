package microapp.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A IssueEmployeeAssignment.
 */
@Table("issue_employee_assignment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IssueEmployeeAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("issue_id")
    private Long issueId;

    @NotNull(message = "must not be null")
    @Column("issue_uuid")
    private UUID issueUuid;

    @NotNull(message = "must not be null")
    @Column("username")
    private String username;

    @NotNull(message = "must not be null")
    @Column("issue_assignment_title")
    private String issueAssignmentTitle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IssueEmployeeAssignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIssueId() {
        return this.issueId;
    }

    public IssueEmployeeAssignment issueId(Long issueId) {
        this.setIssueId(issueId);
        return this;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public UUID getIssueUuid() {
        return this.issueUuid;
    }

    public IssueEmployeeAssignment issueUuid(UUID issueUuid) {
        this.setIssueUuid(issueUuid);
        return this;
    }

    public void setIssueUuid(UUID issueUuid) {
        this.issueUuid = issueUuid;
    }

    public String getUsername() {
        return this.username;
    }

    public IssueEmployeeAssignment username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIssueAssignmentTitle() {
        return this.issueAssignmentTitle;
    }

    public IssueEmployeeAssignment issueAssignmentTitle(String issueAssignmentTitle) {
        this.setIssueAssignmentTitle(issueAssignmentTitle);
        return this;
    }

    public void setIssueAssignmentTitle(String issueAssignmentTitle) {
        this.issueAssignmentTitle = issueAssignmentTitle;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IssueEmployeeAssignment)) {
            return false;
        }
        return id != null && id.equals(((IssueEmployeeAssignment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IssueEmployeeAssignment{" +
            "id=" + getId() +
            ", issueId=" + getIssueId() +
            ", issueUuid='" + getIssueUuid() + "'" +
            ", username='" + getUsername() + "'" +
            ", issueAssignmentTitle='" + getIssueAssignmentTitle() + "'" +
            "}";
    }
}
