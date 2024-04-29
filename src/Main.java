import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

public class Main extends JFrame {

    private JTextArea userListTextArea;

    public Main() {
        initializedUI();
        displayAllUsers();
    }

    private void initializedUI() {
        setTitle("Simple App");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        JButton submitButton = new JButton("Submit");
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(submitButton, gbc);

        JLabel deleteLabel = new JLabel("Delete:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(deleteLabel, gbc);

        JTextField deleteField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(deleteField, gbc);

        JButton deleteButton = new JButton("Delete");
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(deleteButton, gbc);

        userListTextArea = new JTextArea(10, 30);
        userListTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(userListTextArea);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(scrollPane, gbc);

        deleteButton.addActionListener(e -> {
            String deleteUser = deleteField.getText();
            deleteUser(deleteUser);
            deleteField.setText("");
        });

        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            saveUserToDatabase(username, password);
            usernameField.setText("");
            passwordField.setText("");
        });
        add(panel);

        setVisible(true);
    }

    private void saveUserToDatabase(String username, String password) {
        try (DatabaseManager dbManager = new DatabaseManager()){
            dbManager.saveUser(username, password);
            displayAllUsers();
        } catch (SQLException e) {
            System.out.println("Error while saving user" + e.getMessage());
        }
    }

    private void displayAllUsers() {
        try (DatabaseManager dbManager = new DatabaseManager()) {
            ArrayList<String> users = dbManager.getAllUsers();
            userListTextArea.setText("");
            for (String user : users) {
                userListTextArea.append(user + "\n");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteUser(String username) {
        try (DatabaseManager dbManager = new DatabaseManager()) {
            dbManager.deleteUser(username);
            displayAllUsers();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
