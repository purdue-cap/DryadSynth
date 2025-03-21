(set-logic LIA)

(synth-inv InvF ((i Int) (j Int)))

(declare-primed-var i Int)
(declare-primed-var j Int)

(define-fun PreF ((i Int) (j Int)) Bool
   (and (= i 0) (= j 0)))

(define-fun TransF ((i Int) (j Int)
		(i! Int) (j! Int)) Bool
   (and () ()))

(define-fun PostF ((i Int) (j Int)) Bool
   (or () ()))

(inv-constraint InvF PreF TransF PostF)

(check-synth)

