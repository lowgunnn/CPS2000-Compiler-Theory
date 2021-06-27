import java.util.*;


public class Parser {

	
	
	static String[] non_terminals = {"Program", "Block", "Statement", "VariableDecl", "Assignment" , "Print" ,"If" ,
			"For" , "While" , "Return" , "Function" , "Params" ,  "Type" , "Literal", "Expression" ,
			"SimpleExpression", "RelationalOp", "Term" , "AdditiveOp" , "Factor", "MultiplicativeOp" , "FunctionCall", "SubExpression",
			"Unary","ActualParams","Else", "Comma"};
	static String[]terminals = { "Variable_Assigner", "Colon", "Equals", "Semi_Colon", "Print_Keyword" , "Return_Keyword" , "If_Keyword" , "Opening_Bracket" , "Closing_Bracket",
						"Opening_Curly", "Closing_Curly", "Else_Keyword", "For_Keyword" ,"While_Keyword", "Float_Keyword" , "Int_Keyword","Bool_Keyword",
						"String_Keyowrd", "Variable_Identifier", "Integer_Value" , "Float_Value", "True_Keyword", "False_Keyword", "String_Value", "Not_Keyword", "Asterisk",
						"Division_Slash", "And_Keyword", "Or_Keyword", "Comparison", "Equality_Class", "Addition", "End_Of_File" , "Comma"
			
	};
	
	
	//1-8 statements including block and return?? its in the EBNF
	static Stack<String >stack = new Stack<String>();
	static int[][] parser_table ={ 
		{1,0,0,0,2,6,3,0,0,8,0,0,4,5,7,7,7,7,26,0,0,0,0,0,0,0,0,0,0,0,0,0,21,0},  //program
		{0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //block
		{1,0,0,0,2,6,3,0,0,8,22,23,4,5,7,7,7,7,26,0,0,0,0,0,0,0,0,0,0,0,0,0,21,0},  //statement
		{25,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //varaible decl
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,27,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //assignment
		{0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //print
		{0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //if
		{0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //for
		{0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //while
		{0,0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  // return 
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //function
		{0,0,0,0,0,0,0,0,28,0,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,29}, //params
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,10,10,10,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //type
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,11,11,11,11,11,0,0,0,0,0,0,0,0,0,0}, //literal
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,14,0,0,0,0,0,0,14,0,0}, //expression
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,14,0,0,0,0,0,0,14,0,0}, //simple expression
		{0,0,0,32,0,0,0,0,32,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,16,0,0,16,0,32},	//relationalOp
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,14,0,0,0,0,0,0,14,0,0}, //Term
		{0,0,0,31,0,0,0,0,31,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,16,0,0,16,0,31},			//additiveOp
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,14,0,0,0,0,0,0,14,0,0}, //Factor
		{0,0,0,30,0,0,0,0,30,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,17,17,0,0,0,0,0,30}, //MultipilicatievOp
		{0,0,0,0,0,0,0,0,0,24,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Returns 
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,18,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Function Call
		{0,0,0,0,0,0,0,19,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//SubExpression
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,20,0,0,0,0,0,0,20,0,0}, //Unary
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,14,0,0,0,0,0,0,14,0,0},//ActualParams
		{0,0,0,0,0,0,0,0,0,0,0,23,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Else-Statements
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0} //Comma
		};
	
	
	public static void parseSyntax(int production_rule) {
		
		//based on the production rule, the contents of the stack are adjusted
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
			//no need to add statement terminal since block does it
			stack.push("Block");
			stack.push("Closing_Bracket");
			stack.push("Expression");
			stack.push("Opening_Bracket");
			break;
		case 4:
			//For Loop
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
			//while loop
			System.out.println("Entered While Loop");
			stack.push("Block");
			stack.push("Closing_Bracket");
			stack.push("Expression");
			stack.push("Opening_Bracket");
			break;
		case 6:
			//returns
			stack.push("Expression");
			break;
		case 7:
			//Function Declarations
			stack.push("Block");
			stack.push("Closing_Bracket");
			stack.push("Params");
			stack.push("Opening_Bracket");
			stack.push("Variable_Identifier");  //I am aware that function names are not variables but does not make a difference 
			break;
		case 8:
			//block
			System.out.println("ENTERED BLOCK");
			stack.push("Statement");
			stack.push("Closing_Curly");
			stack.push("Statement");
			break;
			
		case 9:
			//Parameters
			stack.push("Params");
			stack.push("Type");
			stack.push("Colon");
			break;
			
		case 10:
			break;
		
		case 11:
			stack.push("SimpleExpression");
			stack.push("RelationalOp");
			stack.push("Term");
			stack.push("AdditiveOp");
			stack.push("Factor");
			stack.push("MultiplicativeOp");  
			break;
			
		case 12:
			
			if(stack.peek()== "(") {
				//function call
				stack.push("SimpleExpression");
				stack.push("RelationalOp");
				stack.push("Term");
				stack.push("AdditiveOp");
				stack.push("Factor");
				stack.push("MultiplicativeOp");
				stack.push(")");
				stack.push("ActualParams");
				stack.push("(");
			}else {
				stack.push("SimpleExpression");
				stack.push("RelationalOp");
				stack.push("Term");
				stack.push("AdditiveOp");
				stack.push("Factor");
				stack.push("MultiplicativeOp");  
			}
			
			break;
		case 13:
			//sub-expression
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
			stack.push("SimpleExpression");
			stack.push("RelationalOp");
			stack.push("Term");
			stack.push("AdditiveOp");
			stack.push("Factor");
			stack.push("MultiplicativeOp");  
			stack.push("Expression");
			break;
		case 17:
			//Multiplicative Op Encountered
			stack.push("MultiplicativeOp");
			stack.push("Factor");
			break;
		//These cases may be numbered wrong because are either
		// a) Production Rules brought about by use of Follow-Sets, I first started iplementing only those which make use of the FIRST Set
		// b) Cases which I did not think of
		case 21:
			System.out.println(stack);
			System.out.println("Successfully Parsed!");
			break;
		case 22:
			stack.pop(); //pops the closing curling bracket
			System.out.println("EXITTED BLOCK~~~~~~~~~~~~~~~~~~~~~~~");
			//end of block
			break;
		case 23:
			stack.push("Block");
			break;
		case 25:
			// secondary variable declaration parse rule, this does not include adding other statement afterwards
			//used for one-off for loop variable declarations
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
			break;
			
		case 28:
			System.out.println("No more parameters!");
			stack.pop(); //pops the closing bracket expected afterwards
			System.out.println("POPPED");
			break;
		case 29:
			//Parameters
			stack.push("Params");
			stack.push("Type");
			stack.push("Colon");
			stack.push("Variable_Identifier");
			break;
		case 30:
			//END THE EXPRESSION AT LOWEST RECURSION
			stack.pop(); //FACTOR
			stack.pop(); //ADDITIVE
			stack.pop(); //TERM
			stack.pop(); //RELATIONAL
			stack.pop(); //SIMPLE EXP
			stack.pop(); //FOLLOW CHAR -> ; ) ,
			break;
		case 31:
			//END THE EXPRESSION AT SECOND LOWEST RECURSION
			stack.pop(); //TERM
			stack.pop(); //RELATIONAL
			stack.pop(); //SIMPLE EXP
			stack.pop(); //FOLLOW CHAR -> ; ) ,
			break;
		case 32:
			//END THE EXPRESSION AT HIGHEST RECURSION
			stack.pop(); //SIMPLE EXP
			stack.pop(); //FOLLOW CHAR -> ; ) ,
			break;
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
