(set-logic LIA)

(synth-inv inv-f ((i Int) (j Int) (k Int) (c1 Int) (c2 Int) (n Int) (v Int)))

(declare-primed-var i Int)
(declare-primed-var j Int)
(declare-primed-var k Int)
(declare-primed-var c1 Int)
(declare-primed-var c2 Int)
(declare-primed-var n Int)
(declare-primed-var v Int)

(define-fun pre-f ((i Int) (j Int) (k Int) (c1 Int) (c2 Int) (n Int) (v Int)) Bool
(and (> n 0) (< n 10) (= k 0) (= i 0) (= c1 4000) (= c2 2000)))

(define-fun trans-f ((i Int) (j Int) (k Int) (c1 Int) (c2 Int) (n Int) (v Int) (i! Int) (j! Int) (k! Int) (c1! Int) (c2! Int) (n! Int) (v! Int)) Bool
(or 
(and (>= i n) (= i! i) (= j! j) (= k! k) (= c1! c1) (= c2! c2) (= n! n) (= v! v))
(and (< i n) (= i! (+ i 1)) (= j! j) (= k! (+ k c1)) (= c1! c1) (= c2! c2) (= n! n) (= v! 0))
(and (< i n) (= i! (+ i 1)) (= j! j) (= k! (+ k c2)) (= c1! c1) (= c2! c2) (= n! n) (= v! 1))
))

(define-fun post-f ((i Int) (j Int) (k Int) (c1 Int) (c2 Int) (n Int) (v Int)) Bool
(=> (>= i n) (> k n)))

(inv-constraint inv-f pre-f trans-f post-f)

(check-synth)
