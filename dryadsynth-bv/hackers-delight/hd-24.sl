; Round up to the next higher power of 2.

(set-logic BV)

(define-fun hd24 ((x (BitVec 32))) (BitVec 32)
(bvadd (bvor (bvor (bvor (bvor (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001))
(bvlshr (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001)) #x00000002))
(bvlshr (bvor (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001))
(bvlshr (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001)) #x00000002)) #x00000004))
(bvlshr (bvor (bvor (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001))
(bvlshr (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001)) #x00000002))
(bvlshr (bvor (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001))
(bvlshr (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001)) #x00000002)) #x00000004)) #x00000008))
(bvlshr (bvor (bvor (bvor (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001))
(bvlshr (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001)) #x00000002))
(bvlshr (bvor (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001))
(bvlshr (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001)) #x00000002)) #x00000004))
(bvlshr (bvor (bvor (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001))
(bvlshr (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001)) #x00000002))
(bvlshr (bvor (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001))
(bvlshr (bvor (bvsub x #x00000001) (bvlshr (bvsub x #x00000001) #x00000001)) #x00000002)) #x00000004))
#x00000008)) #x00000010)) #x00000001))

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
                         #x00000004
                         #x00000008
                         #x00000010
                         #xffffffff
                         (ite StartBool Start Start)))

                         (StartBool Bool
                         ((= Start Start)
                         ))))

(declare-var x (BitVec 32))
(constraint (= (hd24 x) (f x)))
(check-synth)

