; compute next higher unsigned number with the same number of 1 bits.

(set-logic BV)

(define-fun hd20 ((x (BitVec 64))) (BitVec 64)
(bvor (bvadd x (bvand (bvneg x) x)) (bvudiv (bvlshr (bvxor x (bvand (bvneg x) x)) #x0000000000000002) (bvand (bvneg x) x))))

(synth-fun f ((x (BitVec 64))) (BitVec 64)
    ((Start (BitVec 64) ((bvnot Start)
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
						 #x0000000000000000
                         #x0000000000000001
                         #x0000000000000002
                         #x000000000000001f
                         #xffffffffffffffff
                         (ite StartBool Start Start)))

                         (StartBool Bool
                         ((= Start Start)
                         ))))

(declare-var x (BitVec 64))
(constraint (= (hd20 x) (f x)))
(check-synth)

