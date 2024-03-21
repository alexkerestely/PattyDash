package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class LoadSave {
    public static final String PLAYER_ATLAS =  "dashSpriteSheet.png";
    public static final String LEVEL_ATLAS = "level_sprites.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.jpg";
    public static final String LVL1_BG_IMG = "level1bckg.jpg";
    public static final String LVL2_BG_IMG = "level2bckg.jpg";
    public static final String LVL3_BG_IMG = "level3bckg.jpg";
    public static final String GAME_OVER_IMG = "gameOver.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String COMPLETED_IMG = "levelCompleted.png";
    public static final String ENEMY1_SPRITE = "enemy1sprite.png";
    public static final String ENEMY2_SPRITE = "enemy2sprite.png";
    public static final String ENEMY3_SPRITE = "enemy3sprite.png";
    public static final String OBJECTS_SPRITE = "objSprites.png";
    public static final String STATUS_BAR = "health_power_bar.png";

    public static BufferedImage getSpriteAtlas(String fileName) {
        BufferedImage image = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);

        try {
            image = ImageIO.read(is);

        } catch (IOException e ){
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public static BufferedImage[] getAllLevels() throws URISyntaxException {
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File[] files = file.listFiles();
        File[] filesSorted= new File[files.length];

        for(int i=0;i<filesSorted.length;i++)
            for(int j=0;j<files.length;j++)
                if(files[j].getName().equals(""+(i+1)+".png"))
                    filesSorted[i]= files[j];

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for(int i=0;i<imgs.length;i++) {
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return imgs;
    }
}
