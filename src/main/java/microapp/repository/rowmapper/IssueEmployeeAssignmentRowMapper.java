package microapp.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import microapp.domain.IssueEmployeeAssignment;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IssueEmployeeAssignment}, with proper type conversions.
 */
@Service
public class IssueEmployeeAssignmentRowMapper implements BiFunction<Row, String, IssueEmployeeAssignment> {

    private final ColumnConverter converter;

    public IssueEmployeeAssignmentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IssueEmployeeAssignment} stored in the database.
     */
    @Override
    public IssueEmployeeAssignment apply(Row row, String prefix) {
        IssueEmployeeAssignment entity = new IssueEmployeeAssignment();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIssueId(converter.fromRow(row, prefix + "_issue_id", Long.class));
        entity.setIssueUuid(converter.fromRow(row, prefix + "_issue_uuid", UUID.class));
        entity.setUsername(converter.fromRow(row, prefix + "_username", String.class));
        entity.setIssueAssignmentTitle(converter.fromRow(row, prefix + "_issue_assignment_title", String.class));
        return entity;
    }
}
