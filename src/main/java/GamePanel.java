import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GamePanel extends JPanel {
    private int score = 0;
    private int difficultyLevel = 1;
    private Rectangle player;
    private ArrayList<Rectangle> collectibles;
    private ArrayList<Rectangle> enemies;
    private ArrayList<int[]> enemyDirections;
    private Random random;
    private boolean gameRunning = false;
    private javax.swing.Timer enemyMoveTimer;
    private String playerName;
    private int targetScore;
    private GameFrame gameFrame;
    private LocalDateTime startTime;
    private int currentLevel;

    public GamePanel(String playerName, int startLevel, GameFrame gameFrame) {
        this.playerName = playerName;
        this.difficultyLevel = startLevel;
        this.gameFrame = gameFrame;
        this.currentLevel = startLevel;
        setFocusable(true);
        requestFocusInWindow();
        player = new Rectangle(100, 100, 30, 30);
        collectibles = new ArrayList<>();
        enemies = new ArrayList<>();
        enemyDirections = new ArrayList<>();
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

        enemyMoveTimer = new javax.swing.Timer(50, new ActionListener() {
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

    public void startGame() {
        gameRunning = true;
        score = 0;
        player.setLocation(100, 100);
        startTime = LocalDateTime.now();
        setupLevel(currentLevel);
        repaint();
    }

    private void setupLevel(int level) {
        collectibles.clear();
        enemies.clear();
        enemyDirections.clear();

        if (level < 6) { // Livelli normali
            targetScore = 5; // Imposta il punteggio target per i livelli normali
            generateCollectibles(targetScore);
            generateEnemies(level + 2);
        } else { // Livello bonus
            targetScore = 1; // Solo un oggetto da raccogliere nel livello bonus
            generateCollectibles(targetScore);
            generateEnemies(20); // Molti nemici per il livello bonus
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
        for (int i = 0; i < enemies.size(); i++) {
            Rectangle enemy = enemies.get(i);
            int[] direction = enemyDirections.get(i);

            enemy.x += direction[0] * difficultyLevel;
            enemy.y += direction[1] * difficultyLevel;

            if (enemy.x <= 0 || enemy.x >= this.getWidth() - enemy.width) {
                direction[0] = -direction[0];
            }
            if (enemy.y <= 0 || enemy.y >= this.getHeight() - enemy.height) {
                direction[1] = -direction[1];
            }

            for (int j = 0; j < enemies.size(); j++) {
                if (i != j && enemy.intersects(enemies.get(j))) {
                    direction[0] = -direction[0];
                    direction[1] = -direction[1];
                }
            }

            enemies.set(i, enemy);
            enemyDirections.set(i, direction);
        }
    }

    private void checkCollisions() {
        Iterator<Rectangle> iterator = collectibles.iterator();
        while (iterator.hasNext()) {
            Rectangle collectible = iterator.next();
            if (player.intersects(collectible)) {
                iterator.remove();
                score++;
                if (score % targetScore == 0) { // Passa al livello successivo
                    if (currentLevel < 6) {
                        currentLevel++;
                        difficultyLevel++;
                        setupLevel(currentLevel);
                    } else {
                        gameRunning = false;
                        saveScore();
                        showVictoryScreen();
                    }
                }
            }
        }

        for (Rectangle enemy : enemies) {
            if (player.intersects(enemy)) {
                gameRunning = false;
                saveScore();
                JOptionPane.showMessageDialog(this, "Hai perso!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                gameFrame.showHomePanel(); // Torna alla schermata iniziale
                break;
            }
        }
    }

    private void showVictoryScreen() {
        JOptionPane.showMessageDialog(this, "Hai vinto!", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
        gameFrame.showHomePanel(); // Torna alla schermata iniziale
    }

    private void saveScore() {
        Duration gameTime = Duration.between(startTime, LocalDateTime.now());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("docs/leaderboard.txt", true))) {
            writer.write(playerName + " - " + score + " - " + gameTime.getSeconds() + " seconds");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateCollectibles(int number) {
        for (int i = 0; i < number; i++) {
            int x = random.nextInt(this.getWidth() - 20);
            int y = random.nextInt(this.getHeight() - 20);
            collectibles.add(new Rectangle(x, y, 20, 20));
        }
    }

    private void generateEnemies(int number) {
        for (int i = 0; i < number; i++) {
            Rectangle enemy;
            int[] direction = new int[2];
            boolean validPosition;

            do {
                int x = random.nextInt(this.getWidth() - 30);
                int y = random.nextInt(this.getHeight() - 30);
                enemy = new Rectangle(x, y, 30, 30);
                direction[0] = random.nextInt(7) - 3;
                direction[1] = random.nextInt(7) - 3;

                // Verifica che il nemico non si generi sopra un altro nemico o sopra il giocatore
                validPosition = !enemy.intersects(player) && !isCollidingWithOtherEnemies(enemy);

         } while (!validPosition || (direction[0] == 0 && direction[1] == 0));  // Continua finché la posizione non è valida

            enemies.add(enemy);
            enemyDirections.add(direction);
        }
    }

    private boolean isCollidingWithOtherEnemies(Rectangle newEnemy) {
        for (Rectangle existingEnemy : enemies) {
            if (newEnemy.intersects(existingEnemy)) {
                return true;  // C'è una collisione
            }
        }
        return false;  // Nessuna collisione
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
            g.setColor(Color.RED);
            for (Rectangle enemy : enemies) {
                g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
            }
        } else {
            g.drawString("Premi 'Inizia Gioco' dal menu per iniziare", 10, 20);
        }
    }
}
