import java.util.Arrays;

public class GameState {
	private Player[][] boardState = new Player[3][3];
	private Player turn; 
	private int numMovesLeft; 
	private Outcome outcome;

	GameState(){
		numMovesLeft = 1000;
		outcome = Outcome.UNKNOWN;
	}

	public int getNumMovesLeft() {
		return numMovesLeft;
	}


	public void setNumMovesLeft(int numMovesLeft) {
		this.numMovesLeft = numMovesLeft;
	}


	public Outcome getOutcome() {
		return outcome;
	}


	public void setOutcome(Outcome outcome) {
		this.outcome = outcome;
	}


	public Player getTurn() {
		return turn;
	}


	public void setTurn(Player turn) {
		this.turn = turn;
	}


	public Player[][] getBoardState() {
		return boardState;
	}


	public void setBoardState(Player[][] boardState) {
		for (int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				this.boardState[i][j] = boardState[i][j];
			}
		}
	}


	public void printInfo(){
		//System.out.println("Here is info about this game state: ");
		System.out.println(Arrays.deepToString(boardState));
		System.out.println("It is the turn of Player " + turn);
		System.out.println("There are " + numMovesLeft + " moves until the game reaches " + outcome + " with best play.");
		System.out.println();
	}


	public void changeBoardValue(int i, int j, Player turn) {
		boardState[i][j] = turn;		
	}

}
