package org.ucoz.intelstat.pong;

import java.awt.Graphics2D;
import java.awt.event.MouseListener;

import org.ucoz.intelstat.pong.UIControlHandler.Button;
import org.ucoz.intelstat.pong.UIControlHandler.Panel;

public class MainMenuStage extends GameStage {

	private UIControlHandler uich = new UIControlHandler();
	private UIControlHandler.Renderer uir = new PongUIRenderer();
	private GameStage nextStage = null;
	
	public MainMenuStage() {
		super("main-menu");
	}
	
	@Override
	public void onLoad(GameStage prevStage, int width, int height) {
		Panel panel = new Panel(width/3, height/3, width/3, height*2/3);
		Button btnNewGame = new Button(0, 0, width/3, 150, "New Game");
		btnNewGame.setAction((e) -> {
			// TODO: set nextStage to a new game stage thing
			nextStage = new MatchStage();
		});
		panel.addControl(btnNewGame);
		uich.addControl(panel);
	}
	
	@Override
	public void renderFrame(int time, int irfps, Graphics2D g) {
		uir.render(g, uich.controls());
	}

	@Override
	public boolean update(int time, int iufps) {
		if(nextStage != null) {
			return true;
		}
		return false;
	}

	@Override
	public GameStage nextStage() {
		return nextStage;
	}

	@Override
	public MouseListener mouseListener() {
		return uich.mouseListener();
	}
	
}
