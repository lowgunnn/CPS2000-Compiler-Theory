import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lexer {

	public static int[][] transition_table = {
			{ 1, 1, 3, 3, -1, -1, -1, -1, -1, -1, 10, -1, 12, 13, -1, 13, -1, 17, 17, 17, -1, -1, -1 }, // digits
			{ 2, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 12, 13, -1, 13, -1, 17, 17, 17, -1, -1, -1 }, // . as in 5.6
			{ 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 12, 13, -1, 13, -1, 17, 17, 17, -1, -1, -1 }, //
			{ 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 13, 12, 15, -1, 15, -1, 17, 17, 17, -1, -1, -1 },
			{ 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 12, 13, -1, 13, -1, 17, 17, 17, -1, -1, -1 },
			{ 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 12, 13, -1, 13, -1, 17, 17, 17, -1, -1, -1 },
			{ 8, -1, -1, -1, -1, -1, 9, 9, 9, -1, -1, -1, 12, 13, -1, 13, -1, 17, 17, 17, -1, -1, -1 },
			{ 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, -1, 12, 13, -1, 13, -1, 17, 17, 17, -1, -1, -1 },
			{ 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 12, 12, 13, -1, 16, -1, 17, 17, 17, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 12, 13, -1, 13, -1, 18, 18, 18, -1, -1, -1 },
			{ 17, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 12, 13, -1, 13, -1, 20, 19, 20, -1, -1, -1 },
			{ 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 12, 13, -1, 13, -1, 17, 17, 17, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 14, 13, -1, 13, -1, -1, -1, -1, -1, -1, -1 },
			{ 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, -1, 12, 13, -1, 13, -1, 17, 17, 17, -1, -1, -1 },
			{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 12, 13, -1, 13, -1, 17, 17, 17, -1, -1, -1 },
			{ 22, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }

	};

	public static int[] final_states = { 1, 3, 4, 5, 7, 8, 9, 10, 11, 14, 16, 20, 21,22};

	public static int new_state_transition(int current_state, char encountered_char) {

		int ascii_value = (int) encountered_char;

		// digits
		if (ascii_value <= 57 && ascii_value >= 48) {

			return transition_table[0][current_state];

		}
		// letters
		else if ((ascii_value <= 90 && ascii_value >= 65) || (ascii_value >= 97 && ascii_value <= 122)) {
			return transition_table[13][current_state];
		}
		// dot in numbers
		else if (encountered_char == '.') {
			return transition_table[1][current_state];
		}
		// plus or minus
		else if ((encountered_char == '+' || encountered_char == '-')) {
			return transition_table[2][current_state];
		}
		// * for multiplication or multi-line comments
		else if ((encountered_char == '*')) {
			return transition_table[3][current_state];

		}
		// Not operator
		else if ((encountered_char == '!')) {
			return transition_table[4][current_state];
		}
		// comparison operators
		else if ((encountered_char == '>' || encountered_char == '<')) {
			return transition_table[5][current_state];
		}
		// equals sign
		else if ((encountered_char == '=')) {
			return transition_table[6][current_state];
		}
		// underscore for naming variables
		else if ((encountered_char == '_')) {
			return transition_table[7][current_state];
		}
		// division or comment slash
		else if (encountered_char == '/') {
			return transition_table[8][current_state];
		}
		// escpe cahracters slash
		else if (encountered_char == '\\') {
			return transition_table[9][current_state];
		}
		// iverted commas tal istrings
		else if (encountered_char == '"') {
			return transition_table[10][current_state];
		}
		// one spacers
		else if (encountered_char == ';' || encountered_char == ':' || encountered_char == '('
				|| encountered_char == ')' || encountered_char == '{' || encountered_char == '}') {
			return transition_table[11][current_state];
		} 
		else if (encountered_char == '\n') {
			return transition_table[12][current_state];
		}
		// a printable character enclosed within " ", <- case for encountering
		else if ((32 <= ascii_value) && (ascii_value <= 127)) {

			return transition_table[14][current_state];
		}
		else if(encountered_char == '\u001a') {
			return transition_table[15][current_state];
		}
		// otherwise an unexpected character not in grammer, send 0 state
		else {
			return -1;
		}

	}

	public static String readText(String filename) {
		String data = "empty";
		String entire = "";
		String lexeme = "";
		int state = 0;
		int new_state;
		boolean is_final = false;

		try {

			File file = new File(filename);
			Scanner sc = new Scanner(file);
			int line_number = 0;
			while (sc.hasNextLine()) {
				line_number++;
				data = sc.nextLine();
				entire = data + "\n";
				
				if(!sc.hasNextLine()) {
					entire +='\u001a';
				}
				
				for (int i = 0; i < entire.length(); i++) {
					char encountered_char = entire.charAt(i);

					// skips initials spaces or newlines before tokens, this does not include those
					// inside " " string identifiers since previously we would have encountered an
					// inverted comma
					if (lexeme == ""
							&& (encountered_char == ' ' || encountered_char == '\t' || encountered_char == '\n')) {
						continue;
					}

					new_state = new_state_transition(state, encountered_char);
					
					if (new_state == -1) {
						
						// bad character, transitions to state delta

						// check if current state is a final state, if it is then correct Token
						is_final = false;
						for (int final_state : final_states) {

							if (final_state == state) {
								is_final = true;
							}

						}

						if (is_final) {

							System.out.println(lexeme);
							lexeme = "";
							state = 0;
							i--;
						} else {
							System.out.println("LEXICAL ERROR ENCOUNTERED AT LINE NUMBER: " + line_number);
							break;
						}

					}
					else {
						lexeme += encountered_char;
						state = new_state;
					}

				}

			}
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.\nThe file " + filename + " was not found");
			// opens and reads the specified file
		}

		return entire;
	}

	public static int processChars(String text) {

		return 0;

	}

}
