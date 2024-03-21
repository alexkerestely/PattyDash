package utils;
import main.Game;
public class Constants {

    public static final float GRAVITY = 0.04f * Game.SCALE;

    public static class PlayerConstants {
        public static final int HOVER =0;
        public static final int JUMP =1;

        public static int getSpriteAmount(int playerAction)
        {
            switch(playerAction) {
                case HOVER:
                    return 3;
                    case JUMP:
                        return 2;
                default:
                    return 0;

            }
        }
    }

    public static class Directions {
        public static final int RIGHT =0;
        public static final int UP=1;
        public static final int DOWN =2;
        public static final int LEFT =3;
    }

    public static class UI {
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);

        }
    }

    public static class Environment {

        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;

        public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
    }

    public static class EnemyConstants {
        public static final int BURGER = 0;
        public static final int FRY = 1;
        public static final int JUICE = 2;
        public static final int IDLE =0;
        public static final int DEAD =1;
        public static final int ATTACK =2;

        public static final int BURGER_WIDTH_DEFAULT = 50;
        public static final int BURGER_HEIGHT_DEFAULT = 50;
        public static final int BURGER_WIDTH = (int)(BURGER_WIDTH_DEFAULT * Game.SCALE);
        public static final int BURGER_HEIGHT = (int)(BURGER_HEIGHT_DEFAULT * Game.SCALE);

        public static final int BURGER_DRAWOFFSET_X = (int)(10*Game.SCALE);
        public static final int BURGER_DRAWOFFSET_Y = (int)(10*Game.SCALE);

        public static final int FRY_WIDTH_DEFAULT = 50;
        public static final int FRY_HEIGHT_DEFAULT = 50;
        public static final int FRY_WIDTH = (int)(BURGER_WIDTH_DEFAULT * Game.SCALE);
        public static final int FRY_HEIGHT = (int)(BURGER_HEIGHT_DEFAULT * Game.SCALE);

        public static final int FRY_DRAWOFFSET_X = (int)(10*Game.SCALE);
        public static final int FRY_DRAWOFFSET_Y = (int)(20*Game.SCALE);

        public static final int JUICE_WIDTH_DEFAULT = 50;
        public static final int JUICE_HEIGHT_DEFAULT = 50;
        public static final int JUICE_WIDTH = (int)(JUICE_WIDTH_DEFAULT * Game.SCALE);
        public static final int JUICE_HEIGHT = (int)(JUICE_HEIGHT_DEFAULT * Game.SCALE);

        public static final int JUICE_DRAWOFFSET_X = (int)(20*Game.SCALE);
        public static final int JUICE_DRAWOFFSET_Y = (int)(17*Game.SCALE);

        public static int getSpriteAmount(int enemyType, int enemyState) {

            switch(enemyType) {
                case BURGER, FRY, JUICE -> {
                    return 4;
                }
            }
            return 0;

        }

        public static int getMaxHealth(int enemy_type) {
            switch (enemy_type) {
                case BURGER:
                    return 10;
                case FRY:
                    return 20;
                case JUICE:
                    return 30;
                default:
                    return 1;
            }
        }

        public static int getEnemyDamage(int enemy_type) {
            switch (enemy_type) {
                case BURGER:
                    return 15;
                case FRY:
                    return 25;
                case JUICE:
                    return 100;
                default:
                    return 0;
            }
        }


    }

    public static class ObjectConstants {
        public static final int PATTY = 0;
        public static final int BAGEL = 1;
        public static final int TOMATO = 2;
        public static final int SALAD = 3;
        public static final int CHEESE = 4;

        public static final int PATTY_VALUE = 20;
        public static final int BAGEL_VALUE = 5;
        public static final int TOMATO_VALUE = 10;
        public static final int SALAD_VALUE = 10;
        public static final int CHEESE_VALUE = 10;

        public static final int OBJECT_WIDTH_DEFAULT = 30;
        public static final int OBJECT_HEIGHT_DEFAULT = 30;
        public static final int OBJECT_WIDTH = (int) (Game.SCALE *  OBJECT_WIDTH_DEFAULT);
        public static final int OBJECT_HEIGHT = (int) (Game.SCALE * OBJECT_HEIGHT_DEFAULT);

        public static int getSpriteAmount(int object_type) {
            return 4;
        }
    }
}
