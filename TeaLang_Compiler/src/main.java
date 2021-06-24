import java.util.ArrayList;

public class main {

	public static void main(String[] args) {
		
		
		Lexer lexer  = new Lexer();
		Parser parser = new Parser();
		
		ArrayList<Token> lexedTokens = new ArrayList<>();
		
		lexedTokens = lexer.readText("example.txt");
		
		parser.parseProgram(lexedTokens);
		
		
		
		
		
	}

}
