(set-logic LIA)
(synth-fun inv-f1 ((i Int) (j Int) (k Int)) Bool)
(synth-fun inv-f2 ((i Int) (j Int) (k Int)) Bool)
(synth-fun inv-f3 ((i Int) (j Int) (k Int)) Bool)

(declare-var i Int)
(declare-var i! Int)
(declare-var j Int)
(declare-var j! Int)
(declare-var k Int)
(declare-var k! Int)

(define-fun pre-f ((i Int) (j Int) (k Int)) Bool
		(and (= i 0) (= k 0) (= j (- 0 100))))

(constraint (=> (pre-f i j k)
		(inv-f1 i j k)))

(constraint (=> (and (inv-f1 i j k) (<= i 100) (= i! (+ i 1)))
		(inv-f2 i j k)))

(constraint (=> (and (inv-f2 i j k) (< j 20) (= j! (+ i j)))
		(inv-f2 i! j! k!)))

(constraint (=> (and (inv-f2 i j k) (>= j 20) (= k! 4))
		(inv-f3 i j k)))

(constraint (=> (and (inv-f3 i j k) (<= k 3) (= k! (+ k 1)))
		(inv-f3 i! j! k!)))

(constraint (=> (and (inv-f3 i j k) (> k 3))
		(inv-f1 i! j! k!)))

(constraint (=> (and (inv-f1 i j k) (>= i 100))
		(= k 4)))

(check-synth)
