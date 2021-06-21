import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;



public class Lexer {
	
	
	public static String readText(String filename) {
		
		
		String data = "empty";
		String entire = "";
		try {

			File file = new File(filename);
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				data = sc.nextLine();
				entire += data;
				
			}
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.\nThe file " + filename + " was not found");
			// opens and reads the specified file
    }
		
	
		return entire;
	}
	
}


