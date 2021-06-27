import java.util.ArrayList;

public class AST {

	ArrayList<AST> childNodes = new ArrayList<>();
	AST parentNode;
	String node_type;
	String value;
	
	public AST(String node_type) {
		this.node_type = node_type;
	}
	
	public AST(String node_type, String value) {
		this.node_type = node_type;
		this.value = value;
	}
	
	public void addNode(String node_type) {
		//adds the child to the nodes children list;
		childNodes.add(new AST(node_type));
		//sets the parent of the child node as the node which called the function
		childNodes.get(childNodes.size() - 1).parentNode = this;
	}
	
	public void addNode(String node_type, String value) {
		//adds the child to the nodes children list;
		childNodes.add(new AST(node_type,value));
		//sets the parent of the child node as the node which called the function
		childNodes.get(childNodes.size() - 1).parentNode = this;
	}
	
	public AST switchRoot(AST root) {
		return root.childNodes.get(root.childNodes.size() - 1);
	}
	
}
