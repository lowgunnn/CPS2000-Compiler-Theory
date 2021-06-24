import java.util.ArrayList;


public class Parser {

	
	
	public static void parseProgram(ArrayList<Token> lexedTokens) {
		
		System.out.println("~~~~~~~~~~~~~~PARSER~~~~~~~~~~~~~~~~~~~~~~~");
		
		int no_of_tokens = lexedTokens.size();
		
		for(int index = 0; index < no_of_tokens; index++) {
			
			
			System.out.println(lexedTokens.get(index).value);
			
		}
		
		
		
		
		
	}
	
	
	
}
