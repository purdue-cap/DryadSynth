(set-logic LIA)
(synth-fun inv-f1 ((i Int) (j Int) (k Int) (n Int) (l Int) (m Int)) Bool)
(synth-fun inv-f2 ((i Int) (j Int) (k Int) (n Int) (l Int) (m Int)) Bool)
(synth-fun inv-f3 ((i Int) (j Int) (k Int) (n Int) (l Int) (m Int)) Bool)

(declare-var i Int)
(declare-var i! Int)
(declare-var j Int)
(declare-var j! Int)
(declare-var k Int)
(declare-var k! Int)
(declare-var n Int)
(declare-var n! Int)
(declare-var l Int)
(declare-var l! Int)
(declare-var m Int)
(declare-var m! Int)

(define-fun pre-f ((i Int) (j Int) (k Int) (n Int) (l Int) (m Int)) Bool
		(and (= i 0) (<= (* 3 n) (+ m l))))

(constraint (=> (pre-f i j k n l m)
		(inv-f1 i j k n l m)))

(constraint (=> (and (inv-f1 i j k n l m) (< i n) (= j! (* 2 i)))
		(inv-f2 i j k n l m)))

(constraint (=> (and (inv-f2 i j k n l m) (< j (* 3 i)) (= k! i))
		(inv-f3 i j k n l m)))

(constraint (=> (and (inv-f3 i j k n l m) (< k j) (<= (- k i) (* 2 n)) (= k! (+ k 1)))
		(inv-f3 i! j! k! n! l! m!)))

(constraint (=> (and (inv-f3 i j k n l m) (>= k j) (= j! (+ j 1)))
		(inv-f2 i! j! k! n! l! m!)))

(constraint (=> (and (inv-f2 i j k n l m) (>= j (* 3 i)) (= i! (+ i 1)))
		(inv-f1 i! j! k! n! l! m!)))

(constraint (=> (and (inv-f1 i j k n l m) (>= i n))
		true))

(check-synth)
