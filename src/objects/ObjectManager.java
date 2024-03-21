package objects;

import gamestates.Playing;
import levels.Level;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.ObjectConstants.*;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] objImages;
    private ArrayList<Ingredient> ingredients;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImages();

       ingredients = new ArrayList<>();

    }

    private void loadImages() {

        BufferedImage objSprite = LoadSave.getSpriteAtlas(LoadSave.OBJECTS_SPRITE);
        objImages = new BufferedImage[5][4];

        for (int j = 0; j < objImages.length; j++)
            for (int i = 0; i < objImages[j].length; i++)
                objImages[j][i] = objSprite.getSubimage(30 * i, 30 * j, 30, 30);

    }

    public void loadObjects(Level newLevel) {
        ingredients = newLevel.getIngredientList();

    }

    public void checkObjectIsTouched(Rectangle2D.Float hitbox) {

        for(Ingredient i : ingredients) {
            if(i.isActive()) {
                if(hitbox.intersects(i.getHitbox())) {
                    i.setActive(false);
                    applyEffect(i);
                }
            }
        }

    }

    public void applyEffect(Ingredient i) {

        switch (i.getObjType()) {
            case TOMATO -> playing.getPlayer().changeScore(TOMATO_VALUE);
            case SALAD -> playing.getPlayer().changeScore(SALAD_VALUE);
            case PATTY -> playing.getPlayer().changeScore(PATTY_VALUE);
            case CHEESE ->playing.getPlayer().changeScore(CHEESE_VALUE);
            case BAGEL ->playing.getPlayer().changeScore(BAGEL_VALUE);

        }

    }


    public void update() {
        for (Ingredient i : ingredients)
            if (i.isActive())
                i.update();
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawIngredients(g, xLvlOffset);
    }

    private void drawIngredients(Graphics g, int xLvlOffset) {
        for (Ingredient i : ingredients)
            if (i.isActive()) {
                int type=-1;
                switch (i.getObjType()) {
                    case TOMATO -> type = 0;
                    case SALAD -> type = 1;
                    case PATTY -> type = 2;
                    case CHEESE -> type = 3;
                    case BAGEL -> type = 4;

                }
                if(type !=-1)
                    g.drawImage(objImages[type][i.getAnimationIndex()], (int) (i.getHitbox().x - i.getxDrawOffset() - xLvlOffset), (int) (i.getHitbox().y - i.getyDrawOffset()), OBJECT_WIDTH, OBJECT_HEIGHT,
                        null);
            }
    }

    public void resetAllObjects() {
        for (Ingredient i : ingredients)
            i.reset();
    }
}
