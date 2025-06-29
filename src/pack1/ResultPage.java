package pack1;

import javax.swing.*;
import java.awt.*;

public class ResultPage extends JFrame {
    public ResultPage(User user, int score) {
        setTitle("Results");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
               
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
 
        JLabel scoreLabel = new JLabel("Your score: " + score);
        JButton retryButton = new JButton("Re-take Exam");
        JButton exitButton = new JButton("Exit App");
        
        panel.add(scoreLabel);
        panel.add(retryButton);
        panel.add(exitButton);
        
        retryButton.addActionListener(actionEvent -> {
            dispose();
            new InstructionsPage(user);
        });

        exitButton.addActionListener(actionEvent -> System.exit(0));
        panel.add(exitButton);

        add(panel);
        setVisible(true);
    }
}