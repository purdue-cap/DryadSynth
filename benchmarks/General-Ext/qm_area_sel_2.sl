(set-logic LIA)

(define-fun qm ((a Int) (b Int)) Int
      (ite (< a 0) b a))

(synth-fun qm-foo ((x Int) (y Int) (ax Int) (ay Int) (bx Int) (by Int)) Int
    ((Start Int (x
                 y
                 ax
                 ay
                 bx
                 by
                 0
                 1
                 (- Start Start)
                 (+ Start Start)
                 (qm Start Start)))))

(declare-var x Int)
(declare-var y Int)
(declare-var ax Int)
(declare-var ay Int)
(declare-var bx Int)
(declare-var by Int)

(constraint (= (qm-foo x y ax ay bx by) 
	(ite (< (+ (ite (< (- x ax) 0) (- 0 (- x ax)) (- x ax)) 
			(ite (< (- y ay) 0) (- 0 (- y ay)) (- y ay)))
		(+ (ite (< (- x bx) 0) (- 0 (- x bx)) (- x bx)) 
			(ite (< (- y by) 0) (- 0 (- y by)) (- y by)))) 
	     1 0 )))

(check-synth)
