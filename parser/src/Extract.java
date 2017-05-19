import java.util.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Extract {
	public static void main(String[] args) throws Exception {
		ANTLRFileStream input = new ANTLRFileStream(args[0]);
		SygusLexer lexer = new SygusLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SygusParser parser = new SygusParser(tokens);

		ANTLRErrorStrategy es = new CustomErrorStrategy();
		parser.setErrorHandler(es);

		ParseTree tree;
		try{
			tree = parser.start();
			System.out.println("Accepted");
		} catch(Exception ex) {
			System.out.println("Not Accepted");
			return;
		}

		ParseTreeWalker walker = new ParseTreeWalker();
		SygusExtractor extractor = new SygusExtractor();
		walker.walk(extractor, tree);

		for(SygusExtractor.SynthRequest request : extractor.requests) {
			System.out.println("Name:" + request.name);
			for(Map.Entry<String, String> entry : request.arguments.entrySet()) {
				System.out.println("Argument:" + entry.getKey() + " Type:" + entry.getValue());
			}
			System.out.println("Return Type:" + request.returnType);
		}
	}
}

class CustomErrorStrategy extends DefaultErrorStrategy{
	@Override
	public void reportError(Parser recognizer, RecognitionException e){
		throw e;
	}
}
