(set-logic LIA)

(synth-inv InvF ((i Int) (j Int) (k Int)))

(declare-primed-var i Int)
(declare-primed-var j Int)
(declare-primed-var k Int)

(define-fun PreF ((i Int) (j Int) (k Int)) Bool
   (and (= i 0) (= k 9) (= j -100)))

(define-fun TransF ((i Int) (j Int) (k Int)
			(i! Int) (j! Int) (k! Int)) Bool
   (and (<= i 100) 
	(= i! (+ i 1))
	))

(define-fun PostF ((i Int) (j Int) (k Int)) Bool
(or (<= i 100) (= k 4)))

(inv-constraint InvF PreF TransF PostF)

(check-synth)


