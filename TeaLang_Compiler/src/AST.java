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
	
	public AST operatorSwitch(AST root, String value) {
			
		root.addNode("Operator", value);
		root.childNodes.get(root.childNodes.size()-1).childNodes.add(root.childNodes.get(root.childNodes.size()-2));
		root.childNodes.remove(root.childNodes.size()-2);
		root.childNodes.get(root.childNodes.size()-1).childNodes.get(0).parentNode = root.childNodes.get(root.childNodes.size()-1);
		return root.childNodes.get(root.childNodes.size() - 1);
		
		
	}
	
	public AST expressionEscape(AST root) {
		
		int count = 0;
		
		while(root.parentNode.node_type == "Operator") {
			count++;
			System.out.println(root.value);
			root = root.parentNode;
		}
		
		if(root.node_type == "Operator" && root.parentNode.node_type != "ForLoop" && root.parentNode.node_type != "FunctionCall") {
		return root.parentNode;
		}
		else {
			return root;
		}
		//return root.parentNode;
	}
	
	
	public void traverse(AST root, int tabs) {
		
		if(tabs == 7) {
			//System.exit(1);
		}
		
		AST temp;
		
		String indentation = "";
		
		for(int i =0; i < tabs; i++) {
			indentation += "\t";
		}
		
		if(root.childNodes.size() == 0) {
			
			System.out.print(indentation+"<"+root.node_type+">");
			System.out.print(root.value);
			System.out.print("</"+root.node_type+">\n");
			return;
			
		}
		else {
			if(root.value == null) {
				System.out.println(indentation+"<"+root.node_type+">");
			}else {
				System.out.println(indentation+"<"+root.node_type+" = "+root.value+">");
			}
			
		
			for(int i =0; i<root.childNodes.size(); i++) {
				
				temp = root.childNodes.get(i);
				root.traverse(temp, tabs+1);
				
				
			}
			System.out.println(indentation+"</"+root.node_type+">");
			return;
		}
		
		
	}
	
}
