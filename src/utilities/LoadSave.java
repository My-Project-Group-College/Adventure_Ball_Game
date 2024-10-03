package utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Bablu;
import main.Game;
import static utilities.Constants.EnemyConstants.*;

public class LoadSave {
	public static final String PLAYER_ATLAS = "/sprites/playerSprite.png";
	public static final String LEVEL_ATLAS = "/sprites/ground_sprites.png";
	public static final String LEVEL_ONE_DATA = "/lvls/lvl1_data.png";
	public static final String MENU_BUTTONS = "/MenusAndBars/button_atlas.png";
	public static final String MENU_ICON = "/MenusAndBars/startMenu.png";
	public static final String MENU_BACKGROUND = "/MenusAndBars/menuBackground.png";
	public static final String PAUSE_BACKGROUND = "/MenusAndBars/pause_menu.png";
	public static final String SOUND_BUTTONS = "/MenusAndBars/sound_button.png";
	public static final String URM_BUTTONS = "/MenusAndBars/urm_buttons.png";
	public static final String VOLUME_BUTTONS = "/MenusAndBars/volume_buttons.png";
	public static final String LVL1_BACKGROUND = "/backgrounds/Background_1.png";
	public static final String LVL2_BACKGROUND = "/backgrounds/Background_2.png";
	public static final String BABLU_SPRITE = "/sprites/BabluEnemySprite.png";
	public static final String STATUS_BAR = "/MenusAndBars/health_power_bar.png";
	public static final String COMPLETED_IMG = "/MenusAndBars/completed_sprite.png";
	public static final String COIN_IMG = "/objectNcoin/coin.png";
	public static final String SPIKES_IMG = "/objectNcoin/spikes.png";

	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream inpStream = LoadSave.class.getResourceAsStream(fileName);
		if (inpStream == null)
			System.out.println("Input Stream in GetSpriteAtlas not loaded file");
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

	public static BufferedImage[] GetAllLevels() {
		URL url = LoadSave.class.getResource("/lvls"); // Levels Folder
		File file = null;

		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		File[] files = file.listFiles();

		File[] filesSorted = new File[files.length];

		for (int i = 0; i < filesSorted.length; i++)
			for (int j = 0; j < files.length; j++) {
				if (files[j].getName().contains((String) String.valueOf(i + 1)))
					filesSorted[i] = files[j];
			}

//		for (File f : filesSorted)
//			System.out.println("File: " + f.getName()); // to check if level names are in proper order

		BufferedImage[] imgs = new BufferedImage[filesSorted.length];

		for (int i = 0; i < imgs.length; i++)
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return imgs;
	}

//	public static ArrayList<Bablu> GetBablu() {
//		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
//		ArrayList<Bablu> list = new ArrayList<Bablu>();
//
//		for (int j = 0; j < img.getHeight(); j++)
//			for (int i = 0; i < img.getWidth(); i++) {
//				Color color = new Color(img.getRGB(i, j));
//				int value = color.getGreen();
//				if (value == BABLU)
//					list.add(new Bablu(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
//			}
//
//		return list;
//	}
//
//	public static int[][] GetLevelData() {
//		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
//		int[][] lvlData = new int[img.getHeight()][img.getWidth()];
//
//		for (int j = 0; j < img.getHeight(); j++)
//			for (int i = 0; i < img.getWidth(); i++) {
//				Color color = new Color(img.getRGB(i, j));
//				int value = color.getRed();
//				if (value >= 9)// size of levelSprite Image in LevelManager
//					value = 0;
//				lvlData[j][i] = value;
//			}
//		return lvlData;
//	}
}
