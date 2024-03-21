package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utils.Constants.EnemyConstants.*;
import static utils.HelpMethods.canMoveHere;

public class Fry extends Enemy {

    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;

    public Fry(float x, float y) {
        super(x, y ,FRY_WIDTH, FRY_HEIGHT, FRY);
        initHitbox(x,y,(int)(30* Game.SCALE),(int)(30*Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(50* Game.SCALE), (int)(30*Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE * 5);

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
                        System.out.println("here");
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
        g.drawRect((int) (attackBox.x - xLvlOffset - attackBoxOffsetX) , (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    public void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) (hitbox.x - xLvlOffset), (int) hitbox.y, (int) hitbox.width, (int) hitbox.height );
    }
}
