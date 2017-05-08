
public enum Player {
	X,O,EMPTY;

	public Player nextTurn(){
		if(this.equals(O))
			return X;
		else if (this.equals(X))
			return O;
		else
			return EMPTY;
	}
}
