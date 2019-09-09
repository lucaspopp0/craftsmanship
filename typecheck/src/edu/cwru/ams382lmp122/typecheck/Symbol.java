package edu.cwru.ams382lmp122.typecheck;

import java.util.List;

interface Symbol {
	
	ParseState parse(List<Token> input);
	
}
