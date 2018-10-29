(set-logic LIA)

(synth-inv InvF ((last Int) (a Int) (b Int) (c Int) (st Int)))

(declare-primed-var last Int)
(declare-primed-var a Int)
(declare-primed-var b Int)
(declare-primed-var c Int)
(declare-primed-var st Int)

(define-fun PreF ((last Int) (a Int) (b Int) (c Int) (st Int)) Bool
   (and (= a 0) (= b 0) (= c 0) (= st 0)))

(define-fun TransF ((last Int) (a Int) (b Int) (c Int) (st Int)
		(last! Int) (a! Int) (b! Int) (c! Int) (st! Int)) Bool
   (and (= st! 1) ()))

(define-fun PostF ((last Int) (a Int) (b Int) (c Int) (st Int)) Bool
   (or () ()))

(inv-constraint InvF PreF TransF PostF)

(check-synth)

