package utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Bablu;
import main.Game;
import static utilities.Constants.EnemyConstants.*;

public class LoadSave {
	public static final String PLAYER_ATLAS = "/sprites/playerSprite.png";
	public static final String LEVEL_ATLAS = "/levels/ground_sprites.png";
	public static final String LEVEL_TEMP_DATA = "/levels/level_temp_data.png";
	public static final String LEVEL_ONE_DATA = "/levels/lvl1_data.png";
	public static final String LEVEL_TWO_DATA = "/levels/lvl2_data.png";
	public static final String MENU_BUTTONS = "/StartMenu/button_atlas.png";
	public static final String MENU_ICON = "/StartMenu/startMenu.png";
	public static final String MENU_BACKGROUND = "/StartMenu/menuBackground.png";
	public static final String PAUSE_BACKGROUND = "/PauseMenu/pause_menu.png";
	public static final String SOUND_BUTTONS = "/PauseMenu/sound_button.png";
	public static final String URM_BUTTONS = "/PauseMenu/urm_buttons.png";
	public static final String VOLUME_BUTTONS = "/PauseMenu/volume_buttons.png";
	public static final String LVL1_BACKGROUND = "/backgrounds/Background_1.png";
	public static final String BABLU_SPRITE = "/sprites/BabluEnemySprite.png";
	public static final String STATUS_BAR = "/HealthBar/health_power_bar.png";

	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream inpStream = LoadSave.class.getResourceAsStream(fileName);

		try {
			img = ImageIO.read(inpStream);

		} catch (IOException e) {
			System.out.println("Player Image Loading Error");
			e.printStackTrace();
		} finally {
			try {
				inpStream.close();
			} catch (IOException e) {
				System.out.println("Error While Closing Input Stream");
				e.printStackTrace();
			}
		}
		return img;
	}

	public static ArrayList<Bablu> GetBablu() {
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		ArrayList<Bablu> list = new ArrayList<Bablu>();

		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == BABLU)
					list.add(new Bablu(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
			}

		return list;
	}

	public static int[][] GetLevelData() {
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		int[][] lvlData = new int[img.getHeight()][img.getWidth()];

		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if (value >= 9)// size of levelSprite Image in LevelManager
					value = 0;
				lvlData[j][i] = value;
			}
		return lvlData;
	}
}
