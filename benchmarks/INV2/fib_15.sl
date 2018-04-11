(set-logic LIA)

(synth-inv inv-f ((n Int) (j Int) (k Int)))

(declare-primed-var n Int)
(declare-primed-var j Int)
(declare-primed-var k Int)

(define-fun pre-f ((n Int) (j Int) (k Int)) Bool
(and (= j 0) (> n 0) (> k n) ))

(define-fun trans-f ((n Int) (j Int) (k Int) (n! Int) (j! Int) (k! Int)) Bool
(or (and (< j n) (= j! (+ j 1)) (= k! (- k 1)) (= n! n) )
(and (>= j n) (= j! j) (= k! k) (= n! n)) 
))

(define-fun post-f ((n Int) (j Int) (k Int)) Bool
(=> (not (< j n)) (>= k 0)))

(inv-constraint inv-f pre-f trans-f post-f)

(check-synth)
