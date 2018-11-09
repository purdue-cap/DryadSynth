(set-logic LIA)
(synth-fun inv-f1 ((i Int) (k Int) (n Int) (l Int)) Bool)
(synth-fun inv-f2 ((i Int) (k Int) (n Int) (l Int)) Bool)

(declare-var i Int)
(declare-var i! Int)
(declare-var k Int)
(declare-var k! Int)
(declare-var n Int)
(declare-var n! Int)
(declare-var l Int)
(declare-var l! Int)

(define-fun pre-f ((i Int) (k Int) (n Int) (l Int)) Bool
		(and (> l 0) (< l 1000000) (< n 1000000) (= k 1)))

(constraint (=> (pre-f i k n l)
		(inv-f1 i k n l)))

(constraint (=> (and (inv-f1 i k n l) (< k n) (= i! l))
		(inv-f2 i k n l)))

(constraint (=> (and (inv-f2 i k n l) (< i n) (<= 1 i) (= i! (+ i 1)))
		(inv-f2 i! k! n! l!)))

(constraint (=> (and (inv-f2 i k n l) (>= i n) 
			(or (= l! (+ l 1)) (= l! l))
			(= k! (+ k 1)))
		(inv-f1 i! k! n! l!)))

(constraint (=> (inv-f1 i k n l)
		true))

(check-synth)
