(set-logic LIA)

(synth-inv InvF ((n Int) (i Int) (k Int) (j Int)))

(declare-primed-var n Int)
(declare-primed-var i Int)
(declare-primed-var k Int)
(declare-primed-var j Int)

(define-fun PreF ((n Int) (i Int) (k Int) (j Int)) Bool
   (and (= i 0) (= k 0) (= j 0)))

(define-fun TransF ((n Int) (i Int) (k Int) (j Int)
		(n! Int) (i! Int) (k! Int) (j! Int)) Bool
   (or (and (< i n) 
		(= i! (+ i 1))
		(= k! (+ k 1))
		(= n! n) (= j! j)) 
	(and (>= i n) (< j n)
		(= j! (+ j 1))
		(= k! (- k 1))
		(= n! n) (= i! i))
	))

(define-fun PostF ((n Int) (i Int) (k Int) (j Int)) Bool
   (or (< i n) (< j n) (> k 0)))

(inv-constraint InvF PreF TransF PostF)

(check-synth)

