package utils;

import entities.Burger;
import entities.Fry;
import entities.Juice;
import main.Game;
import objects.Ingredient;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.ObjectConstants.*;

public class HelpMethods {

    public static boolean canMoveHere(float x, float y, float width, float height, int[][] lvlData) {

        for(float i=x;i<=x+width;i++)  //bottom
            if(isSolid(i, y+height, lvlData))
                return false;

        for(float i=y;i<=y+height;i++)  //right
            if(isSolid(x+width, i, lvlData))
                return false;

        for(float i=y;i<=y+height;i++)  //left
            if(isSolid(x, i, lvlData))
                return false;

        for(float i=x;i<=x+width;i++)  //up
            if(isSolid(i, y, lvlData))
                return false;

        /*
        if(!isSolid(x,y,lvlData)){
            //top left
            if(!isSolid(x+width, y+height, lvlData))  // bottom right
                if(!isSolid(x+width, y, lvlData))  // top right
                    if(!isSolid(x, y+height, lvlData))  // bottom left
                        return true;
        }
        return false;*/
        return true;

    }

    private  static boolean isSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length*Game.TILES_SIZE;
        if(x<0 || x >= maxWidth)
            return true;

        if(y<0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x/ Game.TILES_SIZE;
        float yIndex = y/ Game.TILES_SIZE;

        return isTileSolid((int)xIndex, (int)yIndex, lvlData);
    }

    public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int)(hitbox.x / Game.TILES_SIZE);
        if(xSpeed > 0) {
            //Right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int)(Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            //Left
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float getEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox,float airSpeed){
        int currentTile = (int)(hitbox.y / Game.TILES_SIZE);
        if(airSpeed > 0) {
            //Falling
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset =  (int)(Game.TILES_SIZE - hitbox.height);
            return tileYPos +yOffset -1;
        }else {
            //Jumping
            return currentTile * Game.TILES_SIZE;

        }
    }

    public static boolean isEntityOnTheFloor(Rectangle2D.Float hitbox, int[][] lvlData) {

        //check the pixel below bottom left and bottom right
        if(!isSolid(hitbox.x, hitbox.y+hitbox.height+1, lvlData) )  //bottom left
            if(!isSolid(hitbox.x+hitbox.width, hitbox.y+hitbox.height+1, lvlData)) //bottom right
                return false;
        return true;
    }

    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if(xSpeed > 0)
            return isSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);  // hitbox.width
        else
            return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean isTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];

        if(value >=0 && value <6 ) {   // val = 0->5 solid
            return true;
        }
        return false;
    }

    public static boolean areAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (isTileSolid(xStart + i, y, lvlData))
                return false;
            if (isTileSolid(xStart + i, y + 1, lvlData))
                return false;
        }

        return true;
    }

    public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float hitbox1, Rectangle2D.Float hitbox2, int tileY) {

        int tileX1 = (int) (hitbox1.x / Game.TILES_SIZE);
        int tileX2 = (int) (hitbox2.x / Game.TILES_SIZE);

        if (tileX1 > tileX2)
            return areAllTilesWalkable(tileX2, tileX1, tileY, lvlData);
        else
            return areAllTilesWalkable(tileX1, tileX2, tileY, lvlData);
    }



    public static ArrayList<Ingredient> getIngredients(BufferedImage img) {

        ArrayList<Ingredient> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                int type =-1;
                switch (value) {
                    case 0 -> type = TOMATO;
                    case 1 -> type = SALAD;
                    case 2 -> type = PATTY;
                    case 3 -> type = CHEESE;
                    case 4 -> type = BAGEL;

                }
                if(type!=-1)
                    list.add(new Ingredient(i * Game.TILES_SIZE, j * Game.TILES_SIZE, type));
            }
        return list;

    }

    public static int[][] GetLevelData(BufferedImage img) {

        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 6){
                    value = 6;
                }

                lvlData[j][i] = value;
                //  System.out.print(value + " ");
            }
            //   System.out.println();
        }
        return lvlData;
    }

    public static ArrayList<Burger> GetBurgers(BufferedImage img) {

        ArrayList<Burger> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == BURGER)
                    list.add(new Burger(i * Game.TILES_SIZE, j * Game.TILES_SIZE));

            }
        return list;

    }

    public static ArrayList<Fry> GetFries(BufferedImage img) {

        ArrayList<Fry> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == FRY)
                    list.add(new Fry(i * Game.TILES_SIZE, j * Game.TILES_SIZE));

            }
        return list;

    }

    public static ArrayList<Juice> GetJuices(BufferedImage img) {

        ArrayList<Juice> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == JUICE)
                    list.add(new Juice(i * Game.TILES_SIZE, j * Game.TILES_SIZE));

            }
        return list;

    }

}
