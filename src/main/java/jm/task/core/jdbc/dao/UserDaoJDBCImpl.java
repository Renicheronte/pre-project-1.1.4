package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Statement statement = Util.getConnection().createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Users" +
                    "(id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(32), last_name VARCHAR(32), age TINYINT DEFAULT 0)");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Statement statement = Util.getConnection().createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Users");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String insertQuery = "INSERT INTO Users (name, last_name, age) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(insertQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("Пользователь с именем " + name + " добавлен в базу данных");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        String deleteQuery = "DELETE FROM Users WHERE id = ?";
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(deleteQuery)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement("SELECT * FROM Users");
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while (resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                                     resultSet.getString("last_name"),
                                     resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                userList.add(user);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Statement statement = Util.getConnection().createStatement()) {
            statement.execute("TRUNCATE TABLE Users");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
