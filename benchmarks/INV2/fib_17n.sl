(set-logic LIA)

(synth-inv inv-f ((k Int) (i Int) (j Int) (n Int) (turn Int)))

(declare-primed-var k Int)
(declare-primed-var i Int)
(declare-primed-var j Int)
(declare-primed-var n Int)
(declare-primed-var turn Int)

(define-fun pre-f ((k Int) (i Int) (j Int) (n Int) (turn Int)) Bool
(and (= k 1) (= i 1) (= j 0) (= turn 0)))

(define-fun trans-f ((k Int) (i Int) (j Int) (n Int) (turn Int) (k! Int) (i! Int) (j! Int) (n! Int) (turn! Int)) Bool
(or (and (= turn 0) (< i n) (= k! k) (= i! i) (= j! 0) (= n! n) (= turn! 1))
(and (= turn 0) (>= i n) (= k! k) (= i! i) (= j! j) (= n! n) (= turn! 3))
(and (= turn 1) (< j i) (= k! (- (+ k i) j)) (= i! i) (= j! (+ j 1)) (= n! n) (= turn! turn))
(and (= turn 1) (>= j i) (= k! k) (= i! i) (= j! j) (= n! n) (= turn! 2))
(and (= turn 2) (= k! k) (= i! (+ i 1)) (= j! j) (= n! n) (= turn! 0))
(and (>= turn 3) (= k! k) (= i! i) (= j! j) (= n! n) (= turn! turn))
(and (< turn 0) (= k! k) (= i! i) (= j! j) (= n! n) (= turn! turn))
))

(define-fun post-f ((k Int) (i Int) (j Int) (n Int) (turn Int)) Bool
(=> (= turn 3) (>= k n)))

(inv-constraint inv-f pre-f trans-f post-f)

(check-synth)
