# Compiler-Theory
A Basic Compiler written in Java for the fictitious TeaLang programming language.

The TeaLang programming language

The following rules describe the syntax of TeaLang in EBNF. Each rule has three parts: a left
hand side (LHS), a right-hand side (RHS) and the ‘::=’ symbol separating these two sides. The
LHS names the EBNF rule whereas the RHS provides a description of this name. Note that the
RHS uses four control forms namely sequence, choice, option and repetition. In a sequence order
is important and items appear left-to-right. The stroke symbol ( . . . | . . . ) is used to denote choice
between alternatives. One item is chosen from this list; order is not important. Optional items are
enclosed in square brackets ([ . . . ]) indicating that the item can either be included or discarded.
Repeatable items are enclosed in curly brackets ({ . . . }); the items within can be repeated zero or
more times. For example, a Block consists of zero or more Statement enclosed in curly brackets.

〈Letter〉::= [A-Za-z] <br>
〈Digit〉::= [0-9]<br>
〈Printable〉::= [\x20-\x7E]<br>
〈Type〉::= ‘float’ | ‘int’ | ‘bool’ | ‘string’<br>
〈BooleanLiteral 〉::= ‘true’ | ‘false’<br>
〈IntegerLiteral 〉::= 〈Digit〉{ 〈Digit〉}<br>
〈FloatLiteral 〉::= 〈Digit〉{ 〈Digit〉} ‘.’ 〈Digit〉{ 〈Digit〉}<br>
〈StringLiteral 〉::= ‘ " ’ { 〈Printable〉} ‘ " ’<br>
〈Literal 〉::= 〈BooleanLiteral 〉
              | 〈IntegerLiteral 〉
              | 〈FloatLiteral 〉
              | 〈StringLiteral 〉<br>
〈Identifier 〉::= ( ‘ ’ | 〈Letter 〉) { ‘ ’ | 〈Letter 〉| 〈Digit〉}<br>
〈MultiplicativeOp〉::= ‘*’ | ‘/’ | ‘and’<br>
〈AdditiveOp〉::= ‘+’ | ‘-’ | ‘or’<br>
〈RelationalOp〉::= ‘<’ | ‘>’ | ‘==’ | ‘!=’ | ‘<=’ | ‘>=’<br>
〈ActualParams〉::= 〈Expression〉{ ‘,’ 〈Expression〉}<br>
〈FunctionCall 〉::= 〈Identifier 〉‘(’ [ 〈ActualParams〉] ‘)’<br>
〈SubExpression〉::= ‘(’ 〈Expression〉‘)’<br>
〈Unary〉::= ( ‘-’ | ‘not’ ) 〈Expression〉<br>
〈Factor 〉::= 〈Literal 〉
             | 〈Identifier 〉
             | 〈FunctionCall 〉
             | 〈SubExpression〉
             | 〈Unary〉<br>

〈Term〉::= 〈Factor 〉{ 〈MultiplicativeOp〉〈Factor 〉}<br>
〈SimpleExpression〉::= 〈Term〉{ 〈AdditiveOp〉〈Term〉}<br>
〈Expression〉::= 〈SimpleExpression〉{ 〈RelationalOp〉〈SimpleExpression〉}<br>
〈Assignment〉::= 〈Identifier 〉‘=’ 〈Expression〉<br>
〈VariableDecl 〉::= ‘let’ 〈Identifier 〉‘:’ 〈Type〉‘=’ 〈Expression〉<br>
〈PrintStatement〉::= ‘print’ 〈Expression〉<br>
〈RtrnStatement〉::= ‘return’ 〈Expression〉<br>
〈IfStatement〉::= ‘if’ ‘(’ 〈Expression〉‘)’ 〈Block〉[ ‘else’ 〈Block〉]<br>
〈ForStatement〉::= ‘for’ ‘(’ [ 〈VariableDecl 〉] ’;’ 〈Expression〉’;’ [ 〈Assignment〉] ‘)’ 〈Block〉<br>
〈WhileStatement〉::= ‘while’ ‘(’ 〈Expression〉‘)’ 〈Block〉<br>
〈FormalParam〉::= 〈Identifier 〉‘:’ 〈Type〉<br>
〈FormalParams〉::= 〈FormalParam〉{ ‘,’ 〈FormalParam〉}<br>
〈FunctionDecl 〉::= 〈type〉〈Identifier 〉‘(’ [ 〈FormalParams〉] ‘)’ 〈Block〉<br>
〈Statement〉::= 〈VariableDecl 〉‘;’
                | 〈Assignment〉‘;’
                | 〈PrintStatement〉‘;’
                | 〈IfStatement〉
                | 〈ForStatement〉
                | 〈WhileStatement〉
                | 〈RtrnStatement〉‘;’
                | 〈FunctionDecl 〉
                | 〈Block〉<br>
〈Block〉::= ‘{’ { 〈Statement〉} ‘}’<br>
〈Program〉::= { 〈Statement〉}<br>

<h2>Lexer</h2><br>
The Lexer makes use of a DFSA, such as in [Formal Language and Automate Theory](https://en.wikipedia.org/wiki/Automata_theory), with a set of states, final states, defined alphabet, and valid and erroneous transitions, which are taking an input of an alphabet member, and traversing to the next state if possible.
The lexer is implemented using a table-driven approach, in which a table, with coloumns representing the current state and rows representing the input character type, represents all the possible transitions from all the possible states using all the possible characters.

For example, if the process is currently in State 1, and it receives a number as the next character input, the we locate the value located at coloumn "State 1" and row "digit", to identify the next State within our DFA. The character types would be the language's alphabet, of which different tokens can be constructed with. The tokens themselves are defined by crafting out the different transitions from different states using different character types and cross-checking with our implemented table to see the resultant state, and its final configuration determines the type of token acquired, or if it is still expecting more elements to finish said token.<br>

<h2>Parser</h2><br>
The Parser is responsible for ensuring syntactical correctness. The approach taken was through an implementation of a Predictive Parser in which from an initial start token of any type, the remaining tokens in that sequence are predicted, and if any other tokens are presented which do not match the predicted sequence, then we can conlcude that there is a syntax error.<br>
The Parser may predict a terminal, or non-terminal symbol.<br>
A terminal symbol terminates, whilst a non-terminal may be further expanded into more terminal or non-terminal symbols.
Using the above EBNF as an example, the non-terminal symbol Return, which houses the expression for a return statement consists of the terminal symbol "return" and the non-terminal symbol Expression.
These terminal and non-terminal symbols are present in the sequence that the parser predicts, so that it may be able to construct further predictions as each token is supplied

FIRST - The First Set indicates all the token types that can be encountered upon
