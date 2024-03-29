(set-logic LIA)
(synth-fun mux_13 ((x1 Int) (x2 Int) (x3 Int) (x4 Int) (x5 Int) (x6 Int) (x7 Int) (x8 Int) (x9 Int) (x10 Int) (x11 Int) (x12 Int) (x13 Int)) Int)
(declare-var x1 Int)
(declare-var x2 Int)
(declare-var x3 Int)
(declare-var x4 Int)
(declare-var x5 Int)
(declare-var x6 Int)
(declare-var x7 Int)
(declare-var x8 Int)
(declare-var x9 Int)
(declare-var x10 Int)
(declare-var x11 Int)
(declare-var x12 Int)
(declare-var x13 Int)
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x1))
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x2))
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x3))
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x4))
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x5))
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x6))
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x7))
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x8))
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x9))
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x10))
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x11))
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x12))
(constraint (>= (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13) x13))
(constraint (or (= x1 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)) (or (= x2 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)) (or (= x3 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)) (or (= x4 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)) (or (= x5 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)) (or (= x6 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)) (or (= x7 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)) (or (= x8 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)) (or (= x9 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)) (or (= x10 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)) (or (= x11 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)) (or (= x12 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)) (= x13 (mux_13 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13)))))))))))))))
(check-synth)
