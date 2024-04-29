import java.sql.*;
import java.util.ArrayList;

class DatabaseManager implements AutoCloseable {
    private final Connection connection;

    public DatabaseManager() throws SQLException {
        String user = "";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/";

        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to MySQL database");
    }

    public void saveUser(String username, String password) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User saved to successfully!");
            }
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
        }
    }

    public ArrayList<String> getAllUsers() throws SQLException {
        ArrayList<String> userList = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String userString = "ID: " + id + ", Username: " + username + ", Password: " + password;
                userList.add(userString);
            }
        }

        System.out.println("List of users: " + userList);
        return userList;
    }


    public void deleteUser(String username) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE username = ?")) {
            statement.setString(1, username);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User successfully deleted!");
            }
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Connection closed");
        }
    }
}