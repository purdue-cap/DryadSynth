; Test if (nlz x) < (nlz y) where nlz is the number of leading zeros

(set-logic BV)

(define-fun hd12 ((x (BitVec 64)) (y (BitVec 64))) Bool (bvule (bvand y (bvnot x)) x))

(synth-fun f ((x (BitVec 64)) (y (BitVec 64))) Bool

((Start Bool ((bvule StartBV StartBV)
(bvult StartBV StartBV)
(bvslt StartBV StartBV)
(bvsle StartBV StartBV)
(bvugt StartBV StartBV)
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
                    y
                    (ite Start StartBV StartBV)))))

(declare-var x (BitVec 64))
(declare-var y (BitVec 64))
(constraint (= (hd12 x y) (f x y)))
(check-synth)

