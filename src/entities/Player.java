package entities;


import database.Database;
import gamestates.Playing;
import levels.LevelManager;
import main.Game;
import utils.LoadSave;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.sql.DriverManager;
import java.sql.SQLException;


import static utils.Constants.GRAVITY;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;
import static database.Database.*;
import static utils.LoadSave.getSpriteAtlas;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private Playing playing;
    private LevelManager levelManager;
    private Database database;
    private int aniSpeed=40;
    private  boolean up, right, down, jump;
    private boolean moving = false;
    private float playerSpeed = 1.0f * Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 12* Game.SCALE, yDrawOffset = 10*Game.SCALE;
    private float airSpeed = 0f; //falling and jumping
    private float jumpSpeed =-2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.03f*Game.SCALE;
    private boolean inAir = false;
    private int score = 0;

    //statBar
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);
    private int scoreY = (int) (20 * Game.SCALE);


    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);

    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    //attackBox
    private Rectangle2D.Float attackBox;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private boolean attacking;

    public  Player(float x, float y, int width, int height, Playing playing, Database database, LevelManager levelManager) {
        super(x,y, width, height);
        this.state = HOVER;
        this.playing = playing;
        this.database = database;
        this.levelManager = levelManager;
        loadAnimations();
        initHitbox(x,y, (int) (40* Game.SCALE),(int)(60* Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y, (int) (60* Game.SCALE) ,(int)(60* Game.SCALE));
    }

    public void update() {

        if(currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }

        checkScore();
        if(playing.isLevelCompleted()) {
            playing.setLevelCompleted(true);
            return;
        }

        updateHealthBar();
        updateAttackBox();
        updatePosition();
        if(attacking) 
            checkAttack();
        updateAnimation();
        setAnimation();

        if(moving) {
            checkIsTouchingIngredient();
        }

    }

    private void checkAttack() {
        if(attackChecked)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {

        attackBox.x = hitbox.x;
        attackBox.y = hitbox.y;

    }

    private void checkIsTouchingIngredient() {
        playing.checkIsIngredientTouched(hitbox);
    }

    public void render(Graphics g, int xLvlOffset) {
        g.drawImage(animations[state][animationIndex], (int)(hitbox.x - xDrawOffset) - xLvlOffset, (int)(hitbox.y - yDrawOffset), width, height, null);
        //drawHitbox(g, xLvlOffset);
        //drawAttackBox(g, xLvlOffset);
        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - xLvlOffset - (int)(xDrawOffset/2), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
        g.setColor(Color.BLUE);
        Font font= new Font("Helvetica", Font.BOLD, 20);
        g.setFont(font);

        g.drawString("SCORE: "+score, statusBarX + statusBarWidth*2  ,scoreY);

    }

    private void updateHealthBar() {
        healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
    }

    public void changeHealth(int value) {
        currentHealth += value;

        if(currentHealth <=0){
            currentHealth =0;
            //gameOver();
        } else if(currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }

    private void loadAnimations() {

        BufferedImage image = getSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[2][3];

        for(int j=0;j<animations.length;j++)
            for(int i=0;i<animations[j].length;i++)
                animations[j][i] = image.getSubimage(i*50,j*50, 50, 50);

        statusBarImg = getSpriteAtlas(LoadSave.STATUS_BAR);

    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!isEntityOnTheFloor(hitbox, lvlData))
            inAir = true;
    }

    private void updatePosition() {
        moving = false;

        if(jump)
            jump();
        
        if(!right && !inAir) {
            return;
        }

        float xSpeed=0;


        if(right) {
            xSpeed += playerSpeed;
        }

        if(!inAir) {
            if(!isEntityOnTheFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if(inAir) {
            if(canMoveHere(hitbox.x+xSpeed, hitbox.y+airSpeed,hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
               // hitbox.y= getEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);


                if(airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = 0; //fallSpeedAfterCollision;


            }
        } else {
            updateXPos(xSpeed);
        }
       moving = true;
    }

    private void jump() {
        if(inAir)
            return ;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed =0;
    }

    private void updateXPos(float xSpeed) {
        if(canMoveHere(hitbox.x+xSpeed, hitbox.y,hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;

        }
        else {
                //still a little space left
               hitbox.x = getEntityXPosNextToWall(hitbox,xSpeed);
            }
    }

    private void setAnimation() {
        int startAni = state;
        if (isJump())
            state = JUMP;
        else
            state = HOVER;

        if (startAni != state)
            resetAniTick();
    }

    private void updateAnimation() {
        animationTick++;
        if(animationTick >= aniSpeed)
        {
            animationTick =0;
            animationIndex++;
            if(animationIndex >= getSpriteAmount(state)) {
                animationIndex =0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void resetAniTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    public void changeScore(int value) {
        score += value;
        String sql = "INSERT INTO SCOREDB (SCORE)" + "VALUES (" + score + ")";
        database.setScoreInDatabase(value);

    }

    public void checkScore() {
        if(score >= 35 && levelManager.getLvlIndex() == 0)
            playing.setLevelCompleted(true);

        if(score >= 50 && levelManager.getLvlIndex() == 1)
            playing.setLevelCompleted(true);

        if(score >= 60 && levelManager.getLvlIndex() == 2)
            playing.setLevelCompleted(true);
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void resetDirBooleans() {
        right = false;
        up = false;
        down = false;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        moving = false;
        state = HOVER;
        attacking = false;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if (!isEntityOnTheFloor(hitbox, lvlData))
            inAir = true;

        score=0;
    }

    public void setAttacking(boolean b) {
        attacking = b;
    }
}
