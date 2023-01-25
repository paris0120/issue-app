package microapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class IssueSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("username", table, columnPrefix + "_username"));
        columns.add(Column.aliased("issue_title", table, columnPrefix + "_issue_title"));
        columns.add(Column.aliased("issue_content", table, columnPrefix + "_issue_content"));
        columns.add(Column.aliased("issue_type", table, columnPrefix + "_issue_type"));
        columns.add(Column.aliased("issue_status", table, columnPrefix + "_issue_status"));
        columns.add(Column.aliased("issue_priority", table, columnPrefix + "_issue_priority"));
        columns.add(Column.aliased("uuid", table, columnPrefix + "_uuid"));
        columns.add(Column.aliased("created", table, columnPrefix + "_created"));
        columns.add(Column.aliased("modified", table, columnPrefix + "_modified"));
        columns.add(Column.aliased("deleted", table, columnPrefix + "_deleted"));

        return columns;
    }
}
