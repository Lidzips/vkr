package com.example.demo.repository;

import com.example.demo.entity.Progress;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;

@Repository
public class ProgressRepository implements IRestRepository<Progress> {
    protected final JdbcOperations jdbcOperations;

    private static String selectQuery = "SELECT \"id\", \"user_id\", \"task_id\", \"completed\", \"available\"" +
            "FROM \"progress\" " +
            "ORDER BY \"id\"";

    private static String selectByIdQuery = "SELECT  \"id\", \"user_id\", \"task_id\", \"completed\", \"available\"" +
            "FROM \"progress\" " +
            "WHERE \"id\" = ?";

    private static String selectByUserIdQuery = "SELECT  \"id\", \"user_id\", \"task_id\", \"completed\", \"available\"" +
            "FROM \"progress\" " +
            "WHERE \"user_id\" = ?";

    private static String selectIdByUserAndTaskQuery =
            "SELECT  \"id\", \"user_id\", \"task_id\", \"completed\", \"available\" " +
            "FROM \"progress\" " +
            "WHERE \"user_id\" = ? AND \"task_id\" = ?";

    private static String selectCompletedByIdQuery = "SELECT  \"completed\" " +
            "FROM \"progress\" " +
            "WHERE \"id\" = ?";

    private static String selectAvailableByIdQuery = "SELECT  \"completed\" " +
            "FROM \"progress\" " +
            "WHERE \"id\" = ?";

    private static String insertQuery = "INSERT INTO \"progress\"(\"user_id\", \"task_id\", \"completed\", \"available\")" +
            "VALUES (?, ?, ?, ?) " +
            "RETURNING \"id\", \"user_id\", \"task_id\", \"completed\", \"available\"";

    private static String updateQuery = "UPDATE \"progress\" " +
            "SET \"completed\" = ? " +
            "WHERE \"id\" = ? " +
            "RETURNING \"id\", \"user_id\", \"task_id\", \"completed\", \"available\"";

    private static String deleteQuery = "DELETE FROM \"progress\" " +
            "WHERE \"id\" = ? " +
            "RETURNING \"id\", \"user_id\", \"task_id\", \"completed\", \"available\"";

    public ProgressRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Progress[] select() {
        ArrayList<Progress> values = new ArrayList<Progress>();
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectQuery);
        while (rowSet.next()) {
            values.add(new Progress(
                    rowSet.getInt(1),
                    rowSet.getInt(2),
                    rowSet.getInt(3),
                    rowSet.getBoolean(4),
                    rowSet.getBoolean(5)
            ));
        }
        Progress[] result = new Progress[values.size()];
        result = values.toArray(result);
        return result;
    }

    @Override
    public Progress select(Integer id) {
        Object[] params = new Object[] { id };
        int[] types = new int[] { Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectByIdQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new Progress(
                rowSet.getInt(1),
                rowSet.getInt(2),
                rowSet.getInt(3),
                rowSet.getBoolean(4),
                rowSet.getBoolean(5)
        );
    }

    public Progress[] selectByUserId(Integer userId) {
        ArrayList<Progress> values = new ArrayList<Progress>();
        Object[] params = new Object[] { userId };
        int[] types = new int[] { Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectByUserIdQuery, params, types);
        while (rowSet.next()) {
            values.add(new Progress(
                    rowSet.getInt(1),
                    rowSet.getInt(2),
                    rowSet.getInt(3),
                    rowSet.getBoolean(4),
                    rowSet.getBoolean(5)
            ));
        }
        Progress[] result = new Progress[values.size()];
        result = values.toArray(result);
        return result;
    }

    public Progress selectIdByUserAndTask(Integer userId, Integer taskId) {
        ArrayList<Progress> values = new ArrayList<Progress>();
        Object[] params = new Object[] { userId, taskId };
        int[] types = new int[] { Types.INTEGER, Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectIdByUserAndTaskQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new Progress(
                rowSet.getInt(1),
                rowSet.getInt(2),
                rowSet.getInt(3),
                rowSet.getBoolean(4),
                rowSet.getBoolean(5)
        );
    }

    public Boolean selectCompletedById(Integer id) {
        ArrayList<Progress> values = new ArrayList<Progress>();
        Object[] params = new Object[] { id};
        int[] types = new int[] { Types.INTEGER};
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectCompletedByIdQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return rowSet.getBoolean(1);
    }

    public Boolean selectAvailableById(Integer id) {
        ArrayList<Progress> values = new ArrayList<Progress>();
        Object[] params = new Object[] { id};
        int[] types = new int[] { Types.INTEGER};
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectAvailableByIdQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return rowSet.getBoolean(1);
    }

    @Override
    public Progress insert(Progress entity) {
        Object[] params = new Object[] { entity.getUserId(), entity.getTaskId(), entity.getCompleted(), entity.getAvailable() };
        int[] types = new int[] { Types.INTEGER, Types.INTEGER, Types.BOOLEAN, Types.BOOLEAN };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(insertQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new Progress(
                rowSet.getInt(1),
                rowSet.getInt(2),
                rowSet.getInt(3),
                rowSet.getBoolean(4),
                rowSet.getBoolean(5)
        );
    }

    @Override
    public Progress update(Integer id, Progress entity) {
        Object[] params = new Object[] { entity.getCompleted(), id };
        int[] types = new int[] { Types.BOOLEAN, Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(updateQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new Progress(
                rowSet.getInt(1),
                rowSet.getInt(2),
                rowSet.getInt(3),
                rowSet.getBoolean(4),
                rowSet.getBoolean(5)
        );
    }

    @Override
    public Progress delete(Integer id) {
        Object[] params = new Object[] { id };
        int[] types = new int[] {Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(deleteQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new Progress(
                rowSet.getInt(1),
                rowSet.getInt(2),
                rowSet.getInt(3),
                rowSet.getBoolean(4),
                rowSet.getBoolean(5)
        );
    }
}
