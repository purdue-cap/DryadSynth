; Hacker's delight 02, difficulty 5
; Test if unsigned int is of form 2^n - 1

(set-logic BV)

(define-fun hd02 ((x (BitVec 64))) (BitVec 64) (bvand x (bvadd x #x0000000000000001)))

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
						 #xffffffffffffffff
                         #x0000000000000001
                         (ite StartBool Start Start)))

                         (StartBool Bool
                         ((= Start Start)
                         ))))

(declare-var x (BitVec 64))
(constraint (= (hd02 x) (f x)))
(check-synth)

