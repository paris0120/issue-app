package microapp.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A IssueTag.
 */
@Table("issue_tag")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IssueTag implements Serializable {

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
    @Column("tag")
    private String tag;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IssueTag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIssueId() {
        return this.issueId;
    }

    public IssueTag issueId(Long issueId) {
        this.setIssueId(issueId);
        return this;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public UUID getIssueUuid() {
        return this.issueUuid;
    }

    public IssueTag issueUuid(UUID issueUuid) {
        this.setIssueUuid(issueUuid);
        return this;
    }

    public void setIssueUuid(UUID issueUuid) {
        this.issueUuid = issueUuid;
    }

    public String getTag() {
        return this.tag;
    }

    public IssueTag tag(String tag) {
        this.setTag(tag);
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IssueTag)) {
            return false;
        }
        return id != null && id.equals(((IssueTag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IssueTag{" +
            "id=" + getId() +
            ", issueId=" + getIssueId() +
            ", issueUuid='" + getIssueUuid() + "'" +
            ", tag='" + getTag() + "'" +
            "}";
    }
}
