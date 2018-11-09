(set-logic LIA)
(synth-fun inv-f1 ((i Int) (j Int) (k Int) (n Int) (m Int)) Bool)
(synth-fun inv-f2 ((i Int) (j Int) (k Int) (n Int) (m Int)) Bool)

(declare-var i Int)
(declare-var i! Int)
(declare-var j Int)
(declare-var j! Int)
(declare-var k Int)
(declare-var k! Int)
(declare-var n Int)
(declare-var n! Int)
(declare-var m Int)
(declare-var m! Int)

(define-fun pre-f ((i Int) (j Int) (k Int) (n Int) (m Int)) Bool
		(and (= k 0) (<= 10 n) (<= n 10000) (<= 10 m) (<= m 10000) (= i 0)))

(constraint (=> (pre-f i j k n m)
		(inv-f1 i j k n m)))

(constraint (=> (and (inv-f1 i j k n m) (< i n) (= j! 0))
		(inv-f2 i j k n m)))

(constraint (=> (and (inv-f2 i j k n m) (< j m) (= k! (+ k 1)) (= j! (+ j 1)))
		(inv-f2 i! j! k! n! m!)))

(constraint (=> (and (inv-f2 i j k n m) (>= j m) (= i! (+ i 1)))
		(inv-f1 i! j! k! n! m!)))

(constraint (=> (and (inv-f1 i j k n m) (>= i n))
		(>= k 100)))

(check-synth)
