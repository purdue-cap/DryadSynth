; Hacker's delight 07, difficulty 5
; Isolate the rightmost 0 bit

(set-logic BV)

(define-fun hd07 ((x (BitVec 64))) (BitVec 64) (bvand (bvnot x) (bvadd x #x0000000000000001)))

(synth-fun f ((x (BitVec 64))) (BitVec 64)
    ((Start (BitVec 64) ((bvnot Start)
						 (bvand Start Start)
						 (bvxor Start Start)
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
						 #xffffffffffffffff
                         x
                         (ite StartBool Start Start)))

                         (StartBool Bool
                         ((= Start Start)
                         ))))

(declare-var x (BitVec 64))
(constraint (= (hd07 x) (f x)))
(check-synth)

