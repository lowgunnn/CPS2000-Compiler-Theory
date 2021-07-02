
public class Token {

	String type;
	String value;
	int line_number;

	public Token(String lexeme, int line_number, int final_state) {

		this.value = lexeme;
		this.line_number = line_number;

		this.type = getTokenType(final_state,lexeme);

	}

	public String getTokenType(int final_state, String lexeme) {

		switch (final_state) {

		case 1:
			return "Integer_Value";
		case 3:
			return "Float_Value";
		case 4:
			return "Addition";
		case 5:
			return "Asterisk";
		case 7:
			return "Comparison";
		case 8:
			return "Equals";
		case 9:
			return "Equality_Class";
		case 10:
			
			//These are the "strings" so we must include reserved key words aswell
			switch(lexeme) {
				
			case "let": 
				return "Variable_Assigner";
			case "print":
				return "Print_Keyword";
			case "return":
				return "Return_Keyword";
			case "if":
				return "If_Keyword";
			case "else":
				return "Else_Keyword";
			case "for":
				return "For_Keyword";
			case "while":
				return "While_Keyword";
			case "float":
				return "Float_Keyword";
			case "int":
				return "Int_Keyword";
			case "bool":
				return  "Bool_Keyword";
			case "string":
				return "String_Keyword";
			case "true":
				return "True_Keyword";
			case "false":
				return "False_Keyword";
			case "and":
				return "And_Keyword";
			case "or":
				return "Or_Keyword";
			case "not":
				return "Not_Keyword";
			default:
				return "Variable_Identifier";
			
			
			}
		case 11:
			return "Division_Slash";
		case 14:
			return "Single_Line_Comment";
		case 16:
			return "Multi_Line_Comment";
		case 20:
			return "String_Value";
		case 21:
			
			switch(lexeme) {
			
			case ";":
				return "Semi_Colon";
			case ":":
				return "Colon";
			case "(":
				return "Opening_Bracket";
			case ")":
				return "Closing_Bracket";
			case "{":
				return "Opening_Curly";
			case "}":
				return "Closing_Curly";
			case ",":
				return "Comma";
			
			}
		case 22:
			
			return "End_Of_File";
		
		default:
			System.out.println("Unexpected Token Type Encountered!!");
			return "Error_Token ??";
					
			
			
		}

	}

}
