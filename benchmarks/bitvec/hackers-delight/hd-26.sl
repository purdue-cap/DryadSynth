; Round up x to a multiple of k-th power of 2

(set-logic BV)

(define-fun hd26 ((x (BitVec 64)) (k (BitVec 64))) (BitVec 64) (bvand (bvsub (bvsub x (bvshl #xffffffffffffffff k)) #xffffffffffffffff) (bvshl #xffffffffffffffff k)))

(synth-fun f ((x (BitVec 64)) (k (BitVec 64))) (BitVec 64)

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
k
(ite StartBool Start Start)))

(StartBool Bool
((= Start Start)
))))

(declare-var x (BitVec 64))
(declare-var k (BitVec 64))
(constraint (= (hd26 x k) (f x k)))
(check-synth)

