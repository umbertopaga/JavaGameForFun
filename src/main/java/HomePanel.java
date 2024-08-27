import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePanel extends JPanel {
    private GameFrame gameFrame;
    private JTextField nameField;
    private JComboBox<Integer> levelSelector;

    public HomePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;

        JLabel nameLabel = new JLabel("Inserisci il tuo nome:");
        nameField = new JTextField(10);
        
        JLabel levelLabel = new JLabel("Seleziona il livello di partenza:");
        Integer[] levels = {1, 2, 3, 4, 5};
        levelSelector = new JComboBox<>(levels);

        JButton startButton = new JButton("Inizia Gioco");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText().trim();
                if (playerName.isEmpty()) {
                    playerName = "Giocatore";
                }
                int startLevel = (int) levelSelector.getSelectedItem();
                gameFrame.startGame(playerName, startLevel);
            }
        });

        this.add(nameLabel);
        this.add(nameField);
        this.add(levelLabel);
        this.add(levelSelector);
        this.add(startButton);
    }
}
