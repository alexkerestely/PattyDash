package entities;

import gamestates.Playing;
import levels.Level;
import utils.HelpMethods;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] burgerArr;
    private BufferedImage[][] fryArr;
    private BufferedImage[][] juiceArr;
    private ArrayList<Burger> burgers = new ArrayList<>();
    private ArrayList<Fry> fries = new ArrayList<>();
    private ArrayList<Juice> juices = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImages();
    }

    public void loadEnemies(Level level) {

        burgers = level.getBurgers();
        fries = level.getFries();
        juices = level.getJuices();
    }

    private void loadEnemyImages() {
        burgerArr = new BufferedImage[3][4];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.ENEMY1_SPRITE);
        for (int j = 0; j < burgerArr.length; j++)
            for (int i = 0; i < burgerArr[j].length; i++)
                burgerArr[j][i] = temp.getSubimage(i * BURGER_WIDTH_DEFAULT, j * BURGER_HEIGHT_DEFAULT, BURGER_WIDTH_DEFAULT, BURGER_HEIGHT_DEFAULT);

        fryArr = new BufferedImage[3][4];
        BufferedImage temp1 = LoadSave.getSpriteAtlas(LoadSave.ENEMY2_SPRITE);
        for (int j = 0; j < fryArr.length; j++)
            for (int i = 0; i < fryArr[j].length; i++)
                fryArr[j][i] = temp1.getSubimage(i * FRY_WIDTH_DEFAULT, j * FRY_HEIGHT_DEFAULT, FRY_WIDTH_DEFAULT, FRY_HEIGHT_DEFAULT);

        juiceArr = new BufferedImage[3][4];
        BufferedImage temp2 = LoadSave.getSpriteAtlas(LoadSave.ENEMY3_SPRITE);
        for (int j = 0; j < juiceArr.length; j++)
            for (int i = 0; i < juiceArr[j].length; i++)
                juiceArr[j][i] = temp2.getSubimage(i * JUICE_WIDTH_DEFAULT, j * JUICE_HEIGHT_DEFAULT, JUICE_WIDTH_DEFAULT, JUICE_HEIGHT_DEFAULT);
    }

    public void draw(Graphics g, int xLvlOffset) {

        drawBurgers(g, xLvlOffset);
        drawFries(g, xLvlOffset);
        drawJuices(g, xLvlOffset);
    }

    private void drawBurgers(Graphics g, int xLvlOffset) {
        for (Burger b : burgers) {
            if(b.isActive()) {
                //b.drawHitbox(g, xLvlOffset);
                g.drawImage(burgerArr[b.getState()][b.getAnimationIndex()], (int) b.getHitbox().x - xLvlOffset - BURGER_DRAWOFFSET_X, (int) b.getHitbox().y - BURGER_DRAWOFFSET_Y, BURGER_WIDTH, BURGER_HEIGHT, null);
                //b.drawAttackBox(g, xLvlOffset);

            }
        }
    }
    private void drawFries(Graphics g, int xLvlOffset) {
        for (Fry f : fries) {
            if(f.isActive()) {
                //f.drawHitbox(g, xLvlOffset);
                g.drawImage(fryArr[f.getState()][f.getAnimationIndex()], (int) f.getHitbox().x - xLvlOffset - FRY_DRAWOFFSET_X , (int) f.getHitbox().y - FRY_DRAWOFFSET_Y, FRY_WIDTH, FRY_HEIGHT, null);
                //f.drawAttackBox(g, xLvlOffset);

            }
        }
    }

    private void drawJuices(Graphics g, int xLvlOffset) {
        for (Juice j : juices) {
            if(j.isActive()) {
                //j.drawHitbox(g, xLvlOffset);
                g.drawImage(juiceArr[j.getState()][j.getAnimationIndex()], (int) j.getHitbox().x - xLvlOffset , (int) j.getHitbox().y - JUICE_DRAWOFFSET_Y, JUICE_WIDTH, JUICE_HEIGHT, null);
                //j.drawAttackBox(g, xLvlOffset);

            }
        }
    }


    public void checkEnemyIsGettingHit(Rectangle2D.Float attackBox) { //enemy takes damage
        for (Burger b : burgers) {
            if(b.isActive()) {
                if (attackBox.intersects(b.getHitbox())) {
                    b.hurt(10);
                    return;
                }
            }
        }

        for (Fry f : fries) {
            if(f.isActive()) {
                if (attackBox.intersects(f.getHitbox())) {
                    f.hurt(10);
                    return;
                }
            }
        }

        for (Juice j : juices) {
            if(j.isActive()) {
                if (attackBox.intersects(j.getHitbox())) {
                    j.hurt(10);
                    return;
                }
            }
        }
    }

    public void update(int[][] lvlData, Player player) {

        for(Burger b : burgers) {
            if(b.isActive())
            {   b.update(lvlData, player);
                }
        }

        for(Fry f : fries) {
            if(f.isActive())
            {   f.update(lvlData, player);
                }
        }

        for(Juice j : juices) {
            if(j.isActive())
            {   j.update(lvlData, player);
                }
        }


    }

    public void resetAllEnemies() {
        for(Burger b : burgers)
            b.resetEnemy();

        for(Fry f : fries)
            f.resetEnemy();

        for(Juice j : juices)
            j.resetEnemy();
    }
}
