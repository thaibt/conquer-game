
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Wood extends GamePiece {
	
	public Wood(int row, int col) {
		super (row, col, Player.EMPTY, 15, 0, 1);
		name = "Wood";
	}
	
	public Wood (Position pos) {
		this (pos.r, pos.c);
	}
	
	protected boolean validJump(int dr, int dc) {
		return false;
	}
	protected boolean validBasicMove(int dr, int dc, GameSquares squares) {
		return false;
	}
	protected boolean validAttack(int dr, int dc, GameSquares squares) {
		return false;
	}
	public void drawPiece(Graphics2D g2, int x, int y, int width, int height) throws IOException {
        Stroke tmpS = g2.getStroke();
        Color tmpC = g2.getColor();
        
        final BufferedImage image = ImageIO.read(getClass().getResource("art/wood.png"));
        g2.drawImage(image, x, y, null);

        g2.setColor(tmpC);
        g2.setStroke(tmpS);
		
        if (selected)
		{
        	Rectangle2D.Double bkgnd = new Rectangle2D.Double (x, y, width, height);
        	g2.setColor(Color.BLACK);
        	g2.draw(bkgnd);
		}
        
	}
	protected boolean validClaim(int dr, int dc, GameSquares squares) {
		return false;
	}

}
