package org.ucoz.intelstat.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	private final int WIDTH = 900, HEIGHT = 800;
	private final int PLAYER_WIDTH = 10, PLAYER_HEIGHT = 100;
	private final double PLAYER_SPEED = 5;
	private final double SPEED_UP = 1.2;

	private double py1, py2;
	private boolean isP1Moving, isP2Moving;
	private int p1d, p2d;
	private int p1p, p2p;

	private boolean goal;

	private double bx, by;
	private double bvx = 6, bvy = 3;
	private double bspeedup = 1;
	private final int BR = 10;

	private int ifps = 16;
	private int time;
	private int mt = -2000;
	private int st = 5000;

	private Thread gameLoop = new Thread(this);

	public GamePanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		addKeyListener(this);

		resetBall();
		gameLoop.start();
	}

	private void resetBall() {
		bx = WIDTH / 2;
		by = HEIGHT / 2;
	}

	Font scoreFont = new Font("Century Gothic", Font.BOLD, 30);
	Font goalFont = new Font("Century Gothic", Font.BOLD, 52);

	@Override
	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		Graphics2D g = (Graphics2D) gr;

		// Antialias
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Fekete hatter
		g.setBackground(Color.BLACK);
		g.clearRect(0, 0, WIDTH, HEIGHT);

		// Maga a jatek grafikaja
		g.setColor(Color.WHITE);
		// Jatekosok
		g.fillRect(0, (int) py1, PLAYER_WIDTH, PLAYER_HEIGHT);
		g.fillRect(WIDTH - PLAYER_WIDTH, (int) py2, PLAYER_WIDTH, PLAYER_HEIGHT);
		// Golyo
		g.fillOval((int) bx - BR, (int) by - BR, 2 * BR, 2 * BR);

		// Pontszam
		String p1Score = "" + p1p;
		String p2Score = "" + p2p;
		String colon = ":";
		g.setFont(scoreFont);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(p1Score, (WIDTH - 2*fm.stringWidth(p1Score) - 20) / 2, fm.getAscent() + 10);
		g.drawString(p2Score, (WIDTH + 20) / 2, fm.getAscent() + 10);
		g.drawString(colon, (WIDTH - fm.stringWidth(colon)) / 2, fm.getAscent() + 10);

		// Gyorsitas
		String speedUpString = "x" + bspeedup;
		g.drawString(speedUpString, WIDTH - fm.stringWidth(speedUpString) - 20, fm.getAscent()+10);
		
		// Ido
		if (mt == -1) {
			String timeString = "" + (time - st) / 1000;
			g.drawString(timeString, 20, fm.getAscent() + 10);
		}

		// Gol
		if (goal) {
			mt = time;
			goal = false;
		}
		if (mt != -1) {
			g.setFont(goalFont);
			fm = g.getFontMetrics();
			if (time - mt < 2000) {
				g.drawString("GOAL!", (WIDTH - fm.stringWidth("GOAL!")) / 2, 100);
			} else if (time - mt < 3000) {
				g.drawString("3", (WIDTH - fm.stringWidth("3")) / 2, 100);
			} else if (time - mt < 4000) {
				g.drawString("2", (WIDTH - fm.stringWidth("2")) / 2, 100);
			} else if (time - mt < 5000) {
				g.drawString("1", (WIDTH - fm.stringWidth("1")) / 2, 100);
			} else {
				mt = -1;
				st = time;
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				// Jatekosok mozgatasa
				if (isP1Moving) {
					py1 += p1d * PLAYER_SPEED;
					if (py1 < 0) {
						py1 = 0;
					} else if (py1 > HEIGHT - PLAYER_HEIGHT) {
						py1 = HEIGHT - PLAYER_HEIGHT;
					}
				}
				if (isP2Moving) {
					py2 += p2d * PLAYER_SPEED;
					if (py2 < 0) {
						py2 = 0;
					} else if (py2 > HEIGHT - PLAYER_HEIGHT) {
						py2 = HEIGHT - PLAYER_HEIGHT;
					}
				}
				// Labda mozgatasa
				if (mt == -1) {
					bx += bvx;
					by += bvy;
					// Fent-lent utkozes
					if (by - BR < 0) {
						by = BR;
						bvy = -bvy;
					} else if (by + BR > HEIGHT) {
						by = HEIGHT - BR;
						bvy = -bvy;
					}
					// Bal oldali utkozes
					if (bx - BR < PLAYER_WIDTH) {
						if (by + BR > py1 && by - BR < py1 + PLAYER_HEIGHT) {
							bx = PLAYER_WIDTH + BR;
							bvx = -bvx;
						} else { // Golsa
							p2p++;
							goal = true;
							mt = time;
							resetBall();
						}
					} else if (bx + BR > WIDTH - PLAYER_WIDTH) {
						if (by + BR > py2 && by - BR < py2 + PLAYER_HEIGHT) {
							bx = WIDTH - PLAYER_WIDTH - BR;
							bvx = -bvx;
						} else { // Gol
							p1p++;
							goal = true;
							mt = time;
							resetBall();
						}
					}
				}
				if ((time - st) % 20000 > 0 && (time - st) % 20000 <= ifps && (time - st) / 20000 > 0) {
					bvx *= SPEED_UP;
					bvy *= SPEED_UP;
					bspeedup *= SPEED_UP;
				}
				repaint();
				time += ifps;
				Thread.sleep(16);
			} catch (InterruptedException e) {

			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			isP1Moving = true;
			p1d = -1;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			isP1Moving = true;
			p1d = 1;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			isP2Moving = true;
			p2d = -1;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			isP2Moving = true;
			p2d = 1;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			isP1Moving = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			isP1Moving = false;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			isP2Moving = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			isP2Moving = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
