package pack1;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InstructionsPage extends JFrame {
    private JComboBox<Integer> questionCountCombox;
    private JLabel instructionsLabel, leaderboardLabel;
    private JButton startButton;
    private JTextArea leaderboardArea, instructionsArea;
    private JPanel instructionsPanel;
    private User user;
    private DatabaseManager dbManager;

    public InstructionsPage(User user) {
        this.user = user;
        
        
        try { dbManager = new DatabaseManager(); } catch (Exception exception) { showError("Ther was an Error creating an object from database class"); return; }
        
        
        setTitle("Instructions Page");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        instructionsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        instructionsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        
        instructionsArea = new JTextArea("This is a multiple choice quiz. Select one option per question or skip using 'Next'.");
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setLineWrap(true);
        instructionsArea.setEditable(false);
        instructionsLabel = new JLabel("Instructions:");
        
        instructionsPanel.add(instructionsLabel);
        instructionsPanel.add(instructionsArea);
        
        
        leaderboardLabel = new JLabel("Leaderboard:");
        instructionsPanel.add(leaderboardLabel);
        leaderboardArea = new JTextArea();
        leaderboardArea.setEditable(false);
        try { 
            List<String> userScores = dbManager.getTopScores();
            for (String score : userScores) leaderboardArea.append(score + "\n");
        } catch (Exception exception) {
            leaderboardArea.setText("Failed to load leaderboard from the database.");
        }
        instructionsPanel.add(new JScrollPane(leaderboardArea));
        
        
        instructionsPanel.add(new JLabel("Number of Questions:"));
        questionCountCombox = new JComboBox<>();
        for (int i = 1; i <= 20; i++) questionCountCombox.addItem(i);
        instructionsPanel.add(questionCountCombox);

        
        startButton = new JButton("Start Exam");
        startButton.addActionListener(actionEvent -> startExam());
 
        
        add(instructionsPanel, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);
       
        
        setVisible(true);
    }

    private void startExam() {
        int count = (Integer) questionCountCombox.getSelectedItem();
        dispose();
        new ExamPage(user, count);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}