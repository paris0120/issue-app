package microapp.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A IssueEmployee.
 */
@Table("issue_employee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IssueEmployee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

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

    public IssueEmployee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public IssueEmployee username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIssueAssignmentTitle() {
        return this.issueAssignmentTitle;
    }

    public IssueEmployee issueAssignmentTitle(String issueAssignmentTitle) {
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
        if (!(o instanceof IssueEmployee)) {
            return false;
        }
        return id != null && id.equals(((IssueEmployee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IssueEmployee{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", issueAssignmentTitle='" + getIssueAssignmentTitle() + "'" +
            "}";
    }
}
