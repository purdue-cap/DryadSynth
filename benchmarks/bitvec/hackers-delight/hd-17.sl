; Hacker's delight 17, difficulty 5
; turn off the rightmost string of contiguous ones

(set-logic BV)

(define-fun hd17 ((x (BitVec 64))) (BitVec 64) (bvand (bvadd (bvor x (bvsub x #x0000000000000001)) #x0000000000000001) x))

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
                         #x000000000000001f
                         #xffffffffffffffff
                         (ite StartBool Start Start)))

                         (StartBool Bool
                         ((= Start Start)
                         ))))

(declare-var x (BitVec 64))
(constraint (= (hd17 x) (f x)))
(check-synth)

