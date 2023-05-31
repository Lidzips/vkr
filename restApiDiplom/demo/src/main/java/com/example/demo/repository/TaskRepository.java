package com.example.demo.repository;

import com.example.demo.entity.Task;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;

@Repository
public class TaskRepository implements IRestRepository<Task> {
    protected final JdbcOperations jdbcOperations;

    private static String selectQuery = "SELECT \"id\", \"complexity\", \"task_text\", \"file\", \"prompt\", \"topic\", \"out1\", \"out2\", \"out3\"" +
            "FROM \"task\" " +
            "ORDER BY \"id\"";

    private static String selectByIdQuery = "SELECT  \"id\", \"complexity\", \"task_text\", \"file\", \"prompt\", \"topic\", \"out1\", \"out2\", \"out3\"" +
            "FROM \"task\" " +
            "WHERE \"id\" = ?";

    private static String selectTaskTextQuery = "SELECT  \"task_text\" " +
            "FROM \"task\" " +
            "WHERE \"id\" = ?";

    private static String selectFileQuery = "SELECT  \"file\" " +
            "FROM \"task\" " +
            "WHERE \"id\" = ?";

    private static String insertQuery = "INSERT INTO \"task\"(\"complexity\", \"task_text\", \"file\", \"prompt\", \"topic\", \"out1\", \"out2\", \"out3\")" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
            "RETURNING \"id\", \"complexity\", \"task_text\", \"file\", \"prompt\", \"topic\", \"out1\", \"out2\", \"out3\"";

    private static String updateQuery = "UPDATE \"task\" " +
            "SET \"complexity\" = ?, \"task_text\" = ?, \"file\" = ?, \"prompt\" = ?, \"topic\" = ?, \"out1\" = ?, \"out2\" = ?, \"out3\" = ? " +
            "WHERE \"id\" = ? " +
            "RETURNING \"id\", \"complexity\", \"task_text\", \"file\", \"prompt\", \"topic\", \"out1\", \"out2\", \"out3\"";

    private static String deleteQuery = "DELETE FROM \"task\" " +
            "WHERE \"id\" = ? " +
            "RETURNING \"id\", \"complexity\", \"task_text\", \"file\", \"prompt\", \"topic\", \"out1\", \"out2\", \"out3\"";

    public TaskRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Task[] select() {
        ArrayList<Task> values = new ArrayList<Task>();
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectQuery);
        while (rowSet.next()) {
            values.add(new Task(
                    rowSet.getInt(1),
                    rowSet.getInt(2),
                    rowSet.getString(3),
                    rowSet.getString(4),
                    rowSet.getString(5),
                    rowSet.getString(6),
                    rowSet.getString(7),
                    rowSet.getString(8),
                    rowSet.getString(9)
            ));
        }
        Task[] result = new Task[values.size()];
        result = values.toArray(result);
        return result;
    }

    @Override
    public Task select(Integer id) {
        Object[] params = new Object[] { id };
        int[] types = new int[] { Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectByIdQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new Task(
                rowSet.getInt(1),
                rowSet.getInt(2),
                rowSet.getString(3),
                rowSet.getString(4),
                rowSet.getString(5),
                rowSet.getString(6),
                rowSet.getString(7),
                rowSet.getString(8),
                rowSet.getString(9)
        );
    }

    public String selectTaskText(Integer id) {
        Object[] params = new Object[] { id };
        int[] types = new int[] { Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectTaskTextQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return rowSet.getString(1);
    }

    public String selectFile(Integer id) {
        Object[] params = new Object[] { id };
        int[] types = new int[] { Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectFileQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return rowSet.getString(1);
    }

    @Override
    public Task insert(Task entity) {
        Object[] params = new Object[] { entity.getComplexity(), entity.getTaskText(), entity.getFile(), entity.getPrompt(), entity.getTopic(), entity.getOut1(), entity.getOut2(), entity.getOut3() };
        int[] types = new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,};
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(insertQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new Task(
                rowSet.getInt(1),
                rowSet.getInt(2),
                rowSet.getString(3),
                rowSet.getString(4),
                rowSet.getString(5),
                rowSet.getString(6),
                rowSet.getString(7),
                rowSet.getString(8),
                rowSet.getString(9)
        );
    }

    @Override
    public Task update(Integer id, Task entity) {
        Object[] params = new Object[] { entity.getComplexity(), entity.getTaskText(), entity.getFile(), entity.getPrompt(), entity.getTopic(), entity.getOut1(), entity.getOut2(), entity.getOut3(), id };
        int[] types = new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(updateQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new Task(
                rowSet.getInt(1),
                rowSet.getInt(2),
                rowSet.getString(3),
                rowSet.getString(4),
                rowSet.getString(5),
                rowSet.getString(6),
                rowSet.getString(7),
                rowSet.getString(8),
                rowSet.getString(9)
        );
    }

    @Override
    public Task delete(Integer id) {
        Object[] params = new Object[] { id };
        int[] types = new int[] {Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(deleteQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new Task(
                rowSet.getInt(1),
                rowSet.getInt(2),
                rowSet.getString(3),
                rowSet.getString(4),
                rowSet.getString(5),
                rowSet.getString(6),
                rowSet.getString(7),
                rowSet.getString(8),
                rowSet.getString(9)
        );
    }
}
