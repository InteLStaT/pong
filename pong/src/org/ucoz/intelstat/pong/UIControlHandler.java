package org.ucoz.intelstat.pong;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class UIControlHandler {
	
	private JPanel container = new JPanel(null);
	
	public UIControlHandler(int width, int height) {
		container.setSize(new Dimension(width, height));
	}
	
	public void addControl(JComponent control) {
		container.add(control);
	}
	
	public void removeControl(JComponent control) {
		container.remove(control);
	}
	
	public Component[] controls() {
		return container.getComponents();
	}
	
	public MouseAdapter mouseListener() {
		return new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				Component control = container.findComponentAt(e.getPoint());
				e.setSource(control);
				control.dispatchEvent(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				Component control = container.findComponentAt(e.getPoint());
				e.setSource(control);
				control.dispatchEvent(e);
				if(control instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) control;
					ButtonModel model = button.getModel();
					model.setArmed(true);
					model.setPressed(true);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				Component control = container.findComponentAt(e.getPoint());
				e.setSource(control);
				control.dispatchEvent(e);
				if(control instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) control;
					ButtonModel model = button.getModel();
					model.setPressed(false);
					model.setArmed(false);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				//Component control = container.findComponentAt(e.getPoint());
				//control.dispatchEvent(e);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				//Component control = container.findComponentAt(e.getPoint());
				//control.dispatchEvent(e);
			}
		};
	}
	
	public KeyListener keyListener() {
		return new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				container.dispatchEvent(e);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				container.dispatchEvent(e);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				container.dispatchEvent(e);
			}
		};
	}
	
	public static interface Renderer {
		
		public default void render(Graphics2D g, UIControlHandler uich) {
			for(Component control : uich.controls()) {
				g.translate(control.getX(), control.getY());
				control.print(g);
				g.translate(-control.getX(), -control.getY());
			}
		}
		
	}

}
