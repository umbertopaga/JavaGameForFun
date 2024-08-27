import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private HomePanel homePanel;

    public GameFrame() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Il mio Gioco in Java");

        homePanel = new HomePanel(this);
        this.add(homePanel);
        this.setVisible(true);
    }

    public void startGame(String playerName, int startLevel) {
        this.remove(homePanel);
        gamePanel = new GamePanel(playerName, startLevel);
        this.add(gamePanel);
        this.setJMenuBar(createMenuBar());
        this.revalidate();
        gamePanel.startGame();
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

    private void showLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setVisible(true);
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
