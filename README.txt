Valuable Sheffey
CS 362
README

How to Run
This program takes a string representing the initial configuration of the tic-tac-toe board and returns the moves of best play by both sides for the rest of the game using retrograde analysis. Here best play is defined as the fastest way to move to the most optimal end game.

The accepted string input is a sequence of letters (x for X, o for O, and dash for empty) with a comma separating each row.
Ex. "XOX,-O-,OX-" is equivalent to the following tic-tac-toe board:
        _X|O|X_
        _ |O| _
 	 O|X|
  
To run the program, unzip the Retrograde Analysis TicTacToe folder. Open the command prompt in the unzipped folder and type:
       javac TicTacToe.java
       java TicTacToe “string representing tic-tac-toe board”


Implementation
This program consists of five files:
	Outcome.java and Player.java are enums that represent the game ends states and the game players respectively.
	Tree.java is a tree structure in which nodes contain a game state, the parent node, a list of children nodes, an indication of which level of the tree the current node resides, and the child node that will give the best play from the current node.
	GameState.java holds information about a game state, such as what the board looks like, whose turn it is, how many moves are left until the optimal end game state, and the optimal game end state from the current node.
	TicTacToe.java is the main class in which the retrograde analysis is run.

In TicTacToe.java, the program takes the user-input string and creates a game state object. The program then builds a game tree by placing the game state object at the root. Once the game tree is built, the program performs retrograde analysis on the tree.
This involves pruning nodes that continue after a win and then labeling all final positions with their outcome. Then, starting from the root, the program recursively calls retrograde analysis for every child of a node until the node can label itself based on the best outcome of its children.
While retrograde analysis is being performed, each node saves the child node representing the best play from that point. Once the retrograde analysis is finished, the program prints the list of best plays starting from the root.


Assumptions
If an even number of Xs and Os are placed on the board, the program assumes that it Xs turn next.
If there are multiple ways to reach an optimal end state in the same number of moves, the program only returns one way.
       
