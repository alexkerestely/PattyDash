package UI;

import database.Database;
import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.URMButtons.URM_SIZE;

public class LevelCompletedOverlay {

    private Playing playing;
    private Database database;
    private UrmButton menu, next;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;

    public LevelCompletedOverlay(Playing playing, Database database) {
        this.playing = playing;
        this.database = database;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (250 * Game.SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void initImg() {
        img = LoadSave.getSpriteAtlas(LoadSave.COMPLETED_IMG);
        bgW = (int) (img.getWidth() * Game.SCALE);
        bgH = (int) (img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (75 * Game.SCALE);
    }

    public void draw(Graphics g) {

        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);


        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        g.setColor(Color.blue);
        Font font= new Font("Helvetica", Font.BOLD, 40);
        g.setFont(font);
        g.drawString("SCORE: "+ Integer.toString(database.getScore()), bgX + bgW * 2/5, (int) (370 * Game.SCALE));
        next.draw(g);
        menu.draw(g);
    }

    private boolean isIn(UrmButton b, MouseEvent e) {

        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void update() {
        next.update();
        menu.update();
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (isIn(menu, e))
            menu.setMouseOver(true);
        else if (isIn(next, e))
            next.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)){
            if (menu.isMousePressed()) {
                playing.resetAll();
                Gamestate.state = Gamestate.MENU;
            }
        }
        else if (isIn(next, e))
                if (next.isMousePressed()) {
                    playing.loadNextLevel();
                }

        menu.resetBools();
        next.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e))
            menu.setMousePressed(true);
        else if (isIn(next, e)) {
            next.setMousePressed(true);
        }
    }
}
