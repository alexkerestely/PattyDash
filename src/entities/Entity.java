package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

    protected float x,y;
    protected int width, height;
    protected int animationTick, animationIndex;
    protected Rectangle2D.Float hitbox;
    protected int state;
    public Entity (float x, float y, int width, int height) {
        this.x = x;
        this.y =y;
        this.width = width;
        this.height = height;


    }

    protected void drawHitbox(Graphics g, int xLvlOffset) {
        //For debugging the hitbox
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    protected void initHitbox(float x, float y, int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    public int getState() {
        return state;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
}
