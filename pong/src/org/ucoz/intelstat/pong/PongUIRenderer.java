package org.ucoz.intelstat.pong;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PongUIRenderer implements UIControlHandler.Renderer {

	private Font buttonBigFont = new Font("Century Gothic", Font.BOLD, 50);
	private FontMetrics buttonBigFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(buttonBigFont);
	private Stroke buttonStroke = new BasicStroke(5);

	@Override
	public void render(Graphics2D g, UIControlHandler uich) {
		render_impl(g, uich.controls(), 0, 0);
	}

	private void render_impl(Graphics2D g, Component[] controls, int offx, int offy) {
		for (Component control : controls) {
			if (control instanceof JButton) {
				JButton button = (JButton) control;
				g.setStroke(buttonStroke);
				g.drawRect(offx + button.getX(), offy + button.getY(), button.getWidth(), button.getHeight());
				g.setFont(buttonBigFont);
				g.drawString(button.getText(),
						offx + button.getX() + (button.getWidth() - buttonBigFontMetrics.stringWidth(button.getText())) / 2,
						offy + button.getY() + (button.getHeight() + buttonBigFontMetrics.getAscent()) / 2 - 5);

			} else if (control instanceof JPanel) {
				render_impl(g, ((JPanel) control).getComponents(), control.getX(), control.getY());
			}
		}
	}
}
