(set-logic LIA)

(define-fun qm ((a Int) (b Int)) Int
      (ite (< a 0) b a))

(synth-fun qm-foo ((v Int) (w Int) (x Int) (y Int) (z Int) (l Int) (m Int)) Int
    ((Start Int (v
		 w
		 x
                 y
                 z
		 l
		 m
                 0
                 1
                 (- Start Start)
                 (qm Start Start)))))

(declare-var v Int)
(declare-var w Int)
(declare-var x Int)
(declare-var y Int)
(declare-var z Int)
(declare-var l Int)
(declare-var m Int)

(constraint (= (qm-foo v w x y z l m)  
                (ite (and (<= m 0) (and (<= l 0) (and (<= v 0) (and (<= w 0) (and (<= x 0) (and (<= y 0) (<= z 0))))))) 1 0))) 

(check-synth)
