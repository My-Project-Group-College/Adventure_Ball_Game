package utilities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import main.Game;

public class LoadSave 
{
	public static final String PLAYER_ATLAS = "/sprites/playerSprite.png";
	public static final String LEVEL_ATLAS = "/levels/ground_sprites.png";
	public static final String LEVEL_ONE_DATA = "/levels/level_temp_data.png";//add level 1 data
	
	public static BufferedImage GetSpriteAtlas(String fileName)
	{
		BufferedImage img = null;
		InputStream inpStream = LoadSave.class.getResourceAsStream(fileName);
		
		try 
		{
			img = ImageIO.read(inpStream);
			
			
		} 
		catch (IOException e) 
		{
			System.out.println("Player Image Loading Error");
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				inpStream.close();
			} 
			catch (IOException e) 
			{
				System.out.println("Error While Closing Input Stream");
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static int[][] GetLevelData()
	{
		int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) 
			{
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if (value >= 9)//size of levelSprite Image in LevelManager
					value = 0;
				lvlData[j][i] = value;
			}
		return lvlData;
	}
}
