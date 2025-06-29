package pack1;

import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JFrame {
	private JLabel usernameLabel, fullNameLabel, passwordLabel;
    private JTextField usernameField, fullNameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton;
    private DatabaseManager dbManager;

    public WelcomePage() {
        try { dbManager = new DatabaseManager(); } catch (Exception e) { showError("Data Base Error! Check if its running or not!"); return; }
    	
    	setTitle("The Quizzer - Welcome Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel welcomePanel = new JPanel(new GridLayout(4, 2, 10, 10));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        fullNameLabel = new JLabel("Full Name:");
        fullNameField = new JTextField();
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");

        welcomePanel.add(usernameLabel);
        welcomePanel.add(usernameField);
        welcomePanel.add(fullNameLabel);
        welcomePanel.add(fullNameField);
        welcomePanel.add(passwordLabel);
        welcomePanel.add(passwordField);
        welcomePanel.add(loginButton);
        welcomePanel.add(signupButton);

        add(welcomePanel);

        loginButton.addActionListener(actionEvent -> handleLogin());
        signupButton.addActionListener(actionEvent -> handleSignup());

        setVisible(true);
    }

    private void handleLogin() {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            User user = dbManager.login(username, password);
            if (user != null) {
                dispose();
                new InstructionsPage(user);
            } else showError("Invalid credentials");
        } catch (Exception exception) { showError(exception.getMessage()); }
    }

    private void handleSignup() {
        try {
            String username = usernameField.getText();
            String fullName = fullNameField.getText();
            String password = new String(passwordField.getPassword());

            if (dbManager.isUsernameTaken(username)) {
                showError("Username is taken. If it's yours, log in.");
                return;
            }

            String passwordError = validatePassword(password);
            if (passwordError != null) { showError(passwordError); return; }

            if (dbManager.insertUser(username, fullName, password)) {
                JOptionPane.showMessageDialog(this, "Account created. Please log in.");
            }
        } catch (Exception exception) { showError(exception.getMessage()); }
    }

    private String validatePassword(String password) {
        if (password.length() < 8) return "Password must be at least 8 characters.";
        if (!password.matches(".*[A-Z].*")) return "Password must contain an uppercase letter.";
        if (!password.matches(".*[a-z].*")) return "Password must contain a lowercase letter.";
        if (!password.matches(".*\\d.*")) return "Password must contain a digit.";
        if (!password.matches(".*[^a-zA-Z0-9].*")) return "Password must contain a special character.";
        return null;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
