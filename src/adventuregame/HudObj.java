package adventuregame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;

import worlds.World;

public class HudObj {

	public Rectangle hrect;
	public Point mouse;
	public Color colord, color2, color;
	public String text;
	public boolean highlight = true;
	public String id;
	public Font font;
	public boolean visible = true;
	boolean hasImage = false;
	private boolean selected = false;
	BufferedImage bf;
	boolean center = true;
	int tx, ty;
	private int index = 0;
	Color textcolor = Color.WHITE;
	Color textHighlightColor = Color.ORANGE;
	Color currentTextColor = Color.ORANGE;
	
	public HudObj(int x, int y, int w, int h, Color c) {
		colord = c;
		hrect = new Rectangle(w, h);
		hrect.setLocation(x, y);
		color2 = colord.brighter();			
		color = colord;
	}
	
	public void setHighlightColor(Color c) {
		color2 = c;
	}

	public void select(boolean b) {
		selected = b;
	}

	public void setHighlightTextColor(Color c) {
		textHighlightColor = c;
	}
	
	public void setId(String s) {
		id = s;
	}
	
	public void updatePos(Rectangle r) {
		hrect = r;
	}
	
	public void setIndex(int i) {
		index = i;
	}
	
	public HudObj copy(HudObj o) {
		o = this;
		return o;
	}
	
	public void setFont(Font f) {
		font = f;
	}
	
	public void addText(String t) {
		text = t;
	}

	public void update() {
		mouse = MouseInfo.getPointerInfo().getLocation();
		
		if (highlight == true) {
			if (hrect.contains(mouse) || selected) {
				color = color2;
				currentTextColor = textHighlightColor;
			} else {
				color = colord;
				currentTextColor = textcolor;
			}
		}
	}
	
	public void textPos(int x, int y) {
		center = false;
	}
	
	public void addImage(String s) {
		try {
			bf = ImageIO.read(new File(s + ".png"));
			hasImage = true;
		} catch (Exception e) {e.printStackTrace();}
	}

	public boolean mouseOver() {
		mouse = MouseInfo.getPointerInfo().getLocation();
		return hrect.contains(mouse);
	}

	public void paint(Graphics g) {
		g.setFont(font);
		g.setColor(color);
		g.fillRect(hrect.x, hrect.y, hrect.width, hrect.height);
		if (text != null) {
			g.setColor(currentTextColor);
			if (center) {
				g.drawString(text, (int) (hrect.getMinX() + (hrect.getWidth() / 2) - (g.getFontMetrics().stringWidth(text) / 2)), (int)(hrect.getMaxY() - (hrect.getHeight() / 2) + (g.getFontMetrics().getHeight() / 4)));
			}
			else {
				g.drawString(text, tx, ty);
			}
		}
		if (hasImage == true) {
			g.drawImage(bf, (int) hrect.x, (int) hrect.y, hrect.width, hrect.height, null);
		}
	}

}
