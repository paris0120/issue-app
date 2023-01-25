package microapp.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.validation.constraints.*;
import microapp.domain.enumeration.IssuePriority;
import microapp.domain.enumeration.IssueStatus;
import microapp.domain.enumeration.IssueType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Issue.
 */
@Table("issue")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Issue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("username")
    private String username;

    @NotNull(message = "must not be null")
    @Size(min = 2)
    @Column("issue_title")
    private String issueTitle;

    @NotNull(message = "must not be null")
    @Size(min = 10)
    @Column("issue_content")
    private String issueContent;

    @NotNull(message = "must not be null")
    @Column("issue_type")
    private IssueType issueType;

    @NotNull(message = "must not be null")
    @Column("issue_status")
    private IssueStatus issueStatus;

    @NotNull(message = "must not be null")
    @Column("issue_priority")
    private IssuePriority issuePriority;

    @NotNull(message = "must not be null")
    @Column("uuid")
    private UUID uuid;

    @NotNull(message = "must not be null")
    @Column("created")
    private Instant created;

    @NotNull(message = "must not be null")
    @Column("modified")
    private Instant modified;

    @Column("deleted")
    private Instant deleted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Issue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public Issue username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIssueTitle() {
        return this.issueTitle;
    }

    public Issue issueTitle(String issueTitle) {
        this.setIssueTitle(issueTitle);
        return this;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getIssueContent() {
        return this.issueContent;
    }

    public Issue issueContent(String issueContent) {
        this.setIssueContent(issueContent);
        return this;
    }

    public void setIssueContent(String issueContent) {
        this.issueContent = issueContent;
    }

    public IssueType getIssueType() {
        return this.issueType;
    }

    public Issue issueType(IssueType issueType) {
        this.setIssueType(issueType);
        return this;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public IssueStatus getIssueStatus() {
        return this.issueStatus;
    }

    public Issue issueStatus(IssueStatus issueStatus) {
        this.setIssueStatus(issueStatus);
        return this;
    }

    public void setIssueStatus(IssueStatus issueStatus) {
        this.issueStatus = issueStatus;
    }

    public IssuePriority getIssuePriority() {
        return this.issuePriority;
    }

    public Issue issuePriority(IssuePriority issuePriority) {
        this.setIssuePriority(issuePriority);
        return this;
    }

    public void setIssuePriority(IssuePriority issuePriority) {
        this.issuePriority = issuePriority;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Issue uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Instant getCreated() {
        return this.created;
    }

    public Issue created(Instant created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return this.modified;
    }

    public Issue modified(Instant modified) {
        this.setModified(modified);
        return this;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Instant getDeleted() {
        return this.deleted;
    }

    public Issue deleted(Instant deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Instant deleted) {
        this.deleted = deleted;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Issue)) {
            return false;
        }
        return id != null && id.equals(((Issue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Issue{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", issueTitle='" + getIssueTitle() + "'" +
            ", issueContent='" + getIssueContent() + "'" +
            ", issueType='" + getIssueType() + "'" +
            ", issueStatus='" + getIssueStatus() + "'" +
            ", issuePriority='" + getIssuePriority() + "'" +
            ", uuid='" + getUuid() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
