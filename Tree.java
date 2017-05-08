import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Tree {
	private GameState state;
	private int treeLevel;

	private Tree parent;
	private ArrayList<Tree> children;
	private Tree bestPlay;

	Tree(GameState gs){
		state = gs;
		children = new ArrayList<Tree>();
	}


	public void addNode(Tree child){
		child.parent = this;
		child.treeLevel = this.treeLevel + 1;
		children.add(child);
	}


	// BFS implementation
	public void printTree(){	
		Queue<Tree> trees = new LinkedList<Tree>();

		treeInfo();
		trees.add(this);

		while(!trees.isEmpty()){
			Tree t =  trees.remove();
			for(Tree child : t.children)
			{
				child.treeInfo();
				trees.add(child);
			}
		}		
	}


	public void treeInfo(){	
		System.out.println("This is on tree level " + treeLevel);
		state.printInfo();
		System.out.println();
	}


	/*
	// DFS implementation
	public void printTree(){	
		System.out.println("This is Tree " + treeLevel);
		state.printInfo();
		System.out.println();

		for(Tree t : children)
			t.printTree();
	}	
	 */


	public Tree getParent() {
		return parent;
	}


	public void setParent(Tree parent) {
		this.parent = parent;
	}	


	public ArrayList<Tree> getChildren() {
		return children;
	}


	public GameState getState() {
		return state;
	}


	public void setState(GameState state) {
		this.state = state;
	}


	public Tree getBestPlay() {
		return bestPlay;
	}


	public void setBestPlay(Tree t) {
		bestPlay = t;
	}


	public void setAsRoot(){
		this.treeLevel = 0;
	}


	public void printBestPlay() {
		System.out.println("The following are the moves left with best play by both sides: ");

		printBestChildren();
	}

	private void printBestChildren(){
		state.printInfo();		
		if(bestPlay != null)
			bestPlay.printBestChildren();
	}

}