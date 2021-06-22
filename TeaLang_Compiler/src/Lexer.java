import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lexer {
	
	

	int delta = -10;
	public int[][] transition_table = {
			{ 1, 1, 3, 3, delta, delta, delta, delta, delta, delta, 10, delta, 12, 13, delta, 13, delta, 17, 17, 17,
					delta, delta, delta },
			{ 2, 3, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, 12, 13, delta, 13, delta, 17,
					17, 17, delta, delta, delta },
			{ 4, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, 12, 13, delta, 13, delta,
					17, 17, 17, delta, delta, delta },
			{ 5, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, 13, 12, 15, delta, 15, delta, 17,
					17, 17, delta, delta, delta },
			{ 6, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, 12, 13, delta, 13, delta,
					17, 17, 17, delta, delta, delta },
			{ 7, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, 12, 13, delta, 13, delta,
					17, 17, 17, delta, delta, delta },
			{ 8, delta, delta, delta, delta, delta, 9, 9, 9, delta, delta, delta, 12, 13, delta, 13, delta, 17, 17, 17,
					delta, delta, delta },
			{ 10, delta, delta, delta, delta, delta, delta, delta, delta, delta, 10, delta, 12, 13, delta, 13, delta,
					17, 17, 17, delta, delta, delta },
			{ 11, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, 12, 12, 13, delta, 16, delta,
					17, 17, 17, delta, delta, delta },
			{ delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, 12, 13, delta, 13,
					delta, 18, 18, 18, delta, delta, delta },
			{ 17, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, 12, 13, delta, 13, delta,
					20, 19, 20, delta, delta, delta },
			{ 21, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, 12, 13, delta, 13, delta,
					17, 17, 17, delta, delta, delta },
			{ delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, 14, 13, delta, 13,
					delta, delta, delta, delta, delta, delta, delta },
			{ 22, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta,
					delta, delta, delta, delta, delta, delta, delta, delta },
			{ 10, delta, delta, delta, delta, delta, delta, delta, delta, delta, 10, delta, 12, 13, delta, 13, delta,
					17, 17, 17, delta, delta, delta },
			{ delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, 12, 13, delta, 13,
					delta, 17, 17, 17, delta, delta, delta },
			{ delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta,
					delta, delta, delta, delta, delta, delta, delta, delta }

	};

	public static int new_state_transition(int current_state, char encountered_char) {

		int ascii_value = (int) encountered_char;

		// digits
		if (ascii_value <= 57 && ascii_value >= 48) {

			

		}
		// letters
		else if ((ascii_value <= 90 && ascii_value >= 65) || (ascii_value >= 97 && ascii_value <= 122)) {
			
		}
		// dot in numbers
		else if(encountered_char == '.'){
			
		}
		//plus or minus
		else if ((encountered_char == '+' || encountered_char == '-')) {
			
		}
		//* for mulitplication or multi-line comments
		else if ((encountered_char == '*')) {
			

		}
		//Not operator
		else if ((encountered_char == '!')) {
			
		}
		//comparison operators
		else if ((encountered_char == '>' || encountered_char == '<')) {
			
		}
		//equals sign 
		else if ((encountered_char == '=')) {
			
		}
		//underscore for naming variables 
		else if ((encountered_char == '_')) {
			
		}	
		//division or comment slash
		else if (encountered_char == '/') {
			
		}
		// escpe cahracters slash
		else if (encountered_char == '\\'){
		
		}
		//iverted commas tal istrings
		else if (encountered_char == '"') {
			
		}
		//one spacers
		else if (encountered_char == ';' || encountered_char == ':' || encountered_char == '(' || encountered_char == ')' || encountered_char == '{' || encountered_char == '}'  ) {
			
		}
		else if(encountered_char == '\n') {
			
		}
		return 0;

	}

	public static String readText(String filename) {
		String data = "empty";
		String entire = "";

		try {

			File file = new File(filename);
			Scanner sc = new Scanner(file);
			int line_number = 0;
			while (sc.hasNextLine()) {
				line_number++;
				data = sc.nextLine();
				entire += data + "\n";
				String lexeme = "";
				int state = 0;

				for (int i = 0; i < data.length(); i++) {
					char encountered_char = data.charAt(i);

					if (encountered_char == ' ') {
						lexeme = "";
						state = 0;
						System.out.println("space");
					} else {
						if (encountered_char == '\t') {
							lexeme = "";
							state = 0;
							System.out.println("tabs");
						} else {

							if (encountered_char == '\n') {
								lexeme = "";
								state = 0;
								System.out.println("NewLine");
							} else {
								System.out.println(encountered_char);
								state = new_state_transition(state, encountered_char);
								lexeme += encountered_char;

							}
						}
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
