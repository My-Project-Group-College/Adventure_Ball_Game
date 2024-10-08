package utilities;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import gamestates.Playing;

public class LoadSave {

	public static Font Minecraft_Font;
	public static GraphicsEnvironment graphics_Environment;

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
	public static final String OPTIONS_MENU = "/MenusAndBars/options_background.png";
	public static final String LVL1_BACKGROUND = "/backgrounds/Background_1.png";
	public static final String LVL2_BACKGROUND = "/backgrounds/Background_2.png";
	public static final String BABLU_SPRITE = "/sprites/BabluEnemySprite.png";
	public static final String STATUS_BAR = "/MenusAndBars/health_power_bar.png";
	public static final String GAME_COMPLETED_IMG = "/MenusAndBars/game_completed_sprite.png";
	public static final String LEVEL_COMPLETED_IMG = "/MenusAndBars/completed_sprite.png";
	public static final String COIN_IMG = "/objectNcoin/coin.png";
	public static final String SPIKES_IMG = "/objectNcoin/spikes.png";
	public static final String DEATH_SCREEN = "/MenusAndBars/death_screen.png";
	public static final String MINECRAFT_FONT_FILE = "minecraft_font.tff";
	public static final String SCORES_FILE = "scores.txt";
	public static final String ICON = "icon.png";

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

	public static void CustomFontLoad() {
		try {
			Minecraft_Font = Font.createFont(Font.TRUETYPE_FONT, new File("minecraft_font.ttf"));
			graphics_Environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			graphics_Environment.registerFont(Minecraft_Font);
		} catch (IOException | FontFormatException e) {
			System.out.println("Error While Loading Font !");
			e.printStackTrace();
		}
	}

	public static Font GetMinecraft_Font() {
		return Minecraft_Font;
	}

	public static void WriteScores(Playing playing, int... highscores) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORES_FILE))) {
			writer.write(playing.getHighScore() + "\n");
			for (int h : highscores)
				writer.write(h + "\n");

		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
	}

	public static int[] ReadScores(int count) {
		int[] scores = new int[count];
		try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE))) {
			for (int i = 0; i < count; i++) {
				String line = reader.readLine();
				if (line == null) {
					throw new IOException("Unexpected end of file. Expected " + count + " scores, but found only " + i);
				}
				scores[i] = Integer.parseInt(line.trim());
			}
		} catch (IOException e) {
			System.err.println("Error reading from file: " + e.getMessage());
			return null;
		} catch (NumberFormatException e) {
			System.err.println("Invalid number format in file: " + e.getMessage());
			return null;
		}
		return scores;
	}

}
