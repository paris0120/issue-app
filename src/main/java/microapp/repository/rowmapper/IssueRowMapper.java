package microapp.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.UUID;
import java.util.function.BiFunction;
import microapp.domain.Issue;
import microapp.domain.enumeration.IssuePriority;
import microapp.domain.enumeration.IssueStatus;
import microapp.domain.enumeration.IssueType;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Issue}, with proper type conversions.
 */
@Service
public class IssueRowMapper implements BiFunction<Row, String, Issue> {

    private final ColumnConverter converter;

    public IssueRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Issue} stored in the database.
     */
    @Override
    public Issue apply(Row row, String prefix) {
        Issue entity = new Issue();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUsername(converter.fromRow(row, prefix + "_username", String.class));
        entity.setIssueTitle(converter.fromRow(row, prefix + "_issue_title", String.class));
        entity.setIssueContent(converter.fromRow(row, prefix + "_issue_content", String.class));
        entity.setIssueType(converter.fromRow(row, prefix + "_issue_type", IssueType.class));
        entity.setIssueStatus(converter.fromRow(row, prefix + "_issue_status", IssueStatus.class));
        entity.setIssuePriority(converter.fromRow(row, prefix + "_issue_priority", IssuePriority.class));
        entity.setUuid(converter.fromRow(row, prefix + "_uuid", UUID.class));
        entity.setCreated(converter.fromRow(row, prefix + "_created", Instant.class));
        entity.setModified(converter.fromRow(row, prefix + "_modified", Instant.class));
        entity.setDeleted(converter.fromRow(row, prefix + "_deleted", Instant.class));
        return entity;
    }
}
