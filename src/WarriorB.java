
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

public class WarriorB extends GamePiece {
	
	public WarriorB(int row, int col) {
		super (row, col, Player.SNOW, 20, 4, 4);
		name = "Snow Warrior";
		
	}
	public WarriorB (Position pos) {
		this (pos.r, pos.c);
	}
	
	protected boolean validJump(int dr, int dc) {
		return false;
	}
	
	protected boolean validBasicMove(int dr, int dc, GameSquares squares) {
		if ((Math.abs(dr) == 1 && Math.abs(dc) == 1) ||
				(Math.abs(dr) == 1 && Math.abs(dc) == 0) ||
				(Math.abs(dr) == 0 && Math.abs(dc) == 1))
		{
			if (squares.getSquare(pos.r + dr, pos.c + dc).getPiece() == null)
				return true;
		}
		
		return false;
	}

	public void drawPiece(Graphics2D g2, int x, int y, int width, int height)
			throws IOException {
	       // Store before changing.
		BasicStroke stroke = new BasicStroke (10);
		g2.setStroke(stroke);
		
        if (level == 0){
			BufferedImage image = ImageIO.read(getClass().getResource("art/warriorB.png"));
			g2.drawImage(image, x, y, null);
		}else{
			BufferedImage image = ImageIO.read(getClass().getResource("art/warriorB2.png"));
			g2.drawImage(image, x, y, null);
		}
        
        if (selected)
		{
        	Rectangle2D.Double bkgnd = new Rectangle2D.Double (x, y, width, height);
        	g2.setColor(Color.BLACK);
        	g2.draw(bkgnd);
		}
		
	}
	
	protected boolean validClaim(int dr, int dc, GameSquares squares) {
		if ((Math.abs(dr) == 1 && Math.abs(dc) == 1) ||
			(Math.abs(dr) == 1 && Math.abs(dc) == 0) ||
			(Math.abs(dr) == 0 && Math.abs(dc) == 1))
		{
			if (squares.getSquare(pos.r + dr, pos.c + dc).getPiece().getPlayer() == Player.EMPTY){
				return true;
			}
		}
		return false;
	}
	
	protected boolean validAttack(int dr, int dc, GameSquares squares) {
		if ((Math.abs(dr) == 1 && Math.abs(dc) == 1) ||
			(Math.abs(dr) == 1 && Math.abs(dc) == 0) ||
			(Math.abs(dr) == 0 && Math.abs(dc) == 1))
		{
			if (squares.getSquare(pos.r + dr, pos.c + dc).getPiece().getPlayer() == Player.LAVA || squares.getSquare(pos.r + dr, pos.c + dc).getPiece().pieceID == 6){
				return true;
			}
		}
		return false;
	}
	
}
