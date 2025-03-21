; determine if power of two

(set-logic BV)

(define-fun hd18 ((x (BitVec 64))) Bool (and (not (bvredor (bvand (bvsub x #x0000000000000001) x))) (bvredor x)))

(synth-fun f ( (x (BitVec 64)) ) Bool
((Start Bool ((bvule StartBV StartBV)
(bvult StartBV StartBV)
(bvslt StartBV StartBV)
(bvsle StartBV StartBV)
(bvugt StartBV StartBV)
(bvredor StartBV)
(not Start)
(and Start Start)
true false
(= StartBV StartBV)))

(StartBV (BitVec 64) ((bvnot StartBV)
					(bvxor StartBV StartBV)
					(bvand StartBV StartBV)
					(bvor StartBV StartBV)
					(bvneg StartBV)
					(bvadd StartBV StartBV)
					(bvmul StartBV StartBV)
					(bvudiv StartBV StartBV)
					(bvurem StartBV StartBV)
					(bvlshr StartBV StartBV)
					(bvashr StartBV StartBV)
					(bvshl StartBV StartBV)
					(bvsdiv StartBV StartBV)
					(bvsrem StartBV StartBV)
					(bvsub StartBV StartBV)
                    x
                    #x0000000000000001
                    (ite Start StartBV StartBV)))))

(declare-var x (BitVec 64))
(constraint (= (hd18 x) (f x)))
(check-synth)

