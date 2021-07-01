import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class main {

	public static void main(String[] args) {
		
		
		Lexer lexer  = new Lexer();
		Parser parser = new Parser();
		Semantic_Visitor semantic = new Semantic_Visitor();
		
		ArrayList<Token> lexedTokens = new ArrayList<>();
		
		lexedTokens = lexer.readText("example.txt");
		
		AST root = parser.parseProgram(lexedTokens);
		
		
		
		XML_Visitor xml = new XML_Visitor();
		
		xml.traverse(root, 0);
		
		Stack<Map<String, String>> table = semantic.traverse(root);
		
		System.out.println(table);
		
		
		
		
	}

}
