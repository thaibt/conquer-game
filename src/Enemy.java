import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.*;

import javax.imageio.ImageIO;


public class Enemy extends GamePiece {

	public Enemy(int row, int col) {
		super (row, col, Player.EMPTY, 6, 2, 6);
		name = "Enemy";
	}
	
	public Enemy (Position pos) {
		this (pos.r, pos.c);
	}
	
	// Enemies can't claim resources
	protected boolean validClaim(int dr, int dc, GameSquares squares) {
		return false;
	}

	protected boolean validBasicMove(int dr, int dc, GameSquares squares) {
		if ((Math.abs(dr) == 1 && Math.abs(dc) == 1) ||
				(Math.abs(dr) == 1 && Math.abs(dc) == 0) ||
				(Math.abs(dr) == 0 && Math.abs(dc) == 1))
		{
			if (squares.getSquare(pos.r + dr, pos.c + dc).getPiece() == null)
			{
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
			if (squares.getSquare(pos.r + dr, pos.c + dc).getPiece() != null) {
				
			if (squares.getSquare(pos.r + dr, pos.c + dc).getPiece().getPlayer() != Player.EMPTY) {
				return true; }
			}
		}
		return false;
	}

	protected Position findClosestPlayer(GameSquares squares)
	{
		Position closest = new Position(999,999);
		int rDist = Math.abs(closest.r - pos.r);
		int cDist = Math.abs(closest.c - pos.c);
		double minDist = Math.sqrt(Math.pow(rDist, 2) + Math.pow(cDist, 2));
		
		for (int r = 0; r < 18; r++) {
			for (int c = 0; c < 18; c++) {
				if ((squares.getSquare(r,c).getPiece() != null)) {
					if ((squares.getSquare(r,c).getPiece().getPlayer() == Player.LAVA) ||
						(squares.getSquare(r,c).getPiece().getPlayer() == Player.SNOW)) {
						if ((squares.getSquare(r,c).getPiece().pieceID != 1) &&
							(squares.getSquare(r,c).getPiece().pieceID != 2) &&
							(squares.getSquare(r,c).getPiece().pieceID != 6)) {
							int newRDist = Math.abs(r - pos.r);
							int newCDist = Math.abs(c - pos.c);
							double distance = Math.sqrt(Math.pow(newRDist, 2) + Math.pow(newCDist, 2));

							if (distance < minDist)
							{
								closest = new Position(r,c);
								minDist = distance;
							}
						}
					}
				}
			}
		}
		
		return closest;
	}
	
	public void drawPiece(Graphics2D g2, int x, int y, int width, int height)
			throws IOException {
		
	    // Store before changing.
		BasicStroke stroke = new BasicStroke (10);
		g2.setStroke(stroke);
		
		BufferedImage image = ImageIO.read(getClass().getResource("art/enemy.png"));
		g2.drawImage(image, x, y, null);

	}

}
