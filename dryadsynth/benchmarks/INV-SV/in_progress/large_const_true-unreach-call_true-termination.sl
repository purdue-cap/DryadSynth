(set-logic LIA)

(synth-inv InvF ((i Int)))

(declare-primed-var i Int)

(define-fun PreF ((i Int)) Bool
   (and () ()))

(define-fun TransF ((i Int) (i! Int)) Bool
   (and () ()))

(define-fun PostF ((i Int)) Bool
   (or () ()))

(inv-constraint InvF PreF TransF PostF)

(check-synth)

