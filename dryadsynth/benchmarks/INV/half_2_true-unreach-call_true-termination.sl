(set-logic LIA)

(synth-inv InvF ((n Int) (i Int) (k Int) (j Int)))

(declare-primed-var n Int)
(declare-primed-var i Int)
(declare-primed-var k Int)
(declare-primed-var j Int)

(define-fun PreF ((n Int) (i Int) (k Int) (j Int)) Bool
   (and (<= n 1000000) (= k n) (= i 0) (= j 0)))

(define-fun TransF ((n Int) (i Int) (k Int) (j Int)
			(n! Int) (i! Int) (k! Int) (j! Int)) Bool
   (or (and (< i n) 
		(= k! (- k 1))
		(= i! (+ i 2))
		(= n! n)
		(= j! j)) 
       (and (> i n) 
		(< j (/ n 2))
		(= k! (- k 1))
		(= j! (+ j 1))
		(= i! i)
		(= n! n))
	))

(define-fun PostF ((n Int) (i Int) (k Int) (j Int)) Bool
   (or (< i n) (< j (/ n 2)) (> k 0)))

(inv-constraint InvF PreF TransF PostF)

(check-synth)

