(set-logic LIA)
(synth-inv inv-f ((i Int) (b Int) (n Int)) )



(define-fun pre-f ((i Int) (b Int) (n Int)) Bool (and (= i 0) (< i n)))
(define-fun trans-f ((i Int) (b Int) (n Int) (i! Int) (b! Int) (n! Int)) Bool (and (< i n) (= n! n) (or (and (= b! 0) (= i! i)) (and (not (= b! 0)) (= i! (+ i 1))))))
(define-fun post-f ((i Int) (b Int) (n Int)) Bool (or (< i n) (and (= i n) (not (= b 0)))))
(inv-constraint inv-f pre-f trans-f post-f )
(check-synth)
