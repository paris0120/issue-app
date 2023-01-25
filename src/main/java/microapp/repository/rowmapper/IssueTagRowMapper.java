package microapp.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.UUID;
import java.util.function.BiFunction;
import microapp.domain.IssueTag;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link IssueTag}, with proper type conversions.
 */
@Service
public class IssueTagRowMapper implements BiFunction<Row, String, IssueTag> {

    private final ColumnConverter converter;

    public IssueTagRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link IssueTag} stored in the database.
     */
    @Override
    public IssueTag apply(Row row, String prefix) {
        IssueTag entity = new IssueTag();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setIssueId(converter.fromRow(row, prefix + "_issue_id", Long.class));
        entity.setIssueUuid(converter.fromRow(row, prefix + "_issue_uuid", UUID.class));
        entity.setTag(converter.fromRow(row, prefix + "_tag", String.class));
        return entity;
    }
}
