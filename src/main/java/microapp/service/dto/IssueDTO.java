package microapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;
import microapp.domain.enumeration.IssuePriority;
import microapp.domain.enumeration.IssueStatus;
import microapp.domain.enumeration.IssueType;

/**
 * A DTO for the {@link microapp.domain.Issue} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IssueDTO implements Serializable {

    private Long id;

    private String username;

    @NotNull(message = "must not be null")
    @Size(min = 2)
    private String issueTitle;

    @NotNull(message = "must not be null")
    @Size(min = 10)
    private String issueContent;

    @NotNull(message = "must not be null")
    private IssueType issueType;

    @NotNull(message = "must not be null")
    private IssueStatus issueStatus;

    @NotNull(message = "must not be null")
    private IssuePriority issuePriority;

    @NotNull(message = "must not be null")
    private UUID uuid;

    @NotNull(message = "must not be null")
    private Instant created;

    @NotNull(message = "must not be null")
    private Instant modified;

    private Instant deleted;

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

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getIssueContent() {
        return issueContent;
    }

    public void setIssueContent(String issueContent) {
        this.issueContent = issueContent;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public IssueStatus getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(IssueStatus issueStatus) {
        this.issueStatus = issueStatus;
    }

    public IssuePriority getIssuePriority() {
        return issuePriority;
    }

    public void setIssuePriority(IssuePriority issuePriority) {
        this.issuePriority = issuePriority;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Instant getDeleted() {
        return deleted;
    }

    public void setDeleted(Instant deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IssueDTO)) {
            return false;
        }

        IssueDTO issueDTO = (IssueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, issueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IssueDTO{" +
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
