package adventuregame;

import java.awt.Graphics;
import java.awt.Rectangle;

import worlds.World;

public class PointRect extends Object {
	
	Rectangle r;
	
	public PointRect(Main f, World w) {
		super(f, w);
	}

	public void paint(Graphics g) {
		g.setColor(getCOLOR());
		g.fillRect(getCx(), getCy(), getWidth(), getHeight());
	}
}
