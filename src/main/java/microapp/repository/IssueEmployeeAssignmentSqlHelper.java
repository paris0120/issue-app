package microapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class IssueEmployeeAssignmentSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("issue_id", table, columnPrefix + "_issue_id"));
        columns.add(Column.aliased("issue_uuid", table, columnPrefix + "_issue_uuid"));
        columns.add(Column.aliased("username", table, columnPrefix + "_username"));
        columns.add(Column.aliased("issue_assignment_title", table, columnPrefix + "_issue_assignment_title"));

        return columns;
    }
}
