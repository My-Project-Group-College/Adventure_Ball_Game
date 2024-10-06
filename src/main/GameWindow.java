package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import utilities.LoadSave;

public class GameWindow {

	JFrame frame;

	public GameWindow(GamePanel gamePanel) {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(500, 500);
		frame.setResizable(false);

		frame.add(gamePanel);
		ImageIcon icon = new ImageIcon(LoadSave.ICON);
		frame.setIconImage(icon.getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {

			}
		});
	}
}
