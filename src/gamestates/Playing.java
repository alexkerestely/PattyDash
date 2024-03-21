package gamestates;

import UI.GameOverOverlay;
import UI.LevelCompletedOverlay;
import database.Database;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utils.Constants.Environment.SMALL_CLOUD_HEIGHT;
import static utils.Constants.Environment.SMALL_CLOUD_WIDTH;

public class Playing extends State implements Statemethods {

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private LevelCompletedOverlay levelCompletedOverlay;
    private GameOverOverlay gameOverOverlay;
    private Database database;

    public static int xLvlOffset;
    private int leftBorder = (int)(0.2* Game.GAME_WIDTH);
    private int rightBorder = (int)(0.8* Game.GAME_WIDTH);
    private int maxLvlOffsetX;

    private BufferedImage backgroundImage, smallCloud;
    private int[] smallCloudPos;
    private Random rnd = new Random();

    private boolean lvlCompleted;
    private  boolean gameOver = false;

    public Playing(Game game) {
        super(game);
        initClasses();
        backgroundImage = LoadSave.getSpriteAtlas(LoadSave.LVL1_BG_IMG);
        smallCloud = LoadSave.getSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudPos = new int[8];
        for(int i=0;i<smallCloudPos.length;i++)
            smallCloudPos[i] = (int)(30*Game.SCALE) + rnd.nextInt((int)(200* Game.SCALE));

        calcLvlOffset();
        loadStartLevel();

    }

    public void setBackground() {
        switch(levelManager.getLvlIndex()) {
            case 1 ->{
                backgroundImage = LoadSave.getSpriteAtlas(LoadSave.LVL2_BG_IMG);
            }
            case 2 -> {
                backgroundImage = LoadSave.getSpriteAtlas(LoadSave.LVL3_BG_IMG);
            }
        }
    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
    }

    private void loadStartLevel() {

        objectManager.loadObjects(levelManager.getCurrentLevel());
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {

        maxLvlOffsetX = levelManager.getCurrentLevel().getMaxLvlOffset();
    }

    private void initClasses() {
        database = Database.getInstance();
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);
        player = new Player(100,100,  (int) (64 * Game.SCALE), (int) (64 * Game.SCALE), this, database, levelManager);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        levelCompletedOverlay = new LevelCompletedOverlay(this, database);
        gameOverOverlay = new GameOverOverlay(this);
    }

    @Override
    public void update() {
        player.checkScore();
        if(lvlCompleted) {
            levelCompletedOverlay.update();

        } else if(gameOver) {
            gameOverOverlay.update();
        }
        else
        {
               // levelManager.update();
                enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
                objectManager.update();
                player.update();
                checkCloseToBorder();
        }

    }

    @Override
    public void draw(Graphics g) {

        g.drawImage(backgroundImage, 0, 0,Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g,xLvlOffset);
        objectManager.draw(g, xLvlOffset);
        drawClouds(g);

        if(gameOver)
            gameOverOverlay.draw(g);
        else if(lvlCompleted)
            levelCompletedOverlay.draw(g);


    }

    public void checkIsIngredientTouched(Rectangle2D.Float hitbox ) {
        objectManager.checkObjectIsTouched(hitbox);
    }

    public void resetAll() {

        gameOver = false;
        lvlCompleted = false;
        player.resetAll();
        objectManager.resetAllObjects();
        enemyManager.resetAllEnemies();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver)
            if (lvlCompleted)
                levelCompletedOverlay.mousePressed(e);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver)
            if(lvlCompleted)
                levelCompletedOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver)
            if(lvlCompleted)
                levelCompletedOverlay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver)
            gameOverOverlay.keyPressed(e);
        else
            switch(e.getKeyCode()) {
                case KeyEvent.VK_W:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_S:
                    player.setDown(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    Gamestate.state = Gamestate.MENU;
                    break;
                case KeyEvent.VK_SPACE:
                    player.setAttacking(true);
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W:
                player.setJump(false);
                break;
            case KeyEvent.VK_S:
                player.setDown(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
        }
    }

    private void checkCloseToBorder() {

        int playerX = (int)player.getHitbox().x;
        int difference = playerX - xLvlOffset;
        if (difference > rightBorder)
            xLvlOffset += difference - rightBorder;

        if(difference < leftBorder)
            xLvlOffset += difference - leftBorder;

        if(xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        if(xLvlOffset < 0)
            xLvlOffset = 0;
    }

    private  void drawClouds(Graphics g) {

        for(int i=0;i<smallCloudPos.length;i++)
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 3* i - (int)(xLvlOffset * 0.7), smallCloudPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }

    public Player getPlayer() {
        return player;
    }

    public ObjectManager getObjectManager() {return objectManager;}

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public void setLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }

    public void setLevelCompleted(boolean lvlCompleted) {
        this.lvlCompleted = lvlCompleted;

    }

    public boolean isLevelCompleted() {
        return lvlCompleted;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyIsGettingHit(attackBox);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;

    }
}
