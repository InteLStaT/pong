package org.ucoz.intelstat.pong;

import java.util.ArrayList;

public class UIControlHandler {

	
	
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
		
	}
	
	public static class Button extends Control {
		
		public Button(int posx, int posy, int width, int height) {
			super(posx, posy, width, height);
		}
		
	}
	
	public static class Panel extends Control {
		
		private ArrayList<Control> children = new ArrayList<>();
		
		public Panel(int posx, int posy, int width, int height) {
			super(posx, posy, width, height);
		}
		
		public void addControl(Control control) {
			if(control.parent != null) {
				if(control.parent instanceof Panel) {
					((Panel) control.parent).removeControl(control);
					control.parent = this;
					children.add(control);
				}
			}
		}
		
		public void removeControl(Control control) {
			children.remove(control);
			control.parent = null;
		}
	}
	
}
