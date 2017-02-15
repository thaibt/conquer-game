import java.util.ArrayList;

public class EnemyController {
	
	private ArrayList<Enemy> enemies;
	private GameSquares squares;
	private int length;
	
	public EnemyController(int numEnemies, GameSquares square) {
		enemies = new ArrayList<Enemy>(numEnemies);
		squares = square;
		length = 0;
	}
	
	public void addEnemy(Enemy e) {
		enemies.add(e);
	}
	
	public void removeEnemy(Enemy e) {
		enemies.remove(e);
	}
	
	public void updateEnemies() {
		for (int i = 0; i < enemies.size()-1; i++) {
			Enemy e = enemies.get(i);
			Position closestPiece = e.findClosestPlayer(squares);
			int dr = closestPiece.r - e.getPosition().r;
			int dc = closestPiece.c - e.getPosition().c;
			if (e.hp > 0){
				if ((Math.abs(dr) <= 1) && (Math.abs(dc) <= 1))
				{
					Position oldPos = e.getPosition();
					Position newPos = new Position(e.getPosition().r + dr, e.getPosition().c + dc);
					e.attack(newPos, squares);
				}
				else {
					if (dr != 0)
						dr = dr / Math.abs(dr);
					if (dc != 0)
						dc = dc / Math.abs(dc);
				
					Position oldPos = e.getPosition();
					Position newPos = new Position(e.getPosition().r + dr, e.getPosition().c + dc);
					if (e.validBasicMove (dr, dc, squares)){
						e.move(newPos,squares);
						squares.getSquare(oldPos).setPiece(null);
						squares.getSquare(newPos).setPiece(e);
					}
				}
			}
		}
	}
}
