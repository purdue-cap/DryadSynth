(set-logic LIA)

(synth-inv InvF ((i Int) (pvlen Int) (tmp Int) (k Int) (n Int) (j Int)))

(declare-primed-var i Int)
(declare-primed-var pvlen Int)
(declare-primed-var tmp Int)
(declare-primed-var k Int)
(declare-primed-var n Int)
(declare-primed-var j Int)

(define-fun PreF ((i Int) (pvlen Int) (tmp Int) (k Int) (n Int) (j Int)) Bool
   (and (= k 0) 
	(<= pvlen 1000000) 
	(= i 0)
	(= j 0)))

(define-fun TransF ((i Int) (pvlen Int) (tmp Int) (k Int) (n Int) (j Int)
			(i! Int) (pvlen! Int) (tmp! Int) (k! Int) (n! Int) (j! Int)) Bool
   (and () ()))

(define-fun PostF ((i Int) (pvlen Int) (tmp Int) (k Int) (n Int) (j Int)) Bool
   (or () ()))

(inv-constraint InvF PreF TransF PostF)

(check-synth)

