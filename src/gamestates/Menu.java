package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import utilities.LoadSave;
import static utilities.Constants.UI.Buttons.*;

public class Menu extends State implements StateMethods {
	private MenuButton[] buttons = new MenuButton[3];
	private boolean option = false;
	private BufferedImage backgroundImg;
	private BufferedImage menuIconImg;
	private int menuX, menuY, menuWidth, menuHeight, buttonXOffset, buttonInitialX, buttonYPos;

	public Menu(Game game) {
		super(game);
		loadButtons();
		loadBackgroundAndIcon();
	}

	private void loadBackgroundAndIcon() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);

		menuIconImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_ICON);
		menuWidth = (int) (menuIconImg.getWidth() * Game.SCALE);
		menuHeight = (int) (menuIconImg.getHeight() * Game.SCALE);
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (15 * Game.SCALE);
	}

	private void loadButtons() {
		buttonInitialX = (int) (Game.GAME_WIDTH / 2 - B_WIDTH * 1.2);
		buttonXOffset = (int) (B_WIDTH + 25 * Game.SCALE);
		buttonYPos = (int) (263 * Game.SCALE);
		buttons[0] = new MenuButton(buttonInitialX, buttonYPos, 0, GameState.PLAYING);
		buttons[1] = new MenuButton(buttonInitialX + buttonXOffset, buttonYPos, 1, GameState.OPTIONS);
		buttons[2] = new MenuButton(buttonInitialX + 2 * buttonXOffset, buttonYPos, 2, GameState.QUIT);
	}

	@Override
	public void update() {
		for (MenuButton mb : buttons)
			mb.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(menuIconImg, menuX, menuY, menuWidth, menuHeight, null);
		for (MenuButton mb : buttons) {
			if (!option && mb.getRowIndex() == 1)
				continue;
			mb.draw(g);
		}
	}

	private void resetButtons() {
		for (MenuButton mb : buttons)
			mb.resetBools();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons)
			if (isIn(e, mb)) {
				if (!option && mb.getRowIndex() == 1)
					continue;
				mb.setMousePressed(true);
				break;
			}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons)
			if (isIn(e, mb)) {
				if (!option && mb.getRowIndex() == 1)
					continue;
				if (mb.isMousePressed())
					mb.applyGamestate();
				break;
			}
		if (buttons[0].isMousePressed()) {
			game.getPlaying().resetAll();
			game.getPlaying().unpauseGame();
		}
		resetButtons();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons)
			mb.setMouseOver(false);

		for (MenuButton mb : buttons)
			if (isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			GameState.state = GameState.PLAYING;
		else if (e.getKeyCode() == KeyEvent.VK_O)
			GameState.state = GameState.OPTIONS;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
