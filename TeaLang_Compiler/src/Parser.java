import java.util.*;


public class Parser {

	
	
	static String[] non_terminals = {"Program", "Block", "Statement", "VariableDecl", "Assignment" , "Print" ,"If" ,
			"For" , "While" , "Return" , "Function" , "Params" , "Param" , "Type" , "Literal", "Expression" ,
			"SimpleExpression", "RelationalOp", "Term" , "AdditiveOp" , "Factor", "MultiplicativeOp" , "FunctionCall", "SubExpression",
			"Unary","ActualParams"};
	static String[]terminals = { "Variable_Assigner", "Colon", "Equals", "Semi_Colon", "Print_Keyword" , "Return_Keyword" , "If_Keyword" , "Opening_Bracket" , "Closing_Bracket",
						"Opening_Curly", "Closing_Curly", "Else_Keyword", "For_Keyword" ,"While_Keyword", "Float_Keyword" , "Int_Keyword","Bool_Keyword",
						"String_Keyowrd", "Variable_Identifier", "Integer_Value" , "Float_Value", "True_Keyword", "False_Keyword", "String_Value", "Not_Keyword", "Asterisk",
						"Division_Slash", "And_Keyword", "Or_Keyword", "Comparison", "Equality_Class", "Addition"
			
	};
	
	
	//1-8 statements including block and return?? its in the EBNF
	
	static int[][] parser_table ={ 
		{1,0,0,0,2,6,3,0,0,8,0,0,4,5,7,7,7,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //program
		{0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //block
		{1,0,0,0,2,6,3,0,0,8,0,0,4,5,7,7,7,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //statement
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //varaible decl
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0},  //assignment
		{0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //print
		{0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //if
		{0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //for
		{0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //while
		{0,0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  // return 
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //function
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0}, //params
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0}, //param
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,10,10,10,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //type
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,11,11,11,11,11,0,0,0,0,0,0,0,0}, //literal
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,15,0,0,0,0,0,0,14}, //expression
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,15,0,0,0,0,0,0,14}, //simple expression
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,16,0,0,16},			//additiveOp
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,15,0,0,0,0,0,0,14}, //Term
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,17,17,0,0,0,0}, //MultipilicatievOp
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,15,0,0,0,0,0,0,14}, //Factor
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,18,0,0,0,0,0,0,0,0,0,0,0,0,0}, //Function Call
		{0,0,0,0,0,0,0,19,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//SubExpression
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,20,0,0,0,0,0,0,20}, //Unary
		{0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,0,12,11,11,11,11,11,0,15,0,0,0,0,0,0,14}//ActualParams
		};
			
	
	public static void parseProgram(ArrayList<Token> lexedTokens) {
		
		
		
		System.out.println("~~~~~~~~~~~~~~PARSER~~~~~~~~~~~~~~~~~~~~~~~");
		
		Stack<String >stack = new Stack<String>();
		
		stack.push("$");
		stack.push("Program");
		
		int no_of_tokens = lexedTokens.size();
		int production_rule;
		
		int index1,index2;
		
		String last_terminal, last_non_terminal;
		
		Token current_token;
		
		for(int index = 0; index < no_of_tokens; index++) {
			
			current_token = lexedTokens.get(index);
			
			
			
			if(Arrays.asList(terminals).contains(stack.peek())) {
				//top of stack is a terminal
				
				last_terminal = stack.pop();
				
				if(last_terminal != current_token.type) {
					//Report a syntax error
					
					System.out.println("Syntax Error at line "+current_token.line_number);
				}
				
				
			}
			else {
				//it was a non-terminal
				
				last_non_terminal = stack.pop();
				
				index1 = Arrays.asList(non_terminals).indexOf(last_non_terminal);
				index2 = Arrays.asList(terminals).indexOf(current_token.type);
				
				
				production_rule = parser_table[index1][index2];
				
				if(production_rule == 0) {
					System.out.println("Syntax error at line "+current_token.line_number);
				}
				else {
					switch(production_rule) {
					
					
					case 1:
					case 2:
						//etc ghalissa ha mmur norqod
					
					
					}
					
					
					
				}
				
			}
				
			
			
			
		}
		
		
		
		
		
	}
	
	
	
}
