package org.ucoz.intelstat.pong;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.HashMap;

public abstract class GameStage {
	
	private static int stageCount = 0;
	private static final HashMap<String, GameStage> ALL_STAGES = new HashMap<>();
	
	public static final GameStage EMPTY_STAGE = new GameStage("EMPTY-STAGE") {
		
		@Override
		public boolean update(int time, int iufps) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void renderFrame(int time, int irfps, Graphics2D g) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLoad(GameStage prevStage, int width, int height) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public GameStage nextStage() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	public final int STAGE_ID;
	public final String STAGE_NAME;
	
	public GameStage(String stageName) {
		STAGE_ID = stageCount++;
		STAGE_NAME = stageName;
		ALL_STAGES.put(stageName, this);
	}
	
	public abstract void onLoad(GameStage prevStage, int width, int height);
	
	public abstract void renderFrame(int time, int irfps, Graphics2D g);
	
	public abstract boolean update(int time, int iufps);
	
	public abstract GameStage nextStage();
	
	public KeyListener keyListener() {
		return null;
	}
	
	public MouseListener mouseListener() {
		return null;
	}
	
	public static GameStage byName(String name) {
		return ALL_STAGES.get(name);
	}
	
}
