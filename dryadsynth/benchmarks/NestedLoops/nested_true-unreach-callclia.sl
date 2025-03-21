(set-logic LIA)
(synth-fun inv-f1 ((a Int) (b Int) (c Int) (st Int) (last Int)) Bool)
(synth-fun inv-f2 ((a Int) (b Int) (c Int) (st Int) (last Int)) Bool)

(declare-var a Int)
(declare-var a! Int)
(declare-var b Int)
(declare-var b! Int)
(declare-var c Int)
(declare-var c! Int)
(declare-var st Int)
(declare-var st! Int)
(declare-var last Int)
(declare-var last! Int)

(define-fun pre-f ((a Int) (b Int) (c Int) (st Int) (last Int)) Bool
		(and (= a 0) (= b 0) (= c 0) (= st 0)))

(constraint (=> (pre-f a b c st last)
		(inv-f1 a b c st last)))

(constraint (=> (and (inv-f1 a b c st last) (= st! 1) (= c! 0))
		(inv-f2 a b c st last)))

(constraint (=> (and (inv-f2 a b c st last) (< c 200000) 
			(ite (= c last) (= st! 0) (= st! st)) 
			(= c! (+ c 1)))
		(inv-f2 a! b! c! st! last!)))

(constraint (=> (and (inv-f2 a b c st last) (>= c 200000)
			(ite (and (= st 0) (= c (+ last 1))) 
				(and (= a! (+ a 3)) (= b! (+ b 3))) 
				(and (= a! (+ a 2)) (= b! (+ b 2))))
			(ite (and (= c last) (= st 0)) (= a! (+ a 1)) (= a! a))
			(and (= a b) (= c 200000)))
		(inv-f1 a! b! c! st! last!)))

(constraint (=> (and (inv-f1 a b c st last) false)
		true))

(check-synth)
