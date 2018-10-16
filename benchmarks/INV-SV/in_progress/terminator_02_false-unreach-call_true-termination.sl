(set-logic LIA)

(synth-inv InvF ((x Int) (y Int) (z Int)))

(declare-primed-var x Int)
(declare-primed-var y Int)
(declare-primed-var z Int)

(define-fun PreF ((x Int) (y Int) (z Int)) Bool
   (and () ()))

(define-fun TransF ((x Int) (y Int) (z Int)
		(x! Int) (y! Int) (z! Int)) Bool
   (and () ()))

(define-fun PostF ((x Int) (y Int) (z Int)) Bool
   (or () ()))

(inv-constraint InvF PreF TransF PostF)

(check-synth)

