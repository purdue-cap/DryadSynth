# Generated from Sygus.g4 by ANTLR 4.7.1
from antlr4 import *
if __name__ is not None and "." in __name__:
    from .SygusParser import SygusParser
else:
    from SygusParser import SygusParser

# This class defines a complete generic visitor for a parse tree produced by SygusParser.

class SygusVisitor(ParseTreeVisitor):

    # Visit a parse tree produced by SygusParser#start.
    def visitStart(self, ctx:SygusParser.StartContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#prog.
    def visitProg(self, ctx:SygusParser.ProgContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#symbol.
    def visitSymbol(self, ctx:SygusParser.SymbolContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#setLogicCmd.
    def visitSetLogicCmd(self, ctx:SygusParser.SetLogicCmdContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#cmdPlus.
    def visitCmdPlus(self, ctx:SygusParser.CmdPlusContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#cmdPlusTail.
    def visitCmdPlusTail(self, ctx:SygusParser.CmdPlusTailContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#cmd.
    def visitCmd(self, ctx:SygusParser.CmdContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#varDeclCmd.
    def visitVarDeclCmd(self, ctx:SygusParser.VarDeclCmdContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#sortDefCmd.
    def visitSortDefCmd(self, ctx:SygusParser.SortDefCmdContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#sortExpr.
    def visitSortExpr(self, ctx:SygusParser.SortExprContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#intConst.
    def visitIntConst(self, ctx:SygusParser.IntConstContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#boolConst.
    def visitBoolConst(self, ctx:SygusParser.BoolConstContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#bVConst.
    def visitBVConst(self, ctx:SygusParser.BVConstContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#enumConst.
    def visitEnumConst(self, ctx:SygusParser.EnumConstContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#realConst.
    def visitRealConst(self, ctx:SygusParser.RealConstContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#eCList.
    def visitECList(self, ctx:SygusParser.ECListContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#symbolPlus.
    def visitSymbolPlus(self, ctx:SygusParser.SymbolPlusContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#symbolPlusTail.
    def visitSymbolPlusTail(self, ctx:SygusParser.SymbolPlusTailContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#setOptsCmd.
    def visitSetOptsCmd(self, ctx:SygusParser.SetOptsCmdContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#optList.
    def visitOptList(self, ctx:SygusParser.OptListContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#symbolPairPlus.
    def visitSymbolPairPlus(self, ctx:SygusParser.SymbolPairPlusContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#symbolPairPlusTail.
    def visitSymbolPairPlusTail(self, ctx:SygusParser.SymbolPairPlusTailContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#symbolPair.
    def visitSymbolPair(self, ctx:SygusParser.SymbolPairContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#funDefCmd.
    def visitFunDefCmd(self, ctx:SygusParser.FunDefCmdContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#funDeclCmd.
    def visitFunDeclCmd(self, ctx:SygusParser.FunDeclCmdContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#sortStar.
    def visitSortStar(self, ctx:SygusParser.SortStarContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#argList.
    def visitArgList(self, ctx:SygusParser.ArgListContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#symbolSortPairStar.
    def visitSymbolSortPairStar(self, ctx:SygusParser.SymbolSortPairStarContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#symbolSortPair.
    def visitSymbolSortPair(self, ctx:SygusParser.SymbolSortPairContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#term.
    def visitTerm(self, ctx:SygusParser.TermContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#letTerm.
    def visitLetTerm(self, ctx:SygusParser.LetTermContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#letBindingTermPlus.
    def visitLetBindingTermPlus(self, ctx:SygusParser.LetBindingTermPlusContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#letBindingTermPlusTail.
    def visitLetBindingTermPlusTail(self, ctx:SygusParser.LetBindingTermPlusTailContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#letBindingTerm.
    def visitLetBindingTerm(self, ctx:SygusParser.LetBindingTermContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#termStar.
    def visitTermStar(self, ctx:SygusParser.TermStarContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#literal.
    def visitLiteral(self, ctx:SygusParser.LiteralContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#nTDefPlus.
    def visitNTDefPlus(self, ctx:SygusParser.NTDefPlusContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#nTDefPlusTail.
    def visitNTDefPlusTail(self, ctx:SygusParser.NTDefPlusTailContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#nTDef.
    def visitNTDef(self, ctx:SygusParser.NTDefContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#gTermPlus.
    def visitGTermPlus(self, ctx:SygusParser.GTermPlusContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#gTermPlusTail.
    def visitGTermPlusTail(self, ctx:SygusParser.GTermPlusTailContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#checkSynthCmd.
    def visitCheckSynthCmd(self, ctx:SygusParser.CheckSynthCmdContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#constraintCmd.
    def visitConstraintCmd(self, ctx:SygusParser.ConstraintCmdContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#synthFunCmd.
    def visitSynthFunCmd(self, ctx:SygusParser.SynthFunCmdContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#gTerm.
    def visitGTerm(self, ctx:SygusParser.GTermContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#letGTerm.
    def visitLetGTerm(self, ctx:SygusParser.LetGTermContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#letBindingGTermPlus.
    def visitLetBindingGTermPlus(self, ctx:SygusParser.LetBindingGTermPlusContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#letBindingGTermPlusTail.
    def visitLetBindingGTermPlusTail(self, ctx:SygusParser.LetBindingGTermPlusTailContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#letBindingGTerm.
    def visitLetBindingGTerm(self, ctx:SygusParser.LetBindingGTermContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#gTermStar.
    def visitGTermStar(self, ctx:SygusParser.GTermStarContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#synthInvCmd.
    def visitSynthInvCmd(self, ctx:SygusParser.SynthInvCmdContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#declarePrimedVar.
    def visitDeclarePrimedVar(self, ctx:SygusParser.DeclarePrimedVarContext):
        return self.visitChildren(ctx)


    # Visit a parse tree produced by SygusParser#invConstraintCmd.
    def visitInvConstraintCmd(self, ctx:SygusParser.InvConstraintCmdContext):
        return self.visitChildren(ctx)



del SygusParser