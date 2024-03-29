(set-logic LIA)
(synth-inv inv-f ((x Int) (z Int)) )


(define-fun pre-f ((x Int) (z Int)) Bool (and (> x (- 0 100)) (< x 200) (> z 100) (< z 200)))
(define-fun trans-f ((x Int) (z Int) (x! Int) (z! Int)) Bool (and (< x 100) (> z 100) (or (and (= x! (+ x 1)) (= z! z)) (and (= x! (- x 1)) (= z! (- z 1))))))
(define-fun post-f ((x Int) (z Int)) Bool (or (and (< x 100) (> z 100)) (or (>= x 100) (<= z 100))))
(inv-constraint inv-f pre-f trans-f post-f )
(check-synth)
