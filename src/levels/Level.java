package levels;

import entities.Burger;
import entities.Fry;
import entities.Juice;
import main.Game;
import objects.Ingredient;
import utils.HelpMethods;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.HelpMethods.*;


public class Level {

    private int[][] lvlData;
    private BufferedImage img;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Burger> burgers;
    private ArrayList<Fry> fries;
    private ArrayList<Juice> juices;
    private int lvlTilesWide ;//= LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset ;//= lvlTilesWide - Game.TILE_IN_WIDTH;
    private int maxLvlOffsetX ;//= maxTilesOffset* Game.TILES_SIZE;


    public Level(BufferedImage img) {
        this.img = img;
        createLvlData();
        createEnemies();
        calcLvlOffsets();
        createIngredients();
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILE_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        burgers = GetBurgers(img);
        fries = GetFries(img);
        juices = GetJuices(img);
    }

    private void createLvlData() {
       lvlData = GetLevelData(img);
    }

    private void createIngredients() {
        ingredients = HelpMethods.getIngredients(img);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredients;
    }

    public int getMaxLvlOffset() {
        return maxLvlOffsetX;
    }

    public ArrayList<Burger> getBurgers() {
        return burgers;
    }

    public ArrayList<Fry> getFries() {
        return fries;
    }

    public ArrayList<Juice> getJuices() {
        return juices;
    }
}

