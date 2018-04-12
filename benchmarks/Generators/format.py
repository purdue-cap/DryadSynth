from Parser.SygusLexer import SygusLexer
from Parser.SygusParser import SygusParser
from Parser.SygusVisitor import SygusVisitor
import sys, antlr4

class FormattingVisitor(SygusVisitor):
    def aggregateResult(self, aggregate, nextResult):
        if nextResult == "" or nextResult == " ":
            return aggregate
        if aggregate == "" or aggregate == " ":
            return nextResult
        if aggregate.endswith(" ") or \
                nextResult.startswith(" ") or \
                aggregate.endswith("(") or \
                nextResult.startswith(")"):
            return aggregate + nextResult
        if (len(nextResult) >= 20 and len(aggregate) >= 20) or \
                nextResult == "(check-synth)" or aggregate.endswith("(set-logic LIA)"):
            return aggregate + "\n" + nextResult
        else:
            return aggregate + " " + nextResult

    def defaultResult(self):
        return ""

    def visitTerminal(self, term):
        return term.getText()

    def visitTerm(self, ctx):
        if ctx.symbol() != None and ctx.termStar() != None and\
                ctx.termStar().termStar() != None and ctx.termStar().termStar().termStar() != None and\
                ctx.termStar().termStar().termStar().getChildCount() != 0:
            return self.processTermStar(ctx.symbol(), ctx.termStar())
        else:
            return self.visitChildren(ctx)

    def processTermStar(self, syb, ts):
        syb_t = syb.getText()
        if ts.termStar() == None:
            return ts.getText()
        elif ts.termStar().termStar() == None or ts.termStar().termStar().getChildCount == 0:
            return self.visitChildren(ts);
        else:
            return "({} {} {})".format(syb_t, self.visitTerm(ts.term()), self.processTermStar(syb, ts.termStar()))



i_file = antlr4.FileStream(sys.argv[1])
lexer = SygusLexer(i_file)
stream = antlr4.CommonTokenStream(lexer)
parser = SygusParser(stream)
tree = parser.start()
visitor = FormattingVisitor()
print(visitor.visit(tree))
