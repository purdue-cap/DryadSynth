; Counting number of set bits.

(set-logic BV)

(define-fun hd23 ((x (BitVec 32))) (BitVec 32)
(bvand (bvadd (bvadd (bvand (bvadd (bvlshr (bvadd (bvand (bvsub x (bvand (bvlshr x #x00000001)
#x55555555)) #x33333333) (bvand (bvlshr (bvsub x (bvand (bvlshr x #x00000001) #x55555555))
#x00000002) #x33333333)) #x00000004) (bvadd (bvand (bvsub x (bvand (bvlshr x #x00000001)
#x55555555)) #x33333333) (bvand (bvlshr (bvsub x (bvand (bvlshr x #x00000001) #x55555555))
#x00000002) #x33333333))) #x0000000f0f0f0f0f)
(bvlshr (bvand (bvadd (bvlshr (bvadd (bvand (bvsub x (bvand (bvlshr x #x00000001) #x55555555)) #x33333333)
(bvand (bvlshr (bvsub x (bvand (bvlshr x #x00000001) #x55555555)) #x00000002) #x33333333)) #x00000004) (bvadd (bvand (bvsub x (bvand (bvlshr x #x00000001) #x55555555)) #x33333333)
(bvand (bvlshr (bvsub x (bvand (bvlshr x #x00000001) #x55555555)) #x00000002) #x33333333))) #x0000000f0f0f0f0f) #x00000008))
(bvlshr (bvadd (bvand (bvadd (bvlshr (bvadd (bvand (bvsub x (bvand (bvlshr x #x00000001) #x55555555)) #x33333333)
(bvand (bvlshr (bvsub x (bvand (bvlshr x #x00000001) #x55555555)) #x00000002) #x33333333)) #x00000004)
(bvadd (bvand (bvsub x (bvand (bvlshr x #x00000001) #x55555555)) #x33333333)
(bvand (bvlshr (bvsub x (bvand (bvlshr x #x00000001) #x55555555)) #x00000002) #x33333333))) #x0000000f0f0f0f0f)
(bvlshr (bvand (bvadd (bvlshr (bvadd (bvand (bvsub x (bvand (bvlshr x #x00000001) #x55555555)) #x33333333)
(bvand (bvlshr (bvsub x (bvand (bvlshr x #x00000001) #x55555555)) #x00000002) #x33333333)) #x00000004)
(bvadd (bvand (bvsub x (bvand (bvlshr x #x00000001) #x55555555)) #x33333333) (bvand (bvlshr (bvsub x (bvand
(bvlshr x #x00000001) #x55555555)) #x00000002) #x33333333))) #x0000000f0f0f0f0f) #x00000008))
#x00000010)) #x0000003f))

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
                         #x00000001
                         #x00000002
                         #x00000004
                         #x00000008
                         #x0000003f
                         #x55555555
                         #x0000000f0f0f0f0f
                         #x33333333
                         #x00000010
                         (ite StartBool Start Start)))
                         (StartBool Bool
                         ((= Start Start)
                         ))))

(declare-var x (BitVec 32))
(constraint (= (hd23 x) (f x)))
(check-synth)

