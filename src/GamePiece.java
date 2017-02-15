import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public abstract class GamePiece implements PlayableGamePiece
{
	protected final static double DIST_FROM_EDGE = 0.1;
	protected final static int LINE_WIDTH = 5;

	protected boolean selected = false;
	protected Position pos = null;
	protected Player player = Player.EMPTY;
	protected int pieceID;
	/*
	 * 0 = castle
	 * 1 = wood
	 * 2 = crystal
	 * 3 = digger
	 * 4 = warrior
	 * 6 = enemy
	 */
	protected int hp;
	protected int atkp;
	protected int wood = 0;
	protected int crystal = 0;
	protected ArrayList<Integer> holdings;
	protected int level = 0;
	protected int exp = 0; //level up after 10
	protected String name;
	

	public GamePiece (int row, int col, Player p, int health, int atk, int ID) {
		pos = new Position (row, col);
		player = p;
		hp = health;
		atkp = atk;
		pieceID = ID;
		holdings = new ArrayList<Integer>();
	}

	public GamePiece (Position pos, Player p, int health, int atk, int ID) {
		this (pos.r, pos.c, p, health, atk, ID);
	}

	public Position getPosition () {
		return pos;
	}

	public void setPosition (Position pos) {
		pos = new Position (pos);
	}
	
	public Player getPlayer () {
		return player;
	}
	
	public String getName () {
		return name;
	}

	public void claim(Player p){
		player = p;
	}
	
	public void attack(int damage){
		hp -= damage;
	}
	
	public void setPieceSelected (boolean b)
	{
		if ((b == true) && (player != Player.EMPTY))
			selected = true;
		else
			selected = false;
	}

	public boolean isPieceSelected() {
		return selected;
	}

	public int validMove (int targetRow, int targetCol, GameSquares squares) {
		return validMove (new Position (targetRow, targetCol), squares);
	}
	
	public int validMove (Position targetPos, GameSquares squares)
	{
		Position dp = targetPos.offset (pos);
		int dr = dp.r;
		int dc = dp.c;
		
		if (validBasicMove (dr, dc, squares))
			return 1;
		else if (validAttack (dr, dc, squares))
			return 2;
		else if (validClaim(dr, dc, squares))
			return 3;
				
		return 0;
	}
	
	protected abstract boolean validClaim (int dr, int dc, GameSquares squares);
	
	protected abstract boolean validBasicMove (int dr, int dc, GameSquares squares);	
	
	protected abstract boolean validAttack (int dr, int dc, GameSquares squares);
	
	public void claim (Position newPos, GameSquares squares)
	{
		Position dp = newPos.offset (pos);
		int dr = dp.r;
		int dc = dp.c;
		if (validClaim(dr, dc, squares)){
			squares.getSquare(pos.r + dr, pos.c+ dc).getPiece().claim(player);
			exp += 4;
			if (player == Player.LAVA){
				squares.getSquare(0,0).getPiece().holdings.add(squares.getSquare(pos.r + dr, pos.c+ dc).getPiece().pieceID);
				/*
				 * Bonuses for Level 2 Diggers
				 */
				if (level == 1){
					if (squares.getSquare(pos.r + dr, pos.c+ dc).getPiece().pieceID == 1)
						squares.getSquare(0,0).getPiece().wood += 5;
					else if (squares.getSquare(pos.r + dr, pos.c+ dc).getPiece().pieceID == 2)
						squares.getSquare(0,0).getPiece().crystal += 5;
				}
			}
			else if (player == Player.SNOW){
				squares.getSquare(GameBoard.SQUARES_1D - 1,GameBoard.SQUARES_1D-1).getPiece().holdings.add(squares.getSquare(pos.r + dr, pos.c+ dc).getPiece().pieceID);
				/*
				 * Bonuses for Level 2 Diggers
				 */
				if (level ==1){
					if (squares.getSquare(pos.r + dr, pos.c+ dc).getPiece().pieceID == 1)
						squares.getSquare(GameBoard.SQUARES_1D - 1,GameBoard.SQUARES_1D-1).getPiece().wood += 5;
					else if (squares.getSquare(pos.r + dr, pos.c+ dc).getPiece().pieceID == 2)
						squares.getSquare(GameBoard.SQUARES_1D - 1,GameBoard.SQUARES_1D-1).getPiece().crystal += 5;
				}
			}
			evolve(squares);
		}
	}
	
	public void attack (Position newPos, GameSquares squares)
	{
		Position dp = newPos.offset (pos);
		int dr = dp.r;
		int dc = dp.c;
		
		if (validAttack(dr, dc, squares)){
			GamePiece temp = squares.getSquare(pos.r + dr, pos.c + dc).getPiece();
			/*
			 * Special attack set up for warriors, Diggers already have bonuses so
			 * this is exclusively special for warriors
			 */
			if (level == 1 && pieceID == 4){
				Object[] options = { "Basic Attack (Base Damage)", "Special Attack(Base Damage + 2)" };
				int n = JOptionPane.showOptionDialog(null,
						   "Choose Your Move: (will default 'Basic Attack' if closed.",
						   "Attack Initiated!",
						   JOptionPane.YES_NO_OPTION,
						   JOptionPane.QUESTION_MESSAGE,
						   null,     //do not use a custom Icon
						   options,  //the titles of buttons
						   options[0]); //default button title
				if (n == 1){
					temp.attack(atkp + 2);
				}else
					temp.attack(atkp);
			}else
				temp.attack(atkp);
			if (temp.hp <= 0){
				if (temp.pieceID == 2 || temp.pieceID == 1){
					if (temp.getPlayer() == Player.LAVA){
						squares.getSquare(0,0).getPiece().holdings.remove(Integer.valueOf(temp.pieceID));
					}
					if (temp.getPlayer() == Player.SNOW){
						squares.getSquare(17,17).getPiece().holdings.remove(Integer.valueOf(temp.pieceID));
					}
					exp += 2;
				}
				else if (temp.pieceID == 3 || temp.pieceID == 4 || temp.pieceID == 6){
					exp += 5;
				}
				evolve(squares);
				squares.getSquare(pos.r + dr, pos.c + dc).removePiece();
				
			}
		}
	}
	
	
	public void evolve (GameSquares squares){
		if (exp >= 15)
			if (level == 0){
				level = 1;
				exp -= 15;
				if (pieceID == 4){
					hp = 30;
				}else if (pieceID == 3){
					hp = 20;
				}
				JOptionPane.showMessageDialog(null, "Piece at position "+ pos.r + ":" + pos.c + " has evolved. Unlocked special skill");
			}else
				JOptionPane.showMessageDialog(null, "Can not evolved, already evolved");
	}
	
	
	public void move (Position newPos, GameSquares squares)
	{
		Position dp = newPos.offset (pos);
		int dr = dp.r;
		int dc = dp.c;
		
		if (validBasicMove (dr, dc, squares)){
			pos = new Position (newPos);
		}
		
	}
	
	public int castleClick (GameSquares squares){
		Object[] options = { "Repair Castle(5x Wood, +10HP)", "Summon Warrior (5x Crystal, 1x Warrior)","Nothing!" };
		int n = JOptionPane.showOptionDialog(null,
			    "What Would You Like To Do?",
			    "Welcome to Your Kingdom!",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,     //do not use a custom Icon
			    options,  //the titles of buttons
			    options[2]); //default button title
		
		//repair castle check
		if (n == 0){
			if ((wood - 5) < 0){
				JOptionPane.showMessageDialog(null,"Not enough wood! You need " + (Math.abs(wood - 5)) +" more pieces.");
				return 2;
			}else{
				wood -= 5;
				hp += 10;
				//System.out.println(hp);
			}
			return n;
		//summon soldiers
		}else if (n == 1){
			if ((crystal - 5) < 0){
				JOptionPane.showMessageDialog(null,"Not enough crystal! You need " + (Math.abs(crystal - 5)) +" more pieces.");
				return 2;
			}else{
				if (player == Player.LAVA){
					Position p = openPos(squares, player);
					squares.getSquare(p.r, p.c).setPiece(new WarriorR(p.r, p.c));
					crystal -= 5;
				}else if (player == Player.SNOW){
					Position p = openPos(squares, player);
					squares.getSquare(p.r, p.c).setPiece(new WarriorB(p.r, p.c));
					crystal -= 5;
				}
			}
			return n;
		}
		else
			return 2;
		}
	
	/*
	 * Find open position base on left top corner, or bottom right
	 */
	public Position openPos(GameSquares squares, Player p){
		if (player == Player.LAVA){
			int csize = GameBoard.SQUARES_1D/2+1;
			for (int r = 0; r < GameBoard.SQUARES_1D/2; r++){
				csize--;
				for (int c = 0; c < csize; c++){
					if (squares.getSquare(r,c).getPiece() == null){
						Position empty = new Position(r,c);
						return empty;
					}
				}
			}
		}else if (player == Player.SNOW){
			int csize = GameBoard.SQUARES_1D/2;
			for (int r = GameBoard.SQUARES_1D -1; r > GameBoard.SQUARES_1D/2; r--){
				csize++;
				for (int c = GameBoard.SQUARES_1D -1; c > csize; c--){
					if (squares.getSquare(r,c).getPiece() == null){
						Position empty = new Position(r,c);
						return empty;
					}
				}
			}
		}
		return null;
	}
	
	
	public abstract void drawPiece (Graphics2D g2, int x, int y, int width, int height) throws IOException;
}
