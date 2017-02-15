
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BlueCastle extends GamePiece {
	
	public BlueCastle(int row, int col) {
		super (row, col, Player.SNOW, 50, 0, 0);
		name = "Snow Castle";
		crystal = 5;
		
	}
	public BlueCastle (Position pos) {
		this (pos.r, pos.c);
	}
	

	public boolean validMove(int dr, int dc, GameSquare sq) {
		return false;
	}
	public boolean validMove(Position dp, GameSquare sq) {
		return false;
	}
	
	protected boolean validJump(int dr, int dc) {
		return false;
	}
	protected boolean validBasicMove(int dr, int dc, GameSquares squares) {
		// TODO Auto-generated method stub
		return false;
	}
	protected boolean validAttack(int dr, int dc, GameSquares squares) {
		// TODO Auto-generated method stub
		return false;
	}
	public void drawPiece(Graphics2D g2, int x, int y, int width, int height) throws IOException {
        Stroke tmpS = g2.getStroke();
        Color tmpC = g2.getColor();
        
        final BufferedImage image = ImageIO.read(getClass().getResource("art/snowbase.png"));
        g2.drawImage(image, x, y, null);

        g2.setColor(tmpC);
        g2.setStroke(tmpS);
		
	}
	protected boolean validClaim(int dr, int dc, GameSquares squares) {
		return false;
	}
}
