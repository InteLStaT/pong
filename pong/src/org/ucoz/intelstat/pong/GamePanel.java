package org.ucoz.intelstat.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

	public static final int MIN_WIDTH = 250, MIN_HEIGHT = 250;
	public static final int MIN_IUFPS = 8, MIN_IRFPS = 8;
	
	public final int WIDTH, HEIGHT;
	public final int IUFPMS, IRFPS; // read: inverse of update/render frames per millisecond (update/render milliseconds per frame)
	private boolean started = false;
	private int time;
	
	private final BufferedImage osc;
	private final Graphics2D g;
	private final Color BG_COLOR = Color.BLACK;
	
	private GameStage currentStage;

	private Thread gameLoop;
	private long lastRealNanoTime;
	private long timeToSleep;

	public GamePanel(int width, int height, int iufps, int irfps) {
		WIDTH = width < MIN_WIDTH ? MIN_WIDTH : width;
		HEIGHT = width < MIN_HEIGHT ? MIN_HEIGHT : height;
		IUFPMS = iufps < MIN_IUFPS ? MIN_IUFPS : iufps;
		IRFPS = irfps < MIN_IRFPS ? MIN_IRFPS : irfps;
		
		osc = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = osc.createGraphics();
		g.setBackground(BG_COLOR);
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	public void start() {
		if(started) {
			throw new IllegalStateException("game already started");
		}
		started = true;
		gameLoop = new Thread(this);
		gameLoop.start();
	}
	
	public void stop() {
		started = false;
	}
	
	/**
	 * Warning: not thread-safe
	 */
	public void setStage(GameStage newStage) {
		currentStage = newStage;
	}
	
	@Override
	public void paintComponent(Graphics gp) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		currentStage.renderFrame(time, IRFPS, g);
		// TODO: fill in with stage gfx
		gp.drawImage(osc, 0, 0, null);
	}
	
	@Override
	public void run() {
		long IUFPNS = IUFPMS * 1_000_000; // read: inverse of update frames per nanosecond (update nanoseconds per frame)
		boolean advanceStage;
		while (started) {
			lastRealNanoTime = System.nanoTime();
			time += IUFPMS;
			
			advanceStage = currentStage.update(time, IUFPMS);
			repaint(); // TODO: this is to be moved to a render loop
			if(advanceStage) {
				GameStage nextStage = currentStage.nextStage();
				if(nextStage != null) {
					currentStage = nextStage;
				}
			}
			
			
			try {
				timeToSleep = IUFPNS - (System.nanoTime() - lastRealNanoTime);
				if(timeToSleep < 0) {
					timeToSleep = 0;
				}
				
				TimeUnit.NANOSECONDS.sleep(timeToSleep);
			} catch(InterruptedException e) {
				
			}
		}
	}

}
