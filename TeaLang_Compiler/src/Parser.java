import java.util.ArrayList;
import java.util.Stack;


public class Parser {

	
	
	String[] non_terminals = {"Program", "Block", "Statement", "VariableDecl", "Assignment" , "Print" ,"If" ,
			"For" , "While" , "Return" , "Function" , "Params" , "Param" , "Type" , "Literal", "Expression" ,
			"SimpleExpression", "RelationalOp", "Term" , "AdditiveOp" , "Factor", "MultiplicativeOp" , "FunctionCall", "SubExpression",
			"Unary","ActualParams"};
	String[]terminals = { "Variable_Assigner", "Colon", "Equals", "Semi_Colon", "Print_Keyword" , "Return_Keyword" , "If_Keyword" , "Opening_Bracket" , "Closing_Bracket",
						"Opening_Curly", "Closing_Curly", "Else_Keyword", "For_Keyword" ,"While_Keyword", "Float_Keyword" , "Int_Keyword","Bool_Keyword",
						"String_Keyowrd", "Variable_Identifier", "Integer_Value" , "Float_Value", "True_Keyword", "False_Keyword", "String_Value", "Not_Keyword", "Asterisk",
						"Division_Slash", "And_Keyword", "Or_Keyword", "Comparison", "Equality_Class"
			
	};
	
	
	//1-8 statements including block and return?? its in the EBNF
	
	int[][] parser_table ={ 
		{1,0,0,0,2,6,3,0,0,8,0,0,4,5,7,7,7,7,0,0,0,0,0,0,0,0,0,0,0,0,0},  //program
		{0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //block
		{1,0,0,0,2,6,3,0,0,8,0,0,4,5,7,7,7,7,0,0,0,0,0,0,0,0,0,0,0,0,0},  //statement
		{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //varaible decl
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0},  //assignment
		{0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //print
		{0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //if
		{0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //for
		{0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  //while
		{0,0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},  // return 
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,0,0,0,0,0,0,0},  //function
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0}, //params
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,0}, //param
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,7,7,7,0,0,0,0,0,0,0,0,0,0,0,0,0}, //type
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,9,9,9,9,0,0,0,0,0,0,0}, //literal
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,9,9,9,9,0,0,0,0,0,0,0,0}, //expression
		};
			
	
	public static void parseProgram(ArrayList<Token> lexedTokens) {
		
		System.out.println("~~~~~~~~~~~~~~PARSER~~~~~~~~~~~~~~~~~~~~~~~");
		
		Stack<String >stack = new Stack<String>();
		
		stack.push("$");
		stack.push("Program");
		
		int no_of_tokens = lexedTokens.size();
		
		
		
		
		for(int index = 0; index < no_of_tokens; index++) {
			
			
			System.out.println(lexedTokens.get(index).value);
			
		}
		
		
		
		
		
	}
	
	
	
}
