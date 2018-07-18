package org.ucoz.intelstat.pong;

import java.awt.Graphics2D;

public class MatchStage extends GameStage {

	public MatchStage() {
		super("match-stage");
	}

	private int WIDTH, HEIGHT;
	
	@Override
	public void onLoad(GameStage prevStage, int width, int height) {
		WIDTH = width;
		HEIGHT = height;
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
