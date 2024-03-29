(set-logic LIA)
(synth-inv inv-f ((i Int) (j Int)) )


(define-fun pre-f ((i Int) (j Int)) Bool (and (= i 1) (= j 10)))
(define-fun trans-f ((i Int) (j Int) (i! Int) (j! Int)) Bool (and (>= j i) (= i! (+ i 2)) (= j! (+ j (- 0 1)))))
(define-fun post-f ((i Int) (j Int)) Bool (or (>= j i) (= j 6)))
(inv-constraint inv-f pre-f trans-f post-f )
(check-synth)
