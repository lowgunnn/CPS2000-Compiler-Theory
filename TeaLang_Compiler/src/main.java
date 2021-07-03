import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class main {

	public static void main(String[] args) {
		
		
		Lexer lexer  = new Lexer();
		Parser parser = new Parser();
		Semantic_Visitor semantic = new Semantic_Visitor();
		Interpreter_Visitor interpreter = new Interpreter_Visitor();
		
		ArrayList<Token> lexedTokens = new ArrayList<>();
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~LEXER~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("The below text is the generated list of tokens");
		
		lexedTokens = lexer.readText("example.txt");
		
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~PARSER~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Below we see the contents of the stack, which contain terminals and non-terminals and how they change\n"
				+ "after pop and push operations, we also see what Production Rules have been fired and its result\n");
		AST root = parser.parseProgram(lexedTokens);
		
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~XML~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Below is the XML Representation built from the previously generated AST\n");
		
		XML_Visitor xml = new XML_Visitor();
		
		xml.traverse(root, 0);
		
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~SEMANTIC~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("All created scope instances, as well as updated function headers follow, i decided to not include expression evaluations"
				+ "\nas they can be very lengthy at times");
		
		Stack<Map<String, String>> table = semantic.traverse(root);
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~TEA LANG INTERPRETER~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		interpreter.traverse(root,true);
		
		
		String x = "Hello";
		String y = "World";
		
	
	}

}
