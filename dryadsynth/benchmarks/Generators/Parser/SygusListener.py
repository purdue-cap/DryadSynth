# Generated from Sygus.g4 by ANTLR 4.7.1
from antlr4 import *
if __name__ is not None and "." in __name__:
    from .SygusParser import SygusParser
else:
    from SygusParser import SygusParser

# This class defines a complete listener for a parse tree produced by SygusParser.
class SygusListener(ParseTreeListener):

    # Enter a parse tree produced by SygusParser#start.
    def enterStart(self, ctx:SygusParser.StartContext):
        pass

    # Exit a parse tree produced by SygusParser#start.
    def exitStart(self, ctx:SygusParser.StartContext):
        pass


    # Enter a parse tree produced by SygusParser#prog.
    def enterProg(self, ctx:SygusParser.ProgContext):
        pass

    # Exit a parse tree produced by SygusParser#prog.
    def exitProg(self, ctx:SygusParser.ProgContext):
        pass


    # Enter a parse tree produced by SygusParser#symbol.
    def enterSymbol(self, ctx:SygusParser.SymbolContext):
        pass

    # Exit a parse tree produced by SygusParser#symbol.
    def exitSymbol(self, ctx:SygusParser.SymbolContext):
        pass


    # Enter a parse tree produced by SygusParser#setLogicCmd.
    def enterSetLogicCmd(self, ctx:SygusParser.SetLogicCmdContext):
        pass

    # Exit a parse tree produced by SygusParser#setLogicCmd.
    def exitSetLogicCmd(self, ctx:SygusParser.SetLogicCmdContext):
        pass


    # Enter a parse tree produced by SygusParser#cmdPlus.
    def enterCmdPlus(self, ctx:SygusParser.CmdPlusContext):
        pass

    # Exit a parse tree produced by SygusParser#cmdPlus.
    def exitCmdPlus(self, ctx:SygusParser.CmdPlusContext):
        pass


    # Enter a parse tree produced by SygusParser#cmdPlusTail.
    def enterCmdPlusTail(self, ctx:SygusParser.CmdPlusTailContext):
        pass

    # Exit a parse tree produced by SygusParser#cmdPlusTail.
    def exitCmdPlusTail(self, ctx:SygusParser.CmdPlusTailContext):
        pass


    # Enter a parse tree produced by SygusParser#cmd.
    def enterCmd(self, ctx:SygusParser.CmdContext):
        pass

    # Exit a parse tree produced by SygusParser#cmd.
    def exitCmd(self, ctx:SygusParser.CmdContext):
        pass


    # Enter a parse tree produced by SygusParser#varDeclCmd.
    def enterVarDeclCmd(self, ctx:SygusParser.VarDeclCmdContext):
        pass

    # Exit a parse tree produced by SygusParser#varDeclCmd.
    def exitVarDeclCmd(self, ctx:SygusParser.VarDeclCmdContext):
        pass


    # Enter a parse tree produced by SygusParser#sortDefCmd.
    def enterSortDefCmd(self, ctx:SygusParser.SortDefCmdContext):
        pass

    # Exit a parse tree produced by SygusParser#sortDefCmd.
    def exitSortDefCmd(self, ctx:SygusParser.SortDefCmdContext):
        pass


    # Enter a parse tree produced by SygusParser#sortExpr.
    def enterSortExpr(self, ctx:SygusParser.SortExprContext):
        pass

    # Exit a parse tree produced by SygusParser#sortExpr.
    def exitSortExpr(self, ctx:SygusParser.SortExprContext):
        pass


    # Enter a parse tree produced by SygusParser#intConst.
    def enterIntConst(self, ctx:SygusParser.IntConstContext):
        pass

    # Exit a parse tree produced by SygusParser#intConst.
    def exitIntConst(self, ctx:SygusParser.IntConstContext):
        pass


    # Enter a parse tree produced by SygusParser#boolConst.
    def enterBoolConst(self, ctx:SygusParser.BoolConstContext):
        pass

    # Exit a parse tree produced by SygusParser#boolConst.
    def exitBoolConst(self, ctx:SygusParser.BoolConstContext):
        pass


    # Enter a parse tree produced by SygusParser#bVConst.
    def enterBVConst(self, ctx:SygusParser.BVConstContext):
        pass

    # Exit a parse tree produced by SygusParser#bVConst.
    def exitBVConst(self, ctx:SygusParser.BVConstContext):
        pass


    # Enter a parse tree produced by SygusParser#enumConst.
    def enterEnumConst(self, ctx:SygusParser.EnumConstContext):
        pass

    # Exit a parse tree produced by SygusParser#enumConst.
    def exitEnumConst(self, ctx:SygusParser.EnumConstContext):
        pass


    # Enter a parse tree produced by SygusParser#realConst.
    def enterRealConst(self, ctx:SygusParser.RealConstContext):
        pass

    # Exit a parse tree produced by SygusParser#realConst.
    def exitRealConst(self, ctx:SygusParser.RealConstContext):
        pass


    # Enter a parse tree produced by SygusParser#eCList.
    def enterECList(self, ctx:SygusParser.ECListContext):
        pass

    # Exit a parse tree produced by SygusParser#eCList.
    def exitECList(self, ctx:SygusParser.ECListContext):
        pass


    # Enter a parse tree produced by SygusParser#symbolPlus.
    def enterSymbolPlus(self, ctx:SygusParser.SymbolPlusContext):
        pass

    # Exit a parse tree produced by SygusParser#symbolPlus.
    def exitSymbolPlus(self, ctx:SygusParser.SymbolPlusContext):
        pass


    # Enter a parse tree produced by SygusParser#symbolPlusTail.
    def enterSymbolPlusTail(self, ctx:SygusParser.SymbolPlusTailContext):
        pass

    # Exit a parse tree produced by SygusParser#symbolPlusTail.
    def exitSymbolPlusTail(self, ctx:SygusParser.SymbolPlusTailContext):
        pass


    # Enter a parse tree produced by SygusParser#setOptsCmd.
    def enterSetOptsCmd(self, ctx:SygusParser.SetOptsCmdContext):
        pass

    # Exit a parse tree produced by SygusParser#setOptsCmd.
    def exitSetOptsCmd(self, ctx:SygusParser.SetOptsCmdContext):
        pass


    # Enter a parse tree produced by SygusParser#optList.
    def enterOptList(self, ctx:SygusParser.OptListContext):
        pass

    # Exit a parse tree produced by SygusParser#optList.
    def exitOptList(self, ctx:SygusParser.OptListContext):
        pass


    # Enter a parse tree produced by SygusParser#symbolPairPlus.
    def enterSymbolPairPlus(self, ctx:SygusParser.SymbolPairPlusContext):
        pass

    # Exit a parse tree produced by SygusParser#symbolPairPlus.
    def exitSymbolPairPlus(self, ctx:SygusParser.SymbolPairPlusContext):
        pass


    # Enter a parse tree produced by SygusParser#symbolPairPlusTail.
    def enterSymbolPairPlusTail(self, ctx:SygusParser.SymbolPairPlusTailContext):
        pass

    # Exit a parse tree produced by SygusParser#symbolPairPlusTail.
    def exitSymbolPairPlusTail(self, ctx:SygusParser.SymbolPairPlusTailContext):
        pass


    # Enter a parse tree produced by SygusParser#symbolPair.
    def enterSymbolPair(self, ctx:SygusParser.SymbolPairContext):
        pass

    # Exit a parse tree produced by SygusParser#symbolPair.
    def exitSymbolPair(self, ctx:SygusParser.SymbolPairContext):
        pass


    # Enter a parse tree produced by SygusParser#funDefCmd.
    def enterFunDefCmd(self, ctx:SygusParser.FunDefCmdContext):
        pass

    # Exit a parse tree produced by SygusParser#funDefCmd.
    def exitFunDefCmd(self, ctx:SygusParser.FunDefCmdContext):
        pass


    # Enter a parse tree produced by SygusParser#funDeclCmd.
    def enterFunDeclCmd(self, ctx:SygusParser.FunDeclCmdContext):
        pass

    # Exit a parse tree produced by SygusParser#funDeclCmd.
    def exitFunDeclCmd(self, ctx:SygusParser.FunDeclCmdContext):
        pass


    # Enter a parse tree produced by SygusParser#sortStar.
    def enterSortStar(self, ctx:SygusParser.SortStarContext):
        pass

    # Exit a parse tree produced by SygusParser#sortStar.
    def exitSortStar(self, ctx:SygusParser.SortStarContext):
        pass


    # Enter a parse tree produced by SygusParser#argList.
    def enterArgList(self, ctx:SygusParser.ArgListContext):
        pass

    # Exit a parse tree produced by SygusParser#argList.
    def exitArgList(self, ctx:SygusParser.ArgListContext):
        pass


    # Enter a parse tree produced by SygusParser#symbolSortPairStar.
    def enterSymbolSortPairStar(self, ctx:SygusParser.SymbolSortPairStarContext):
        pass

    # Exit a parse tree produced by SygusParser#symbolSortPairStar.
    def exitSymbolSortPairStar(self, ctx:SygusParser.SymbolSortPairStarContext):
        pass


    # Enter a parse tree produced by SygusParser#symbolSortPair.
    def enterSymbolSortPair(self, ctx:SygusParser.SymbolSortPairContext):
        pass

    # Exit a parse tree produced by SygusParser#symbolSortPair.
    def exitSymbolSortPair(self, ctx:SygusParser.SymbolSortPairContext):
        pass


    # Enter a parse tree produced by SygusParser#term.
    def enterTerm(self, ctx:SygusParser.TermContext):
        pass

    # Exit a parse tree produced by SygusParser#term.
    def exitTerm(self, ctx:SygusParser.TermContext):
        pass


    # Enter a parse tree produced by SygusParser#letTerm.
    def enterLetTerm(self, ctx:SygusParser.LetTermContext):
        pass

    # Exit a parse tree produced by SygusParser#letTerm.
    def exitLetTerm(self, ctx:SygusParser.LetTermContext):
        pass


    # Enter a parse tree produced by SygusParser#letBindingTermPlus.
    def enterLetBindingTermPlus(self, ctx:SygusParser.LetBindingTermPlusContext):
        pass

    # Exit a parse tree produced by SygusParser#letBindingTermPlus.
    def exitLetBindingTermPlus(self, ctx:SygusParser.LetBindingTermPlusContext):
        pass


    # Enter a parse tree produced by SygusParser#letBindingTermPlusTail.
    def enterLetBindingTermPlusTail(self, ctx:SygusParser.LetBindingTermPlusTailContext):
        pass

    # Exit a parse tree produced by SygusParser#letBindingTermPlusTail.
    def exitLetBindingTermPlusTail(self, ctx:SygusParser.LetBindingTermPlusTailContext):
        pass


    # Enter a parse tree produced by SygusParser#letBindingTerm.
    def enterLetBindingTerm(self, ctx:SygusParser.LetBindingTermContext):
        pass

    # Exit a parse tree produced by SygusParser#letBindingTerm.
    def exitLetBindingTerm(self, ctx:SygusParser.LetBindingTermContext):
        pass


    # Enter a parse tree produced by SygusParser#termStar.
    def enterTermStar(self, ctx:SygusParser.TermStarContext):
        pass

    # Exit a parse tree produced by SygusParser#termStar.
    def exitTermStar(self, ctx:SygusParser.TermStarContext):
        pass


    # Enter a parse tree produced by SygusParser#literal.
    def enterLiteral(self, ctx:SygusParser.LiteralContext):
        pass

    # Exit a parse tree produced by SygusParser#literal.
    def exitLiteral(self, ctx:SygusParser.LiteralContext):
        pass


    # Enter a parse tree produced by SygusParser#nTDefPlus.
    def enterNTDefPlus(self, ctx:SygusParser.NTDefPlusContext):
        pass

    # Exit a parse tree produced by SygusParser#nTDefPlus.
    def exitNTDefPlus(self, ctx:SygusParser.NTDefPlusContext):
        pass


    # Enter a parse tree produced by SygusParser#nTDefPlusTail.
    def enterNTDefPlusTail(self, ctx:SygusParser.NTDefPlusTailContext):
        pass

    # Exit a parse tree produced by SygusParser#nTDefPlusTail.
    def exitNTDefPlusTail(self, ctx:SygusParser.NTDefPlusTailContext):
        pass


    # Enter a parse tree produced by SygusParser#nTDef.
    def enterNTDef(self, ctx:SygusParser.NTDefContext):
        pass

    # Exit a parse tree produced by SygusParser#nTDef.
    def exitNTDef(self, ctx:SygusParser.NTDefContext):
        pass


    # Enter a parse tree produced by SygusParser#gTermPlus.
    def enterGTermPlus(self, ctx:SygusParser.GTermPlusContext):
        pass

    # Exit a parse tree produced by SygusParser#gTermPlus.
    def exitGTermPlus(self, ctx:SygusParser.GTermPlusContext):
        pass


    # Enter a parse tree produced by SygusParser#gTermPlusTail.
    def enterGTermPlusTail(self, ctx:SygusParser.GTermPlusTailContext):
        pass

    # Exit a parse tree produced by SygusParser#gTermPlusTail.
    def exitGTermPlusTail(self, ctx:SygusParser.GTermPlusTailContext):
        pass


    # Enter a parse tree produced by SygusParser#checkSynthCmd.
    def enterCheckSynthCmd(self, ctx:SygusParser.CheckSynthCmdContext):
        pass

    # Exit a parse tree produced by SygusParser#checkSynthCmd.
    def exitCheckSynthCmd(self, ctx:SygusParser.CheckSynthCmdContext):
        pass


    # Enter a parse tree produced by SygusParser#constraintCmd.
    def enterConstraintCmd(self, ctx:SygusParser.ConstraintCmdContext):
        pass

    # Exit a parse tree produced by SygusParser#constraintCmd.
    def exitConstraintCmd(self, ctx:SygusParser.ConstraintCmdContext):
        pass


    # Enter a parse tree produced by SygusParser#synthFunCmd.
    def enterSynthFunCmd(self, ctx:SygusParser.SynthFunCmdContext):
        pass

    # Exit a parse tree produced by SygusParser#synthFunCmd.
    def exitSynthFunCmd(self, ctx:SygusParser.SynthFunCmdContext):
        pass


    # Enter a parse tree produced by SygusParser#gTerm.
    def enterGTerm(self, ctx:SygusParser.GTermContext):
        pass

    # Exit a parse tree produced by SygusParser#gTerm.
    def exitGTerm(self, ctx:SygusParser.GTermContext):
        pass


    # Enter a parse tree produced by SygusParser#letGTerm.
    def enterLetGTerm(self, ctx:SygusParser.LetGTermContext):
        pass

    # Exit a parse tree produced by SygusParser#letGTerm.
    def exitLetGTerm(self, ctx:SygusParser.LetGTermContext):
        pass


    # Enter a parse tree produced by SygusParser#letBindingGTermPlus.
    def enterLetBindingGTermPlus(self, ctx:SygusParser.LetBindingGTermPlusContext):
        pass

    # Exit a parse tree produced by SygusParser#letBindingGTermPlus.
    def exitLetBindingGTermPlus(self, ctx:SygusParser.LetBindingGTermPlusContext):
        pass


    # Enter a parse tree produced by SygusParser#letBindingGTermPlusTail.
    def enterLetBindingGTermPlusTail(self, ctx:SygusParser.LetBindingGTermPlusTailContext):
        pass

    # Exit a parse tree produced by SygusParser#letBindingGTermPlusTail.
    def exitLetBindingGTermPlusTail(self, ctx:SygusParser.LetBindingGTermPlusTailContext):
        pass


    # Enter a parse tree produced by SygusParser#letBindingGTerm.
    def enterLetBindingGTerm(self, ctx:SygusParser.LetBindingGTermContext):
        pass

    # Exit a parse tree produced by SygusParser#letBindingGTerm.
    def exitLetBindingGTerm(self, ctx:SygusParser.LetBindingGTermContext):
        pass


    # Enter a parse tree produced by SygusParser#gTermStar.
    def enterGTermStar(self, ctx:SygusParser.GTermStarContext):
        pass

    # Exit a parse tree produced by SygusParser#gTermStar.
    def exitGTermStar(self, ctx:SygusParser.GTermStarContext):
        pass


    # Enter a parse tree produced by SygusParser#synthInvCmd.
    def enterSynthInvCmd(self, ctx:SygusParser.SynthInvCmdContext):
        pass

    # Exit a parse tree produced by SygusParser#synthInvCmd.
    def exitSynthInvCmd(self, ctx:SygusParser.SynthInvCmdContext):
        pass


    # Enter a parse tree produced by SygusParser#declarePrimedVar.
    def enterDeclarePrimedVar(self, ctx:SygusParser.DeclarePrimedVarContext):
        pass

    # Exit a parse tree produced by SygusParser#declarePrimedVar.
    def exitDeclarePrimedVar(self, ctx:SygusParser.DeclarePrimedVarContext):
        pass


    # Enter a parse tree produced by SygusParser#invConstraintCmd.
    def enterInvConstraintCmd(self, ctx:SygusParser.InvConstraintCmdContext):
        pass

    # Exit a parse tree produced by SygusParser#invConstraintCmd.
    def exitInvConstraintCmd(self, ctx:SygusParser.InvConstraintCmdContext):
        pass


