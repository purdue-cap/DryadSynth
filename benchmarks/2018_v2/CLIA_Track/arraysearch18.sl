(set-logic LIA)
(synth-fun findIdx ((x0 Int) (x1 Int) (x2 Int) (x3 Int) (x4 Int) (x5 Int) (x6 Int) (x7 Int) (x8 Int) (x9 Int) (x10 Int) (x11 Int) (x12 Int) (x13 Int) (x14 Int) (x15 Int) (x16 Int) (x17 Int) (k Int)) Int)
(declare-var x0 Int)
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
(declare-var x14 Int)
(declare-var x15 Int)
(declare-var x16 Int)
(declare-var x17 Int)
(declare-var k Int)
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (< k x0)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 0)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x0) (< k x1)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 1)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x1) (< k x2)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 2)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x2) (< k x3)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 3)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x3) (< k x4)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 4)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x4) (< k x5)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 5)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x5) (< k x6)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 6)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x6) (< k x7)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 7)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x7) (< k x8)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 8)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x8) (< k x9)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 9)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x9) (< k x10)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 10)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x10) (< k x11)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 11)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x11) (< k x12)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 12)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x12) (< k x13)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 13)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x13) (< k x14)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 14)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x14) (< k x15)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 15)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x15) (< k x16)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 16)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x16) (< k x17)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 17)))
(constraint (=> (and (< x0 x1) (< x1 x2) (< x2 x3) (< x3 x4) (< x4 x5) (< x5 x6) (< x6 x7) (< x7 x8) (< x8 x9) (< x9 x10) (< x10 x11) (< x11 x12) (< x12 x13) (< x13 x14) (< x14 x15) (< x15 x16) (< x16 x17) (> k x17)) (= (findIdx x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 x10 x11 x12 x13 x14 x15 x16 x17 k) 18)))
(check-synth)
