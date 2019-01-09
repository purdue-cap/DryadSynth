(set-logic LIA)

(synth-fun f ((a Int) (b Int) (c Int)) Bool
)

(declare-var a Int)
(declare-var b Int)
(declare-var c Int)

(constraint (f 2 2 3))
(constraint (f 2 3 2))
(constraint (f 3 2 2))
(constraint (not (f 3 2 2)))


(check-synth)

