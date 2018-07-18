package org.ucoz.intelstat.pong;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame f = new JFrame("Pong");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(new GamePanel());
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.getContentPane().requestFocus();
	}
	
}
