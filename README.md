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
