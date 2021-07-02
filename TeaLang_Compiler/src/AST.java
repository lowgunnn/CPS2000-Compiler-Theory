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

	public AST(AST source) {
		this.parentNode = source.parentNode;
		this.node_type = source.node_type;
		this.value = source.value;
		this.childNodes = source.childNodes;
	}

	public void addNode(String node_type) {
		// adds the child to the nodes children list;
		childNodes.add(new AST(node_type));
		// sets the parent of the child node as the node which called the function
		childNodes.get(childNodes.size() - 1).parentNode = this;
	}

	public void addNode(String node_type, String value) {
		// adds the child to the nodes children list;
		childNodes.add(new AST(node_type, value));
		// sets the parent of the child node as the node which called the function
		childNodes.get(childNodes.size() - 1).parentNode = this;
	}

	public AST switchRoot(AST root) {
		return root.childNodes.get(root.childNodes.size() - 1);
	}

	public AST operatorSwitch(AST root, String value) {

		if( root.node_type == "Operator"){
		if(value.equals("+") || value.equals("-") || value.equals("or")) {
			
			if(root.value.equals("*") || root.value.equals("/") || root.value.equals("and") ) {
				root = root.parentNode;
			}
		}
		
		if(value.equals("==" )|| value.equals( "<" )|| value.equals(">" )|| value.equals("<=")
				|| value.equals("<=" )|| value.equals( "!=") ) {
			
			if(root.value.equals("*") || root.value.equals("/") || root.value.equals("and") ||
					value.equals("+") || value.equals("-") || value.equals("or")	) {
				root = root.parentNode;
			}
		}
		}
		root.addNode("Operator", value);
		root.childNodes.get(root.childNodes.size() - 1).childNodes.add(root.childNodes.get(root.childNodes.size() - 2));
		root.childNodes.remove(root.childNodes.size() - 2);
		root.childNodes.get(root.childNodes.size() - 1).childNodes.get(0).parentNode = root.childNodes
				.get(root.childNodes.size() - 1);
		return root.childNodes.get(root.childNodes.size() - 1);

	}

	public AST expressionEscape(AST root) {

		int count = 0;
		
		
		while (root.parentNode.node_type == "Operator") {
			count++;
			System.out.println(root.value);
			root = root.parentNode;
		}
		
		if (root.node_type == "Operator" && root.parentNode.node_type != "ForLoop"
				&& root.parentNode.node_type != "FunctionCall") {
			return root.parentNode;
		} else {
			return root;
		}
		// return root.parentNode;
	}
	
	public AST orderOfOperations(AST root, String token) {
		
		
		System.out.println("THE ROOT BEFORE ORDER OF OPERATIONS "+root.node_type+"  "+token);
		
		if (token.equals( "*" )|| token.equals("/") || token.equals("and")) {
		
		} else {
			boolean rel;
			if(token.equals("==" )|| token.equals( "<" )|| token.equals(">" )|| token.equals("<=")
							|| token.equals("<=" )|| token.equals( "!=") ){
				rel = true;
			}else {
				rel = false;
			}
			
			System.out.println(root.node_type);
			
			if(!(root.parentNode.node_type.equals("Operator")) && (root.node_type.equals("Operator")) ){
				
				if(rel) {
					if (!root.value.equals("==")&& !root.value.equals("<") && !root.value.equals(">") && !root.value.equals("<=")
							&& !root.value.equals("<=") && !root.value.equals("!=")
							) {
						root = root.parentNode;
						return root;
					}
				}
			else {
				if((!root.value.equals("==")&& !root.value.equals("<") && !root.value.equals(">") && !root.value.equals("<=")
						&& !root.value.equals("<=") && !root.value.equals("!=")
						&& !root.value.equals("+") && !root.value.equals("-")
						&& !root.value.equals("or"))) {
					root = root.parentNode;
					return root;
				}
			}
			}
			while (root.parentNode.node_type.equals("Operator")) {
				System.out.println("ORDER OF OPERATIONSORDER OF OPERATIONSORDER OF OPERATIONSORDER OF OPERATIONSORDER OF OPERATIONSORDER OF OPERATIONSORDER OF OPERATIONSORDER OF OPERATIONS");
				if(rel) {
					System.out.println("RELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELRELREL");
					if (!root.value.equals("==")&& !root.value.equals("<") && !root.value.equals(">") && !root.value.equals("<=")
							&& !root.value.equals("<=") && !root.value.equals("!=")
							) {
						root = root.parentNode;
					} else {
						if (!root.value.equals("==")&& !root.value.equals("<") && !root.value.equals(">") && !root.value.equals("<=")
								&& !root.value.equals("<=") && !root.value.equals("!=") ) {
							root = root.parentNode;
						}
						break;
					}
				root = root.parentNode;
				}
				else {
				if (!root.value.equals("==")&& !root.value.equals("<") && !root.value.equals(">") && !root.value.equals("<=")
						&& !root.value.equals("<=") && !root.value.equals("!=")
						&& !root.parentNode.value.equals("+") && !root.parentNode.value.equals("-")
						&& !root.parentNode.value.equals("or")) {
					root = root.parentNode;
				} else {
					if( !root.value.equals("==")&& !root.value.equals("<") && !root.value.equals(">") && !root.value.equals("<=")
					&& !root.value.equals("<=") && !root.value.equals("!=")
					&& !root.parentNode.value.equals("+") && !root.parentNode.value.equals("-")
					&& !root.parentNode.value.equals("or")) {
						root = root.parentNode;
					}
					break;
				}

			}
			}
		}
		
		System.out.println("THE ROOT BEFORE ORDER OF OPERATIONS "+root.value);
		return root;
	}

}
