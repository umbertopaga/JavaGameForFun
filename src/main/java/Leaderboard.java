import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;  // Import statement for JOptionPane
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Leaderboard extends JFrame {
    private JTextField nameField;
    private JTextField scoreField;
    private JTextArea leaderboardArea;

    public Leaderboard() {
        // Set up the JFrame
        this.setSize(400, 500);
        this.setTitle("Leaderboard");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Changed to DISPOSE_ON_CLOSE
        this.setLayout(new BorderLayout());

        // Create and set up the JTextArea
        leaderboardArea = new JTextArea();
        leaderboardArea.setEditable(false);
        this.add(new JScrollPane(leaderboardArea), BorderLayout.CENTER);

        // Create and set up the input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Score:"));
        scoreField = new JTextField();
        inputPanel.add(scoreField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener());
        inputPanel.add(submitButton);

        this.add(inputPanel, BorderLayout.SOUTH);

        // Load existing leaderboard data
        loadLeaderboard();

        this.setVisible(true);
    }

    private void loadLeaderboard() {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/upaganelli/Desktop/c/docs/leaderboard.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                leaderboardArea.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLeaderboard(String name, String score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/upaganelli/Desktop/c/docs/leaderboard.txt", true))) {
            writer.write(name + " " + score);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String score = scoreField.getText();

            if (name.isEmpty() || score.isEmpty()) {
                JOptionPane.showMessageDialog(Leaderboard.this, "Name and score cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update the leaderboard file
            updateLeaderboard(name, score);

            // Update the JTextArea to reflect the new entry
            leaderboardArea.append(name + " " + score + "/n");

            // Clear the input fields
            nameField.setText("");
            scoreField.setText("");
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new Leaderboard());
    }
}
