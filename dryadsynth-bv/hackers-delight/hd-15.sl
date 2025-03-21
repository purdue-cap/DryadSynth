; floor of average of two integers

(set-logic BV)

(define-fun hd15 ((x (BitVec 64)) (y (BitVec 64))) (BitVec 64) (bvsub (bvor x y) (bvlshr (bvxor x y) #x0000000000000001)))

(synth-fun f ((x (BitVec 64)) (y (BitVec 64))) (BitVec 64)

((Start (BitVec 64)
((bvnot Start)
(bvxor Start Start)
(bvand Start Start)
(bvor Start Start)
(bvneg Start)
(bvadd Start Start)
(bvmul Start Start)
(bvudiv Start Start)
(bvurem Start Start)
(bvlshr Start Start)
(bvashr Start Start)
(bvshl Start Start)
(bvsdiv Start Start)
(bvsrem Start Start)
(bvsub Start Start)
#x0000000000000000
#x0000000000000001
#x000000000000001f
#xffffffffffffffff
x
y
(ite StartBool Start Start)))

(StartBool Bool
((= Start Start)
))))

(declare-var x (BitVec 64))
(declare-var y (BitVec 64))
(constraint (= (hd15 x y) (f x y)))
(check-synth)

