import java.util.*;

public class Semantic_Visitor {

	static Stack<Map<String, String>> symbol_table = new Stack<Map<String, String>>();
	
	static Map<String, ArrayList<String>> function_headers = new LinkedHashMap<String, ArrayList<String>>();

	public Stack<Map<String, String>> traverse(AST root) {

		Map<String, String> map = new LinkedHashMap<String, String>();
		// preferable from ordinary maps, so we can preserve the order of inserted keys.
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

						// type check before so we dont get, let x:int = x;
						typeCheck(temp.childNodes.get(2), temp.childNodes.get(1).value);

						symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).value,
								temp.childNodes.get(1).value);

						// Evaluate Expression Type

						continue;
					}

				} else if (temp.node_type == "ForLoop" || temp.node_type == "WhileLoop"
						|| temp.node_type == "IfStatements" || temp.node_type == "ElseBlock"
						|| temp.node_type == "Block") {
					
					
					if(temp.node_type == "WhileLoop"|| temp.node_type == "IfStatements") {
						
						typeCheck(temp.childNodes.get(0), "bool");
						//check that the condition evalutes to a bool type
					}
					
					
					
					this.traverse(temp);
					symbol_table.pop();

				} else if (temp.node_type == "FunctionDecl") {

					if (checkVariable(temp.childNodes.get(1).value)) {
						System.out
								.println("Function with the name " + temp.childNodes.get(1).value + " already exists!");
						System.exit(1);
					}
							
									
					
					if (checkReturn(temp)) {
						System.out.println("Function " + temp.childNodes.get(1).value + " has no return statement!");
						System.exit(1);
					} else {

						symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(1).value,
								temp.childNodes.get(0).value);
						//create function header space
						function_headers.put(temp.childNodes.get(1).value, new ArrayList<String>());
						
						this.traverse(temp);
						System.out.println(function_headers);
						
						symbol_table.pop();
					}
				} else if (temp.node_type == "ReturnStatement") {

					// evaluate the expression, check if it matches with the last declared
					// function's return type
					AST parent;

					parent = temp.parentNode;
					int levels = 1;
					while (parent.node_type != "FunctionDecl") {
						levels++;
						parent = parent.parentNode;
					}

					Object[] key_set = symbol_table.get(symbol_table.size() - (levels) - 1).keySet().toArray();

					String expected_type = symbol_table.get(symbol_table.size() - (levels) - 1)
							.get(key_set[key_set.length - 1]);

					// variable

					typeCheck(temp.childNodes.get(0), expected_type);

					/*
					 * if(temp.childNodes.get(0).node_type == "Variable_Identifier" ||
					 * temp.childNodes.get(0).node_type == "FunctionCall") {
					 * 
					 * 
					 * evaluateVariable(temp.childNodes.get(0).value, return_type);
					 * 
					 * 
					 * }else if(temp.childNodes.get(0).node_type == "Integer_Value" ||
					 * temp.childNodes.get(0).node_type == "Float_Value"
					 * ||temp.childNodes.get(0).node_type == "String_Value" ||
					 * temp.childNodes.get(0).node_type == "True_Keyword"
					 * ||temp.childNodes.get(0).node_type == "False_Keyword"){
					 * 
					 * switch(temp.childNodes.get(0).node_type) {
					 * 
					 * case "Integer_Value": if(return_type.equals("int")){ break; }else {
					 * System.out.println("Expected "+return_type+" Return, instead of int literal"
					 * ); System.exit(1); }
					 * 
					 * case "Float_Value": if(return_type.equals("float")){ break; }else {
					 * System.out.println("Expected "+return_type+" Return, insted of float literal"
					 * ); System.exit(1); }
					 * 
					 * case "String_Value": if(return_type.equals("string")){ break; }else {
					 * System.out.println("Expected "
					 * +return_type+" Return, isntead of string literal"); System.exit(1); }
					 * 
					 * case "True_Keyword": case "False_Keyword": if(return_type.equals("bool")){
					 * break; }else {
					 * System.out.println("Expected "+return_type+" Return, instead of bool literal"
					 * ); System.exit(1); }
					 * 
					 * }
					 * 
					 * 
					 * 
					 * }else {
					 * 
					 * 
					 * 
					 * 
					 * }
					 */

				}

				else if (temp.node_type == "FormalParams") {

					for (int j = 0; j < temp.childNodes.size(); j += 2) {

						if (checkVariable(temp.childNodes.get(j).value)) {
							System.out.println(
									"Semantic Error, variable " + temp.childNodes.get(j).value + " already exists!");
							System.exit(1);
						} else {
							
							String function_name = temp.parentNode.childNodes.get(1).value;
							
							ArrayList<String> parameters = function_headers.get(function_name);
							
							parameters.add(temp.childNodes.get(j + 1).value);
							
							function_headers.put(function_name, parameters);
							
							symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(j).value,
									temp.childNodes.get(j + 1).value);
						}
					}
						
					//ADD FUNCTION SIGNATURES HERE
					
				} else if (temp.node_type == "VariableAssignment") {

					if (!checkVariable(temp.childNodes.get(0).value)) {
						System.out.println("Variable " + temp.childNodes.get(0).value + " has not been declared!");
						System.exit(1);
					} else {

						String expected_type = getType(temp.childNodes.get(0).value);

						typeCheck(temp.childNodes.get(1), expected_type);
						
					}

				} else if(temp.node_type == "PrintStatement") {
					
					
					
				} else if(temp.node_type == "FunctionCall") {
					
					
					if(!checkVariable(temp.childNodes.get(0).value)) {
						System.out.println("Function with name "+temp.childNodes.get(0).value+" has not been declared yet.");
						System.exit(1);
					}
					//called function exists
					//check if same number of parameters
					
					if(function_headers.get(temp.childNodes.get(0).value).size() != temp.childNodes.size() -1) {
						System.out.println("Semantic Error, wrong arguments in function call "+temp.childNodes.get(0).value+"()");
						System.exit(1);
					}
					
					String parameter_type;
					String expected_parameter_type;
					
					
					for(int z = 1; z <temp.childNodes.size(); z++) {
						
						expected_parameter_type = function_headers.get(temp.childNodes.get(0).value).get(z-1);
						
					
						typeCheck(temp.childNodes.get(z), expected_parameter_type);
						
						
						
						}
						
					}
				
			
		else if(temp.parentNode.node_type == "ForLoop"){
					
					typeCheck(temp, "bool");
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

			if (temp.node_type == "ReturnStatement") {

				return false;
			}

			boolean flag = checkReturn(temp);
			if (flag == false) {
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
		// because conditional return scope
		return "None";
	}

	public String evaluateVariable(String variable_identifier, String expected_return) {

		if (checkVariable(variable_identifier)) {

			String variable_type = getType(variable_identifier);

			if (!expected_return.equals(variable_type)) {
				System.out.println("Semamtic Error, identifier expects " + expected_return + " return, instead got "
						+ variable_type + " from " + variable_identifier);
				System.exit(1);
			}else {
				return variable_type;
			}

		} else {

			System.out.println("Semantic Error, variable " + variable_identifier + " has not been declared!");
			System.exit(1);

		}
		return "";
	}

	public String typeCheck(AST node, String expected_type) {

		if (node.node_type == "Variable_Identifier" || node.node_type == "FunctionCall") {

			return evaluateVariable(node.value, expected_type);

		} else if (node.node_type == "Integer_Value" || node.node_type == "Float_Value"
				|| node.node_type == "String_Value" || node.node_type == "True_Keyword"
				|| node.node_type == "False_Keyword") {

			switch (node.node_type) {

			case "Integer_Value":
				if (expected_type.equals("int")) {
					return "int";
				} else {
					System.out.println("Expected " + expected_type + " value, instead of int literal");
					System.exit(1);
				}

			case "Float_Value":
				if (expected_type.equals("float")) {
					return "float";
				} else {
					System.out.println("Expected " + expected_type + " value, insted of float literal");
					System.exit(1);
				}

			case "String_Value":
				if (expected_type.equals("string")) {
					return "string";
				} else {
					System.out.println("Expected " + expected_type + " value, isntead of string literal");
					System.exit(1);
				}

			case "True_Keyword":
			case "False_Keyword":
				if (expected_type.equals("bool")) {
					return "bool";
				} else {
					System.out.println("Expected " + expected_type + " value, instead of bool literal");
					System.exit(1);
				}

			}

		} else {
			// is an operator so we must evaluate the expression and climb da tree
			String evaluated_type = expressionOperationTraversal(node);
			if(!expected_type.equals(evaluated_type)) {
				System.out.println();
				System.out.println("Expected " + expected_type + " value, instead of "+evaluated_type+" expression");
				System.exit(1);
			}else {
				return evaluated_type;
			}

		}
		return "";
	}

	public String expressionOperationTraversal(AST node) {
		// expression nodes will have either one or two children , a traversal of the
		// tree
		//System.out.println(node.value);
		if (node.childNodes.size() == 0) {

			if (node.node_type == "Variable_Identifier" || node.node_type == "FunctionCall") {
				return getType(node.value);
			} else {
				switch (node.node_type) {
				case "Integer_Value":
					return "int";

				case "Float_Value":
					return "float";

				case "String_Value":
					return "string";

				case "True_Keyword":
				case "False_Keyword":
					return "bool";

				}

			}

		} else if (node.childNodes.size() == 2) {
			String type1 = expressionOperationTraversal(node.childNodes.get(0));
			String type2 = expressionOperationTraversal(node.childNodes.get(1));
			
			/*if(type1.equals("bool") && !type2.equals("bool")) {
				String resultant_type = checkOperatorConstraint(node, type2);
				return resultant_type;
			}
			else if (!type1.equals("bool") && type2.equals("bool")) {
				String resultant_type = checkOperatorConstraint(node, type1);
				return resultant_type;
			}
			
			else */if (!type1.equals(type2)) {
				System.out.println(node.childNodes.get(0).value + " "+node.childNodes.get(1).value);
				System.out.println(type1+" "+type2);
				System.out.println("Semantic Error Mismatched types");
				System.exit(1);
			}
			else {
			String resultant_type = checkOperatorConstraint(node, type1);
			return resultant_type;
			}
			
			

		} else if(node.childNodes.size() == 1) {
			String type = expressionOperationTraversal(node.childNodes.get(0));
			
			return type;
		}

		return "";
	}

	public String checkOperatorConstraint(AST operator_node, String type1) {
		
		
		
		switch(type1) {
		case "string":
			if(operator_node.value.equals("+")) {
				return "string";
			}
			else if(operator_node.value.equals("==")) {
				return "bool";
			}
			else {
				System.out.println("Semantic Error " + operator_node.value + " operation not defined for strings!");
				System.exit(1);
			}
			break;
		case "int":
			if(operator_node.value.equals("+") || operator_node.value.equals("-") || operator_node.value.equals("*") || operator_node.value.equals("/")  ) {
				return "int";
			}
			else if(operator_node.value.equals("<") || operator_node.value.equals(">") || operator_node.value.equals("<=") || operator_node.value.equals(">=") ||
					operator_node.value.equals("==") || operator_node.value.equals("!=")){
				return "bool";
			}
			else {
				System.out.println("Semantic Error " + operator_node.value + " operation not defined for numbers!");
				System.exit(1);
			}
			break;
		case "float":
			if(operator_node.value.equals("+") || operator_node.value.equals("-") || operator_node.value.equals("*") || operator_node.value.equals("/")  ) {
				return "float";
			}
			else if(operator_node.value.equals("<") || operator_node.value.equals(">") || operator_node.value.equals("<=") || operator_node.value.equals(">=") ||
					operator_node.value.equals("==") || operator_node.value.equals("!=")){
				return "bool";
			}
			else {
				System.out.println("Semantic Error " + operator_node.value + " operation not defined for numbers!");
				System.exit(1);
			}
			break;
			
		case "bool":
			if(operator_node.value.equals("and") || operator_node.value.equals("or") || operator_node.value.equals("not") || operator_node.value.equals("!=") ||
			   operator_node.value.equals("==")) {
				return "bool";
			}
			else {
				System.out.println("Semantic Error " + operator_node.value + " operation not defined for bools!");
				System.exit(1);
			}
		}
			
return "";
	}

}
