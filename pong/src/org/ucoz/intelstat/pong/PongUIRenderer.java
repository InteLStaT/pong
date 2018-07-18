package org.ucoz.intelstat.pong;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.util.HashSet;

import org.ucoz.intelstat.pong.UIControlHandler.Button;
import org.ucoz.intelstat.pong.UIControlHandler.Control;
import org.ucoz.intelstat.pong.UIControlHandler.Panel;

public class PongUIRenderer implements UIControlHandler.Renderer {

	private Font defaultFont = new Font("Century Gothic", Font.BOLD, 32);
	private FontMetrics defaultFontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(defaultFont);
	private Stroke buttonStroke = new BasicStroke(5);

	@Override
	public void render(Graphics2D g, HashSet<Control> controls) {
		render_impl(g, controls, 0, 0);
	}

	private void render_impl(Graphics2D g, HashSet<Control> controls, int offx, int offy) {
		for (Control control : controls) {
			if (control instanceof Panel) {
				render_impl(g, ((Panel) control).children(), control.posx, control.posy);
			} else if (control instanceof Button) {
				Button button = (Button) control;
				g.setStroke(buttonStroke);
				g.drawRect(offx + button.posx, offy + button.posy, button.width, button.height);
				g.setFont(defaultFont);
				g.drawString(button.text(),
						offx + button.posx + (button.width - defaultFontMetrics.stringWidth(button.text())) / 2,
						offy + button.posy + (button.height + defaultFontMetrics.getAscent()) / 2);
			}
		}
	}
}
