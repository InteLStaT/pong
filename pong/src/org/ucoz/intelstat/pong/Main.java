package org.ucoz.intelstat.pong;

import javax.swing.JFrame;

public class Main {

	public static final String GAME_TITLE = "Pong";
	private final static GamePanel GAME_PANEL = new GamePanel(900, 800, 8, 16);
	
	public static void main(String[] args) {
		JFrame f = new JFrame(GAME_TITLE);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(GAME_PANEL);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		GAME_PANEL.setStage(new MainMenuStage());
		GAME_PANEL.start();
	}
	
}
