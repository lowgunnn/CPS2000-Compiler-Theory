import java.util.*;

public class Semantic_Visitor {

	static Stack<Map<String, String>> symbol_table = new Stack<Map<String, String>>();

	public Stack<Map<String, String>> traverse(AST root) {

		Map<String, String> map = new LinkedHashMap<String, String>();
		//preferrable from ordinary maps, so we can preserve the order of inserted keys.
		symbol_table.push(map);

		AST temp;

		

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
						
						
						
						typeCheck(temp.childNodes.get(2), temp.childNodes.get(1).value);
						
						symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).value,
								temp.childNodes.get(1).value);
						
						//Evaluate Expression Type
						
						continue;
					}

				} else if (temp.node_type == "ForLoop" || temp.node_type == "WhileLoop"
						|| temp.node_type == "IfStatements" || temp.node_type == "ElseBlock"
						|| temp.node_type == "Block") {

					this.traverse(temp);
					symbol_table.pop();

				} else if (temp.node_type == "FunctionDecl") {
					
					
					
					if(checkVariable(temp.childNodes.get(1).value)) {
						System.out.println("Function with the name "+temp.childNodes.get(1).value+" already exists!");
						System.exit(1);
					}
					
					
					if(checkReturn(temp)) {
						System.out.println("Function "+temp.childNodes.get(1).value+" has no return statement!");
						System.exit(1);
					}else {

					symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(1).value,
							temp.childNodes.get(0).value);
					this.traverse(temp);
					symbol_table.pop();
					}
				} else if(temp.node_type == "ReturnStatement") {
					
					//evaluate the expression, check if it matches with the last declared function's return type
					AST parent;
					
					parent = temp.parentNode;
					int levels = 1;
					while(parent.node_type != "FunctionDecl") {
						levels++;
						parent = parent.parentNode;
					}
					
					
					Object[] key_set = symbol_table.get(symbol_table.size()-(levels)-1).keySet().toArray();
					
					String expected_type = symbol_table.get(symbol_table.size()-(levels)-1).get(key_set[key_set.length - 1]);
					
	
						//variable 
						
					typeCheck(temp.childNodes.get(0), expected_type);
						/*if(temp.childNodes.get(0).node_type == "Variable_Identifier" || temp.childNodes.get(0).node_type == "FunctionCall") {
							
					
							evaluateVariable(temp.childNodes.get(0).value, return_type);
							
							
						}else if(temp.childNodes.get(0).node_type == "Integer_Value" || temp.childNodes.get(0).node_type == "Float_Value" 
								||temp.childNodes.get(0).node_type == "String_Value" || temp.childNodes.get(0).node_type == "True_Keyword"
								||temp.childNodes.get(0).node_type == "False_Keyword"){
							
								switch(temp.childNodes.get(0).node_type) {
								
								case "Integer_Value":
									if(return_type.equals("int")){
										break;
									}else {
										System.out.println("Expected "+return_type+" Return, instead of int literal");
										System.exit(1);
									}
									
								case "Float_Value":
									if(return_type.equals("float")){
										break;
									}else {
										System.out.println("Expected "+return_type+" Return, insted of float literal");
										System.exit(1);
									}
								
								case "String_Value":
									if(return_type.equals("string")){
										break;
									}else {
										System.out.println("Expected "+return_type+" Return, isntead of string literal");
										System.exit(1);
									}
								
								case "True_Keyword":
								case "False_Keyword":
									if(return_type.equals("bool")){
										break;
									}else {
										System.out.println("Expected "+return_type+" Return, instead of bool literal");
										System.exit(1);
									}
								
								}
							
							
							
						}else {
							
							
							
							
						}*/
						
						
					}
					
				
				
				else if (temp.node_type == "FormalParams") {

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
	
	public String getType(String variable_identifier) {
	
		for (int i = 0; i < symbol_table.size(); i++) {

			if (symbol_table.get(i).containsKey(variable_identifier)) {
				return symbol_table.get(i).get(variable_identifier);
			}
		
	}
	//because conditional return scope
	return "None";
	}
	
	public void evaluateVariable(String variable_identifier, String expected_return) {
		
		
		
		if (checkVariable(variable_identifier)) {
			
			String variable_type = getType(variable_identifier);
			
			
			
			if(!expected_return.equals(variable_type)) {
				System.out.println("Semamtic Error, function expects "+expected_return+" return, instead got "+variable_type);
				System.exit(1);
			}
			
		} else {
			
			System.out.println(
					"Semantic Error, variable " + variable_identifier + " has not been declared!");
			System.exit(1);
			
		}
		
	}
	
	public void typeCheck(AST node, String expected_type) {
		
		if(node.node_type == "Variable_Identifier" || node.node_type == "FunctionCall") {
			
			
			evaluateVariable(node.value, expected_type);
			
			
		}else if(node.node_type == "Integer_Value" || node.node_type == "Float_Value" 
				||node.node_type == "String_Value" || node.node_type == "True_Keyword"
				||node.node_type == "False_Keyword"){
			
				switch(node.node_type) {
				
				case "Integer_Value":
					if(expected_type.equals("int")){
						break;
					}else {
						System.out.println("Expected "+expected_type+" value, instead of int literal");
						System.exit(1);
					}
					
				case "Float_Value":
					if(expected_type.equals("float")){
						break;
					}else {
						System.out.println("Expected "+expected_type+" value, insted of float literal");
						System.exit(1);
					}
				
				case "String_Value":
					if(expected_type.equals("string")){
						break;
					}else {
						System.out.println("Expected "+expected_type+" value, isntead of string literal");
						System.exit(1);
					}
				
				case "True_Keyword":
				case "False_Keyword":
					if(expected_type.equals("bool")){
						break;
					}else {
						System.out.println("Expected "+expected_type+" value, instead of bool literal");
						System.exit(1);
					}
				
				}
			
			
			
		}else {
			//is an operator so we must evaluate everything
			
			
			
		}
		
		
	}
	
}
