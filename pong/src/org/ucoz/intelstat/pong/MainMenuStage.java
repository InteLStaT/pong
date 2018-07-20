package org.ucoz.intelstat.pong;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenuStage extends GameStage {

	private UIControlHandler uich;
	private UIControlHandler.Renderer uir = new PongUIRenderer();
	private GameStage nextStage = null;
	
	private int width, height;
	
	private Font titleFont = new Font("Century Gothic", Font.BOLD, 72);
	private FontMetrics titleFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(titleFont);
	
	public MainMenuStage() {
		super("main-menu", false, true);
	}
	
	@Override
	public void onLoad(GameStage prevStage, int width, int height) {
		this.width = width;
		this.height = height;
		
		uich = new UIControlHandler(width, height);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setLocation(width/3, height*2/5);
		panel.setSize(new Dimension(width/3, height*3/5));
		
		JButton btnNewGame = new JButton("New game");
		btnNewGame.setLocation(0, 0);
		btnNewGame.setSize(new Dimension(width/3, 100));
		btnNewGame.addActionListener((e) -> {
			nextStage = new MatchStage();
		});
		panel.add(btnNewGame);
		
		JButton btnShowInstructions = new JButton("Controls");
		btnShowInstructions.setLocation(0, 125);
		btnShowInstructions.setSize(new Dimension(width/3, 100));
		btnShowInstructions.addActionListener((e) -> {
			nextStage = GameStage.byName("instructions-stage");
			if(nextStage == null) {
				nextStage = new InstructionsStage();
			}
		});
		panel.add(btnShowInstructions);
		
		uich.addControl(panel);
	}
	
	@Override
	public void renderFrame(int time, int irfps, Graphics2D g) {
		String title = "Pong";
		g.setFont(titleFont);
		g.drawString(title, (width - titleFontMetrics.stringWidth(title)) / 2, height/5);
		
		uir.render(g, uich);
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
		GameStage nextStage = this.nextStage;
		this.nextStage = null;
		return nextStage;
	}

	@Override
	public MouseListener mouseListener() {
		return uich.mouseListener();
	}
	
	@Override
	public MouseMotionListener mouseMotionListener() {
		return uich.mouseListener();
	}
	
}
