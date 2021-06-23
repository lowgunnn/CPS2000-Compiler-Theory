
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
			if(lexeme == +)
			
		}

	}

}
