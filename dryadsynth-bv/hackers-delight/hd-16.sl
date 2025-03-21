(set-logic BV)

(define-fun hd16 ((x (BitVec 64)) (y (BitVec 64))) (BitVec 64) (ite (bvslt x y) y x))

(synth-fun f ((x (BitVec 64)) (y (BitVec 64))) (BitVec 64)
    ((Start (BitVec 64) (x y
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
                    (ite StartBool Start Start)))

                (StartBool Bool
                ((= Start Start)
                (bvslt Start Start)
                (bvule Start Start)
                (bvult Start Start)
                (bvsle Start Start)
                (bvugt Start Start)
                ))))

(declare-var x (BitVec 64))
(declare-var y (BitVec 64))
(constraint (= (hd16 x y) (f x y)))
(check-synth)

