package org.ucoz.intelstat.pong;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class InstructionsStage extends GameStage {

	private UIControlHandler uich;
	private UIControlHandler.Renderer uir = new PongUIRenderer();
	private GameStage nextStage = null;

	private int width, height;

	private Font titleFont = new Font("Century Gothic", Font.BOLD, 72);
	private FontMetrics titleFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(titleFont);
	private Font textFont = new Font("Century Gothic", 0, 40);
	private FontMetrics textFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(textFont);

	public InstructionsStage() {
		super("instructions-stage", false, true);
	}

	@Override
	public void onLoad(GameStage prevStage, int width, int height) {
		this.width = width;
		this.height = height;

		uich = new UIControlHandler(width, height);

		JButton btnBackToMenu = new JButton("Back to menu");
		btnBackToMenu.setLocation(width / 12, height * 7 / 8);
		btnBackToMenu.setSize(350, 75);
		btnBackToMenu.addActionListener((e) -> {
			nextStage = GameStage.byName("main-menu");
		});

		uich.addControl(btnBackToMenu);
	}

	@Override
	public void renderFrame(int time, int irfps, Graphics2D g) {
		String title = "Controls";
		g.setFont(titleFont);
		g.drawString(title, (width - titleFontMetrics.stringWidth(title)) / 2, height / 5);

		String[] instructions = { "Player 1: W, S", "Player 2: up arrow, down arrow",
				"Don't let the ball touch your side.", "Hit power-ups for special effects." };
		g.setFont(textFont);
		for (int i = 0; i < instructions.length; i++) {
			String instruction = instructions[i];
			g.drawString(instruction, (width - textFontMetrics.stringWidth(instructions[2])) / 2,
					height * 2 / 5 + i * textFontMetrics.getHeight());
		}

		uir.render(g, uich);
	}

	@Override
	public boolean update(int time, int iufps) {
		if (nextStage != null) {
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

}
