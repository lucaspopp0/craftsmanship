package parser;

import java.util.List;

interface Symbol {
	
	public ParseState parse(List<Token> input);
	
}
