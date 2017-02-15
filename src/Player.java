
public enum Player {
	EMPTY, LAVA, SNOW;
	
	public static Player switchPlayer (Player p)
	{
		if (p == Player.LAVA)
			return Player.SNOW;
		else if (p == Player.SNOW)
			return Player.LAVA;
		else
			return Player.EMPTY;
	}
	
	public static Player getOpponent (Player p) {
		return switchPlayer (p);
	}
}
