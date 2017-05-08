/*
 *  Takes a string representing the tic-tac-toe board.
 *  The string is a sequence of letters (x for X, o for O, and dash for empty) 
 *  with a comma separating each row.
 * 
 *  Ex. "XOX,-O-,OX-" is equivalent to:
 * 										  _X|O|X_
 * 										  _ |O| _
 * 										   O|X|
 * 
 *  Returns the moves of best play by both sides for the rest of the game using retrograde analysis. 
 *  Here best play is defined as the fastest way to move to optimal end game.
 * 
 * */
public class TicTacToe {
	static boolean DEBUG = false;

	public static void main(String[] args) {
		GameState initialState;
		Tree gameTree;

		initialState = buildInputState(args);

		if(DEBUG)
			initialState.printInfo();

		gameTree = new Tree(initialState);
		gameTree.setAsRoot(); 
		buildGameTree(gameTree);
		
		if(DEBUG)
			gameTree.printTree();
		
		retrogradeAnalysis(gameTree);
		
		if(DEBUG)
			gameTree.printTree();

		gameTree.printBestPlay();
	}

	
	// Performs retrograde analysis on given tree
	private static void retrogradeAnalysis(Tree gameTree) {
		Player winner = isWinningConfig(gameTree.getState().getBoardState());

		// For each node, check if win. If so, delete children. They aren't valid moves once a win is found.
		if(winner.equals(Player.X)){
			gameTree.getState().setOutcome(Outcome.WIN_X);
			gameTree.getChildren().clear();
			gameTree.getState().setNumMovesLeft(0);
		}
		else if(winner.equals(Player.O)){
			gameTree.getState().setOutcome(Outcome.WIN_O);
			gameTree.getChildren().clear();
			gameTree.getState().setNumMovesLeft(0);
		}
		else{
			if(gameTree.getChildren().isEmpty()){ // leaf node
				gameTree.getState().setOutcome(Outcome.DRAW);
				gameTree.getState().setNumMovesLeft(0);
			}
			else
				bestOfChildren(gameTree);
		}
	}

	
	// Assigns tree the best outcome label based on its successors after the successors have completed retrograde analysis themselves
	private static void bestOfChildren(Tree t) {		
		for(Tree child: t.getChildren())
			retrogradeAnalysis(child);

		if(!t.getChildren().isEmpty())
		{
			Player currentPlayer = t.getState().getTurn();

			if(currentPlayer.equals(Player.X))
				checkChildrenForWin(t, Outcome.WIN_X, Outcome.WIN_O);			
			else // currentPlayer is Player.O
				checkChildrenForWin(t, Outcome.WIN_O, Outcome.WIN_X);			
		}		
	}


	// Uses min-max rule to assign tree an outcome label depending on the best outcome available from its successors
	private static void checkChildrenForWin(Tree t, Outcome win, Outcome lost){	
		Tree winChild = null;
		Tree drawChild = null;
		Tree loseChild = null;

		// Look for best win, draw, lost options
		for(Tree child: t.getChildren()){
			if (child.getState().getOutcome().equals(win)){
				if(winChild == null || winChild.getState().getNumMovesLeft() > child.getState().getNumMovesLeft())
					winChild = child;
			}			
			else if (child.getState().getOutcome().equals(Outcome.DRAW)){
				if(drawChild == null || drawChild.getState().getNumMovesLeft() > child.getState().getNumMovesLeft())
					drawChild = child;
			}			
			else{ // child outcome equals lost
				if(loseChild == null || loseChild.getState().getNumMovesLeft() > child.getState().getNumMovesLeft())
					loseChild = child;
			}
		}

		// Check if win exists
		if (winChild != null){
			t.setBestPlay(winChild);
			t.getState().setOutcome(win);	
			t.getState().setNumMovesLeft(winChild.getState().getNumMovesLeft() + 1);
			return;
		}

		// Since win not available, look for draw
		if (drawChild != null){
			t.setBestPlay(drawChild);
			t.getState().setOutcome(Outcome.DRAW);	
			t.getState().setNumMovesLeft(drawChild.getState().getNumMovesLeft() + 1);
			return;
		}

		// Otherwise, mark as lost
		if (loseChild != null){
			t.setBestPlay(loseChild);
			t.getState().setOutcome(lost);	
			t.getState().setNumMovesLeft(loseChild.getState().getNumMovesLeft() + 1);
			return;
		}
	}

	
	// Builds tree from input state in which each node represents a game state and each edge represents a move from one game state to another
	private static Tree buildGameTree(Tree t) {
		Player[][] board = t.getState().getBoardState();
		Player current = t.getState().getTurn();

		for (int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(board[i][j].equals(Player.EMPTY))
				{
					GameState newState = new GameState();
					newState.setBoardState(board);
					newState.changeBoardValue(i,j,current);
					newState.setTurn(current.nextTurn());

					t.addNode(new Tree(newState));					
				}
			}
		}

		for(Tree child: t.getChildren())
			buildGameTree(child);

		return t;
	}

	
	// Takes command line input and turns it into a Game State object
	private static GameState buildInputState(String[] args) {
		GameState state = new GameState();
		Player[][] board = new Player[3][3];
		int xNumPlays = 0;
		int oNumPlays = 0;

		if(args.length != 1){
			System.err.println("Incorrect number of arguments");
			System.exit(1);
		}

		String[] ticTacSequence = args[0].split(",");

		for(int j=0; j<3; j++){
			char[] row = ticTacSequence[j].toLowerCase().toCharArray();

			for(int i=0; i<3 ;i++)
			{
				switch (row[i]){
				case 'x':
					board[j][i] = Player.X;
					xNumPlays++;
					break;
				case 'o' :
					board[j][i] = Player.O;
					oNumPlays++;
					break;
				case '-' :
					board[j][i] = Player.EMPTY;
					break;
				default:
					System.err.println(row[i] + " is not a valid position value. Please use x, o, or dash.");
					System.exit(1);
					break;

				}
			}
		}

		state.setBoardState(board);

		if(xNumPlays == oNumPlays)
			state.setTurn(Player.X); // if not obvious whose turn, default is X
		else if (xNumPlays > oNumPlays)
			state.setTurn(Player.O);
		else
			state.setTurn(Player.X);


		return state;			
	}


	// Checks if a win exist in given board state. If so, returns which player won. If neither won, returns Player.EMPTY.
	private static Player isWinningConfig(Player[][] board)
	{
		if(board[0][0] == board[0][1] && board[0][1] == board[0][2] && board[0][2] != Player.EMPTY) // XXX,...,...
			return board[0][0];
		if(board[1][0] == board[1][1] && board[1][1] == board[1][2] && board[1][2] != Player.EMPTY) // ...,XXX,...
			return board[1][0];
		if(board[2][0] == board[2][1] && board[2][1] == board[2][2] && board[2][2] != Player.EMPTY) // ...,...,XXX
			return board[2][0];
		if(board[0][0] == board[1][0] && board[1][0] == board[2][0] && board[2][0] != Player.EMPTY) // X..,X..,X..
			return board[0][0];
		if(board[0][1] == board[1][1] && board[1][1] == board[2][1] && board[2][1] != Player.EMPTY) // .X.,.X.,.X.
			return board[0][1];
		if(board[0][2] == board[1][2] && board[1][2] == board[2][2] && board[2][2] != Player.EMPTY) // ..X,..X,..X
			return board[0][2];
		if(board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] != Player.EMPTY) // X..,.X.,..X
			return board[0][0];
		if(board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[2][0] != Player.EMPTY) // ..X,.X.,X..
			return board[0][2];

		return Player.EMPTY;
	}

}
