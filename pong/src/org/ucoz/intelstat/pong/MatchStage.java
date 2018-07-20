package org.ucoz.intelstat.pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class MatchStage extends GameStage {

	private int WIDTH, HEIGHT;
	private Player player1;
	private Player player2;
	private Player lastHitPlayer;

	private int winScore = 10;

	private final double BASE_BR = 10;
	private final double BASE_BVX = 500, BASE_BVY = 250;

	private Ellipse2D.Double ball;
	private Rectangle2D.Double ballAabb;
	private double br;
	private double bvx = BASE_BVX, bvy = BASE_BVY;

	private Font scoreFont = new Font("Century Gothic", Font.BOLD, 36);
	private FontMetrics scoreFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(scoreFont);

	public MatchStage() {
		super("match-stage", true, false);
	}

	@Override
	public void onLoad(GameStage prevStage, int width, int height) {
		WIDTH = width;
		HEIGHT = height;

		player1 = new Player(0, (height - Player.BASE_HEIGHT) / 2);
		player1.color = Color.RED;
		player2 = new Player(width - Player.BASE_WIDTH, (height - Player.BASE_HEIGHT) / 2);
		player2.color = Color.CYAN;

		ball = new Ellipse2D.Double(width / 2, height / 2, 0, 0);
		ballAabb = new Rectangle2D.Double(ball.x, ball.y, 2 * br, 2 * br);
		br(BASE_BR);
	}

	public void br(double newBr) {
		ball.x -= newBr - br;
		ball.y -= newBr - br;
		br = newBr;
		ball.width = 2 * br;
		ball.height = 2 * br;
		updateBallAabbLoc();
		updateBallAabbSize();
	}

	public double br() {
		return br;
	}

	public void resetBall(boolean resetSize) {
		ball.x = WIDTH / 2 - br;
		ball.y = HEIGHT / 2 - br;
		updateBallAabbLoc();
		if (resetSize) {
			br(BASE_BR);
			updateBallAabbSize();
		}
	}

	private void updateBallAabbLoc() {
		ballAabb.x = ball.x;
		ballAabb.y = ball.y;
	}

	private void updateBallAabbSize() {
		ballAabb.width = ball.width;
		ballAabb.height = ball.height;
	}

	@Override
	public void renderFrame(int time, int irfps, Graphics2D g) {
		// Players
		// for thread safety
		double p1x = player1.x(), p1y = player1.y(), p2x = player2.x(), p2y = player2.y();
		g.translate(p1x, p1y);
		g.setColor(player1.color);
		g.fill(player1.shape);

		g.translate(-p1x + p2x, -p1y + p2y);
		g.setColor(player2.color);
		g.fill(player2.shape);
		g.translate(-p2x, -p2y);

		// Ball
		g.setColor(lastHitPlayer == null ? Color.WHITE : lastHitPlayer.color);
		g.fill(ball);

		// Scores
		String colon = ":";
		String p1ScoreString = "" + player1.score;
		String p2ScoreString = "" + player2.score;
		g.setColor(Color.WHITE);
		g.setFont(scoreFont);
		g.drawString(colon, (WIDTH - scoreFontMetrics.stringWidth(colon)) / 2, scoreFontMetrics.getAscent() + 10);
		g.drawString(p1ScoreString, WIDTH / 2 - scoreFontMetrics.stringWidth(p1ScoreString) - 20,
				scoreFontMetrics.getAscent() + 10);
		g.drawString(p2ScoreString, WIDTH / 2 + 20, scoreFontMetrics.getAscent() + 10);
	}

	@Override
	public boolean update(int time, int iufpms) {
		// Move players
		if (player1.moving) {
			player1.y(player1.y() + player1.dir * player1.vy * iufpms / 1000);
		}
		if (player2.moving) {
			player2.y(player2.y() + player2.dir * player2.vy * iufpms / 1000);
		}

		// TODO: make this mess better
		// - calculate every collision with the same code
		// - reflection of ball takes face normal into account
		// Move ball
		// Check top and bottom
		ball.y += bvy * iufpms / 1000;
		if (ball.y < 0) {
			ball.y = 0;
			bvy = -bvy;
		} else if (ball.y + 2 * br > HEIGHT) {
			ball.y = HEIGHT - 2 * br;
			bvy = -bvy;
		}
		// System.out.println("BallAabb " + ballAabb);
		// System.out.println("Player1Aabb " + player1.aabb);
		// System.out.println("Player2Aabb " + player2.aabb);
		// Check left and right
		ball.x += bvx * iufpms / 1000;
		updateBallAabbLoc();
		// player 1 broad phase
		if (ball.x + br < WIDTH / 2) {
			if (ballAabb.intersects(player1.aabb)) {
				// narrow phase
				Area player1Area = new Area(player1.shape);
				Ellipse2D.Double tempBall = new Ellipse2D.Double(ball.x - player1.x(), ball.y - player1.y(), ball.width,
						ball.height);
				Area ballArea = new Area(tempBall);
				player1Area.intersect(ballArea);
				if (!player1Area.isEmpty()) { // hit for player 1
					ball.x = player1.aabb.width;
					bvx = -bvx;
					lastHitPlayer = player1;
				}

			}
			// goal for player 2
			else if (ball.x < player1.x() + player1.aabb.width) {
				player2.score++;
				resetBall(false);
			}
		} else {
			if (ballAabb.intersects(player2.aabb)) {
				// narrow phase
				Area player2Area = new Area(player2.shape);
				Ellipse2D.Double tempBall = new Ellipse2D.Double(ball.x - player2.x(), ball.y - player2.y(), ball.width,
						ball.height);
				Area ballArea = new Area(tempBall);
				player2Area.intersect(ballArea);
				if (!player2Area.isEmpty()) { // hit for player 2
					ball.x = WIDTH - player1.aabb.width - 2 * br;
					bvx = -bvx;
					lastHitPlayer = player2;
				}

			}
			// goal for player 1
			else if (ball.x > player2.x()) {
				player1.score++;
				resetBall(false);
			}
		}

		return false;
	}

	@Override
	public KeyListener keyListener() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
					player1.dir = -1;
					player1.moving = true;
					break;
				case KeyEvent.VK_S:
					player1.dir = 1;
					player1.moving = true;
					break;
				case KeyEvent.VK_UP:
					player2.dir = -1;
					player2.moving = true;
					break;
				case KeyEvent.VK_DOWN:
					player2.dir = 1;
					player2.moving = true;
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
					player1.moving = false;
					break;
				case KeyEvent.VK_S:
					player1.moving = false;
					break;
				case KeyEvent.VK_UP:
					player2.moving = false;
					break;
				case KeyEvent.VK_DOWN:
					player2.moving = false;
					break;
				}
			}
		};
	}

	@Override
	public GameStage nextStage() {
		// TODO Auto-generated method stub
		return null;
	}

	public class Player {
		public static final double BASE_WIDTH = 10;
		public static final double BASE_HEIGHT = 150;
		public static final double BASE_VX = 0;
		public static final double BASE_VY = 325;

		private int score;
		private boolean moving;
		private double vx = BASE_VX, vy = BASE_VY;
		private int dir;
		private Color color;

		private Shape shape;
		private Rectangle2D.Double aabb;
		// private HashSet<PlayerEffect> effects;

		private Player(double x, double y) {
			setShape(new Rectangle2D.Double(0, 0, BASE_WIDTH, BASE_HEIGHT));
			x(x);
			y(y);
		}

		public void setShape(Shape newShape) {
			shape = newShape;
			Rectangle2D bounds = shape.getBounds2D();
			aabb = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		}

		/**
		 * 
		 * @param newX
		 * @return true if newX is out of the enclosing MatchStage's bounds,
		 *         false otherwise
		 */
		public boolean x(double newX) {
			if (newX < 0) {
				aabb.x = 0;
				return true;
			}
			if (newX + aabb.getWidth() > WIDTH) {
				aabb.x = WIDTH - aabb.getWidth();
				return true;
			}
			aabb.x = newX;
			return false;
		}

		public double x() {
			return aabb.x;
		}

		public double y() {
			return aabb.y;
		}

		public boolean y(double newY) {
			if (newY < 0) {
				aabb.y = 0;
				return true;
			}
			if (newY + aabb.getHeight() > HEIGHT) {
				aabb.y = HEIGHT - aabb.getHeight();
				return true;
			}
			aabb.y = newY;
			return false;
		}

		public void width(double newWidth) {
			aabb.width = newWidth;
		}

		public void height(double newHeight) {
			aabb.height = newHeight;
		}
	}

}
