
public class Node {
	int index;		//what we assign while dng DFS
	String label;		//node label
	int canberoot;
	public Node(String s){
		label=s;
		index=-1;
		canberoot=1;
	}
	boolean covered;
}
