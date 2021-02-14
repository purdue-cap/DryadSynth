grammar Sygus;

start : (cmd)+ ;

literal : numeral
		| decimal
        | boolconst
        | hexconst
        | binconst
        | stringconst
        ;

negativeNumber : '(' '-' NUMERAL ')'
		;

numeral : '0'
		| NUMERAL
		| negativeNumber
		;

decimal : (numeral '.' ('0')* numeral)
		;

boolconst : 'true'
          | 'false'
          ;

hexconst : HEXCONST ;

binconst : BINCONST ;

stringconst : STRINGCONST
			| EMPTYSTRING
			; 



identifier : symbol 
		   | identifierextra
		   ;
identifierextra : '(' '_' symbol (index)+ ')';

index : numeral
	  | symbol
	  ;

sort : identifier
	 | sortextra
	 ;
sortextra : '(' identifier (sort)+ ')';

term : identifier
	 | literal
	 | identermplus
	 | exists
	 | forall
	 | let
	 ;

iteexpr : '(' 'ite' term term term ')';

boolexpr : boolconst
		 | andexpr
		 | orexpr
		 | notexpr
		 | eqexpr
		 | gtexpr
		 | geexpr
		 | ltexpr
		 | leexpr
		 | toexpr
		 | bvuge
		 | bvugt
		 | bvule
		 | bvult
		 | bvslt
		 | bvsge
		 | bvsgt
		 | bvsle
		 | xor

		 ;
andexpr : '(' 'and' (term)+ ')';
orexpr : '(' 'or' (term)+ ')';
notexpr : '(' 'not' term ')';
eqexpr : '(' '=' term term ')';
gtexpr : '(' '>' term term ')';
geexpr : '(' '>=' term term ')';
ltexpr : '(' '<' term term ')';
leexpr : '(' '<=' term term ')';
toexpr : '(' '=>' term term ')';
bvuge  : '(' 'bvuge' term term ')';
bvugt  : '(' 'bvugt' term term ')';
bvule  : '(' 'bvule' term term ')';
bvult  : '(' 'bvult' term term ')';
bvslt  : '(' 'bvslt' term term ')';
bvsge  : '(' 'bvsge' term term ')';
bvsgt  : '(' 'bvsgt' term term ')';
bvsle  : '(' 'bvsle' term term ')';
xor  : '(' 'xor' term term ')';

intexpr : numeral
		| addexpr
		| minusexpr
		| negexpr
		| mulexpr
		;
addexpr : '(' '+' (term)+ ')';
minusexpr : '(' '-' term term ')';
negexpr : '(' '-' term ')';
mulexpr : '(' '*' term term ')';

bitexpr : bitarith
		| bitwise
		;
bitarith : bvadd
		 | bvsub
		 | bvneg
		 | bvmul
		 | bvurem
		 | bvudiv
		 | bvsdiv
		 | bvsrem
		 | bvsmod
		 | bvshl
		 | bvlshr
		 | bvashr
		 ;
bvadd : '(' 'bvadd' term term ')';
bvsub : '(' 'bvsub' term term ')';
bvneg : '(' 'bvneg' term ')';
bvmul : '(' 'bvmul' term term ')';
bvurem : '(' 'bvurem' term term ')';
bvudiv : '(' 'bvudiv' term term ')';
bvsdiv : '(' 'bvsdiv' term term ')';
bvsrem : '(' 'bvsrem' term term ')';
bvsmod : '(' 'bvsmod' term term ')';
bvshl : '(' 'bvshl' term term ')';
bvlshr : '(' 'bvlshr' term term ')';
bvashr : '(' 'bvashr' term term ')';
bitwise : bvor
		| bvand
		| bvnot
		| bvnand
		| bvxor
		| bvnor
		| bvxnor
		;
bvor : '(' 'bvor' term term ')';
bvand : '(' 'bvand' term term ')';
bvnot : '(' 'bvnot' term ')';
bvnand : '(' 'bvnand' term term ')';
bvxor : '(' 'bvxor' term term ')';
bvnor : '(' 'bvnor' term term ')';
bvxnor : '(' 'bvxnor' term term ')';

exists : '(' 'exists' '(' (sortedvar)+ ')' term ')';
forall : '(' 'forall' '(' (sortedvar)+ ')' term ')';
let : '(' 'let' '(' (varbinding)+ ')' term ')';
identermplusextra : '(' identifier (term)+ ')';

bfterm : identifier
	 | literal
	 | idenbftermplus
	 ;

bfiteexpr : '(' 'ite' bfterm bfterm bfterm ')';

bfboolexpr : bfandexpr
		 | bforexpr
		 | bfnotexpr
		 | bfeqexpr
		 | bfgtexpr
		 | bfgeexpr
		 | bfltexpr
		 | bfleexpr
		 | bftoexpr
		 | bfbvuge
		 | bfbvugt
		 | bfbvule
		 | bfbvult
		 | bfbvslt
		 | bfbvsge
		 | bfbvsgt
		 | bfbvsle
		 | bfxor
		 ;
bfandexpr : '(' 'and' (bfterm)+ ')';
bforexpr : '(' 'or' (bfterm)+ ')';
bfnotexpr : '(' 'not' bfterm ')';
bfeqexpr : '(' '=' bfterm bfterm ')';
bfgtexpr : '(' '>' bfterm bfterm ')';
bfgeexpr : '(' '>=' bfterm bfterm ')';
bfltexpr : '(' '<' bfterm bfterm ')';
bfleexpr : '(' '<=' bfterm bfterm ')';
bftoexpr : '(' '=>' bfterm bfterm ')';
bfbvuge  : '(' 'bfbvuge' term term ')';
bfbvugt  : '(' 'bfbvugt' term term ')';
bfbvule  : '(' 'bfbvule' term term ')';
bfbvult  : '(' 'bfbvult' term term ')';
bfbvslt  : '(' 'bfbvslt' term term ')';
bfbvsge  : '(' 'bfbvsge' term term ')';
bfbvsgt  : '(' 'bfbvsgt' term term ')';
bfbvsle  : '(' 'bfbvsle' term term ')';
bfxor : '(' 'bfxor' term term ')';

bfintexpr : bfaddexpr
		| bfminusexpr
		| bfnegexpr
		| bfmulexpr
		;
bfaddexpr : '(' '+' (bfterm)+ ')';
bfminusexpr : '(' '-' bfterm bfterm ')';
bfnegexpr : '(' '-' bfterm ')';
bfmulexpr : '(' '*' bfterm bfterm ')';

bfbitexpr : bfbitarith
		| bfbitwise
		;
bfbitarith : bfbvadd
		 | bfbvsub
		 | bfbvneg
		 | bfbvmul
		 | bfbvurem
		 | bfbvudiv
		 | bfbvsdiv
		 | bfbvsrem
		 | bfbvsmod
		 | bfbvshl
		 | bfbvlshr
		 | bfbvashr
		 ;
bfbvadd : '(' 'bvadd' bfterm bfterm ')';
bfbvsub : '(' 'bvsub' bfterm bfterm ')';
bfbvneg : '(' 'bvneg' bfterm ')';
bfbvmul : '(' 'bvmul' bfterm bfterm ')';
bfbvurem : '(' 'bvurem' bfterm bfterm ')';
bfbvudiv : '(' 'bvudiv' bfterm bfterm ')';
bfbvsdiv : '(' 'bvsdiv' bfterm bfterm ')';
bfbvsrem : '(' 'bvsrem' bfterm bfterm ')';
bfbvsmod : '(' 'bvsmod' bfterm bfterm ')';
bfbvshl : '(' 'bvshl' bfterm bfterm ')';
bfbvlshr : '(' 'bvlshr' bfterm bfterm ')';
bfbvashr : '(' 'bvashr' bfterm bfterm ')';
bfbitwise : bfbvor
		| bfbvand
		| bfbvnot
		| bfbvnand
		| bfbvxor
		| bfbvnor
		| bfbvxnor
		;
bfbvor : '(' 'bvor' bfterm bfterm ')';
bfbvand : '(' 'bvand' bfterm bfterm ')';
bfbvnot : '(' 'bvnot' bfterm ')';
bfbvnand : '(' 'bvnand' bfterm bfterm ')';
bfbvxor : '(' 'bvxor' bfterm bfterm ')';
bfbvnor : '(' 'bvnor' bfterm bfterm ')';
bfbvxnor : '(' 'bvxnor' bfterm bfterm ')';
idenbftermplus : bfiteexpr
			 | bfboolexpr
			 | bfintexpr
			 | bfbitexpr
			 | idenbftermplusextra
			 ;
idenbftermplusextra : '(' identifier (bfterm)+ ')';

identermplus : iteexpr
			 | boolexpr
			 | intexpr
			 | bitexpr
			 | identermplusextra
			 ;


sortedvar : '(' symbol sort ')' ;

varbinding : '(' symbol term ')' ;

feature : 'grammars'
		| 'fwd-decls'
		| 'recursion'
		;

cmd : checksynth
	| constraint
	| declarevar
	| invconstraint
	| setfeature
	| synthfun
	| synthinv
	| smtcmd
	;

checksynth : '(' 'check-synth' ')';
constraint : '(' 'constraint' term ')';
declarevar : '(' 'declare-var' symbol sort ')';
invconstraint : '(' 'inv-constraint' symbol symbol symbol symbol ')';
setfeature : '(' 'set-feature' ':' feature boolconst ')';
synthfun : '(' 'synth-fun' symbol '(' (sortedvar)* ')' sort (grammardef)? ')';
synthinv : '(' 'synth-inv' symbol '(' (sortedvar)* ')' (grammardef)? ')';

smtcmd : declaredatatype
	   | declaredatatypes
	   | declaresort
	   | definefun
	   | definesort
	   | setinfo
	   | setlogic
	   | setoption
	   ;

declaredatatype : '(' 'declare-datatype' symbol dtdec ')';
declaredatatypes : '(' 'declare-datatypes' '(' (sortdecl)+ ')' '(' (dtdec)+ ')' ')';
declaresort : '(' 'declare-sort' symbol numeral ')';
definefun : '(' 'define-fun' symbol '(' (sortedvar)* ')' sort term  ')';
definesort : '(' 'define-sort' symbol sort ')';
setinfo : '(' 'set-info' ':' symbol literal ')';

setlogic : '(' 'set-logic' logicsymbol ')';
logicsymbol : 'LIA'
			| 'SLIA'
			| 'BV'
			;

setoption : '(' 'set-option' ':' symbol literal ')';

sortdecl : '(' symbol numeral ')' ;

dtdec : '(' dtconsdec ')' ;

dtconsdec : '(' symbol sortedvar ')' ;

grammardef : '('  (sortedvar)+ ')' '(' (groupedrulelist)+ ')' ;

groupedrulelist : '(' symbol sort '(' (gterm)+ ')' ')' ;

gterm : '(' 'Constant' sort ')'
	  | '(' 'Variable' sort ')'
	  | bfterm
	  ;

symbol : SYMBOL
       ; 
       //todo:reserved words

WS : ( ' ' | '\t' | '\f' | '\n' )+ -> skip ;

COMMENT
  : ';'(~('\n')*'\n') -> skip
  ;

NUMERAL : (('1'..'9') ('0'..'9')*) ;

HEXCONST : '#x'([0-9] | [a-f] | [A-F])+ ;

BINCONST : '#b'('0' | '1')+ ;

EMPTYSTRING : '""';
STRINGCONST : '"' ((~["])|'""')+ '"';

SYMBOL : ([a-z]|[A-Z]|'_'|'+'|'-'|'*'|'&'|'|'|'!'|'~'|'<'|'>'|'='|'/'|'%'|'?'|'.'|'$'|'^')(([a-z]|[A-Z]|'_'|'+'|'-'|'*'|'&'|'|'|'!'|'~'|'<'|'>'|'='|'/'|'%'|'?'|'.'|'$'|'^') | ([0-9]))* ;

