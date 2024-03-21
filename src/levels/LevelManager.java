package levels;

import gamestates.Gamestate;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;

    private ArrayList<Level> levels;
    private int lvlIndex = 0;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels;
        try {
            allLevels = LoadSave.getAllLevels();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        for (BufferedImage img : allLevels)
            levels.add(new Level(img));
    }

    public void loadNextLevel() {
        lvlIndex++;
        if(lvlIndex >= levels.size()) {
            lvlIndex =0;
            System.out.println("No more levels");
            Gamestate.state = Gamestate.MENU;
        }

        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setLvlOffset(newLevel.getMaxLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
        game.getPlaying().setBackground();

    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);

        levelSprite = new BufferedImage[7];
        levelSprite[0] = img.getSubimage(0, 0 * 32,  32, 32);
        levelSprite[1] = img.getSubimage(32, 0 * 32,  32, 32);
        levelSprite[2] = img.getSubimage(2*32, 0 * 32,  32, 32);
        levelSprite[3] = img.getSubimage(0,1 * 32,  32, 32);
        levelSprite[4] = img.getSubimage(32,1 * 32,  32, 32);
        levelSprite[5] = img.getSubimage(64, 1 * 32, 32, 32);


    }

    public void draw(Graphics g, int xLvlOffset) {
        for (int j = 0; j < Game.TILE_IN_HEIGHT; j++)
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], Game.TILES_SIZE * i - xLvlOffset, Game.TILES_SIZE * j, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
    }

    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    public int getLvlIndex() {
        return lvlIndex;
    }
}
