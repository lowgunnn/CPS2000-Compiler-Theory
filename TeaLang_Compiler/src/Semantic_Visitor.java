import java.util.*;

public class Semantic_Visitor {

	static Stack<Map<String, String>> symbol_table = new Stack<Map<String, String>>();

	public Stack<Map<String, String>> traverse(AST root) {

		Map<String, String> map = new HashMap<String, String>();
		symbol_table.push(map);

		AST temp;

		String indentation = "";

		if (root.childNodes.size() == 0) {
			System.out.println(symbol_table);
			return symbol_table;

		} else {
			if (root.value == null) {

			} else {

			}

			for (int i = 0; i < root.childNodes.size(); i++) {

				temp = root.childNodes.get(i);

				if (temp.node_type == "VariableDecl") {

					if (checkVariable(temp.childNodes.get(0).value)) {
						System.out.println(
								"Semantic Error, variable " + temp.childNodes.get(0).value + " already exists!");
						System.exit(1);
					} else {
						symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).value,
								temp.childNodes.get(1).value);
						continue;
					}

				} else if (temp.node_type == "ForLoop" || temp.node_type == "WhileLoop"
						|| temp.node_type == "IfStatements" || temp.node_type == "ElseBlock"
						|| temp.node_type == "Block") {

					this.traverse(temp);
					symbol_table.pop();

				} else if (temp.node_type == "FunctionDecl") {
					
					
					if(checkReturn(temp)) {
						System.out.println("Function "+temp.childNodes.get(1).value+" has no return statement!");
						System.exit(1);
					}else {

					symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(1).value,
							temp.childNodes.get(0).value);
					this.traverse(temp);
					symbol_table.pop();
					}
				} else if (temp.node_type == "FormalParams") {

					for (int j = 0; j < temp.childNodes.size(); j += 2) {

						if (checkVariable(temp.childNodes.get(j).value)) {
							System.out.println(
									"Semantic Error, variable " + temp.childNodes.get(j).value + " already exists!");
							System.exit(1);
						} else {

							symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(j).value,
									temp.childNodes.get(j + 1).value);
						}
					}

				} else if(temp.node_type == "VariableAssignment") {
					
					if(!checkVariable(temp.childNodes.get(0).value)){
						System.out.println("Variable "+temp.childNodes.get(0).value+" has not been declared!");
						System.exit(1);
					}
					
				}

			}
			System.out.println(symbol_table);
			return symbol_table;
		}

	}

	public boolean checkVariable(String variable_identifier) {

		for (int i = 0; i < symbol_table.size(); i++) {

			if (symbol_table.get(i).containsKey(variable_identifier)) {
				return true;
			}

		}

		return false;

	}
	
	public boolean checkReturn(AST root) {
		
		AST temp;
		
		for (int i = 0; i < root.childNodes.size(); i++) {

			temp = root.childNodes.get(i);
			
			
			if(temp.node_type == "ReturnStatement") {
				
				return false;
			}
			
			boolean flag = checkReturn(temp);
			if(flag == false) {
				return false;
			}
		}
		
		return true;
	}

}
