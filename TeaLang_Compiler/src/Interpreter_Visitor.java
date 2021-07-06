import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import javax.crypto.Mac;

public class Interpreter_Visitor {

	static String last_return;

	static Stack<Map<String, String>> symbol_table = new Stack<Map<String, String>>();
	static Stack<Map<String, String>> value_table = new Stack<Map<String, String>>();

	static Map<String, ArrayList<AST>> function_map = new LinkedHashMap<String, ArrayList<AST>>();

	static Map<String, ArrayList<ArrayList<String>>> function_headers = new LinkedHashMap<String, ArrayList<ArrayList<String>>>();
	static Map<String, ArrayList<String>> function_returns = new LinkedHashMap<String, ArrayList<String>>();

	static Stack<ArrayList<String>> array_table = new Stack<ArrayList<String>>();
	static Stack<Map<String, ArrayList<String>>> array_values = new Stack<Map<String, ArrayList<String>>>();

	public String traverse(AST root, boolean create_scope) {
		boolean functionCall;

		Map<String, String> map = new LinkedHashMap<String, String>();
		Map<String, String> map2 = new LinkedHashMap<String, String>();
		Map<String, ArrayList<String>> map3 = new LinkedHashMap<String, ArrayList<String>>();

		// preferable from ordinary maps, so we can preserve the order of inserted keys.
		if (create_scope) {
			symbol_table.push(map);
			value_table.push(map2);
			array_table.push(new ArrayList<String>());
			array_values.push(map3);
		}
		AST temp;

		if (root.value == null) {

		} else {

		}

		for (int i = 0; i < root.childNodes.size(); i++) {

			temp = root.childNodes.get(i);

			if (temp.node_type == "VariableDecl") {

				if (checkVariable(temp.childNodes.get(0).value)) {
					System.out.println("Semantic Error, variable " + temp.childNodes.get(0).value + " already exists!");
					System.exit(1);
				} else {

					if (temp.childNodes.get(0).childNodes.size() != 0) {
						// arrays

						array_table.get(array_table.size() - 1).add(temp.childNodes.get(0).value);
					
						String[] size_type;

						size_type = valueCheck(temp.childNodes.get(1), "int");
						int size = Integer.valueOf(size_type[0]);

						array_values.peek().put(temp.childNodes.get(0).value, new ArrayList<String>());

						if (temp.childNodes.size() != 3) {

							if (temp.childNodes.get(3).childNodes.size() > size) {
								System.out.println("Runtime Error, Initialised Array exceeds bounds");
								System.exit(1);
							}

							for (int index = 0; index < temp.childNodes.get(3).childNodes.size(); index++) {

								String[] element_value_type = valueCheck(temp.childNodes.get(3).childNodes.get(index),
										temp.childNodes.get(2).value);

								// System.out.println(element_value_type[0]);

								array_values.peek().get(temp.childNodes.get(0).value).add(element_value_type[0]);
							}

						}

						// symbol_table.peek().put(temp.childNodes.get(0).value,
						// temp.childNodes.get(2).value);

						symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).value,
								temp.childNodes.get(2).value);
						
					} else {

						// type check before so we dont get, let x:int = x;
						String[] value_type = valueCheck(temp.childNodes.get(2), temp.childNodes.get(1).value);

						symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).value,
								temp.childNodes.get(1).value);

						value_table.get(value_table.size() - 1).put(temp.childNodes.get(0).value, value_type[0]);

						// Evaluate Expression Type

						continue;
					}
				}
			} else if (temp.node_type == "IfStatements") {

				if (valueCheck(temp.childNodes.get(0), "bool")[0].equals("true")) {

					this.traverse(temp.childNodes.get(1), true);
					symbol_table.pop();
					value_table.pop();
				} else {
					// check if else block exists

					if (temp.childNodes.size() == 3) {
						this.traverse(temp.childNodes.get(2), true);
						symbol_table.pop();
						value_table.pop();
					}

				}

			} else if (temp.node_type == "WhileLoop") {

				symbol_table.push(new LinkedHashMap<String, String>());
				value_table.push(new LinkedHashMap<String, String>());

				boolean condition = Boolean.valueOf(valueCheck(temp.childNodes.get(0), "bool")[0]);

				while (condition) {

					this.traverse(temp.childNodes.get(1), false);
					condition = Boolean.valueOf(valueCheck(temp.childNodes.get(0), "bool")[0]);
				}

				symbol_table.pop();
				value_table.pop();
			} else if (temp.node_type == "ForLoop") {

				symbol_table.push(new LinkedHashMap<String, String>());
				value_table.push(new LinkedHashMap<String, String>());

				String[] value_type = valueCheck(temp.childNodes.get(0).childNodes.get(2),
						temp.childNodes.get(0).childNodes.get(1).value);

				symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(0).childNodes.get(0).value,
						temp.childNodes.get(0).childNodes.get(1).value);

				value_table.get(value_table.size() - 1).put(temp.childNodes.get(0).childNodes.get(0).value,
						value_type[0]);

				boolean condition = Boolean.valueOf(valueCheck(temp.childNodes.get(1), "bool")[0]);

				while (condition) {

					this.traverse(temp.childNodes.get(3), false);

					String expected_type = getType(temp.childNodes.get(2).childNodes.get(0).value);

					String[] value_type_2 = valueCheck(temp.childNodes.get(2).childNodes.get(1), expected_type);

					for (int k = 0; k < value_table.size(); k++) {

						if (value_table.get(k).containsKey(temp.childNodes.get(2).childNodes.get(0).value)) {
							value_table.get(k).put(temp.childNodes.get(2).childNodes.get(0).value, value_type_2[0]);
						}

					}

					condition = Boolean.valueOf(valueCheck(temp.childNodes.get(1), "bool")[0]);
				}

				symbol_table.pop();
				value_table.pop();
				array_table.pop();
				array_values.pop();

			} else if (temp.node_type == "FunctionDecl") {

				if (checkVariable(temp.childNodes.get(1).value)) {

				} else {
					function_headers.put(temp.childNodes.get(1).value, new ArrayList<ArrayList<String>>());
					function_returns.put(temp.childNodes.get(1).value, new ArrayList<String>());
					function_map.put(temp.childNodes.get(1).value, new ArrayList<AST>());
				}

				symbol_table.get(symbol_table.size() - 1).put(temp.childNodes.get(1).value,
						temp.childNodes.get(0).value);
				// create function header space

				function_headers.get(temp.childNodes.get(1).value).add(new ArrayList<String>());

				function_map.get(temp.childNodes.get(1).value).add(temp);

				symbol_table.push(new LinkedHashMap<String, String>());
				value_table.push(new LinkedHashMap<String, String>());

				this.traverse(temp, true);

				symbol_table.pop();
				value_table.pop();
				array_table.pop();
				array_values.pop();

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

				String expected_type = parent.childNodes.get(0).value;
				// variable

				String[] key_value = valueCheck(temp.childNodes.get(0), expected_type);

				// System.out.println(key_value[0]);

				last_return = key_value[0];

				// this.traverse(temp);

				break;
			}

			else if (temp.node_type == "FormalParams") {

				for (int j = 0; j < temp.childNodes.size(); j += 2) {

					if (checkVariable(temp.childNodes.get(j).value)) {
						System.out.println(
								"Semantic Error, variable " + temp.childNodes.get(j).value + " already exists!");
						System.exit(1);
					} else {

						String function_name = temp.parentNode.childNodes.get(1).value;

						function_headers.get(function_name).get(function_headers.get(function_name).size() - 1)
								.add(temp.childNodes.get(j + 1).value);

						if (temp.childNodes.get(j).childNodes.size() != 0) {
							function_headers.get(function_name).get(function_headers.get(function_name).size() - 1)
									.add("[]");
						}

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

					if (checkArray(temp.childNodes.get(0).value)) {

						String[] index_value = valueCheck(temp.childNodes.get(0).childNodes.get(1), "int");
						int index = Integer.valueOf(index_value[0]);

						for (int k = 0; k < array_values.size(); k++) {

							if (array_values.get(k).containsKey(temp.childNodes.get(0).value)) {

								if (array_values.get(k).get(temp.childNodes.get(0).value).size() < index) {
									System.out.println("Runtime Error, specififed index is larger than array size.");
									System.exit(1);
								}

								String[] value_type = valueCheck(temp.childNodes.get(1),
										getType(temp.childNodes.get(0).value));

								array_values.get(k).get(temp.childNodes.get(0).value).set(index, value_type[0]);

						

							}

						}

					} else {

						String expected_type = getType(temp.childNodes.get(0).value);

						String[] value_type = valueCheck(temp.childNodes.get(1), expected_type);

						for (int k = 0; k < value_table.size(); k++) {

							if (value_table.get(k).containsKey(temp.childNodes.get(0).value)) {
								value_table.get(k).put(temp.childNodes.get(0).value, value_type[0]);
							}

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

					if (checkArray(temp.childNodes.get(0).value)) {

						ArrayList<String> array = returnArray(temp.childNodes.get(0).value);

						if (temp.childNodes.get(0).childNodes.size() != 0) {

							String[] index_type = valueCheck(temp.childNodes.get(0).childNodes.get(1),
									getType(temp.childNodes.get(0).value));

							int index = Integer.valueOf(index_type[0]);

							System.out.println(array.get(index));

						} else {

							System.out.println(array);

						}

					} else {

						System.out.println(getValue(temp.childNodes.get(0).value));
					}
					break;

				case "FunctionCall":
					String value = traverse(temp, false);
					System.out.println(value);
					break;

				case "Operator":
					String value_type[] = expressionOperationTraversal(temp.childNodes.get(0));

					System.out.println(value_type[0]);
				}

			} else if (temp.node_type == "FunctionCall") {

				String parameter_type;
				String expected_parameter_type;

				String function_name = temp.value;

				int function_index = getFunctionNode(temp);

				AST function_node = function_map.get(function_name).get(function_index);

				String[] value_type;
				symbol_table.push(new LinkedHashMap<String, String>());
				value_table.push(new LinkedHashMap<String, String>());
				array_values.push(new LinkedHashMap<String, ArrayList<String>>());
				array_table.push(new ArrayList<String>());
				int offset = 0;

				for (int z = 0; z < temp.childNodes.size(); z++) {

					if (checkArray(temp.childNodes.get(z).value)) {

						symbol_table.get(symbol_table.size() - 1).put(
								function_node.childNodes.get(2).childNodes.get(z * 2).value,
								function_node.childNodes.get(2).childNodes.get((z * 2) + 1).value);

						if (temp.childNodes.get(z).childNodes.size() != 0) {
							
							value_table.get(value_table.size() - 1).put(
									function_node.childNodes.get(2).childNodes.get(z * 2).value,
									getArrayValue(temp.childNodes.get(z)));

						} else {

							// whole array
							offset++;
							array_table.peek().add(function_node.childNodes.get(2).childNodes.get(z * 2).value);
							array_values.peek().put(function_node.childNodes.get(2).childNodes.get(z * 2).value,
									returnArray(temp.childNodes.get(z).value));
						}

					}
					
					expected_parameter_type = function_headers.get(temp.value).get(function_index).get(z+offset);
					
					
					value_type = valueCheck(temp.childNodes.get(z), expected_parameter_type);

					
					
					symbol_table.get(symbol_table.size() - 1).put(
							function_node.childNodes.get(2).childNodes.get(z * 2).value,
							function_node.childNodes.get(2).childNodes.get((z * 2) + 1).value);

					value_table.get(value_table.size() - 1)
							.put(function_node.childNodes.get(2).childNodes.get(z * 2).value, value_type[0]);

				}
				
				traverse(function_node.childNodes.get(function_node.childNodes.size() - 1), false);
				symbol_table.pop();
				value_table.pop();
				array_values.pop();
				array_table.pop();
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

	public boolean checkArray(String variable_identifier) {

		for (int i = 0; i < array_table.size(); i++) {

			if (array_table.get(i).contains(variable_identifier)) {
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

			if (value_table.get(i - 1).containsKey(variable_identifier)) {
				return value_table.get(i - 1).get(variable_identifier);
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

		if (node.node_type == "Variable_Identifier") {

			return new String[] { getValue(node.value), getType(node.value) };

		} else if (node.node_type == "FunctionCall") {

			String value = traverse(node.parentNode, false);

			return new String[] { value, getType(node.value) };

		} else if (node.node_type == "Integer_Value" || node.node_type == "Float_Value"
				|| node.node_type == "String_Value" || node.node_type == "True_Keyword"
				|| node.node_type == "False_Keyword") {

			switch (node.node_type) {

			case "Integer_Value":
				if (expected_type.equals("int")) {
					return new String[] { node.value, "int" };
				} else {

				}

			case "Float_Value":
				if (expected_type.equals("float")) {
					return new String[] { node.value, "float" };
				} else {

				}

			case "String_Value":
				if (expected_type.equals("string")) {
					return new String[] { node.value, "string" };
				} else {

				}

			case "True_Keyword":
			case "False_Keyword":
				if (expected_type.equals("bool")) {
					return new String[] { node.value, "bool" };
				} else {

				}

			}

		} else {
			// is an operator so we must evaluate the expression and climb da tree

			String value_type[] = expressionOperationTraversal(node);

			if (!expected_type.equals(value_type[1])) {

			} else {
				return value_type;
			}

		}
		return new String[] { "exit" };
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

				return new String[] { value, getType(node.value) };
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
			
			
			if(node.childNodes.get(0).node_type.equals("Array_Indexing")) {
				
				String value = getArrayValue(node);
				String type = getType(node.value);
				return new String[] {value, type};
				
			}
			
			
			String[] type_value_1 = expressionOperationTraversal(node.childNodes.get(0));
			String[] type_value_2 = expressionOperationTraversal(node.childNodes.get(1));

			String[] resultant_type = checkOperatorConstraint(node, type_value_1[1], type_value_1[0], type_value_2[0]);
			return resultant_type;
		}

		else if (node.childNodes.size() == 1) {

			String[] type_value = expressionOperationTraversal(node.childNodes.get(0));

			if (type_value[1] == "bool") {

				boolean value = Boolean.valueOf(type_value[0]);
				return new String[] { String.valueOf(!value), "bool" };
			}

			if (node.node_type.equals("FunctionCall")) {

				String value = traverse(node.parentNode, false);

				return new String[] { value, getType(node.value) };

			}

			if (node.value.equals("+")) {

				

				return new String[] { type_value[0], type_value[1] };
			}

			if (node.value.equals("-")) {

				int negative = Integer.valueOf(type_value[0]) * -1;

				return new String[] { String.valueOf(negative), type_value[1] };
			}

		}

		return new String[] { "" };
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
				return new String[] { String.valueOf(bool_result), "bool" };

			case "or":

				bool_result = Boolean.parseBoolean(value1) || Boolean.parseBoolean(value2);
				return new String[] { String.valueOf(bool_result), "bool" };

			case "==":

				bool_result = Boolean.parseBoolean(value1) == Boolean.parseBoolean(value2);
				return new String[] { String.valueOf(bool_result), "bool" };

			case "!=":

				bool_result = Boolean.parseBoolean(value1) != Boolean.parseBoolean(value2);
				return new String[] { String.valueOf(bool_result), "bool" };

			}

		}

		return new String[] { "" };
	}

	public int getFunctionNode(AST temp) {

		if (!checkVariable(temp.value)) {
			System.out.println("Function with name " + temp.value + " has not been declared yet.");
			System.exit(1);
		}
		// called function exists
		// check if same number of parameters
		ArrayList<String> parameter_types = new ArrayList<String>();

		String[] parameter_type_value;
		String expected_parameter_type;
		boolean match = true;
		int matching_index = 0;
		String[] index_type;
		int index;

		for (int j = 0; j < function_headers.get(temp.value).size(); j++) {
			match = true;
			// check number of arguments first
			int arrays = 0;
			for (int i = 0; i < function_headers.get(temp.value).get(j).size(); i++) {

				if (function_headers.get(temp.value).get(j).get(i).equals("[]")) {
					arrays++;
				}

			}

			if (function_headers.get(temp.value).get(j).size() - arrays != temp.childNodes.size()) {
				match = false;
				continue;
			}

			if (function_headers.get(temp.value).get(j).size() - arrays == temp.childNodes.size()
					&& temp.childNodes.size() == 0) {

				match = true;
				matching_index = j;
			}

			int offset = 0;
			for (int z = 0; z < temp.childNodes.size(); z++) {

				expected_parameter_type = function_headers.get(temp.value).get(j).get(z + offset);

				if (z != temp.childNodes.size() - 1
						&& function_headers.get(temp.value).get(j).get(z + offset + 1).equals("[]")) {
					// expecting an array

					offset++;
					if (temp.childNodes.get(z).childNodes.size() != 0) { // array element is not an array
						match = false;
						break;
					}

					if (!getType(temp.childNodes.get(z).value).equals(expected_parameter_type)) { // array of same type
						match = false;
						break;
					}

					if (!checkArray(temp.childNodes.get(z).value)) { // variable id must be array
						match = false;
						break;
					}

					continue;
				}

				if (checkArray(temp.childNodes.get(z).value) && temp.childNodes.get(z).childNodes.size() == 0) { // variable
																													// id
																													// must
																													// be
																													// array
					match = false;
					break;
				}

				if (checkArray(temp.childNodes.get(z).value) && temp.childNodes.get(z).childNodes.size() != 0) {

					index_type = valueCheck(temp.childNodes.get(z).childNodes.get(1), "int");
					index = Integer.valueOf(index_type[0]);

					if (index > returnArray(temp.childNodes.get(z).value).size()) {
						System.out.println("Runtime Error, specified index is larger than array size");
						System.exit(1);
					}

					if (!getType(temp.childNodes.get(z).value).equals(expected_parameter_type)) {

						match = false;
						break;
					}
					continue;
				}

				parameter_type_value = valueCheck(temp.childNodes.get(z), expected_parameter_type);

				if (parameter_type_value[0] == "exit") {
					match = false;
					break;
				}

				match = true;
				matching_index = j;
			}

			if (match) {

				return matching_index;
			}

		}
		if (!match) {
			System.out.println("Semantic Error, no matching function found for function call of " + temp.value);
			System.exit(1);
		}

		return -1;
	}

	ArrayList<String> returnArray(String variable_identifier) {

		for (int i = array_values.size(); i > 0; i--) {

			if (array_values.get(i - 1).containsKey(variable_identifier)) {
				return array_values.get(i - 1).get(variable_identifier);
			}

		}
		// because conditional return scope
		return new ArrayList<String>();
	}

	String getArrayValue(AST node) {

		ArrayList<String> array = returnArray(node.value);

		if (node.childNodes.size() != 0) {

			
			
			String[] index_type = valueCheck(node.childNodes.get(1), getType(node.value));
			
			if(!index_type[1].equals("int")) {
				System.out.println("Array indices must be integers!");
				System.exit(1);
			}
			
			int index = Integer.valueOf(index_type[0]);

			return (array.get(index));

		} else {

			return "exit";

		}
	}

}
