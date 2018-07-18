package org.ucoz.intelstat.pong;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;

public class UIControlHandler {

	private HashSet<Control> controls = new HashSet<>();
	
	public UIControlHandler() {
		
	}
	
	public void addControl(Control control) {
		controls.add(control);
	}
	
	public void removeControl(Control control) {
		controls.remove(control);
	}
	
	public HashSet<Control> controls() {
		return controls;
	}
	
	public MouseListener mouseListener() {
		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				HashSet<Control> currentControls = controls;
				boolean found = false;
				while(!found) {
					for(Control control : currentControls) {
						if(control instanceof Panel && control.isInBounds(e.getX(), e.getY())) {
							currentControls = ((Panel) control).children;
							continue;
						} else if(control instanceof Button) {
							Button button = (Button) control;
							if(button.action != null) {
								button.action.actionPerformed(new ActionEvent(button, 0, ""));
								found = true;
								break;
							}
						} else {
							found = true;							
						}
					}
				}
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
	
	public static abstract class Control {
		int posx, posy;
		int width, height;
		Control parent;

		public Control(int posx, int posy, int width, int height) {
			this.posx = posx;
			this.posy = posy;
			this.width = width;
			this.height = height;
		}

		public final boolean isInBounds(int x, int y) {
			return x > posx && y > posy && x < posx + width && y < posy + height;
		}

	}

	public static class Button extends Control {

		private ActionListener action;
		private String text;
		
		public Button(int posx, int posy, int width, int height, String text) {
			super(posx, posy, width, height);
			this.text = text;
		}

		public void setAction(ActionListener action) {
			this.action = action;
		}
		
		public String text() {
			return text;
		}
		
		public void text(String text) {
			this.text = text;
		}
		
	}

	public static class Panel extends Control {

		private HashSet<Control> children = new HashSet<>();

		public Panel(int posx, int posy, int width, int height) {
			super(posx, posy, width, height);
		}

		public void addControl(Control control) {
			if (control.parent instanceof Panel) {
				((Panel) control.parent).removeControl(control);
			}
			control.parent = this;
			children.add(control);
		}

		public void removeControl(Control control) {
			children.remove(control);
			control.parent = null;
		}
		
		public HashSet<Control> children() {
			return children;
		}
	}
	
	public static interface Renderer {
		
		public void render(Graphics2D g, HashSet<Control> controls);
		
	}

}
