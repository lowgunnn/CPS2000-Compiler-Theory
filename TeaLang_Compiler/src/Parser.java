import java.util.*;


public class Parser {

	
	
	static String[] non_terminals = {"Program", "Block", "Statement", "VariableDecl", "Assignment" , "Print" ,"If" ,
			"For" , "While" , "Return" , "Function" , "Params" , "Param" , "Type" , "Literal", "Expression" ,
			"SimpleExpression", "RelationalOp", "Term" , "AdditiveOp" , "Factor", "MultiplicativeOp" , "FunctionCall", "SubExpression",
			"Unary","ActualParams","Else"};
	static String[]terminals = { "Variable_Assigner", "Colon", "Equals", "Semi_Colon", "Print_Keyword" , "Return_Keyword" , "If_Keyword" , "Opening_Bracket" , "Closing_Bracket",
						"Opening_Curly", "Closing_Curly", "Else_Keyword", "For_Keyword" ,"While_Keyword", "Float_Keyword" , "Int_Keyword","Bool_Keyword",
						"String_Keyowrd", "Variable_Identifier", "Integer_Value" , "Float_Value", "True_Keyword", "False_Keyword", "String_Value", "Not_Keyword", "Asterisk",
						"Division_Slash", "And_Keyword", "Or_Keyword", "Comparison", "Equality_Class", "Addition", "End_Of_File"
			
	};
	
	
	//1-8 statements including block and return?? its in the EBNF
	static Stack<String >stack = new Stack<String>();
	static int[][] parser_table ={ 
		{1,0,0,0,2,6,3,0,0,8,0,0,4,5,7,7,7,7,26,0,0,0,0,0,0,0,0,0,0,0,0,0,21},  //program
		{0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //block
		{1,0,0,0,2,6,3,0,0,8,22,0,4,5,7,7,7,7,26,0,0,0,0,0,0,0,0,0,0,0,0,0,21},  //statement
		{25,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //varaible decl
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,27,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //assignment
		{0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //print
		{0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //if
		{0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //for
		{0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //while
		{0,0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  // return 
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //function
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //params
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //param
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,10,10,10,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //type
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,11,11,11,11,11,0,0,0,0,0,0,0,0,0}, //literal
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,15,0,0,0,0,0,0,14,0}, //expression
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,15,0,0,0,0,0,0,14,0}, //simple expression
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,16,0,0,16,0},			//additiveOp
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,15,0,0,0,0,0,0,14,0}, //Term
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,17,17,0,0,0,0,0}, //MultipilicatievOp
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,15,0,0,0,0,0,0,14,0}, //Factor
		{0,0,0,0,0,0,0,0,0,24,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Returns 
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,18,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Function Call
		{0,0,0,0,0,0,0,19,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//SubExpression
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,20,0,0,0,0,0,0,20,0}, //Unary
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,15,0,0,0,0,0,0,14,0},//ActualParams
		{0,0,0,0,0,0,0,0,0,0,0,23,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0} //Else-Statements
		};
	
	
	public static void parseSyntax(int production_rule) {
		
		switch(production_rule) {
		
		case 1:
			//declaring variables
			stack.push("Statement");
			stack.push("Semi_Colon");
			stack.push("Expression");
			stack.push("Equals");
			stack.push("Type");
			stack.push("Colon");
			stack.push("Variable_Identifier");
			break;
		case 2:
			//print statements
			stack.push("Statement");
			stack.push("Semi_Colon");
			stack.push("Expression");
			break;
		case 3:
			//if statements 
			stack.push("Statement");
			stack.push("Else");
			stack.push("Block");
			stack.push("Closing_Bracket");
			stack.push("Expression");
			stack.push("Opening_Bracket");
			break;
		case 4:
			//For Loop
			System.out.println("Entered For-Loop");
			stack.push("Statement");
			stack.push("Block");
			stack.push("Closing_Bracket");
			stack.push("Assignment");
			stack.push("Semi_Colon");
			stack.push("Expression");
			stack.push("VariableDecl");	
			stack.push("Opening_Bracket");
			break;	
		case 5:
			System.out.println("Entered While Loop");
			stack.push("Statement");
			stack.push("Block");
			stack.push("Closing_Bracket");
			stack.push("Expression");
			stack.push("Opening_Bracket");
			break;
		case 6:
			stack.push("Expression");
			break;
		case 8:
			System.out.println("ENTERED BLOCK");
			stack.push("Statement");
			stack.push("Closing_Curly");
			stack.push("Statement");
			break;
		case 21:
			System.out.println("Successfully Parsed!");
			break;
		case 22:
			stack.pop();
			stack.pop();
			System.out.println("EXITTED BLOCK~~~~~~~~~~~~~~~~~~~~~~~");
			//end of block
			break;
		case 23:
			stack.push("Block");
			break;
		case 25:
			// secondary variable declaration parse rule, this does not include adding other statement afterwards
			stack.push("Semi_Colon");
			stack.push("Expression");
			stack.push("Equals");
			stack.push("Type");
			stack.push("Colon");
			stack.push("Variable_Identifier");
			break;
		case 26:
			//variable assignment
			stack.push("Statement");
			stack.push("Semi_Colon");
			stack.push("Expression");
			stack.push("Equals");
			break;
		case 27:
			stack.push("Semi_Colon");
			stack.push("Expression");
			stack.push("Equals");
			
			
		}
		
			
		
		
	}
	
	
	public static void parseProgram(ArrayList<Token> lexedTokens) {
		
		
		
		System.out.println("~~~~~~~~~~~~~~PARSER~~~~~~~~~~~~~~~~~~~~~~~");
		
		
		
		stack.push("$");
		stack.push("Program");
		
		int no_of_tokens = lexedTokens.size();
		int production_rule;
		
		int index1,index2;
		
		String last_terminal, last_non_terminal;
		
		Token current_token;
		
		for(int index = 0; index < no_of_tokens; index++) {
			
			current_token = lexedTokens.get(index);
			
			System.out.println(stack.peek()+current_token.type);
			System.out.println("STACK: "+stack);
			
			
			if(stack.peek() == "$") {
					
					if(index != no_of_tokens-1) {
						System.out.println("Syntax Error at Line "+current_token.line_number);
						System.exit(1);
					}
				
					System.out.println("Succesffuly Parsed");
					break;
				
			}
			
			if(Arrays.asList(terminals).contains(stack.peek())) {
				//top of stack is a terminal
				
				last_terminal = stack.pop();
				
				if(last_terminal != current_token.type) {
					//Report a syntax error
					System.out.println("Syntax Error at Line "+current_token.line_number);
					System.out.println("Expected "+last_terminal+", at "+current_token.type);
					System.exit(1);
					
				}
				
				
			}
			else {
				//it was a non-terminal
				
				last_non_terminal = stack.pop();
				
				index1 = Arrays.asList(non_terminals).indexOf(last_non_terminal);
				index2 = Arrays.asList(terminals).indexOf(current_token.type);
				
				
				
				production_rule = parser_table[index1][index2];
				
				System.out.println(production_rule);
				if(production_rule == 0) {
					System.out.println("Syntax error at line "+current_token.line_number);
					System.exit(1);
				}
				else {
					
						parseSyntax(production_rule);
		
					
					
					
				}
				
			}
				
			
			
			
		}
		
		
		
		
		
	}
	
	
	
}
