
public class XML_Visitor {

	public void traverse(AST root, int tabs) {

		AST temp;

		String indentation = "";

		for (int i = 0; i < tabs; i++) {
			indentation += "\t";
		}

		if (root.childNodes.size() == 0) {

			System.out.print(indentation + "<" + root.node_type + ">");
			System.out.print(root.value);
			System.out.print("</" + root.node_type + ">\n");
			return;

		} else {
			if (root.value == null) {
				System.out.println(indentation + "<" + root.node_type + ">");
			} else {
				System.out.println(indentation + "<" + root.node_type + " = " + root.value + ">");
			}

			for (int i = 0; i < root.childNodes.size(); i++) {

				temp = root.childNodes.get(i);
				this.traverse(temp, tabs + 1);

			}
			System.out.println(indentation + "</" + root.node_type + ">");
			return;
		}

	}

}
