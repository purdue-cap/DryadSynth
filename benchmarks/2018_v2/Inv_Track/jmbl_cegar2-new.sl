(set-logic LIA)
(synth-inv InvF ((x Int) (n Int) (m Int)) )



(define-fun PreF ((x Int) (n Int) (m Int)) Bool (and (= x 1) (= m 1)))
(define-fun TransF ((x Int) (n Int) (m Int) (x! Int) (n! Int) (m! Int)) Bool (or (and (and (and (< x n) (= x! (+ x 1))) (= n! n)) (= m! m)) (and (and (and (< x n) (= x! (+ x 1))) (= n! n)) (= m! x))))
(define-fun PostF ((x Int) (n Int) (m Int)) Bool (not (and (and (>= x n) (> n 1)) (or (<= n m) (< m 1)))))
(inv-constraint InvF PreF TransF PostF )
(check-synth)
