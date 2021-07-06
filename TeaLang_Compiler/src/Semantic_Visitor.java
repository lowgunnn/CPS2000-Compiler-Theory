import java.util.*;

public class Semantic_Visitor {

	static Stack<Map<String, String>> symbol_table = new Stack<Map<String, String>>();

	static Map<String, ArrayList<ArrayList<String>>> function_headers = new LinkedHashMap<String, ArrayList<ArrayList<String>>>();
	static Map<String, ArrayList<String>> function_returns = new LinkedHashMap<String, ArrayList<String>>();

	static ArrayList<ArrayList<String>> arrays = new ArrayList<ArrayList<String>>();

	static ArrayList<String> created_Structs = new ArrayList<String>();
	static Map<String, Map<String, String>> struct_symbols = new LinkedHashMap<String, Map<String, String>>();

	static Map<String, Map<String, ArrayList<String>>> struct_functions = new LinkedHashMap<String, Map<String, ArrayList<String>>>();
	static Map<String, Map<String, ArrayList<String>>> struct_function_returns = new LinkedHashMap<String, Map<String, ArrayList<String>>>();

	public String traverse(AST root) {

		Map<String, String> map = new LinkedHashMap<String, String>();
		// preferable from ordinary maps, so we can preserve the order of inserted keys.
		symbol_table.push(map);
		arrays.add(new ArrayList<String>());
		AST temp;

		if (root.childNodes.size() == 0) {
			System.out.println(symbol_table);
			return "";

		} else {
			if (root.value == null) {

			} else {

			}
			System.out.println("DECLARED FUNCTIONS :" + function_headers);
			for (int i = 0; i < root.childNodes.size(); i++) {

				System.out.println("CURRENT SCOPE :" + symbol_table);
				System.out.println("ARRAYS IN SCOPE: " + arrays);

				temp = root.childNodes.get(i);

				if (temp.node_type == "Struct_Decleration") {

					created_Structs.add(temp.childNodes.get(0).value);
					struct_symbols.put(temp.childNodes.get(0).value, map);

					struct_functions.put(temp.childNodes.get(0).value, new LinkedHashMap<String, ArrayList<String>>());
					struct_function_returns.put(temp.childNodes.get(0).value,
							new LinkedHashMap<String, ArrayList<String>>());

					for (int n = 1; n < temp.childNodes.size(); n++) {

						if (temp.childNodes.get(n).node_type == "VariableDecl") {

							if (struct_symbols.get(temp.childNodes.get(0).value)
									.containsKey(temp.childNodes.get(n).childNodes.get(0).value)) {
								System.out.println("STRUCT VARIABLE ALREADY EXISTS!");
								System.exit(1);
							}

							struct_symbols.get(temp.childNodes.get(0).value).put(
									temp.childNodes.get(n).childNodes.get(0).value,
									temp.childNodes.get(n).childNodes.get(1).value);

						} else {

							ArrayList<String> params = new ArrayList<String>();

							for (int param = 1; param < temp.childNodes.get(n).childNodes.get(2).childNodes
									.size(); param += 2) {
								params.add(temp.childNodes.get(n).childNodes.get(2).childNodes.get(param).value);
							}
							
							if((struct_functions.get(temp.childNodes.get(0).value).get(temp.childNodes.get(n)).size() != 0)){
							if (struct_functions.get(temp.childNodes.get(0).value).get(temp.childNodes.get(n))
									.contains(params)) {
								System.out.println("STRUCT FUNCTION WITH SAME FUNCTION HEADER ALREADY EXISTS");
							}
							}else {
								struct_functions.get(temp.childNodes.get(0).value).put(temp.childNodes.get(n).childNodes.get(1).value, new ArrayList<String>());
							}
							struct_functions.get(temp.childNodes.get(0).value)
									.put(temp.childNodes.get(n).childNodes.get(1).value, params);
							struct_function_returns.get(temp.childNodes.get(0).value)
									.get(temp.childNodes.get(n).childNodes.get(1).value)
									.add(temp.childNodes.get(n).childNodes.get(0).value);

							System.out.println("???????"+function_returns);
						}

					}

				}

				else if (temp.node_type == "VariableDecl") {

					if (checkVariable(temp.childNodes.get(0).value)) {
						System.out.println(
								"Semantic Error, variable " + temp.childNodes.get(0).value + " already exists!");
						System.exit(1);
					} else {

						String type = "";
						// type check before so we dont get, let x:int = x;
						boolean array = false;
						if (temp.childNodes.get(0).childNodes.size() != 0) {
							// declared variable is an array.
							array = true;

						}

						if (!array) {
							if (!temp.childNodes.get(1).value.equals("auto")) {

								typeCheck(temp.childNodes.get(2), temp.childNodes.get(1).value);

								symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).value,
										temp.childNodes.get(1).value);
							} else {

								if (temp.childNodes.get(0).childNodes.size() != 0 && temp.childNodes.size() != 4) {
									// declared variable is an array.
									System.out.println("Semantic Array, AUTO Type cannot be determined.");
									System.exit(1);

								}

								type = expressionOperationTraversal(temp.childNodes.get(2));

								symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).value, type);

								temp.childNodes.get(1).value = type;
							}
						} else {

							String error = typeCheck(temp.childNodes.get(1), "int");
							if (error.equals("exit")) {
								System.out.println("Semantic Error, array must have integer index ");
								System.exit(1);
							}

							//
							if (!temp.childNodes.get(2).value.equals("auto")) {

								// initialised
								if (temp.childNodes.size() > 3) {
									type = expressionOperationTraversal(temp.childNodes.get(3).childNodes.get(0));

									for (int e = 0; e < temp.childNodes.get(3).childNodes.size(); e++) {

										error = typeCheck(temp.childNodes.get(3).childNodes.get(e), type);

										if (error.equals("exit")) {
											System.out
													.println("Semantic Error, array has mismatched elements. Expecting "
															+ type + " in array " + temp.childNodes.get(0).value);
											System.exit(1);
										}
									}
									symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).value, type);

								} else {

									symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).value,
											temp.childNodes.get(2).value);

								} // uninitialised

							} else {

								if (temp.childNodes.size() != 4
										|| !temp.childNodes.get(3).node_type.equals("Elements")) {
									System.out.println(
											"Semantic Error, auto type cannot be determined from unitialised array.");
									System.exit(1);
								}

								type = expressionOperationTraversal(temp.childNodes.get(3).childNodes.get(0));

								for (int e = 0; e < temp.childNodes.get(3).childNodes.size(); e++) {

									error = typeCheck(temp.childNodes.get(3).childNodes.get(e), type);

									if (error.equals("exit")) {
										System.out.println("Semantic Error, array has mismatched elements. Expecting "
												+ type + " in array " + temp.childNodes.get(0).value);
										System.exit(1);
									}
								}

								symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).value, type);

								temp.childNodes.get(2).value = type;

							}

							// add array to current scope of arrays.

							arrays.get(arrays.size() - 1).add(temp.childNodes.get(0).value);
						}
						// Evaluate Expression Type

						continue;
					}

				} else if (temp.node_type == "ForLoop" || temp.node_type == "WhileLoop"
						|| temp.node_type == "IfStatements" || temp.node_type == "ElseBlock"
						|| temp.node_type == "Block") {

					if (temp.node_type == "WhileLoop" || temp.node_type == "IfStatements") {

						typeCheck(temp.childNodes.get(0), "bool");
						// check that the condition evalutes to a bool type
					}

					this.traverse(temp);
					symbol_table.pop();
					arrays.remove(arrays.size() - 1);

				} else if (temp.node_type == "FunctionDecl") {

					if (checkVariable(temp.childNodes.get(1).value)) {

					} else {
						function_headers.put(temp.childNodes.get(1).value, new ArrayList<ArrayList<String>>());
						function_returns.put(temp.childNodes.get(1).value, new ArrayList<String>());
					}

					if (checkReturn(temp)) {
						System.out.println("Function " + temp.childNodes.get(1).value + " has no return statement!");
						System.exit(1);
					} else {

						symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(1).value,
								temp.childNodes.get(0).value);
						System.out.println("Symbol " + symbol_table);
						function_returns.get(temp.childNodes.get(1).value).add(temp.childNodes.get(0).value);
						System.out.println("Returns for a function" + function_returns);

						// create function header space
						ArrayList<ArrayList<String>> overloaders = function_headers.get(temp.childNodes.get(1).value);

						overloaders.add(new ArrayList<String>());

						function_headers.put(temp.childNodes.get(1).value, overloaders);

						System.out.println(function_headers);
						System.out.println(arrays);
						this.traverse(temp);
						arrays.remove(arrays.size() - 1);
						System.out.println(function_headers);

						symbol_table.pop();

					}
				} else if (temp.node_type == "ReturnStatement") {

					// evaluate the expression, check if it matches with the last declared
					// function's return type
					AST parent;

					parent = temp.parentNode;
					int levels = 1;
					try {
						while (parent.node_type != "FunctionDecl") {
							levels++;

							parent = parent.parentNode;

						}
					} catch (Exception e) {
						System.out.println("Semantic Error Return outside of function decleration.");
						System.exit(1);
					}

					Object[] key_set = symbol_table.get(symbol_table.size() - (levels) - 1).keySet().toArray();

					String expected_type = parent.childNodes.get(0).value;

					// variable

					if (expected_type.equals("auto")) {

						String type = expressionOperationTraversal(temp.childNodes.get(0));
						function_returns.get(parent.childNodes.get(1).value)
								.set(function_returns.get(parent.childNodes.get(1).value).size() - 1, type);
						symbol_table.get(0).put(parent.childNodes.get(1).value, type);

						parent.childNodes.get(0).value = type;
					}

					else {

						typeCheck(temp.childNodes.get(0), expected_type);
					}

				}

				else if (temp.node_type == "FormalParams") {

					String function_name = temp.parentNode.childNodes.get(1).value;

					for (int j = 0; j < temp.childNodes.size(); j += 2) {

						if (checkVariable(temp.childNodes.get(j).value)) {
							System.out.println(
									"Semantic Error, variable " + temp.childNodes.get(j).value + " already exists!");
							System.exit(1);
						} else {

							ArrayList<ArrayList<String>> parameters = function_headers.get(function_name);

							parameters.get(parameters.size() - 1).add(temp.childNodes.get(j + 1).value);

							function_headers.put(function_name, parameters);

							symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(j).value,
									temp.childNodes.get(j + 1).value);

							if (temp.childNodes.get(j).childNodes.size() != 0) {
								arrays.get(arrays.size() - 1).add(temp.childNodes.get(j).value);
								System.out.println(arrays);
							}

						}
					}

					for (int k = 0; k < function_headers.get(function_name).size() - 1; k++) {

						if (function_headers.get(function_name).get(k).equals(
								function_headers.get(function_name).get(function_headers.get(function_name).size() - 1))
								&& function_headers.get(function_name).size() != 1) {
							System.out.println("SEMANTIC ERROR, OVERLOADED FUNCTION WITH SAME HEADER ALREADY EXISTS");
							System.exit(1);
						}
					}

					// ADD FUNCTION SIGNATURES HERE

				} else if (temp.node_type == "VariableAssignment") {

					if (!checkVariable(temp.childNodes.get(0).value)) {
						System.out.println("Variable " + temp.childNodes.get(0).value + " has not been declared!");
						System.exit(1);
					} else {

						if (temp.childNodes.get(0).childNodes.size() != 0) {

							String error = typeCheck(temp.childNodes.get(0).childNodes.get(1), "int");
							if (error.equals("exit")) {
								System.out.println("Semantic Error, array must have integer index ");
								System.exit(1);
							}

							String expected_type = getType(temp.childNodes.get(0).value);

							if (typeCheck(temp.childNodes.get(1), expected_type).equals("exit")) {
								System.out.println("Semantic Error, Array element must be set to " + expected_type);
								System.exit(1);
							}

						} else {
							String expected_type = getType(temp.childNodes.get(0).value);

							if (typeCheck(temp.childNodes.get(1), expected_type).equals("exit")) {
								System.out.println("Semantic Error, variable " + temp.childNodes.get(1)
										+ " must be set to " + expected_type);
								System.exit(1);
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
					case "Char_Value":
						break;

					case "Variable_Identifier":

						if (!checkVariable(temp.childNodes.get(0).value)) {
							System.out.println("Semantic Error: Variable " + temp.childNodes.get(0).value
									+ " in print statement has not been declared yet.");
							System.exit(1);

						}

						break;

					case "FunctionCall":
						this.traverse(temp);
						break;

					case "Operator":
						expressionOperationTraversal(temp.childNodes.get(0));

					}

				} else if (temp.node_type == "FunctionCall") {
					// here we only check to see if there exist a function with those

					if (!checkVariable(temp.value)) {
						System.out.println("Function with name " + temp.value + " has not been declared yet.");
						System.exit(1);
					}

					// called function exists
					// check if same number of parameters

					ArrayList<String> parameter_types = new ArrayList<String>();

					String parameter_type;
					String expected_parameter_type;
					boolean match = true;
					int matching_index = 0;

					for (int j = 0; j < function_headers.get(temp.value).size(); j++) {
						match = true;
						// check number of arguments first

						if (function_headers.get(temp.value).get(j).size() != temp.childNodes.size()) {
							match = false;
							continue;
						}

						if (function_headers.get(temp.value).get(j).size() == temp.childNodes.size()
								&& temp.childNodes.size() == 0) {
							match = true;
							matching_index = j;
						}

						for (int z = 0; z < temp.childNodes.size(); z++) {

							expected_parameter_type = function_headers.get(temp.value).get(j).get(z);
							parameter_type = typeCheck(temp.childNodes.get(z), expected_parameter_type);

							if (parameter_type == "exit") {
								match = false;
								break;
							}

							match = true;
							matching_index = j;
						}

						if (match) {
							String return_type = function_returns.get(temp.value).get(matching_index);
							// return return_type;
						}

					}

					if (!match) {
						System.out.println(
								"Semantic Error, no matching function found for function call of " + temp.value);
						System.exit(1);
					}
				}

				else if (temp.parentNode.node_type == "ForLoop") {

					typeCheck(temp, "bool");
				}

			}
			System.out.println(symbol_table);
			return "default";
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
			} else {
				return variable_type;
			}

		} else {

			System.out.println("Semantic Error, variable " + variable_identifier + " has not been declared!");
			System.exit(1);

		}
		return "";
	}

	public String typeCheck(AST node, String expected_type) {

		boolean auto = false;
		if (expected_type.equals("auto")) {
			auto = true;
		}

		if (node.node_type == "Variable_Identifier") {

			return evaluateVariable(node.value, expected_type);

		} else if (node.node_type == "FunctionCall") {

			if (!checkVariable(node.value)) {
				System.out.println("Semantic Error, function call has not been declared");
				System.exit(1);
			}

			String return_type = getFunctionCallType(node);

			if (!return_type.equals(expected_type)) {
				System.out.println("Semantic Error, Function call does not have the same return type");
				System.exit(1);
			}

		}

		else if (node.node_type == "Integer_Value" || node.node_type == "Float_Value"
				|| node.node_type == "String_Value" || node.node_type == "True_Keyword"
				|| node.node_type == "False_Keyword" || node.node_type == "Char_Value") {

			switch (node.node_type) {

			case "Integer_Value":
				if (expected_type.equals("int")) {
					return "int";
				} else {
					System.out.println("Expected " + expected_type + " value, instead of int literal");
					return "exit";
				}

			case "Float_Value":
				if (expected_type.equals("float")) {
					return "float";
				} else {
					System.out.println("Expected " + expected_type + " value, insted of float literal");
					return "exit";
				}

			case "String_Value":
				if (expected_type.equals("string")) {
					return "string";
				} else {

					System.out.println("Expected " + expected_type + " value, isntead of string literal");
					return "exit";
				}

			case "True_Keyword":
			case "False_Keyword":
				if (expected_type.equals("bool")) {
					return "bool";
				} else {
					System.out.println("Expected " + expected_type + " value, instead of bool literal");
					return "exit";
				}

			case "Char_Value":
				if (expected_type.equals("char")) {
					return "char";
				} else {

					System.out.println("Expected " + expected_type + " value, instead of char literal");
					return "exit";
				}

			}

		} else {
			// is an operator so we must evaluate the expression and climb da tree
			String evaluated_type = expressionOperationTraversal(node);
			if (!expected_type.equals(evaluated_type)) {
				System.out.println();
				System.out
						.println("Expected " + expected_type + " value, instead of " + evaluated_type + " expression");
				return "exit";
			} else {
				return evaluated_type;
			}

		}
		return "";
	}

	public String expressionOperationTraversal(AST node) {
		// expression nodes will have either one or two children , a traversal of the
		// tree
		// System.out.println(node.value);

		if (node.node_type == "FunctionCall") {

			if (!checkVariable(node.value)) {
				System.out.println("Semantic Error, function with name " + node.value + " has not been declared yet!");
				System.exit(1);
			}

			else
				return getFunctionCallType(node);

		}

		if (node.childNodes.size() == 0) {

			if (node.node_type == "Variable_Identifier") {
				return getType(node.value);
			} else if (node.node_type == "FunctionCall") {

				if (!checkVariable(node.value)) {
					System.out.println(
							"Semantic Error, function with name " + node.value + " has not been declared yet!");
					System.exit(1);
				}

				else
					return getType(node.value);
			}

			else {
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

				case "Char_Value":
					return "char";
				}

			}

		} else if (node.childNodes.size() == 2) {

			if (node.childNodes.get(0).node_type == "Array_Indexing") {

				return getType(node.value);

			}

			String type1 = expressionOperationTraversal(node.childNodes.get(0));
			String type2 = expressionOperationTraversal(node.childNodes.get(1));

			/*
			 * if(type1.equals("bool") && !type2.equals("bool")) { String resultant_type =
			 * checkOperatorConstraint(node, type2); return resultant_type; } else if
			 * (!type1.equals("bool") && type2.equals("bool")) { String resultant_type =
			 * checkOperatorConstraint(node, type1); return resultant_type; }
			 * 
			 * else
			 */ if (!type1.equals(type2)) {
				System.out.println(type1 + " " + type2);
				System.out.println("Semantic Error Mismatched types");
				System.exit(1);
			} else {
				String resultant_type = checkOperatorConstraint(node, type1);
				return resultant_type;
			}

		} else if (node.childNodes.size() == 1) {
			String type = expressionOperationTraversal(node.childNodes.get(0));

			return type;
		}

		return "";
	}

	public String checkOperatorConstraint(AST operator_node, String type1) {

		switch (type1) {
		case "string":
			if (operator_node.value.equals("+")) {
				return "string";
			} else if (operator_node.value.equals("==")) {
				return "bool";
			} else {
				System.out.println("Semantic Error " + operator_node.value + " operation not defined for strings!");
				System.exit(1);
			}
			break;
		case "int":
			if (operator_node.value.equals("+") || operator_node.value.equals("-") || operator_node.value.equals("*")
					|| operator_node.value.equals("/")) {
				return "int";
			} else if (operator_node.value.equals("<") || operator_node.value.equals(">")
					|| operator_node.value.equals("<=") || operator_node.value.equals(">=")
					|| operator_node.value.equals("==") || operator_node.value.equals("!=")) {
				return "bool";
			} else {
				System.out.println("Semantic Error " + operator_node.value + " operation not defined for numbers!");
				System.exit(1);
			}
			break;
		case "float":
			if (operator_node.value.equals("+") || operator_node.value.equals("-") || operator_node.value.equals("*")
					|| operator_node.value.equals("/")) {
				return "float";
			} else if (operator_node.value.equals("<") || operator_node.value.equals(">")
					|| operator_node.value.equals("<=") || operator_node.value.equals(">=")
					|| operator_node.value.equals("==") || operator_node.value.equals("!=")) {
				return "bool";
			} else {
				System.out.println("Semantic Error " + operator_node.value + " operation not defined for numbers!");
				System.exit(1);
			}
			break;

		case "bool":
			if (operator_node.value.equals("and") || operator_node.value.equals("or")
					|| operator_node.value.equals("not") || operator_node.value.equals("!=")
					|| operator_node.value.equals("==")) {
				return "bool";
			} else {
				System.out.println("Semantic Error " + operator_node.value + " operation not defined for bools!");
				System.exit(1);
			}
		}

		return "";
	}

	public String getFunctionCallType(AST temp) {

		System.out.println(temp.value);

		if (!checkVariable(temp.value)) {
			System.out.println("Function with name " + temp.value + " has not been declared yet.");
			System.exit(1);
		}
		// called function exists
		// check if same number of parameters
		ArrayList<String> parameter_types = new ArrayList<String>();

		String parameter_type;
		String expected_parameter_type;
		boolean match = true;
		int matching_index = 0;

		for (int j = 0; j < function_headers.get(temp.value).size(); j++) {
			match = true;
			// check number of arguments first

			if (function_headers.get(temp.value).get(j).size() != temp.childNodes.size()) {
				match = false;
				continue;
			}

			if (function_headers.get(temp.value).get(j).size() == temp.childNodes.size()
					&& temp.childNodes.size() == 0) {
				match = true;
				matching_index = j;
			}

			for (int z = 0; z < temp.childNodes.size(); z++) {

				expected_parameter_type = function_headers.get(temp.value).get(j).get(z);
				parameter_type = typeCheck(temp.childNodes.get(z), expected_parameter_type);

				if (parameter_type == "exit") {
					match = false;
					break;
				}

				match = true;
				matching_index = j;
			}

			if (match) {
				String return_type = function_returns.get(temp.value).get(matching_index);
				return return_type;
			}

		}

		if (!match) {

			System.out.println("Semantic Error, no matching function found for function call of " + temp.value);
			System.exit(1);
		}

		return "exit";
	}

}
