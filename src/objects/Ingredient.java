package objects;


import main.Game;

public class Ingredient extends GameObject {

    public Ingredient(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true;

        initHitbox(30, 30);

        xDrawOffset = (int) (2 * Game.SCALE);
        yDrawOffset = (int) (4 * Game.SCALE);

    }

    public void update() {
        updateAnimationTick();
    }
}
