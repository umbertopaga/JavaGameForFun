import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private HomePanel homePanel;

    public GameFrame() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Il mio Gioco in Java");

        showHomePanel();
        this.setVisible(true);
    }

    public void showHomePanel() {
        if (homePanel == null) {
            homePanel = new HomePanel(this);
        } else {
            this.remove(gamePanel);
        }
        this.add(homePanel);
        this.revalidate();
        this.repaint();
    }

    public void startGame(String playerName, int startLevel) {
        this.remove(homePanel);
        gamePanel = new GamePanel(playerName, startLevel, this);
        this.add(gamePanel);
        this.setJMenuBar(createMenuBar());
        this.revalidate();

        gamePanel.startGame();
    }

    public void showLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem leaderboard = new JMenuItem("Leaderboard");
        JMenuItem exitGame = new JMenuItem("Esci");

        leaderboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLeaderboard();
            }
        });

        exitGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menu.add(leaderboard);
        menu.add(exitGame);
        menuBar.add(menu);

        return menuBar;
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
