package microapp.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import microapp.domain.IssueEmployee;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IssueEmployee}, with proper type conversions.
 */
@Service
public class IssueEmployeeRowMapper implements BiFunction<Row, String, IssueEmployee> {

    private final ColumnConverter converter;

    public IssueEmployeeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IssueEmployee} stored in the database.
     */
    @Override
    public IssueEmployee apply(Row row, String prefix) {
        IssueEmployee entity = new IssueEmployee();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUsername(converter.fromRow(row, prefix + "_username", String.class));
        entity.setIssueAssignmentTitle(converter.fromRow(row, prefix + "_issue_assignment_title", String.class));
        return entity;
    }
}
