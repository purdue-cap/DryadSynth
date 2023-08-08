; Exchanging 2 fields A and B of the same register x where m is mask which identifies
; field B and k is number of bits from end of A to start of B.

(set-logic BV)

(define-fun hd19 ((x (BitVec 64)) (m (BitVec 64)) (k (BitVec 64))) (BitVec 64) 
  (bvxor x (bvxor (bvshl (bvand (bvxor (bvlshr x k) x) m) k) (bvand (bvxor (bvlshr x k) x) m))))

(synth-fun f ((x (BitVec 64)) (m (BitVec 64)) (k (BitVec 64))) (BitVec 64)
		   
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
x
m
k
#x0000000000000000
#x0000000000000001
#x000000000000001f
#xffffffffffffffff
(ite StartBool Start Start)))

(StartBool Bool
((= Start Start)
))))

(declare-var x (BitVec 64))
(declare-var m (BitVec 64))
(declare-var k (BitVec 64))

(constraint (= (hd19 x m k) (f x m k)))
(check-synth)

