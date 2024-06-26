package gamestates;

import UI.MenuButton;
import main.Game;

import java.awt.event.MouseEvent;

public class State {

    protected Game game;
    public State(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public boolean isPressingInsideButton(MouseEvent e, MenuButton mb) {
            return mb.getBounds().contains(e.getX(), e.getY());
    }
}
