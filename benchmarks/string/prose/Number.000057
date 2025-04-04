
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." "pts" ".2 pts" ".3 pts" arg0
                (str.++ ntString ntString) 
                (str.replace ntString ntString ntString) 
                (str.at ntString ntInt)
                (int.to.str ntInt)
                (ite ntBool ntString ntString)
                (str.substr ntString ntInt ntInt)
            )) 
            (ntInt Int (
                1 0 -1
                (+ ntInt ntInt)
                (- ntInt ntInt)
                (str.len ntString)
                (str.to.int ntString)
                (ite ntBool ntInt ntInt)
                (str.indexof ntString ntString ntInt)
            )) 
            (ntBool Bool (
                true false
                (= ntInt ntInt)
                (str.prefixof ntString ntString)
                (str.suffixof ntString ntString)
                (str.contains ntString ntString)
            ))
        )
    )
(constraint (= (f "1.243") "1.2 pts"))
(constraint (= (f "372.32") "372.3 pts"))
(constraint (= (f "1.25238") "1.3 pts"))
(constraint (= (f "5") "5 pts"))
(constraint (= (f "200.0") "200.0 pts"))
(constraint (= (f "8.238") "8.2 pts"))
(constraint (= (f "4.18") "4.2 pts"))
(constraint (= (f "19.252") "19.3 pts"))
(check-synth)
