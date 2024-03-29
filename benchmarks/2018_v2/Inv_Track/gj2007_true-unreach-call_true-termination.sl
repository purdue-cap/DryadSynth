(set-logic LIA)
(synth-inv inv-f ((x Int) (y Int)) )


(define-fun pre-f ((x Int) (y Int)) Bool (and (= x 0) (= y 50)))
(define-fun trans-f ((x Int) (y Int) (x! Int) (y! Int)) Bool (and (< x 100) (ite (< x 50) (= x! (+ x 1)) (= y! y)) (= x! (+ x 1)) (= y! (+ y 1))))
(define-fun post-f ((x Int) (y Int)) Bool (or (< x 100) (= y 100)))
(inv-constraint inv-f pre-f trans-f post-f )
(check-synth)
