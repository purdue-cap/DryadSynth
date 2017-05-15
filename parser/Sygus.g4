grammar Sygus;

start : prog
      | //epsilon
      ;

prog : setLogicCmd cmdPlus
     | cmdPlus
     ;

symbol : SYMBOL
       ;

setLogicCmd : '(' 'set-logic' symbol ')'
            ;

cmdPlus : cmd cmdPlusTail
        ;

cmdPlusTail : cmd cmdPlusTail
            | //empty
            ;

cmd : funDefCmd
    | funDeclCmd
    | synthFunCmd
    | checkSynthCmd
    | constraintCmd
    | sortDefCmd
    | setOptsCmd
    | varDeclCmd
    //for inv
    | synthInvCmd
    | declarePrimedVar
    | invConstraintCmd
    ;

varDeclCmd : '(' 'declare-var' symbol sortExpr ')'
           ;

sortDefCmd : '(' 'define-sort' symbol sortExpr ')'
           ;

sortExpr : '(' 'BitVec' intConst ')'
         | 'Int'
         | 'Bool'
         | 'Real'
         | '(' 'Enum' eCList ')'
         | '(' 'Array' sortExpr sortExpr ')'
         | symbol
         ;

intConst : INTEGER
         ;

boolConst : 'true' 
          | 'false' 
          ;

bVConst : BVCONST
        ;

enumConst : symbol '::' symbol
          ;

realConst : REALCONST
          ;

eCList : '(' symbolPlus ')'
       ;

symbolPlus : symbol symbolPlusTail
           ;

symbolPlusTail : symbol symbolPlusTail
               | //empty
               ;

setOptsCmd : '(' 'set-options' optList ')'
           ;

optList : '(' symbolPairPlus ')'
        ;

symbolPairPlus : symbolPair symbolPairPlusTail
               ;

symbolPairPlusTail : symbolPair symbolPairPlusTail
                   | //empty
                   ;

symbolPair : '(' symbol QUOTEDLIT ')'
           ;

funDefCmd : '(' 'define-fun' symbol argList sortExpr term ')'
          ;

funDeclCmd : '(' 'declare-fun' symbol '(' sortStar ')' sortExpr ')'
           ;

sortStar : sortExpr sortStar
         | //empty
         ;

argList : '(' symbolSortPairStar ')'
        ;

symbolSortPairStar : symbolSortPair symbolSortPairStar
                   | //epsilon
                   ;

symbolSortPair : '(' symbol sortExpr ')'
               ;

term : '(' symbol termStar ')'
     | literal
     | symbol
     | letTerm
     ;

letTerm : '(' 'let' '(' letBindingTermPlus ')' term ')'
        ;

letBindingTermPlus : letBindingTerm letBindingTermPlusTail
                   ;

letBindingTermPlusTail : letBindingTerm letBindingTermPlusTail
                       | //empty
                       ;

letBindingTerm : '(' symbol sortExpr term ')'
               ;

termStar : term termStar
         | //epsilon
         ;

literal : intConst
        | boolConst
        | bVConst
        | enumConst
        | realConst
        ;

nTDefPlus : nTDef nTDefPlusTail
          ;

nTDefPlusTail : nTDef nTDefPlusTail
              | //empty
              ;

nTDef : '(' symbol sortExpr '(' gTermPlus ')' ')'
      ;

gTermPlus : gTerm gTermPlusTail
          ;

gTermPlusTail : gTerm gTermPlusTail
              | //empty
              ;

checkSynthCmd : '(' 'check-synth' ')'
              ;

constraintCmd : '(' 'constraint' term ')'
              ;

synthFunCmd : '(' 'synth-fun' symbol argList sortExpr
              '(' nTDefPlus ')' ')'
            | '(' 'synth-fun' symbol argList sortExpr ')'
            ;

gTerm : symbol
      | literal
      | '(' symbol gTermStar ')'
      | '(' 'Constant' sortExpr ')'
      | '(' 'Variable' sortExpr ')'
      | '(' 'InputVariable' sortExpr ')'
      | '(' 'LocalVariable' sortExpr ')'
      | letGTerm
      ;

letGTerm : '(' 'let' '(' letBindingGTermPlus ')' gTerm ')'
         ;

letBindingGTermPlus : letBindingGTerm letBindingGTermPlusTail
          ;

letBindingGTermPlusTail : letBindingGTerm letBindingGTermPlusTail
              | //empty
              ;

letBindingGTerm : '(' symbol sortExpr gTerm ')'
                ;

gTermStar : gTerm gTermStar
          | //epsilon
          ;

synthInvCmd : '(' 'synth-inv' symbol argList
              '(' nTDefPlus ')' ')'
            | '(' 'synth-inv' symbol argList ')'
            ;

declarePrimedVar : '(' 'declare-primed-var' symbol sortExpr ')'
                 ;

invConstraintCmd : '(' 'inv-constraint' symbol symbol symbol symbol ')'
                 ;

WS : ( ' ' | '\t' | '\f' | '\n' )+ -> skip
  ; 

COMMENT
  : ';'(~('\n')*'\n') -> skip
  ;

SYMBOL : ([a-z]|[A-Z]|'_'|'+'|'-'|'*'|'&'|'|'|'!'|'~'|'<'|'>'|'='|'/'|'%'|'?'|'.'|'$'|'^')(([a-z]|[A-Z]|'_'|'+'|'-'|'*'|'&'|'|'|'!'|'~'|'<'|'>'|'='|'/'|'%'|'?'|'.'|'$'|'^') | ([0-9]))*
       ;

INTEGER : ('-'?([0-9])+)
        ;

BVCONST : '#x'([0-9] | [a-f] | [A-F])+ | '#b'('0' | '1')+
        ;

REALCONST : ('-'?([0-9])+'.'([0-9])+)
          ;

QUOTEDLIT : '"'([a-z]|[A-Z]|([0-9])|'.')+'"'
          ;
