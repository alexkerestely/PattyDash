package main;
import inputs.keyboardInputs;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseListener;
import inputs.MouseInputs;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {
    private Game game;
    private MouseInputs mouseInputs;
    public GamePanel(Game game) {
        mouseInputs = new MouseInputs(this);
        this.game = game;
        setPanelSize();
        addKeyListener(new keyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH,GAME_HEIGHT);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        game.render(g);
    }

    public void updateGame() {

    }

    public Game getGame() {
        return game;
    }
}
