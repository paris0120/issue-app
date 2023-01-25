package microapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link microapp.domain.IssueTag} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IssueTagDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Long issueId;

    @NotNull(message = "must not be null")
    private UUID issueUuid;

    @NotNull(message = "must not be null")
    private String tag;

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IssueTagDTO)) {
            return false;
        }

        IssueTagDTO issueTagDTO = (IssueTagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, issueTagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IssueTagDTO{" +
            "id=" + getId() +
            ", issueId=" + getIssueId() +
            ", issueUuid='" + getIssueUuid() + "'" +
            ", tag='" + getTag() + "'" +
            "}";
    }
}
