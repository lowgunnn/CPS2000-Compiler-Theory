import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class main {

	public static void main(String[] args) {
		
		
		Lexer lexer  = new Lexer();
		Parser parser = new Parser();
		Semantic_Visitor semantic = new Semantic_Visitor();
		
		ArrayList<Token> lexedTokens = new ArrayList<>();
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~LEXER~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		lexedTokens = lexer.readText("example.txt");
		
		AST root = parser.parseProgram(lexedTokens);
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~XML~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		XML_Visitor xml = new XML_Visitor();
		
		xml.traverse(root, 0);
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~SEMANTIC~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Stack<Map<String, String>> table = semantic.traverse(root);
		
		System.out.println(table);
		
		
		
		
	}

}
