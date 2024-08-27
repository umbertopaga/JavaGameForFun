import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GamePanel extends JPanel {
    private int score = 0;
    private int difficultyLevel = 1;
    private Rectangle player;
    private ArrayList<Rectangle> collectibles;
    private ArrayList<Rectangle> enemies;
    private Random random;
    private boolean gameRunning = false;
    private Timer enemyMoveTimer;
    private String playerName;
    private int targetScore = 20;

    public GamePanel(String playerName, int startLevel) {
        this.playerName = playerName;
        this.difficultyLevel = startLevel;
        setFocusable(true);
        requestFocusInWindow();
        player = new Rectangle(100, 100, 30, 30);
        collectibles = new ArrayList<>();
        enemies = new ArrayList<>();
        random = new Random();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameRunning) {
                    movePlayer(e);
                    checkCollisions();
                    repaint();
                }
            }
        });

        enemyMoveTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    moveEnemies();
                    checkCollisions();
                    repaint();
                }
            }
        });
        enemyMoveTimer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        generateCollectibles(5);
        generateEnemies(3);
    }

    public void startGame() {
        gameRunning = true;
        score = 0;
        player.setLocation(100, 100);
        collectibles.clear();
        enemies.clear();
        generateCollectibles(5);
        generateEnemies(3);
        repaint();
    }

    private void generateCollectibles(int number) {
        if (this.getWidth() > 0 && this.getHeight() > 0) {
            for (int i = 0; i < number; i++) {
                int x = random.nextInt(this.getWidth() - 20);
                int y = random.nextInt(this.getHeight() - 20);
                collectibles.add(new Rectangle(x, y, 20, 20));
            }
        }
    }

    private void generateEnemies(int number) {
        if (this.getWidth() > 0 && this.getHeight() > 0) {
            for (int i = 0; i < number; i++) {
                int x = random.nextInt(this.getWidth() - 30);
                int y = random.nextInt(this.getHeight() - 30);
                enemies.add(new Rectangle(x, y, 30, 30));
            }
        }
    }

    private void movePlayer(KeyEvent e) {
        int speed = 10;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.x = Math.max(0, player.x - speed);
                break;
            case KeyEvent.VK_RIGHT:
                player.x = Math.min(this.getWidth() - player.width, player.x + speed);
                break;
            case KeyEvent.VK_UP:
                player.y = Math.max(0, player.y - speed);
                break;
            case KeyEvent.VK_DOWN:
                player.y = Math.min(this.getHeight() - player.height, player.y + speed);
                break;
        }
    }

    private void moveEnemies() {
        int speed = difficultyLevel * 2;
        for (Rectangle enemy : enemies) {
            int direction = random.nextInt(4);
            switch (direction) {
                case 0:
                    enemy.x = Math.max(0, enemy.x - speed);
                    break;
                case 1:
                    enemy.x = Math.min(this.getWidth() - enemy.width, enemy.x + speed);
                    break;
                case 2:
                    enemy.y = Math.max(0, enemy.y - speed);
                    break;
                case 3:
                    enemy.y = Math.min(this.getHeight() - enemy.height, enemy.y + speed);
                    break;
            }
        }
    }

    private void checkCollisions() {
        Iterator<Rectangle> iterator = collectibles.iterator();
        while (iterator.hasNext()) {
            Rectangle collectible = iterator.next();
            if (player.intersects(collectible)) {
                iterator.remove();
                score++;
                if (score % 5 == 0) {
                    difficultyLevel++;
                    generateCollectibles(5);
                    generateEnemies(1);
                }
                if (score == targetScore) {
                    gameRunning = false;
                    showVictoryScreen();
                }
            }
        }

        for (Rectangle enemy : enemies) {
            if (player.intersects(enemy)) {
                gameRunning = false;
                JOptionPane.showMessageDialog(this, "Hai perso!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }

    private void showVictoryScreen() {
        JOptionPane.showMessageDialog(this, "Hai vinto!", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
        saveScore();
    }

    private void saveScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("docs/leaderboard.txt", true))) {
            writer.write(playerName + ": " + score);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameRunning) {
            g.drawString("Punteggio: " + score + " - Livello: " + difficultyLevel, 10, 20);
            g.fillRect(player.x, player.y, player.width, player.height);
            for (Rectangle item : collectibles) {
                g.fillRect(item.x, item.y, item.width, item.height);
            }
            g.setColor(java.awt.Color.RED);
            for (Rectangle enemy : enemies) {
                g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
            }
        } else {
            g.drawString("Premi 'Inizia Gioco' dal menu per iniziare", 10, 20);
        }
    }
}
