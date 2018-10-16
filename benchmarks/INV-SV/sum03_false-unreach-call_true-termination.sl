(set-logic LIA)

(synth-inv InvF ((sn Int) (loop1 Int) (n1 Int) (x Int)))

(declare-primed-var sn Int)
(declare-primed-var loop1 Int)
(declare-primed-var n1 Int)
(declare-primed-var x Int)

(define-fun PreF ((sn Int) (loop1 Int) (n1 Int) (x Int)) Bool
   (and (= sn 0) (= x 0)))

(define-fun TransF ((sn Int) (loop1 Int) (n1 Int) (x Int)
		(sn! Int) (loop1! Int) (n1! Int) (x! Int)) Bool
   (and (ite (< x 10) (= sn! (+ sn 2)) (= sn! sn))
	(= x! (+ x 1))))

(define-fun PostF ((sn Int) (loop1 Int) (n1 Int) (x Int)) Bool
   (or (= sn (* x 2)) (= sn 0)))

(inv-constraint InvF PreF TransF PostF)

(check-synth)

