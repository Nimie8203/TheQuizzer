package pack1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExamPage extends JFrame {
    private User user;
    private List<Map<String, Object>> questionsList;
    private int currentQuestion = 0, score = 0;
    private int totalNumberOfQuestions;
    private JButton nextButton;
    private ButtonGroup buttonGroup;
    private JRadioButton[] testOptions;
    private JLabel questionLabel;
    private DatabaseManager dbManager;

    public ExamPage(User user, int questionCount) {
        this.user = user;
        this.totalNumberOfQuestions = questionCount;

        try {
            dbManager = new DatabaseManager();
            questionsList = dbManager.getRandomQuestions(totalNumberOfQuestions);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Failed to load questions.");
            return;
        }

        setTitle("Examination");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        questionLabel = new JLabel();
        testOptions = new JRadioButton[4];
        buttonGroup = new ButtonGroup();

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        for (int i = 0; i < 4; i++) {
            testOptions[i] = new JRadioButton();
            buttonGroup.add(testOptions[i]);
            optionsPanel.add(testOptions[i]);
        }

        nextButton = new JButton("Next");
        nextButton.addActionListener(actionEvent -> handleNext());

        add(questionLabel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
        add(nextButton, BorderLayout.SOUTH);

        loadQuestion();
        setVisible(true);
    }

    private void loadQuestion() {
        buttonGroup.clearSelection();
        Map<String, Object> questionMap = questionsList.get(currentQuestion);
        questionLabel.setText("Q" + (currentQuestion + 1) + ": " + questionMap.get("question"));

        List<String> answers = new ArrayList<>();
        answers.add((String) questionMap.get("correct"));
        answers.add((String) questionMap.get("wrong1"));
        answers.add((String) questionMap.get("wrong2"));
        answers.add((String) questionMap.get("wrong3"));
        Collections.shuffle(answers);

        for (int i = 0; i < 4; i++) testOptions[i].setText(answers.get(i));

        if (currentQuestion == totalNumberOfQuestions - 1) nextButton.setText("End Exam");
    }

    private void handleNext() {
        Map<String, Object> questionMap = questionsList.get(currentQuestion);
        String correctAnswer = (String) questionMap.get("correct");

        for (JRadioButton option : testOptions) {
            if (option.isSelected() && option.getText().equals(correctAnswer)) {
                score++;
                break;
            }
        }

        currentQuestion++;
        if (currentQuestion < totalNumberOfQuestions) loadQuestion();
        else {
            int finalScore = (int) ((100.0 / totalNumberOfQuestions) * score);
            try { dbManager.updateScore(user.getId(), finalScore); } catch (Exception exception) {}
            dispose();
            new ResultPage(user, finalScore);
        }
    }
}