package entities;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.EnemyConstants.*;
public class Juice extends Enemy {

    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Juice(float x, float y) {
        super(x, y,JUICE_WIDTH, JUICE_HEIGHT, JUICE);
        initHitbox(x,y,(int)(20* Game.SCALE),(int)(30*Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(35* Game.SCALE),(int)(30*Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE *20);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (state) {
                case IDLE:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }

                    move(lvlData);
                    break;

                case ATTACK:
                    if(animationIndex == 0)
                        attackChecked = false;
                    if(!attackChecked && animationIndex == 2)
                        checkPlayerHit(attackBox, player);
                    break;

            }
        }

    }

    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset + JUICE_DRAWOFFSET_X) , (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK); // Y - JUICE OFFSET
        g.drawRect((int) hitbox.x - xLvlOffset + JUICE_DRAWOFFSET_X , (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
}
