; compute higher order half of product of x and y.
(set-logic BV)

(define-fun hd25 ((x (BitVec 64)) (y (BitVec 64))) (BitVec 64)
    (bvadd (bvadd
		(bvlshr (bvadd (bvmul (bvand x #x00000000ffffffff) (bvlshr y #x0000000000000020))
    		(bvand (bvadd (bvmul (bvlshr x #x0000000000000020) (bvand y #x00000000ffffffff))
    		(bvlshr (bvmul (bvand x #x00000000ffffffff) (bvand y #x00000000ffffffff)) #x0000000000000020))
    		#x00000000ffffffff)) #x0000000000000020)
		(bvlshr (bvadd (bvmul (bvlshr x #x0000000000000020) (bvand y #x00000000ffffffff))
			(bvlshr (bvmul (bvand x #x00000000ffffffff) (bvand y #x00000000ffffffff)) #x0000000000000020)) #x0000000000000020))
		(bvmul (bvlshr x #x0000000000000020) (bvlshr y #x0000000000000020)))
)

(synth-fun f ((x (BitVec 64)) (y (BitVec 64))) (BitVec 64)
    ((Start (BitVec 64) (
						 (bvnot Start)
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
                         y
                         #x0000000000000001
                         #x0000000000000020
                         #x00000000ffffffff
                         (ite StartBool Start Start)))

                         (StartBool Bool
                         ((= Start Start)
                         ))))

(declare-var x (BitVec 64))
(declare-var y (BitVec 64))
(constraint (= (hd25 x y) (f x y)))
(check-synth)

