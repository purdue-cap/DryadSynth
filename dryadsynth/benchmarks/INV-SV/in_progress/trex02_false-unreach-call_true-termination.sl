(set-logic LIA)

(synth-inv InvF ((x Int)))

(declare-primed-var x Int)

(define-fun PreF ((x Int)) Bool
   ())

(define-fun TransF ((x Int) (x! Int)) Bool
   (and (> x 0) 
	(or (= x! (- x 1)) (= x! (- x 1)))))

(define-fun PostF ((x Int)) Bool
   (or (> x 0) (not (= x 0))))

(inv-constraint InvF PreF TransF PostF)

(check-synth)

