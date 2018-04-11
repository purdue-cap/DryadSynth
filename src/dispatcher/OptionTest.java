import java.util.*;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class OptionTest {
    public static void main(String [] args) throws Exception {
        OptionParser parser = new OptionParser();
        parser.acceptsAll(Arrays.asList("m", "maxSAT"), "Enable maxSAT");
        parser.acceptsAll(Arrays.asList("h", "?", "help"), "Print help");
        parser.acceptsAll(Arrays.asList("t", "threads"), "Numbers of parallel threads to use")
            .withRequiredArg().ofType(Integer.class).defaultsTo(Runtime.getRuntime().availableProcessors());
        parser.acceptsAll(Arrays.asList("f", "minFinite"), "Timeout of finite region search, in minutes")
            .withRequiredArg().ofType(Integer.class).defaultsTo(20);
        parser.acceptsAll(Arrays.asList("i", "minInfinite"), "Timeout of infinite region search, in minutes")
            .withRequiredArg().ofType(Integer.class).defaultsTo(5);
        parser.acceptsAll(Arrays.asList("b", "formattingBound"), "Bound of output size to used string formatting instead of parser formatting")
            .withRequiredArg().ofType(Integer.class).defaultsTo(65535);
        parser.acceptsAll(Arrays.asList("C", "CEGISOnly"), "Run synthesiszer in CEGIS mode only, disable all decidable fragments");
        parser.acceptsAll(Arrays.asList("M", "modeCheckOnly"), "Run mode check to determine fragment of the problem only, skipping all synthesis");
        parser.acceptsAll(Arrays.asList("v", "verbose"), "Enable verbose output of logs to stdout");
        parser.nonOptions("SyGuS benchmark file to process");
        OptionSet options = parser.parse(args);
        if (options.has("h")) {
            parser.printHelpOn(System.out);
            return;
        }
        System.out.println("maxSAT:" + options.has("m"));
        System.out.println("threads:" + options.valuesOf("t").toString());
        System.out.println("minFinite:" + options.valuesOf("f").toString());
        System.out.println("minInfinite:" + options.valuesOf("i").toString());
        System.out.println("formattingBound:" + options.valuesOf("b").toString());
        System.out.println("CEGISOnly:" + options.has("C"));
        System.out.println("modeCheckOnly:" + options.has("M"));
        System.out.println("verbose:" + options.has("v"));
        List<?> nargsl = options.nonOptionArguments();
        String filename = (String)nargsl.get(0);
        System.out.println("Filename:" + filename);
    }
}




