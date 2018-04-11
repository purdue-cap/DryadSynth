(set-logic LIA)

(synth-inv inv-f ((x Int) (y Int) (k Int) (j Int) (i Int) (n Int) (m Int)))

(declare-primed-var x Int)
(declare-primed-var y Int)
(declare-primed-var k Int)
(declare-primed-var j Int)
(declare-primed-var i Int)
(declare-primed-var n Int)
(declare-primed-var m Int)

(define-fun pre-f ((x Int) (y Int) (k Int) (j Int) (i Int) (n Int) (m Int)) Bool
(and (= (+ x y) k) (= m 0) (= j 0)))

(define-fun trans-f ((x Int) (y Int) (k Int) (j Int) (i Int) (n Int) (m Int) (x! Int) (y! Int) (k! Int) (j! Int) (i! Int) (n! Int) (m! Int)) Bool
(or
(and (< j n) (= j i) (= x! (+ x 1)) (= y! (- y 1)) (= k! k) (= j! (+ j 1)) (= i! i) (= n! n) (= m! m))
(and (< j n) (= j i) (= x! (+ x 1)) (= y! (- y 1)) (= k! k) (= j! (+ j 1)) (= i! i) (= n! n) (= m! j))
(and (< j n) (not (= j i)) (= x! (- x 1)) (= y! (+ y 1)) (= k! k) (= j! (+ j 1)) (= i! i) (= n! n) (= m! m))
(and (< j n) (not (= j i)) (= x! (- x 1)) (= y! (+ y 1)) (= k! k) (= j! (+ j 1)) (= i! i) (= n! n) (= m! j))
(and (>= j n) (= x! x) (= y! y) (= k! k) (= j! j) (= i! i) (= n! n) (= m! m))
))

(define-fun post-f ((x Int) (y Int) (k Int) (j Int) (i Int) (n Int) (m Int)) Bool
(=> (>= j n) (and (= (+ x y) k) (or (<= n 0) (<= 0 m)) (or (<= n 0) (<= m n)))))

(inv-constraint inv-f pre-f trans-f post-f)

(check-synth)
