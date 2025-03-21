(set-logic LIA)

(synth-inv InvF ((i Int) (n Int) (sum Int)))

(declare-primed-var i Int)
(declare-primed-var n Int)
(declare-primed-var sum Int)

(define-fun PreF ((i Int) (n Int) (sum Int)) Bool
   (and (= sum 0) (<= 1 n) (<= n 1000) (= i 1)))

(define-fun TransF ((i Int) (n Int) (sum Int)
			(i! Int) (n! Int) (sum! Int)) Bool
   (and (<= i n)
	(= sum! (+ sum i))
	(= i! (+ i 1))
	(= n! n)
	))

(define-fun PostF ((i Int) (n Int) (sum Int)) Bool
   (or (<= i n) 
	(= (* 2 sum) (* n (+ n 1)))
	))

(inv-constraint InvF PreF TransF PostF)

(check-synth)

