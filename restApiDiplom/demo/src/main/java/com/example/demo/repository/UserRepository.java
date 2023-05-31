package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
@Repository
public class UserRepository implements IRestRepository<User> {
    protected final JdbcOperations jdbcOperations;

    private static String selectQuery = "SELECT \"id\", \"login\", \"password\", \"is_admin\"" +
            "FROM \"user\" " +
            "ORDER BY \"id\"";

    private static String selectByIdQuery = "SELECT  \"id\", \"login\", \"password\", \"is_admin\"" +
            "FROM \"user\" " +
            "WHERE \"id\" = ?";

    private static String selectByLoginQuery = "SELECT  \"id\", \"login\", \"password\", \"is_admin\"" +
            "FROM \"user\" " +
            "WHERE \"login\" = ?";

    private static String selectIdByLoginQuery = "SELECT  \"id\"" +
            "FROM \"user\" " +
            "WHERE \"login\" = ?";

    private static String selectPasswordByLoginQuery = "SELECT  \"password\" " +
            "FROM \"user\" " +
            "WHERE \"login\" = ?";

    private static String insertQuery = "INSERT INTO \"user\"(\"login\", \"password\")" +
            "VALUES (?, ?) " +
            "RETURNING \"id\", \"login\", \"password\", \"is_admin\"";

    private static String updateQuery = "UPDATE \"user\" " +
            "SET \"password\" = ?" +
            "WHERE \"id\" = ? " +
            "RETURNING \"id\", \"login\", \"password\", \"is_admin\"";

    private static String deleteQuery = "DELETE FROM \"user\" " +
            "WHERE \"id\" = ? " +
            "RETURNING \"id\", \"login\", \"password\", \"is_admin\"";

    public UserRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public User[] select() {
        ArrayList<User> values = new ArrayList<User>();
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectQuery);
        while (rowSet.next()) {
            values.add(new User(
                    rowSet.getInt(1),
                    rowSet.getString(2),
                    rowSet.getString(3),
                    rowSet.getBoolean(4)
            ));
        }
        User[] result = new User[values.size()];
        result = values.toArray(result);
        return result;
    }

    @Override
    public User select(Integer id) {
        Object[] params = new Object[] { id };
        int[] types = new int[] { Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectByIdQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new User(
                rowSet.getInt(1),
                rowSet.getString(2),
                rowSet.getString(3),
                rowSet.getBoolean(4)
        );
    }

    public User selectByLogin(String login) {
        ArrayList<User> values = new ArrayList<User>();
        Object[] params = new Object[] { login };
        int[] types = new int[] { Types.VARCHAR };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectByLoginQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new User(
                rowSet.getInt(1),
                rowSet.getString(2),
                rowSet.getString(3),
                rowSet.getBoolean(4)
        );
    }

    public Integer selectIdByLogin(String login) {
        ArrayList<User> values = new ArrayList<User>();
        Object[] params = new Object[] { login };
        int[] types = new int[] { Types.VARCHAR };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectIdByLoginQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return rowSet.getInt(1);
    }

    public String selectPasswordByLogin(String login) {
        ArrayList<User> values = new ArrayList<User>();
        Object[] params = new Object[] { login };
        int[] types = new int[] { Types.VARCHAR };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(selectPasswordByLoginQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return rowSet.getString(1);
    }

    @Override
    public User insert(User entity) {
        Object[] params = new Object[] { entity.getLogin(), entity.getPassword() };
        int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(insertQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new User(
                rowSet.getInt(1),
                rowSet.getString(2),
                rowSet.getString(3),
                rowSet.getBoolean(4)
        );
    }

    @Override
    public User update(Integer id, User entity) {
        Object[] params = new Object[] { entity.getPassword(), id };
        int[] types = new int[] { Types.VARCHAR, Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(updateQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new User(
                rowSet.getInt(1),
                rowSet.getString(2),
                rowSet.getString(3),
                rowSet.getBoolean(4)
        );
    }

    @Override
    public User delete(Integer id) {
        Object[] params = new Object[] { id };
        int[] types = new int[] {Types.INTEGER };
        SqlRowSet rowSet = jdbcOperations.queryForRowSet(deleteQuery, params, types);
        if (!rowSet.next()) {
            return null;
        }
        return new User(
                rowSet.getInt(1),
                rowSet.getString(2),
                rowSet.getString(3),
                rowSet.getBoolean(4)
        );
    }
}
