package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.EnemyConstants.getSpriteAmount;
import static utils.HelpMethods.*;
import static utils.Constants.Directions.*;
import static utils.Constants.GRAVITY;

public abstract class Enemy extends Entity {

    protected int enemyType;
    protected int animationSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir=false;
    protected float fallSpeed;

    protected float walkSpeed = 0.35f * Game.SCALE;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;

    protected int maxHealth;
    protected  int currentHealth;
    protected  boolean active = true;

    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x,y,width, height);
        initHitbox(x,y,width, height);
        this.enemyType = enemyType;
        maxHealth = getMaxHealth(enemyType);
        currentHealth = maxHealth;
    }

    public void hurt(int amount) {
        currentHealth -= amount;
        if(currentHealth <= 0)
            newState(DEAD);

    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) { //enemy gives damage to player
        if(attackBox.intersects(player.hitbox))
            player.changeHealth(-getEnemyDamage(enemyType));
        attackChecked = true;
    }

    protected void updateAnimationTick() {
        animationTick++;
        if(animationTick >= animationSpeed)
        {
            animationTick =0;
            animationIndex++;
            if(animationIndex >= getSpriteAmount(enemyType, state)) {
                animationIndex =0;

                if(state == ATTACK)
                    state = IDLE;
                else if(state == DEAD)
                    active = false;
            }
        }
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if (!isEntityOnTheFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData) {
        if (canMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += fallSpeed;
            fallSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            tileY = (int) ((hitbox.y + hitbox.height) / Game.TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData) {
        float xSpeed = 0;

        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (isFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }

        changeWalkDir();
    }


    private void changeWalkDir() {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;

    }

    protected void newState(int enemyState) {
        this.state = enemyState;
        animationTick = 0;
        animationIndex = 0;
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int) ((player.getHitbox().y + player.getHitbox().height) / Game.TILES_SIZE);
        if (playerTileY == tileY)
            if (isPlayerInRange(player)) {
                if (isSightClear(lvlData, hitbox, player.hitbox, tileY))
                    return true;
            }

        return false;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x + player.hitbox.height - hitbox.x - hitbox.height);
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x + player.hitbox.height - hitbox.x - hitbox.height);
        return absValue <= attackDistance;
    }

    protected void turnTowardsPlayer(Player player) {
        if (player.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }


    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        newState(IDLE);
        fallSpeed = 0;
        currentHealth = maxHealth;
        active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
