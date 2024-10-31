package repository;

import builder.GenericBuilder;
import database.ConnectionFactory;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository{

    private static volatile UserRepository instance;
    private final Connection connection;

    private UserRepository() {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        connection = connectionFactory.getConnection();
    }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result == null) {
            synchronized (UserRepository.class) {
                result = instance;
                if (result == null) {
                    instance = result = new UserRepository();
                }
            }
        }
        return result;
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(createUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public User findById(Long id) {
        String query = "SELECT * FROM user WHERE id = ?";
        try(PreparedStatement preparedStatement= connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return createUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private User createUser(ResultSet resultSet) throws SQLException {
        return GenericBuilder.of(User::new)
                .with(User::setId, resultSet.getLong("id"))
                .with(User::setUsername, resultSet.getString("username"))
                .with(User::setPassword, resultSet.getString("password"))
                .build();
    }

    @Override
    public void addUser(User user) {
        String query = "INSERT INTO user (username, password) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        String query = "UPDATE user SET username = ?, password = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setLong(3, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(Long id) {
        String query = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
