import java.util.*;

public class Parser {

	static AST root = new AST("ProgramNode");

	static String[] non_terminals = { "Program", "Block", "Statement", "VariableDecl", "Assignment", "Print", "If",
			"For", "While", "Return", "Function", "Params", "Type", "Literal", "Expression", "SimpleExpression",
			"RelationalOp", "Term", "AdditiveOp", "Factor", "MultiplicativeOp", "Return", "FunctionCall",
			"SubExpression", "Unary", "ActualParams", "Else", "Comma", "ArrayAssignment", "Index", "Struct_Statement", "Struct_Initialisation" };
	static String[] terminals = { "Variable_Assigner", "Colon", "Equals", "Semi_Colon", "Print_Keyword",
			"Return_Keyword", "If_Keyword", "Opening_Bracket", "Closing_Bracket", "Opening_Curly", "Closing_Curly",
			"Else_Keyword", "For_Keyword", "While_Keyword", "Float_Keyword", "Int_Keyword", "Bool_Keyword",
			"String_Keyword", "Variable_Identifier", "Integer_Value", "Float_Value", "True_Keyword", "False_Keyword",
			"String_Value", "Not_Keyword", "Asterisk", "Division_Slash", "And_Keyword", "Or_Keyword", "Comparison",
			"Equality_Class", "Addition", "End_Of_File", "Comma", "Char_Keyword", "Char_Value", "Auto_Keyword",
			"Opening_Square", "Closing_Square", "Struct_Keyword", "Struct_Identifier"

	};

	// 1-8 statements including block and return?? its in the EBNF
	static Stack<String> stack = new Stack<String>();
	static int[][] parser_table = {
			{ 1, 0, 0, 0, 2, 6, 3, 0, 0, 8, 0, 0, 4, 5, 7, 7, 7, 7, 26, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 7,
					0, 7, 0, 0, 61, 0 }, // program
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7,
					0, 7, 0, 0, 0 ,0 }, // block
			{ 1, 0, 0, 0, 2, 6, 3, 0, 0, 8, 22, 23, 4, 5, 7, 7, 7, 7, 26, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0,
					7, 0, 7, 0, 0, 61 ,62}, // statement
			{ 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 63}, // varaible
			// decl
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 27, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 64}, // assignment
			{ 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0 }, // print
			{ 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0 }, // if
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0 }, // for
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0 }, // while
			{ 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0 }, // return
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 7, 7, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7,
					0, 7, 0, 0, 0, 0 }, // function
			{ 0, 0, 0, 0, 0, 0, 0, 0, 28, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 29, 0,
					0, 0, 0, 0, 0, 65 }, // params
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 10, 10, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					10, 0, 10, 0, 0, 0 ,0 }, // type
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 11, 11, 11, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 11, 0, 0, 0, 0, 0 }, // literal
			{ 0, 0, 0, 0, 0, 0, 0, 13, 0, 60, 0, 0, 0, 0, 0, 0, 0, 0, 12, 11, 11, 11, 11, 11, 14, 0, 0, 0, 0, 0, 0, 14,
					0, 0, 0, 11, 0, 0, 0, 0, 66 }, // expression
			{ 0, 0, 0, 0, 0, 0, 0, 43, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 42, 41, 41, 41, 41, 41, 44, 0, 0, 0, 0, 0, 0, 44,
					0, 0, 0, 41, 0, 0, 0, 0, 66 }, // simple expression
			{ 0, 0, 0, 32, 0, 0, 0, 0, 55, 0, 59, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 46, 46, 46, 47, 17, 17, 47,
					0, 32, 0, 0, 0, 0, 56, 0, 0 }, // relationalOp
			{ 0, 0, 0, 0, 0, 0, 0, 39, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 38, 37, 37, 37, 37, 37, 40, 0, 0, 0, 0, 0, 0, 40,
					0, 37, 0, 37, 0, 0, 0, 0, 66 }, // Term
			{ 0, 0, 0, 31, 0, 0, 0, 0, 54, 0, 59, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 45, 45, 45, 17, 32, 32, 17,
					31, 0, 0, 0, 0, 0, 56, 0, 0}, // additiveOp
			{ 0, 0, 0, 0, 0, 0, 0, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 34, 33, 33, 33, 33, 33, 36, 0, 0, 0, 0, 0, 0, 36,
					0, 0, 0, 33, 0, 0, 0 ,66 }, // Factor
			{ 0, 0, 0, 30, 0, 0, 0, 0, 53, 0, 59, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 17, 17, 32, 31, 31, 32,
					0, 30, 0, 0, 0, 0, 56, 0 ,0 }, // MultipilicatievOp
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0 }, // Returns
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0 }, // Function
			// Call
			{ 0, 0, 0, 0, 0, 0, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0 }, // SubExpression
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0,
					0, 0, 0 }, // Unary
			{ 0, 0, 0, 0, 0, 0, 0, 50, 28, 0, 0, 0, 0, 0, 0, 0, 0, 0, 49, 48, 48, 48, 48, 48, 51, 0, 0, 0, 0, 0, 0, 51,
					0, 52, 0, 48, 0, 0 }, // ActualParams
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0 }, // Else-Statements
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0 }, // Comma
			{ 0, 0, 57, 58, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0 }, //Array Assignment
			{ 0, 0, 0, 0, 0, 0, 0, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 11, 11, 11, 11, 11, 14, 0, 0, 0, 0, 0, 0, 14,
							0, 0, 0, 11, 0, 0, 56 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 67, 0, 0, 0, 0, 7, 7, 7, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7,
								0, 7, 0, 0, 0, 0  },//Struct Statement
			{ 0, 18, 68, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,
									0, 0, 0, 0, 0, 0}, //Struct initiliase			
							
	};

	public static void parseSyntax(int production_rule, Token current_token, Token next_token, Token next_next_token) {

		boolean op_type;
		boolean array;
		
		
		// based on the production rule, the contents of the stack are adjusted
		switch (production_rule) {

		case 1:
			root.addNode("VariableDecl");
			 array = false;
			if (next_next_token.type == "Opening_Square") {
				array = true;
			}

			root = root.switchRoot(root);
			// declaring variables
			if (array) {
				stack.push("Statement");
				stack.push("ArrayAssignment");
				stack.push("Type");
				stack.push("Colon");
				stack.push("Closing_Square");
				stack.push("Expression");
				stack.push("Opening_Square");
				stack.push("Variable_Identifier");
			}
			else {
			
			stack.push("Statement");
			stack.push("Semi_Colon");
			stack.push("Expression");
			stack.push("Equals");
			stack.push("Type");
			stack.push("Colon");
			stack.push("Variable_Identifier");
			}
			break;
		case 2:
			// print statements
			root.addNode("PrintStatements");
			root = root.switchRoot(root);
			stack.push("Statement");
			stack.push("Semi_Colon");
			stack.push("Expression");
			break;
		case 3:
			// if statements
			// no need to add statement terminal since block does it
			root.addNode("IfStatements");
			root = root.switchRoot(root);
			stack.push("Block");
			stack.push("Closing_Bracket");
			stack.push("Expression");
			stack.push("Opening_Bracket");
			break;
		case 4:
			// For Loop
			root.addNode("ForLoop");
			root = root.switchRoot(root);
			System.out.println("Entered For-Loop");
			stack.push("Block");
			stack.push("Closing_Bracket");
			stack.push("Assignment");
			stack.push("Semi_Colon");
			stack.push("Expression");
			stack.push("VariableDecl");
			stack.push("Opening_Bracket");
			break;
		case 5:
			// while loop
			root.addNode("WhileLoop");
			root = root.switchRoot(root);
			System.out.println("Entered While Loop");
			stack.push("Block");
			stack.push("Closing_Bracket");
			stack.push("Expression");
			stack.push("Opening_Bracket");
			break;
		case 6:
			// returns
			root.addNode("ReturnStatement");
			root = root.switchRoot(root);
			stack.push("Statement");
			stack.push("Semi_Colon");
			stack.push("Expression");
			break;
		case 7:
			// Function Declarations
			root.addNode("FunctionDecl");
			root = root.switchRoot(root);
			root.addNode("Type", current_token.value);
			stack.push("Block");
			stack.push("Closing_Bracket");
			stack.push("Params");
			stack.push("Opening_Bracket");
			stack.push("Variable_Identifier"); // I am aware that function names are not variables but does not make a
												// difference
			break;
		case 8:
			// block
			if (next_token.type == "Opening_Curly" || next_token.type == "Closing_Curly") {
				System.out.println("Syntax Error..... Redundant Block at line " + next_token.line_number);
				System.exit(1);
			}

			if (root.childNodes.size() == 0) {

				root.addNode("Block");
			} else {
				if (root.childNodes.get(root.childNodes.size() - 1).node_type != "ElseBlock") {
					root.addNode("Block");
				}
			}
			root = root.switchRoot(root);
			System.out.println("ENTERED BLOCK");
			stack.push("Statement");
			stack.push("Closing_Curly");
			stack.push("Statement");
			break;

		case 9:
			// Parameters
			root.addNode("FormalParams");
			root = root.switchRoot(root);
			root.addNode("Variable_Identifier", current_token.value);
			stack.push("Params");
			stack.push("Type");
			stack.push("Colon");
			if(next_token.type == "Opening_Square") {
				stack.push("Closing_Square");
				stack.push("Opening_Square");
				root.childNodes.get(0).addNode("[]");
			}
			break;

		case 10:
			if (current_token.value.equals("auto") && root.node_type.equals("FormalParams")) {
				System.out.println("Syntax Error On line " + current_token.line_number
						+ " cannot have auto varaibles as formal parameters.");
				System.exit(1);
			}
			
			if(current_token.type.equals("Variable_Identifier") && next_token.type.equals("Colon")) {
				stack.pop();
				stack.pop();
				stack.pop();
				stack.push("Struct_Initialisation");
			}
			
			root.addNode("Type", current_token.value);
			break;

		case 11:
			if (current_token.type == "String_Value") {
				root.addNode(current_token.type, current_token.value.substring(1, current_token.value.length() - 1));
			} else {
				root.addNode(current_token.type, current_token.value);
			}
			stack.push("SimpleExpression");
			stack.push("RelationalOp");
			stack.push("Term");
			stack.push("AdditiveOp");
			stack.push("Factor");
			stack.push("MultiplicativeOp");
			break;

		case 12:

			if (next_token.type == "Opening_Bracket") {
				root.addNode("FunctionCall", current_token.value);
				root = root.switchRoot(root);
				// function call
				stack.push("SimpleExpression");
				stack.push("RelationalOp");
				stack.push("Term");
				stack.push("AdditiveOp");
				stack.push("Factor");
				stack.push("MultiplicativeOp");
				stack.push("Closing_Bracket");
				stack.push("ActualParams");
				stack.push("Opening_Bracket");
			} else {
				
				
				if(next_token.type == "Opening_Square") {
					array = true;
				}else {
					array = false;
				}
				
				root.addNode("Variable_Identifier", current_token.value);
				stack.push("SimpleExpression");
				stack.push("RelationalOp");
				stack.push("Term");
				stack.push("AdditiveOp");
				stack.push("Factor");
				stack.push("MultiplicativeOp");
				if(array) {
					stack.push("Closing_Square");
					stack.push("Index");
					stack.push("Opening_Square");
					root = root.switchRoot(root);
					
					if(next_next_token.type != "Closing_Square") {
						root.addNode("Array_Indexing");
						root.switchRoot(root);
						
						
					}
				}
			}

			break;
		case 13:
			// sub-expression
			stack.push("SimpleExpression");
			stack.push("RelationalOp");
			stack.push("Term");
			stack.push("AdditiveOp");
			stack.push("Factor");
			stack.push("MultiplicativeOp");
			stack.push(")");
			stack.push("Expression");
			break;

		case 14:
			root.addNode("Operator", current_token.value);
			root = root.switchRoot(root);
			/*
			 * stack.push("SimpleExpression"); stack.push("RelationalOp");
			 * stack.push("Term"); stack.push("AdditiveOp"); stack.push("Factor");
			 * stack.push("MultiplicativeOp");
			 */
			stack.push("Expression");
			break;
		case 17:
			// Multiplicative Op Encountered, or rather correct type of operator, so we re
			// use the same case
			// root.addNode("Operator", current_token.value);

			root = root.orderOfOperations(root, current_token.value);

			root = root.operatorSwitch(root, current_token.value);
			break;

		// These cases may be numbered wrong because are either
		// a) Production Rules brought about by use of Follow-Sets, I first started
		// iplementing only those which make use of the FIRST Set
		// b) Cases which I did not think of
		
		case 18:
			root = root.parentNode;
		
		case 21:

			break;
		case 22:
			
			
			if (root.node_type == "ElseBlock") {
				root = root.parentNode;
				root = root.parentNode;
			} else {
				root = root.parentNode;
				if (root.node_type == "ForLoop" || root.node_type == "WhileLoop" || root.node_type == "FunctionDecl") {
					root = root.parentNode;
				}
				if (root.node_type == "IfStatements" && next_token.type != "Else_Keyword") {
					root = root.parentNode;
					
				}
			}
			stack.pop(); // pops the closing curling bracket
			System.out.println("EXITTED BLOCK~~~~~~~~~~~~~~~~~~~~~~~");
			// end of block
			break;
		case 23:
			root.addNode("ElseBlock");
			stack.push("Block");
			break;
		case 25:
			// secondary variable declaration parse rule, this does not include adding other
			// statement afterwards
			// used for one-off declarations like in for loop variable declarations
			root.addNode("VariableDecl");
			root = root.switchRoot(root);
			stack.push("Semi_Colon");
			stack.push("Expression");
			stack.push("Equals");
			stack.push("Type");
			stack.push("Colon");
			stack.push("Variable_Identifier");
			break;
		case 26:
			// variable assignment -> onestament ma nax kif insejta din lol
			if (next_token.type != "Opening_Bracket") {
				
				
				root.addNode("VariableAssignment");
				root = root.switchRoot(root);
			
				root.addNode("Variable_Identifier", current_token.value);
				stack.push("Statement");
				
				if(next_token.type.equals("Opening_Square")) {
					stack.push("ArrayAssignment");
				}
				else {
					stack.push("Semi_Colon");
					stack.push("Expression");
					stack.push("Equals");
				}
				
				
				if(next_token.type.equals("Opening_Square")) {
					stack.push("Closing_Square");
					stack.push("Index"); //can be non
					stack.push("Opening_Square");
				}
				
			} else {
				// function call

				root.addNode("FunctionCall", current_token.value);
				root = root.switchRoot(root);
				stack.push("Statement");
				stack.push("Semi_Colon");
				stack.push("Closing_Bracket");
				stack.push("ActualParams");
				stack.push("Opening_Bracket");
			}
			break;
		case 27:
			root.addNode("VariableAssignment");
			root = root.switchRoot(root);
			root.addNode("Variable_Identifier", current_token.value);
			stack.push("Semi_Colon");
			stack.push("Expression");
			stack.push("Equals");
			break;

		case 28:
			if (root.node_type == "FormalParams") {
				root = root.parentNode; // only leave if we have began inserting parameters
			} else if (root.node_type == "FunctionCall") {
				root = root.parentNode.parentNode;
			}
			System.out.println("No more parameters!");
			stack.pop(); // pops the closing bracket expected afterwards
			System.out.println("POPPED");
			break;

		case 29:
			// Parameters
			stack.push("Params");
			stack.push("Type");
			stack.push("Colon");
			
			if(next_next_token.type == "Opening_Square") {
				stack.push("Closing_Square");
				stack.push("Opening_Square");
				
			}
			
			
			stack.push("Variable_Identifier");
			break;
		case 30:
			// END THE EXPRESSION AT LOWEST RECURSION

			if (current_token.type != "Comma" && current_token.type != "Semi_Colon") {

				// root.addNode("Operator", current_token.value);
				root = root.operatorSwitch(root, current_token.value);

			} else {
				// root.addNode("ExpressionEnded");
				
				
				root = root.expressionEscape(root);
			
				if (root.node_type != "FunctionCall" && root.node_type != "Elements") {
					root = root.parentNode;
				}
				// while(root.parentNode.node_type != "Operator") {
				// root = root.parentNode;
				// }
				// root = root.parentNode;
			}

			stack.pop(); // FACTOR
			stack.pop(); // ADDITIVE
			stack.pop(); // TERM
			stack.pop(); // RELATIONAL
			stack.pop(); // SIMPLE EXP
			if (stack.peek() != "ActualParams")
				stack.pop(); // FOLLOW CHAR -> ; ) ,

			break;
		case 31:
			// END THE EXPRESSION AT SECOND LOWEST RECURSION
			if (current_token.type != "Comma" && current_token.type != "Semi_Colon") {
				// root.addNode("Operator", current_token.value);

				root = root.orderOfOperations(root, current_token.value);

				root = root.operatorSwitch(root, current_token.value);

			} else {
				// root.addNode("ExpressionEnded");
				// System.exit(1);

				root = root.expressionEscape(root);
				if (root.node_type != "FunctionCall" && root.node_type != "Elements") {
					root = root.parentNode;
				}

				// while(root.parentNode.node_type != "Operator") {
				// root = root.parentNode;
				// }
				// root = root.parentNode;
			}

			stack.pop(); // TERM
			stack.pop(); // RELATIONAL
			stack.pop(); // SIMPLE EXP
			if (stack.peek() != "ActualParams")
				stack.pop(); // FOLLOW CHAR -> ; ) ,
			break;
		case 32:
			// END THE EXPRESSION AT HIGHEST RECURSION

			if (current_token.type != "Comma" && current_token.type != "Semi_Colon") {
				// System.out.println(root.node_type + root.parentNode.node_type);
				// System.exit(1);
				// root.addNode("Operator", current_token.value);

				root = root.orderOfOperations(root, current_token.value);
				root = root.operatorSwitch(root, current_token.value);
			} else {
				// root.addNode("ExpressionEnded");

				root = root.expressionEscape(root);
				if (root.node_type != "FunctionCall" && root.node_type != "Elements") {
					root = root.parentNode;
				}
				// while(root.parentNode.node_type != "Operator") {
				// root = root.parentNode;
				// }
				// root = root.parentNode;
			}
			stack.pop(); // SIMPLE EXP
			if (stack.peek() != "ActualParams")
				stack.pop(); // FOLLOW CHAR -> ; ) ,

			break;

		case 33:
			if (current_token.type == "String_Value") {
				root.addNode(current_token.type, current_token.value.substring(1, current_token.value.length() - 1));
			} else {
				root.addNode(current_token.type, current_token.value);
			}
			stack.push("Factor");
			stack.push("MultiplicativeOp");
			break;

		case 34:
			if (next_token.type == "Opening_Bracket") {
				// function call
				root.addNode("FunctionCall", current_token.value);
				root = root.switchRoot(root);
				stack.push("Factor");
				stack.push("MultiplicativeOp");
				stack.push("Closing_Bracket");
				stack.push("ActualParams");
				stack.push("Opening_Bracket");
			} else {
				root.addNode("Variable_Identifier", current_token.value);
				stack.push("Factor");
				stack.push("MultiplicativeOp");
			}

			break;
		case 35:
			// sub-expression
			stack.push("Factor");
			stack.push("MultiplicativeOp");
			stack.push("Closing_Bracket");
			stack.push("Expression");
			break;

		case 36:

			root.addNode("Operator", current_token.value);
			root = root.switchRoot(root);
			stack.push("Factor");
			// stack.push("MultiplicativeOp");
			// stack.push("Expression");
			break;

		case 37:
			if (current_token.type == "String_Value") {
				root.addNode(current_token.type, current_token.value.substring(1, current_token.value.length() - 1));
			} else {
				root.addNode(current_token.type, current_token.value);
			}
			stack.push("Term");
			stack.push("AdditiveOp");
			break;

		case 38:

			if (next_token.type == "Opening_Bracket") {
				root.addNode("FunctionCall", current_token.value);
				root = root.switchRoot(root);
				// function call
				stack.push("Term");
				stack.push("AdditiveOp");
				stack.push("Closing_Bracket");
				stack.push("ActualParams");
				stack.push("Opening_Bracket");
			} else {
				
				if(next_token.type == "Opening_Square") {
					array = true;
				}else {
					array = false;
				}
				
				root.addNode("Variable_Identifier", current_token.value);
				stack.push("Term");
				stack.push("AdditiveOp");
				stack.push("MultiplicativeOp");
				if(array) {
					stack.push("Closing_Square");
					stack.push("Index");
					stack.push("Opening_Square");
					
					if(next_next_token.type != "Closing_Square") {
						root.addNode("Array_Indexing");
						root.switchRoot(root);
					}
				}
			}

			break;
		case 39:
			// sub-expression
			stack.push("Term");
			stack.push("AdditiveOp");
			stack.push("Closing_Bracket");
			stack.push("Expression");
			break;

		case 40:
			root.addNode("Operator", current_token.value);
			root = root.switchRoot(root);
			stack.push("Term");
			// stack.push("AdditiveOp");
			// stack.push("Expression");
			break;
		case 41:
			if (current_token.type == "String_Value") {
				root.addNode(current_token.type, current_token.value.substring(1, current_token.value.length() - 1));
			} else {
				root.addNode(current_token.type, current_token.value);
			}
			stack.push("SimpleExpression");
			stack.push("RelationalOp");
			break;

		case 42:

			if (next_token.type == "Opening_Bracket") {
				// function call
				root.addNode("FunctionCall", current_token.value);
				root = root.switchRoot(root);
				stack.push("SimpleExpression");
				stack.push("RelationalOp");
				stack.push("Term");
				stack.push("AdditiveOp");
				stack.push("Factor");
				stack.push("MultiplicativeOp");
				stack.push("Closing_Bracket");
				stack.push("ActualParams");
				stack.push("Opening_Bracket");
			} else {
				
				if(next_token.type == "Opening_Square") {
					array = true;
				}else {
					array = false;
				}
				
				root.addNode("Variable_Identifier", current_token.value);
				stack.push("SimpleExpression");
				stack.push("RelationalOp");
				stack.push("Term");
				stack.push("AdditiveOp");
				stack.push("Factor");
				stack.push("MultiplicativeOp");
				if(array) {
					stack.push("Closing_Square");
					stack.push("Index");
					stack.push("Opening_Square");
					root = root.switchRoot(root);
					
					if(next_next_token.type != "Closing_Square") {
						root.addNode("Array_Indexing");
						root.switchRoot(root);
						
					}
				}
			}

			break;
		case 43:
			// sub-expression
			stack.push("SimpleExpression");
			stack.push("RelationalOp");
			stack.push("Term");
			stack.push("AdditiveOp");
			stack.push("Factor");
			stack.push("MultiplicativeOp");
			stack.push("Closing_Bracket");
			stack.push("Expression");
			break;

		case 44:
			// root.addNode("Operator", current_token.value);
			root = root.switchRoot(root);
			stack.push("SimpleExpression");
			/*
			 * stack.push("RelationalOp"); stack.push("Term"); stack.push("AdditiveOp");
			 * stack.push("Factor"); stack.push("MultiplicativeOp");
			 * stack.push("Expression");
			 */
			break;

		case 45:
			// root.addNode("Operator", current_token.value);

			root = root.orderOfOperations(root, current_token.value);

			root = root.operatorSwitch(root, current_token.value);
			stack.push("AdditiveOp");
			stack.push("Factor");
			break;

		case 46:
			// root.addNode("Operator", current_token.value);
			root = root.orderOfOperations(root, current_token.value);

			root = root.operatorSwitch(root, current_token.value);
			stack.push("RelationalOp");
			stack.push("Term");
			stack.push("AdditiveOp");
			stack.push("Factor");
			break;
		case 47:
			// root.addNode("Operator", current_token.value);
			root = root.orderOfOperations(root, current_token.value);
			root = root.operatorSwitch(root, current_token.value);
			stack.push("RelationalOp");
			stack.push("Term");
			break;

		case 48:

			if (current_token.type == "String_Value") {
				root.addNode(current_token.type, current_token.value.substring(1, current_token.value.length() - 1));
			} else {
				root.addNode(current_token.type, current_token.value);
			}
			stack.push("ActualParams");
			stack.push("SimpleExpression");
			stack.push("RelationalOp");
			stack.push("Term");
			stack.push("AdditiveOp");
			stack.push("Factor");
			stack.push("MultiplicativeOp");
			break;

		case 49:

			if (next_token.type == "Opening_Bracket") {
				// function call
				root.addNode("FunctionCall", current_token.value);
				root = root.switchRoot(root);
				stack.push("ActualParams");
				stack.push("SimpleExpression");
				stack.push("RelationalOp");
				stack.push("Term");
				stack.push("AdditiveOp");
				stack.push("Factor");
				stack.push("MultiplicativeOp");
				stack.push("Closing_Bracket");
				stack.push("ActualParams");
				stack.push("Opening_Bracket");
			} else {
				root.addNode("Variable_Identifier", current_token.value);
				stack.push("ActualParams");
				stack.push("SimpleExpression");
				stack.push("RelationalOp");
				stack.push("Term");
				stack.push("AdditiveOp");
				stack.push("Factor");
				stack.push("MultiplicativeOp");
			}

			break;
		case 50:
			// sub-expression
			stack.push("ActualParams");
			stack.push("SimpleExpression");
			stack.push("RelationalOp");
			stack.push("Term");
			stack.push("AdditiveOp");
			stack.push("Factor");
			stack.push("MultiplicativeOp");
			stack.push(")");
			stack.push("Expression");
			break;

		case 51:
			// root.addNode("Operator", current_token.value);
			if (root.node_type != "FunctionCall") {
				root = root.operatorSwitch(root, current_token.value);
			} else {
				root.addNode("Operator", current_token.value);
				root = root.switchRoot(root);
			}

			stack.push("ActualParams");
			/*
			 * stack.push("SimpleExpression"); stack.push("RelationalOp");
			 * stack.push("Term"); stack.push("AdditiveOp"); stack.push("Factor");
			 * stack.push("MultiplicativeOp");
			 */
			stack.push("Expression");
			break;
		case 52:
			root = root.expressionEscape(root);
			stack.push("ActualParams");
			stack.push("Expression");
			break;

		case 53:
			// root.addNode("ExpressionEnded");

			root = root.expressionEscape(root);
			stack.pop(); // FACTOR
			stack.pop(); // ADDITIVE
			stack.pop(); // TERM
			stack.pop(); // RELATIONAL
			stack.pop();
			if (stack.peek() == "ActualParams") {
				stack.pop();
				stack.pop();

				if (stack.peek() != "Semi_Colon") {
					root = root.parentNode;
				}

			} else {
				stack.pop();
			}

			break;

		case 54:
			// root.addNode("ExpressionEnded");
			root = root.expressionEscape(root);
			stack.pop(); // TERM
			stack.pop(); // RELATIONAL
			stack.pop();
			if (stack.peek() == "ActualParams") {
				stack.pop();
				stack.pop();
				if (stack.peek() != "Semi_Colon") {
					root = root.parentNode;
				}

			} else {
				stack.pop();
			}
			break;

		case 55:
			// root.addNode("ExpressionEnded");
			root = root.expressionEscape(root);
			stack.pop();
			if (stack.peek() == "ActualParams") {
				stack.pop();
				stack.pop();
				if (stack.peek() != "Semi_Colon") {
					root = root.parentNode;
				}

			} else {
				stack.pop();
			}
			break;
														//ARRAYS
		case 56:
			while(stack.peek() != "Closing_Square") {
				stack.pop();
			}
			stack.pop();
			
			
			
			root=root.expressionEscape(root);
			
			
			System.out.println(root.node_type);
			
			
			if(root.childNodes.get(root.childNodes.size()-1).node_type != "Variable_Identifier") {
		
			/*root = root.operatorSwitch(root, "*");
			root.node_type = "Array_Indexing";
			root.value = null;*/
			
		
			
			
			if(root.node_type == "Variable_Identifier") {
				root = root.parentNode;
			}
			
			
			}else {
				root.addNode("Array_Indexing");
			}
			
			break;
		
		case 57:
			
			if(next_token.type == "Opening_Curly") {
			
			root.addNode("Elements");
			root = root.switchRoot(root);
			stack.push("Semi_Colon");
			stack.push("Closing_Curly");
			stack.push("ActualParams");
			stack.push("Opening_Curly");
			
			}else {
				stack.push("Semi_Colon");
				stack.push("Expression");
				
			}
			break;
		case 58:
			root = root.parentNode;
			break;
		
		case 59:
			root = root.expressionEscape(root);
			root = root.parentNode;
			while(stack.peek() != "ActualParams") {
				stack.pop();
			}
			stack.pop();
			stack.pop();
			break;
			
		case 60:
			root.addNode("Elements");
			root = root.switchRoot(root);
			stack.push("Closing_Curly");
			stack.push("ActualParams");
			break;
			
		case 61:		//struct decleration
			root.addNode("Struct_Decleration");
			root = root.switchRoot(root);
			stack.push("Statement");
			stack.push("Closing_Curly");
			stack.push("Struct_Statement");
			stack.push("Opening_Curly");
			stack.push("Variable_Identifier");
			break;
		
		case 62:
			if(next_token.type == "Opening_Bracket") {
				root.addNode("FunctionCall", current_token.value);
				root = root.switchRoot(root);
				stack.push("Statement");
				stack.push("Semi_Colon");
				stack.push("Closing_Bracket");
				stack.push("ActualParams");
				stack.push("Opening_Bracket");
			}else {
				root.addNode("VariableAssignment");
				root = root.switchRoot(root);
				root.addNode("Variable_Identifier",current_token.value);
				stack.push("Statement");
				stack.push("Semi_Colon");
				stack.push("Expression");
				stack.push("Equals");
			}
			break;
		case 67:
			stack.pop();
			root = root.parentNode;
			break;
			
		case 68:
			
			
		}

	}

	public static AST parseProgram(ArrayList<Token> lexedTokens) {

		stack.push("$");
		stack.push("Program");

		int no_of_tokens = lexedTokens.size();
		int production_rule;

		int index1, index2;

		String last_terminal, last_non_terminal;

		Token current_token, next_token, next_next_token;
		next_token = lexedTokens.get(0);
		next_next_token = lexedTokens.get(1);
		for (int index = 0; index < no_of_tokens; index++) {

			current_token = lexedTokens.get(index);

			if (current_token.type == "Single_Line_Comment" || current_token.type == "Multi_Line_Comment") {
				continue;
			}

			if (index != no_of_tokens - 1) {
				next_token = lexedTokens.get(index + 1);
			}

			if (index < no_of_tokens - 2) {
				next_next_token = lexedTokens.get(index + 2);
			}

			System.out.println(stack.peek() + " " + current_token.type + " ------------> " + current_token.value);
			System.out.println("STACK: " + stack);

			if (stack.peek() == "$") {

				if (index != no_of_tokens - 1) {
					System.out.println("Syntax Error at Line " + current_token.line_number);
					System.exit(1);
				}

				System.out.println("Succesffuly Parsed");
				break;

			}

			if (Arrays.asList(terminals).contains(stack.peek())) {
				// top of stack is a terminal

				last_terminal = stack.pop();

				if (last_terminal != current_token.type) {
					// Report a syntax error
					System.out.println("Syntax Error at Line " + current_token.line_number);
					System.out.println("Expected " + last_terminal + " in place of " + current_token.type);
					System.exit(1);

				}

				if (last_terminal == "Variable_Identifier") {
					root.addNode(current_token.type, current_token.value);
					
					if(stack.peek() == "Opening_Square" && next_token.type == "Opening_Square" ) {
						root.childNodes.get(root.childNodes.size()-1).addNode("[]");
					}
					
				}

				if (current_token.type == "Semi_Colon") {
					root = root.parentNode;
				}
			} else {
				// it was a non-terminal

				last_non_terminal = stack.pop();

				index1 = Arrays.asList(non_terminals).indexOf(last_non_terminal);
				index2 = Arrays.asList(terminals).indexOf(current_token.type);

				production_rule = parser_table[index1][index2];

				System.out.println(production_rule);
				if (production_rule == 0) {
					System.out.println("Syntax error, unexpected token at line " + current_token.line_number);
					System.exit(1);
				} else {

					parseSyntax(production_rule, current_token, next_token, next_next_token);

				}

			}

		}

		if (stack.peek() != "$") {
			System.out.println("Syntax Error at EOF!");
			System.exit(1);
		} else {
			System.out.println("Succesfully Parsed");
		}

		while (root.parentNode != null) {
			root = root.parentNode;
		}
		return root;

	}

}
