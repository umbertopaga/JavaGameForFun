import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Leaderboard extends JFrame {
    private JTextArea leaderboardArea;

    public Leaderboard() {
        this.setSize(400, 500);
        this.setTitle("Leaderboard");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        leaderboardArea = new JTextArea();
        leaderboardArea.setEditable(false);
        this.add(new JScrollPane(leaderboardArea), BorderLayout.CENTER);

        loadLeaderboard();
        this.setVisible(true);
    }

    private void loadLeaderboard() {
        List<String[]> scores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("docs/leaderboard.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(line.split(" - "));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        scores.sort((a, b) -> {
            int scoreComparison = Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1]));
            if (scoreComparison != 0) {
                return scoreComparison;
            } else {
                return Long.compare(Long.parseLong(a[2].split(" ")[0]), Long.parseLong(b[2].split(" ")[0]));
            }
        });

        for (String[] score : scores) {
            leaderboardArea.append(String.join(" - ", score) + "\n");
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new Leaderboard());
    }
}
