; Compute parity.

(set-logic BV)

(define-fun hd22 ((x (BitVec 32))) (BitVec 32)
(bvand (bvlshr (bvmul (bvand (bvxor (bvxor (bvlshr x #x00000001) x) (bvlshr (bvxor (bvlshr x #x00000001) x)
#x00000002)) #x11111111) #x11111111) #x0000001c) #x00000001))

(synth-fun f ((x (BitVec 32))) (BitVec 32)
    ((Start (BitVec 32) ((bvnot Start)
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
						 #x00000000
                         #x00000001
                         #x00000002
                         #x0000001c
                         #x11111111
                         (ite StartBool Start Start)))

                         (StartBool Bool
                         ((= Start Start)
                         ))))

(declare-var x (BitVec 32))
(constraint (= (hd22 x) (f x)))
(check-synth)

