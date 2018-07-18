package org.ucoz.intelstat.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

	public static final int MIN_WIDTH = 250, MIN_HEIGHT = 250;
	public static final int MIN_IUFPS = 8, MIN_IRFPS = 8;
	
	public final int WIDTH, HEIGHT;
	public final int IUFPS, IRFPS;
	private boolean started = false;
	private int time;
	
	private final BufferedImage osc;
	private final Graphics2D g;
	private final Color BG_COLOR = Color.BLACK;

	private Thread gameLoop = new Thread(this);

	public GamePanel(int width, int height, int iufps, int irfps) {
		WIDTH = width < MIN_WIDTH ? MIN_WIDTH : width;
		HEIGHT = width < MIN_HEIGHT ? MIN_HEIGHT : height;
		IUFPS = iufps < MIN_IUFPS ? MIN_IUFPS : iufps;
		IRFPS = irfps < MIN_IRFPS ? MIN_IRFPS : irfps;
		
		osc = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = osc.createGraphics();
		g.setBackground(BG_COLOR);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	@Override
	public void paintComponent(Graphics gp) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		
		// TODO: fill in with stage gfx
		gp.drawImage(osc, 0, 0, null);
	}

	public void start() {
		if(started) {
			throw new IllegalStateException("game already started");
		}
		gameLoop.start();
	}
	
	@Override
	public void run() {
		while (true) {
			time += IUFPS;
		}
	}

}
