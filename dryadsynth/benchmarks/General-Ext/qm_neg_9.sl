(set-logic LIA)

(define-fun qm ((a Int) (b Int)) Int
      (ite (< a 0) b a))

(synth-fun qm-foo ((v Int) (w Int) (x Int) (y Int) (z Int) (u Int) (m Int) (n Int) (l Int)) Int
    ((Start Int (v
		 w
		 x
                 y
                 z
                 u
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
(declare-var n Int)
(declare-var u Int)

(constraint (= (qm-foo v w x y z u m n l)  
                (ite (and (< l 0) (and (< n 0) (and (< m 0) (and (< u 0) (and (< v 0) (and (< w 0) (and (< x 0) (and (< y 0) (< z 0))))))))) 1 0))) 

(check-synth)
