package org.ucoz.intelstat.pong;

import java.awt.Graphics2D;

public class MainMenuStage extends GameStage {

	private UIControlHandler uich = new UIControlHandler();
	
	public MainMenuStage() {
		super("main-menu");
	}
	
	@Override
	public void onLoad(GameStage prevStage, int width, int height) {
		
	}
	
	@Override
	public void renderFrame(int time, int irfps, Graphics2D g) {
		
	}

	@Override
	public boolean update(int time, int iufps) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GameStage nextStage() {
		// TODO Auto-generated method stub
		return null;
	}

}