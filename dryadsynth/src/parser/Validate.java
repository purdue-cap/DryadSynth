import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Validate {
	public static void main(String[] args) throws Exception {
		ANTLRFileStream input = new ANTLRFileStream(args[0]);
		SygusLexer lexer = new SygusLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SygusParser parser = new SygusParser(tokens);

		ANTLRErrorStrategy es = new CustomErrorStrategy();
		parser.setErrorHandler(es);

		try{
			parser.start();
			System.out.println("Accepted");
		} catch(Exception ex) {
			System.out.println("Not Accepted");
		}

	}
}

class CustomErrorStrategy extends DefaultErrorStrategy{
	@Override
	public void reportError(Parser recognizer, RecognitionException e){
		throw e;
	}
}
