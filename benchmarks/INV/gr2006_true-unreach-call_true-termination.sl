(set-logic LIA)

(synth-inv InvF ((x Int) (y Int)))

(declare-primed-var x Int)
(declare-primed-var y Int)

(define-fun PreF ((x Int) (y Int)) Bool
   (and (= x 0) (= y 0)))

(define-fun TransF ((x Int) (y Int)
			(x! Int) (y! Int)) Bool
   (and (>= y 0) 
	(ite (< x 50) (= y! (+ y 1)) (= y! (- y 1)))
	(= x! (+ x 1))
	))

(define-fun PostF ((x Int) (y Int)) Bool
   (or (>= y 0) (= x 100)))

(inv-constraint InvF PreF TransF PostF)

(check-synth)
