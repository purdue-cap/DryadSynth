(set-logic LIA)
(synth-inv InvF ((i Int) (j Int) (x Int) (y Int) (z1 Int) (z2 Int) (z3 Int)) )







(define-fun PreF ((i Int) (j Int) (x Int) (y Int) (z1 Int) (z2 Int) (z3 Int)) Bool (and (= i x) (= j y)))
(define-fun TransF ((i Int) (j Int) (x Int) (y Int) (z1 Int) (z2 Int) (z3 Int) (i! Int) (j! Int) (x! Int) (y! Int) (z1! Int) (z2! Int) (z3! Int)) Bool (and (= i! i) (and (= j! j) (and (not (= x 0)) (and (= x! (- x 1)) (= y! (- y 1)))))))
(define-fun PostF ((i Int) (j Int) (x Int) (y Int) (z1 Int) (z2 Int) (z3 Int)) Bool (or (not (= x 0)) (or (not (= i j)) (= y 0))))
(inv-constraint InvF PreF TransF PostF )
(check-synth)
