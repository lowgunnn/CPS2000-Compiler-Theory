import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;



public class Interpreter_Visitor {
	
	static String last_return;
	
	static Stack<Map<String, String>> symbol_table = new Stack<Map<String, String>>();
	static Stack<Map<String, String>> value_table = new Stack<Map<String, String>>();
	
	static Map<String, AST> function_map = new LinkedHashMap<String, AST>();
	
	static Map<String, ArrayList<String>> function_headers = new LinkedHashMap<String, ArrayList<String>>();

	public String traverse(AST root, boolean create_scope) {
		
		
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		Map<String, String> map2 = new LinkedHashMap<String, String>();
		// preferable from ordinary maps, so we can preserve the order of inserted keys.
		if(create_scope) {
		symbol_table.push(map);
		value_table.push(map2);
		}
		AST temp;

		
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
						String[] value_type = valueCheck(temp.childNodes.get(2), temp.childNodes.get(1).value);

						symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).value,
								temp.childNodes.get(1).value);
						
						value_table.get(value_table.size() - 1).put(temp.childNodes.get(0).value,
								value_type[0]);

						// Evaluate Expression Type

						continue;
					}

				}else if(temp.node_type == "IfStatements") {
					
				
					
					if(valueCheck(temp.childNodes.get(0),"bool")[0].equals("true") ) {
						
						this.traverse(temp.childNodes.get(1),true);
					}
					else {
						//check if else block exists
						
						if(temp.childNodes.size() == 3) {
							this.traverse(temp.childNodes.get(2),true);
						}
						
					}
					symbol_table.pop();
					value_table.pop();
					
				}else if (temp.node_type == "WhileLoop") {
					
					symbol_table.push(new LinkedHashMap<String, String>());
					value_table.push(new LinkedHashMap<String, String>());
					
					
					boolean condition = Boolean.valueOf(valueCheck(temp.childNodes.get(0),"bool")[0]);
					
					
					while(condition) {
						
						this.traverse(temp.childNodes.get(1),false);
					    condition = Boolean.valueOf(valueCheck(temp.childNodes.get(0),"bool")[0]);
					}
					
					symbol_table.pop();
					value_table.pop();
				}
				else if (temp.node_type == "ForLoop" ) {
					
					symbol_table.push(new LinkedHashMap<String, String>());
					value_table.push(new LinkedHashMap<String, String>());
					
					String[] value_type = valueCheck(temp.childNodes.get(0).childNodes.get(2), temp.childNodes.get(0).childNodes.get(1).value);
					
					
					
					symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).childNodes.get(0).value,
							temp.childNodes.get(0).childNodes.get(1).value);
					
					value_table.get(value_table.size() - 1).put(temp.childNodes.get(0).childNodes.get(0).value,
							value_type[0]);
					
					
					boolean condition = Boolean.valueOf(valueCheck(temp.childNodes.get(1),"bool")[0]);
					
					while(condition) {
						
						this.traverse(temp.childNodes.get(3),false);
						
						

						String expected_type = getType(temp.childNodes.get(2).childNodes.get(0).value);

						String[] value_type_2 = valueCheck(temp.childNodes.get(2).childNodes.get(1), expected_type);
						
						
						for (int k = 0; k < value_table.size(); k++) {
							
							if (value_table.get(k).containsKey(temp.childNodes.get(2).childNodes.get(0).value)) {
								value_table.get(k).put(temp.childNodes.get(2).childNodes.get(0).value, value_type_2[0]);
							}

						}
						
						condition = Boolean.valueOf(valueCheck(temp.childNodes.get(1),"bool")[0]);
					}

					symbol_table.pop();
					value_table.pop();

				} else if (temp.node_type == "FunctionDecl") {
						
						

						symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(1).value,
								temp.childNodes.get(0).value);
						// create function header space
						function_headers.put(temp.childNodes.get(1).value, new ArrayList<String>());

						function_map.put(temp.childNodes.get(1).value, temp);
						
						
						symbol_table.push(new LinkedHashMap<String, String>());
						value_table.push(new LinkedHashMap<String, String>());
				
						this.traverse(temp,true);
						

						symbol_table.pop();
						value_table.pop();
					
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
					
					String function_name = parent.childNodes.get(1).value;
										
					
					String expected_type = getType(function_name);
					// variable
					
					String[] key_value = valueCheck(temp.childNodes.get(0), expected_type);
					
					//System.out.println(key_value[0]);
					
					last_return =  key_value[0];	
						
						//this.traverse(temp);
					

					
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

					// ADD FUNCTION SIGNATURES HERE

				} else if (temp.node_type == "VariableAssignment") {

					if (!checkVariable(temp.childNodes.get(0).value)) {
						System.out.println("Variable " + temp.childNodes.get(0).value + " has not been declared!");
						System.exit(1);
					} else {

						String expected_type = getType(temp.childNodes.get(0).value);

						String[] value_type = valueCheck(temp.childNodes.get(1), expected_type);
						
						
						for (int k = 0; k < value_table.size(); k++) {
							
							if (value_table.get(k).containsKey(temp.childNodes.get(0).value)) {
								value_table.get(k).put(temp.childNodes.get(0).value, value_type[0]);
							}

						}
						
						
						

					}

				} else if (temp.node_type == "PrintStatements") {

					switch (temp.childNodes.get(0).node_type) {

					case "Float_Value":
					case "Integer_Value":
					case "String_Value":
					case "True_Keyword":
					case "False_Keyword":
						System.out.println(temp.childNodes.get(0).value);
						break;

					case "Variable_Identifier":
						
						
						
						System.out.println(getValue(temp.childNodes.get(0).value));

						break;

					case "FunctionCall":
						String value = traverse(temp,false);
						System.out.println(value);
						break;

					case "Operator":
						String value_type[] =	expressionOperationTraversal(temp.childNodes.get(0));
						
						System.out.println(value_type[0]);
					}

				} else if (temp.node_type == "FunctionCall") {

			
					String parameter_type;
					String expected_parameter_type;
					
					String function_name = temp.value;
					
					AST function_node = function_map.get(function_name);
					
					String[] value_type;
					symbol_table.push(new LinkedHashMap<String, String>());
					value_table.push(new LinkedHashMap<String, String>());
			
					
					for (int z = 0; z < temp.childNodes.size(); z++) {

						expected_parameter_type = function_headers.get(temp.value).get(z);
						
						value_type = valueCheck(temp.childNodes.get(z), expected_parameter_type);
						
						
						symbol_table.get(symbol_table.size() - 1).put(function_node.childNodes.get(2).childNodes.get(z*2).value,
								function_node.childNodes.get(2).childNodes.get((z*2)+1 ).value);
						
						value_table.get(value_table.size() - 1).put(function_node.childNodes.get(2).childNodes.get(z*2).value,
								value_type[0]);

					}
					
					traverse(function_node.childNodes.get(function_node.childNodes.size()-1), false);
					symbol_table.pop();
					value_table.pop();
					return last_return;
				}

		

			}
			
			return "";
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

	public String getValue(String variable_identifier) {

		for (int i = value_table.size(); i > 0; i--) {

			if (value_table.get(i-1).containsKey(variable_identifier)) {
				return value_table.get(i-1).get(variable_identifier);
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
			} else {
				return variable_type;
			}

		} else {

			System.out.println("Semantic Error, variable " + variable_identifier + " has not been declared!");
			System.exit(1);

		}
		return "";
	}

	public String[] valueCheck(AST node, String expected_type) {

		if (node.node_type == "Variable_Identifier" ) {

			return new String[] { getValue(node.value), getType(node.value) };
			
			
		}else if ( node.node_type == "FunctionCall") {
			
		
			String value = traverse(node.parentNode, false);
			
			return new String[] {value, getType(node.value)};
			
			
				
			}
		 else if (node.node_type == "Integer_Value" || node.node_type == "Float_Value"
				|| node.node_type == "String_Value" || node.node_type == "True_Keyword"
				|| node.node_type == "False_Keyword") {

			switch (node.node_type) {

			case "Integer_Value":
				if (expected_type.equals("int")) {
					return new String[] {node.value,"int"};
				} else {
					System.out.println("Expected " + expected_type + " value, instead of int literal");
					System.exit(1);
				}

			case "Float_Value":
				if (expected_type.equals("float")) {
					return new String[] {node.value,"float"};
				} else {
					System.out.println("Expected " + expected_type + " value, insted of float literal");
					System.exit(1);
				}

			case "String_Value":
				if (expected_type.equals("string")) {
					return new String[] {node.value,"string"};
				} else {
					System.out.println("Expected " + expected_type + " value, isntead of string literal");
					System.exit(1);
				}

			case "True_Keyword":
			case "False_Keyword":
				if (expected_type.equals("bool")) {
					return new String[] {node.value,"bool"};
				} else {
					System.out.println("Expected " + expected_type + " value, instead of bool literal");
					System.exit(1);
				}

			}

		} else {
			// is an operator so we must evaluate the expression and climb da tree
			
			String value_type[] = expressionOperationTraversal(node);
			
			if (!expected_type.equals(value_type[1])) {
				
				System.out.println();
				System.out.println("Expected " + expected_type + " value, instead of " + value_type[1] + " expression");
				System.exit(1);
			} else {
				return value_type;
			}

		}
		return new String[] { ""};
	}

	public String[] expressionOperationTraversal(AST node) {
		// expression nodes will have either one or two children , a traversal of the
		// tree
		// System.out.println(node.value);
		
		if (node.childNodes.size() == 0) {

			if (node.node_type == "Variable_Identifier") {
				return new String[] { getValue(node.value), getType(node.value) };
			} else if (node.node_type == "FunctionCall") {
				// function call
				String value = traverse(node.parentNode, false);
				
				return new String[] {value, getType(node.value)};
			} else {
				switch (node.node_type) {
				case "Integer_Value":
					return new String[] { node.value, "int" };

				case "Float_Value":
					return new String[] { node.value, "float" };

				case "String_Value":
					
					return new String[] { node.value, "string" };

				case "True_Keyword":
				case "False_Keyword":
					return new String[] { node.value, "bool" };

				}

			}

		} else if (node.childNodes.size() == 2) {
			
			String[] type_value_1 = expressionOperationTraversal(node.childNodes.get(0));
			String[] type_value_2 = expressionOperationTraversal(node.childNodes.get(1));
			
	
			
			String[] resultant_type = checkOperatorConstraint(node, type_value_1[1], type_value_1[0], type_value_2[0]);
			return resultant_type;
		}

		else if (node.childNodes.size() == 1) {
			
		
			String[] type_value = expressionOperationTraversal(node.childNodes.get(0));
			
			
			if(type_value[1] == "bool") {
				
				boolean value = Boolean.valueOf(type_value[0]);
				return new String[] {String.valueOf(!value), "bool"};
			}
			
			if(node.node_type.equals("FunctionCall")) {
				
				String value = traverse(node.parentNode, false);
				
				return new String[] {value, getType(node.value)};
				
			}
			
			if(node.value.equals("+")) {
				
				System.out.println("HMM");
				
				return new String[] {type_value[0], type_value[1]};
			}
			
			if(node.value.equals( "-")) {
				
				int negative = Integer.valueOf(type_value[0])*-1;
				
				return new String[] {String.valueOf(negative), type_value[1]};
			}
				
			
			
		}

		return new String[] {""};
	}

	public String[] checkOperatorConstraint(AST operator_node, String type1, String value1, String value2) {

		int int_result;
		float fl_result;
		boolean bool_result;

		switch (type1) {
		case "string":
			if (operator_node.value.equals("+")) {
				
				
				
				return new String[] { value1.concat(value2), "string" };
			} else if (operator_node.value.equals("==")) {

				if (value1.equals(value2))
					return new String[] { "true", "bool" };
				else
					return new String[] { "false", "bool" };
			} else if (operator_node.value.equals("!=")) {

				if (value1.equals(value2))
					return new String[] { "false", "bool" };
				else
					return new String[] { "true", "bool" };
			} else {
				System.out.println("Semantic Error " + operator_node.value + " operation not defined for strings!");
				System.exit(1);
			}
			break;
		case "int":
			switch (operator_node.value) {

			case "+":

				int_result = Integer.parseInt(value1) + Integer.parseInt(value2);

				return new String[] { String.valueOf(int_result), "int" };
			
			case "-":

				int_result = Integer.parseInt(value1) - Integer.parseInt(value2);

				return new String[] { String.valueOf(int_result), "int" };
		
			case "*":
				
				
				int_result = Integer.parseInt(value1) * Integer.parseInt(value2);

				return new String[] { String.valueOf(int_result), "int" };
		
			case "/":

				int_result = Integer.parseInt(value1) / Integer.parseInt(value2);

				return new String[] { String.valueOf(int_result), "int" };

			case "<":

				bool_result = Integer.parseInt(value1) < Integer.parseInt(value2);

				return new String[] { String.valueOf(bool_result), "bool" };
			

			case ">":

				bool_result = Integer.parseInt(value1) > Integer.parseInt(value2);

				return new String[] { String.valueOf(bool_result), "bool" };
		
			case "<=":

				bool_result = Integer.parseInt(value1) <= Integer.parseInt(value2);

				return new String[] { String.valueOf(bool_result), "int" };
		
			case ">=":

				bool_result = Integer.parseInt(value1) >= Integer.parseInt(value2);

				return new String[] { String.valueOf(bool_result), "bool" };
			
			case "==":

				bool_result = Integer.parseInt(value1) == Integer.parseInt(value2);

				return new String[] { String.valueOf(bool_result), "bool" };
			

			case "!=":

				bool_result = Integer.parseInt(value1) != Integer.parseInt(value2);

				return new String[] { String.valueOf(bool_result), "bool" };

			}
			break;
		case "float":
			switch (operator_node.value) {

			case "+":

				fl_result = Float.parseFloat(value1) + Float.parseFloat(value2);

				return new String[] { String.valueOf(fl_result), "float" };
			case "-":

				fl_result = Float.parseFloat(value1) - Float.parseFloat(value2);

				return new String[] { String.valueOf(fl_result), "float" };
		
			case "*":

				fl_result = Float.parseFloat(value1) * Float.parseFloat(value2);

				return new String[] { String.valueOf(fl_result), "float" };
				
			case "/":
				
								
				fl_result = Float.parseFloat(value1) / Float.parseFloat(value2);
				
				return new String[] { String.valueOf(fl_result), "float" };
				

			case "<":

				bool_result = Float.parseFloat(value1) < Float.parseFloat(value2);

				return new String[] { String.valueOf(bool_result), "bool" };
				

			case ">":

				bool_result = Float.parseFloat(value1) > Float.parseFloat(value2);

				return new String[] { String.valueOf(bool_result), "bool" };
				
			case "<=":

				bool_result = Float.parseFloat(value1) <= Float.parseFloat(value2);

				return new String[] { String.valueOf(bool_result), "bool" };
				

			case ">=":

				bool_result = Float.parseFloat(value1) >= Float.parseFloat(value2);

				return new String[] { String.valueOf(bool_result), "bool" };
		
			case "==":

				bool_result = Float.parseFloat(value1) == Float.parseFloat(value2);

				return new String[] { String.valueOf(bool_result), "bool" };


			case "!=":

				bool_result = Float.parseFloat(value1) != Float.parseFloat(value2);

				return new String[] { String.valueOf(bool_result), "bool" };

			}
		case "bool":
			switch (operator_node.value) {
			
			case "and":
				
				bool_result = Boolean.parseBoolean(value1) && Boolean.parseBoolean(value2);
				return new String[] {String.valueOf(bool_result) , "bool"};
				
			case "or":
				
				bool_result = Boolean.parseBoolean(value1) || Boolean.parseBoolean(value2);
				return new String[] {String.valueOf(bool_result) , "bool"};
				
			case "==":
				
				bool_result = Boolean.parseBoolean(value1) == Boolean.parseBoolean(value2);
				return new String[] {String.valueOf(bool_result) , "bool"};
				
			case "!=":
				
				bool_result = Boolean.parseBoolean(value1) != Boolean.parseBoolean(value2);
				return new String[] {String.valueOf(bool_result) , "bool"};
			
			}

		}

		return new String[] {""};
	}

}
