import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

/**
   A component that shows a scene composed of shapes.
 */
public class GameBoard extends JPanel
{
	public static int SQUARES_1D = 18;
		
	private GameSquares squares;

	private GamePiece selectedPiece = null;
	
	private Player firstP;
		
	private Player currentP = Player.EMPTY;
	
	private Position oldPosition = null;
	
	private GamePiece rCastle;
	
	private GamePiece bCastle;
	
	private EnemyController enemyController;

	public GameBoard()
	{
		super ();
		
		setLayout(new GridLayout (SQUARES_1D,SQUARES_1D,0,0));
		
		if ((int)(Math.random()*2) == 0) // coin toss
			firstP = Player.LAVA;
		else
			firstP = Player.SNOW;
		
		currentP = firstP;

		squares = new GameSquares();
		
		Object[] numEnemiesOptions = { "0", "1", "2", "3", "4", "5" };
		
		int numEnemies = JOptionPane.showOptionDialog(null,
			    "How many enemies would you like to add? Please note that enemies are not fully debugged"
			    + " and may throw errors or cause incorrect game behavior.",
			    "Add Enemies?",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,     //do not use a custom Icon
			    numEnemiesOptions,  //the titles of buttons
			    numEnemiesOptions[0]); //default button title

		int numPieceSpots = 0;
		int numofSpecialPiece = 10;
		
		enemyController = new EnemyController(numEnemies,squares);
		
		for (int r = 0; r < SQUARES_1D; r++){
			for (int c = 0; c < SQUARES_1D; c++)
			{
				if ((r + c) % 2 == 1)
					squares.setSquare (new GameSquare (r, c, new Color(216, 180, 160)));
				else
				{
					squares.setSquare (new GameSquare (r, c, new Color(143, 139, 150)));
				}

				final GameSquare sq = squares.getSquare(r, c);		
				if (c == SQUARES_1D-1 && r == SQUARES_1D-1){
					sq.setPiece(new BlueCastle(r,c));
					bCastle = sq.getPiece();
				}else if (c == 0 && r == 0){
					sq.setPiece(new RedCastle(r,c));
					rCastle = sq.getPiece();
				}
				sq.addMouseListener (new GameSquareMouseListener (sq));
				
				add (squares.getSquare(r, c));
				numPieceSpots++;
			}
		}
		
		squares.getSquare(bCastle.pos.r, bCastle.pos.c - 1).setPiece(new DiggerB(bCastle.pos.r, bCastle.pos.c - 1));
		squares.getSquare(rCastle.pos.r, rCastle.pos.c + 1).setPiece(new DiggerR(rCastle.pos.r, rCastle.pos.c + 1));
		
		/*
		 * Setting up resource pieces, random
		 */
		for (int i = 0 ; i < numofSpecialPiece; i++){
			int rRan = new Random().nextInt(((SQUARES_1D-4)-1) - 1) + 3;
			int cRan = new Random().nextInt(((SQUARES_1D-4)-1) - 1) + 3;
			boolean b = new Random().nextBoolean();
			if (squares.getSquare(rRan, cRan).getPiece() == null && b == true){
				squares.getSquare(rRan, cRan).setPiece(new Crystal(rRan, cRan));
			}else if (squares.getSquare(rRan, cRan).getPiece() == null && b == false){
				squares.getSquare(rRan, cRan).setPiece(new Wood(rRan, cRan));
			}else
				i--;
		}
		
		
		/*
		 * setting up enemy pieces
		 */
		for (int i = 0 ; i < numEnemies; i++){
			int rRan = new Random().nextInt(4) + 7;
			int cRan = new Random().nextInt(4) + 7;
			boolean e = new Random().nextBoolean();
			Enemy newEnemy = new Enemy(rRan, cRan);
			if (squares.getSquare(rRan, cRan).getPiece() == null && e == true){
				squares.getSquare(rRan, cRan).setPiece(newEnemy);
			}else if (squares.getSquare(rRan, cRan).getPiece() == null && e == false){
				squares.getSquare(rRan, cRan).setPiece(newEnemy);
			}else
				i--;
			
			enemyController.addEnemy(newEnemy);
		}
		
		JOptionPane.showMessageDialog(null, "<html> Hello and Welcome to Conquer! Here is a quick guide on how to play the game! <br>" +
				"1. Each team has a base on opposite corners. The base has 50 health points. <br>"+
				"2. Each team starts with a Miner, which is used to obtain resources. <br>"+
				"3. To obtain resources, move the Miner to nearby crystal or wood. Click the Miner and then the resource to claim it. <br>"
				 + "Be careful, as your opponent can attack and destroy your resources! <br>" +
				"4. Every turn, claimed resources generate new resources for your base. Resources generate up to 20 pieces before disappearing from the map. <br>"
				+ "You can spend resources by clicking on your base and selecting an option during your turn. <br>" +
				"5. You have two base options. Repairing a castle gives the castle +10 hp. \"Summon a warrior\" will summon a Warrior with 4 attack points near your base. <br>" +
				"A Miner only deals 2 attack points and has 10 hp less than warrior. <br>" +
				"6. Watch out for the neutral Enemies. They have 6 hp and 3 attack points and will seek out the closest units and castle to attack. <br>" +
				"Enemies move after both players have made their move. <br>" +
				"7. The goal of the game is to destroy all of your opponent's units or destroy their base. </html>");		
	}
	
	public void getResources(GameSquares squares){
		for (int i = 0; i < rCastle.holdings.size(); i++){
			int r = rCastle.holdings.get(i);
			if (r == 1)
				rCastle.wood++;
			else if (r == 2)
				rCastle.crystal++;
		}
		for (int i = 0; i < bCastle.holdings.size(); i++){
			int r = bCastle.holdings.get(i);
			if (r == 1)
				bCastle.wood++;
			else if (r == 2)
				bCastle.crystal++;
		}
		
		/*
		 * Loops through the whole board, find resource pieces, subtract 1 health due to one extraction from above
		 * If health reaches 0, then remove from map and remove an occurrence of it from holdings
		 */
		
		for (int r = 0; r < SQUARES_1D; r++){
			for (int c = 0; c < SQUARES_1D; c++){
				if (squares.getSquare(r,c).getPiece() != null){
					if ((squares.getSquare(r,c).getPiece().pieceID == 1 || squares.getSquare(r,c).getPiece().pieceID == 2) && squares.getSquare(r,c).getPiece().getPlayer() != Player.EMPTY ){
						squares.getSquare(r,c).getPiece().hp -= 1;
						if (squares.getSquare(r,c).getPiece().hp <= 0){
							if (squares.getSquare(r,c).getPiece().getPlayer() == Player.LAVA){
								rCastle.holdings.remove(Integer.valueOf(squares.getSquare(r,c).getPiece().pieceID));
							}
							if (squares.getSquare(r,c).getPiece().getPlayer() == Player.SNOW){
								bCastle.holdings.remove(Integer.valueOf(squares.getSquare(r,c).getPiece().pieceID));
							}
							squares.getSquare(r,c).removePiece();
						}
					}
				}
			}
		}
		
	
		StatusDisp.console.setText("<html> Starting Turn: <br>" + currentP + "<html>");
		
	}
	
	
	/*
	 * Outputting for StatusDisp
	 */
	
	
	public void statusBar(GamePiece selectedPiece){
		int pID = selectedPiece.pieceID;
		if (pID == 1){
			StatusDisp.currentStatus.setText("<html> Type: Resource (Wood) <br>" +
					 "HP: " + selectedPiece.hp+"/15 <br>" +
					 "Attack: " + selectedPiece.atkp + "<br>" +
					 "Player: " + selectedPiece.player+ "<br>" +
					 "Postion: " + selectedPiece.pos.r + ":" + selectedPiece.pos.c + "</html>");
		}else if (pID == 2){
			StatusDisp.currentStatus.setText("<html> Type: Resource (Crystal) <br>" +
					 "HP: " + selectedPiece.hp+"/15 <br>" +
					 "Attack: " + selectedPiece.atkp + "<br>" +
					 "Player: " + selectedPiece.player+ "<br>" +
					 "Postion: " + selectedPiece.pos.r + ":" + selectedPiece.pos.c + "</html>");
		}else if (pID == 3){
			StatusDisp.currentStatus.setText("<html> Type: Miner <br>" +
					 "HP: " + selectedPiece.hp+"/15 <br>" +
					 "Level: " + (selectedPiece.level+1)+" <br>" +
					 "Experience: " + selectedPiece.exp +"/15 <br>" +
					 "Attack: " + selectedPiece.atkp + "<br>" +
					 "Player: " + selectedPiece.player+ "<br>" +
					 "Postion: " + selectedPiece.pos.r + ":" + selectedPiece.pos.c + "</html>");
		}else if (pID == 4){
			StatusDisp.currentStatus.setText("<html> Type: Warrior <br>" +
					 "HP: " + selectedPiece.hp+"/20 <br>" +
					 "Level: " + (selectedPiece.level+1)+" <br>" +
					 "Experience: " + selectedPiece.exp +"/15 <br>" +
					 "Attack: " + selectedPiece.atkp + "<br>" +
					 "Player: " + selectedPiece.player+ "<br>" +
					 "Postion: " + selectedPiece.pos.r + ":" + selectedPiece.pos.c + "</html>");
		}else if (pID == 6){
			StatusDisp.currentStatus.setText("<html> Type: Monster <br>" +
					 "HP: " + selectedPiece.hp+"/6 <br>" +
					 "Level: " + (selectedPiece.level+1)+" <br>" +
					 "Attack: " + selectedPiece.atkp + "<br>" +
					 "Player: " + selectedPiece.player+ "<br>" +
					 "Postion: " + selectedPiece.pos.r + ":" + selectedPiece.pos.c + "</html>");
		}
	}
	
	public void resetBoard() {
		for (int r = 0; r < SQUARES_1D; r++)
			for (int c = 0; c < SQUARES_1D; c++)
			{
				squares.getSquare(r,c).removePiece();
				if (c == SQUARES_1D-1 && r == SQUARES_1D-1){
					squares.getSquare(r,c).setPiece(new BlueCastle(r,c));
					bCastle = squares.getSquare(r,c).getPiece();
				}else if (c == 0 && r == 0){
					squares.getSquare(r,c).setPiece(new RedCastle(r,c));
					rCastle = squares.getSquare(r,c).getPiece();
				}
			}
		

		Object[] numEnemiesOptions = { "0", "1", "2", "3", "4", "5" };
		
		int numEnemies = JOptionPane.showOptionDialog(null,
			    "How many enemies would you like to add? Please note that enemies are not fully debugged"
			    + " and may throw errors or cause incorrect game behavior.",
			    "Add Enemies?",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,     //do not use a custom Icon
			    numEnemiesOptions,  //the titles of buttons
			    numEnemiesOptions[0]); //default button title

		int numPieceSpots = 0;
		int numofSpecialPiece = 10;
		
		enemyController = new EnemyController(numEnemies,squares);
		
		squares.getSquare(bCastle.pos.r, bCastle.pos.c - 1).setPiece(new DiggerB(bCastle.pos.r, bCastle.pos.c - 1));
		squares.getSquare(rCastle.pos.r, rCastle.pos.c + 1).setPiece(new DiggerR(rCastle.pos.r, rCastle.pos.c + 1));
		
		
		for (int i = 0 ; i < numofSpecialPiece; i++){
			int rRan = new Random().nextInt((SQUARES_1D-1) - 1) + 1;
			int cRan = new Random().nextInt((SQUARES_1D-1) - 1) + 1;
			boolean b = new Random().nextBoolean();
			if (squares.getSquare(rRan, cRan).getPiece() == null && b == true){
				squares.getSquare(rRan, cRan).setPiece(new Crystal(rRan, cRan));
			}else if (squares.getSquare(rRan, cRan).getPiece() == null && b == false){
				squares.getSquare(rRan, cRan).setPiece(new Wood(rRan, cRan));
			}else
				i--;
		}
		
		/*
		 * setting up enemy pieces
		 */
		for (int i = 0 ; i < numEnemies; i++){
			int rRan = new Random().nextInt(4) + 7;
			int cRan = new Random().nextInt(4) + 7;
			boolean e = new Random().nextBoolean();
			Enemy newEnemy = new Enemy(rRan, cRan);
			if (squares.getSquare(rRan, cRan).getPiece() == null && e == true){
				squares.getSquare(rRan, cRan).setPiece(newEnemy);
			}else if (squares.getSquare(rRan, cRan).getPiece() == null && e == false){
				squares.getSquare(rRan, cRan).setPiece(newEnemy);
			}else
				i--;
			
			enemyController.addEnemy(newEnemy);
		}
		
		if ((int)(Math.random()*2) == 0) // coin toss
			firstP = Player.LAVA;
		else
			firstP = Player.SNOW;
		
		currentP = firstP;
		repaint();
		
	}
	public class GameSquareMouseListener extends MouseAdapter
	{	
		GameSquare sq;

		public GameSquareMouseListener (GameSquare sq) {
			this.sq = sq;
		}

		
		
		public void mousePressed (MouseEvent event)
		{

			Point mousePoint = event.getPoint();
			if (sq.contains (mousePoint))
			{
				if (selectedPiece == null)
				{
					
					if (sq.getPiece() != null && (sq.getPiece().getPlayer() != currentP)){
						if (sq.getPiece().pieceID == 0){
							int result = sq.getPiece().castleClick(squares);
							if (result != 2){
								currentP = sq.getPiece().getPlayer();
							}
						}
						else{
							selectedPiece = sq.getPiece();
							sq.getPiece().setPieceSelected (true);
							/*
							 * Status Display
							 */
							statusBar(selectedPiece);
						}
					}
				}else{
					Position pos = sq.getPosition();
					GameSquare selSq = squares.getSquare (selectedPiece.getPosition());

					if (selectedPiece.getPosition().equals (sq.getPosition() ))
					{
						selectedPiece.setPieceSelected(false);
						selectedPiece = null;
					}
					else{ 
						if (selectedPiece.validMove (pos, squares) == 1){
							getResources(squares);
							if (selectedPiece.player != Player.EMPTY)
								currentP = selectedPiece.getPlayer();
							StatusDisp.console.setText("<html> ~Action~ <br> " + selectedPiece.name + " At <br>" + selectedPiece.pos.r +":" + selectedPiece.pos.c + "<br> MOVED <br> "+
									   "To Position <br>" + pos.r + ":" + pos.c + "</html>");
							selectedPiece.move(pos, squares);
							sq.setPiece (selectedPiece);			// new square containing selected piece
							selSq.setPiece (null);					// square that used to contain selected piece
							
							if (currentP == firstP)
								enemyController.updateEnemies();
							
						}else if (selectedPiece.validMove(pos, squares) == 2){
							getResources(squares);
							if (selectedPiece.player != Player.EMPTY)
								currentP = selectedPiece.getPlayer();
							StatusDisp.console.setText("<html> ~Action~ <br " + selectedPiece.name + " At <br>" + selectedPiece.pos.r +":" + selectedPiece.pos.c
									+ "<br>  ATTACKED  <br>"+ squares.getSquare(pos).getPiece().name + "At Position <br>" + pos.r + ":" + pos.c + "<br> For " + selectedPiece.atkp + " HP</html>");
							selectedPiece.attack(pos, squares);
							
							if (currentP == firstP)
								enemyController.updateEnemies();
							
						}else if (selectedPiece.validMove (pos,squares) == 3){
							getResources(squares);
							if (selectedPiece.player != Player.EMPTY)
								currentP = selectedPiece.getPlayer();
							StatusDisp.console.setText("<html> ~Action~ <br> " + selectedPiece.name + " At <br>" + selectedPiece.pos.r +":" + selectedPiece.pos.c + "<br> CLAIMED <br>"+
									   squares.getSquare(pos).getPiece().name + " At Position <br>" + pos.r + ":" + pos.c + "</html>");
							selectedPiece.claim(pos, squares);
							
							if (currentP == firstP)
								enemyController.updateEnemies();
							
						}
						
						if (currentP == Player.LAVA)
							StatusDisp.currentPlayer.setText("Snow's TURN");
						else if (currentP == Player.SNOW)
							StatusDisp.currentPlayer.setText("Lava's TURN");
						
						selectedPiece.setPieceSelected(false);	// deselect
						selectedPiece = null;
					}
					
					
					repaint();

					/*
					 * Figure out how many pieces we have left
					 */
					int numRed = 0; int numBlack = 0;
					for (int r = 0; r < SQUARES_1D; r++){
						for (int c = 0; c < SQUARES_1D; c++){
							if (squares.getSquare(r,c).getPiece() != null){
								if (squares.getSquare(r,c).getPiece().getPlayer() == Player.LAVA)
									numRed++;
								else if (squares.getSquare(r,c).getPiece().getPlayer() == Player.SNOW)
									numBlack++;
							}
						}
					}

					int n = -1;
					Object[] options = { "Play Again", "Quit" };
					if (squares.getSquare(bCastle.pos.r, bCastle.pos.c).getPiece() == null || 
					   (numBlack == 1 &&  squares.getSquare(bCastle.pos.r, bCastle.pos.c).getPiece() != null && squares.getSquare(bCastle.pos.r, bCastle.pos.c).getPiece().crystal < 5)){
						n = JOptionPane.showOptionDialog(null,
							    "Lava Team Won. Pick an Option.",
							    "Game Over",
							    JOptionPane.YES_NO_OPTION,
							    JOptionPane.QUESTION_MESSAGE,
							    null,     //do not use a custom Icon
							    options,  //the titles of buttons
							    options[1]); //default button title
					}else if (squares.getSquare(rCastle.pos.r, rCastle.pos.c).getPiece() == null || 
							 (numRed == 1 && squares.getSquare(rCastle.pos.r, rCastle.pos.c).getPiece() != null && squares.getSquare(rCastle.pos.r, rCastle.pos.c).getPiece().crystal < 5 )){
						n = JOptionPane.showOptionDialog(null,
							    "Snow Team Won. Pick an Option.",
							    "Game Over",
							    JOptionPane.YES_NO_OPTION,
							    JOptionPane.QUESTION_MESSAGE,
							    null,     //do not use a custom Icon
							    options,  //the titles of buttons
							    options[1]); //default button title
					}
					if (n == 0){ // play again						
						resetBoard();
					}else if (n == 1){ //quit
						System.exit(0);
					}
					
					
					StatusDisp.lavaresources.setText("<html>" + "Castle HP: " + squares.getSquare(0,0).getPiece().hp +"<br>"
							 + "Wood: " + squares.getSquare(0,0).getPiece().wood + "<br>" 
							 + "Crystal: " + squares.getSquare(0,0).getPiece().crystal+ "<br>" 
							 + "Pieces: " + numRed+ "</html>");
					StatusDisp.snowresources.setText("<html>" + "Castle HP: " + squares.getSquare(17,17).getPiece().hp +"<br>"
							 + "Wood: " + squares.getSquare(17,17).getPiece().wood + "<br>" 
							 + "Crystal: " + squares.getSquare(17,17).getPiece().crystal + "<br>" 
							 + "Pieces: " + numBlack+ "</html>");
					
					
					
					
					
					
				}
			}
			
			repaint();
		}
	}
}
