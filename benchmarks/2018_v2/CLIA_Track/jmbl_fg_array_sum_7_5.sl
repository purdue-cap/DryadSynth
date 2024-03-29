(set-logic LIA)
(synth-fun fnd_sum ((y1 Int) (y2 Int) (y3 Int) (y4 Int) (y5 Int) (y6 Int) (y7 Int)) Int)
(declare-var x1 Int)
(declare-var x2 Int)
(declare-var x3 Int)
(declare-var x4 Int)
(declare-var x5 Int)
(declare-var x6 Int)
(declare-var x7 Int)
(constraint (=> (> (+ x1 x2) 5) (= (fnd_sum x1 x2 x3 x4 x5 x6 x7) (+ x1 x2))))
(constraint (=> (and (<= (+ x1 x2) 5) (> (+ x2 x3) 5)) (= (fnd_sum x1 x2 x3 x4 x5 x6 x7) (+ x2 x3))))
(constraint (=> (and (and (<= (+ x1 x2) 5) (<= (+ x2 x3) 5)) (> (+ x3 x4) 5)) (= (fnd_sum x1 x2 x3 x4 x5 x6 x7) (+ x3 x4))))
(constraint (=> (and (and (<= (+ x1 x2) 5) (and (<= (+ x2 x3) 5) (<= (+ x3 x4) 5))) (> (+ x4 x5) 5)) (= (fnd_sum x1 x2 x3 x4 x5 x6 x7) (+ x4 x5))))
(constraint (=> (and (and (<= (+ x1 x2) 5) (and (<= (+ x2 x3) 5) (and (<= (+ x3 x4) 5) (<= (+ x4 x5) 5)))) (> (+ x5 x6) 5)) (= (fnd_sum x1 x2 x3 x4 x5 x6 x7) (+ x5 x6))))
(constraint (=> (and (and (<= (+ x1 x2) 5) (and (<= (+ x2 x3) 5) (and (<= (+ x3 x4) 5) (and (<= (+ x4 x5) 5) (<= (+ x5 x6) 5))))) (> (+ x6 x7) 5)) (= (fnd_sum x1 x2 x3 x4 x5 x6 x7) (+ x6 x7))))
(constraint (=> (and (<= (+ x1 x2) 5) (and (<= (+ x2 x3) 5) (and (<= (+ x3 x4) 5) (and (<= (+ x4 x5) 5) (and (<= (+ x5 x6) 5) (<= (+ x6 x7) 5)))))) (= (fnd_sum x1 x2 x3 x4 x5 x6 x7) 0)))
(check-synth)
