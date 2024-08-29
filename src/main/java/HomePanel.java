import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class HomePanel extends JPanel {
    private GameFrame gameFrame;

    public HomePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.setLayout(new GridBagLayout()); // Usare GridBagLayout per centrare i componenti

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spaziatura tra i componenti
        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = JOptionPane.showInputDialog("Inserisci il tuo nome:");
                if (playerName == null || playerName.trim().isEmpty()) {
                    playerName = "Giocatore";
                }
                gameFrame.startGame(playerName, 1); // Inizia dal livello 1
            }
        });

        JButton leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.showLeaderboard(); // Mostra la leaderboard
            }
        });

        JButton chooseLevelButton = new JButton("Choose Level");
        chooseLevelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] levels = {"1", "2", "3", "4", "5"};
                String level = (String) JOptionPane.showInputDialog(
                        null,
                        "Scegli un livello",
                        "Choose Level",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        levels,
                        levels[0]
                );
                if (level != null) {
                    int startLevel = Integer.parseInt(level);
                    String playerName = JOptionPane.showInputDialog("Inserisci il tuo nome:");
                    if (playerName == null || playerName.trim().isEmpty()) {
                        playerName = "Giocatore";
                    }
                    gameFrame.startGame(playerName, startLevel); // Inizia dal livello scelto
                }
            }
        });

        gbc.gridy++;
        this.add(startButton, gbc);

        gbc.gridy++;
        this.add(leaderboardButton, gbc);

        gbc.gridy++;
        this.add(chooseLevelButton, gbc);
    }
}
