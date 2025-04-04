
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("0." "0.2" arg0
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
(constraint (= (f "0.41") "0.4"))
(constraint (= (f "0.28") "0.3"))
(constraint (= (f "0.28") "0.3"))
(constraint (= (f "0.22") "0.2"))
(constraint (= (f "0.14") "0.15"))
(constraint (= (f "0.23") "0.25"))
(constraint (= (f "0.23") "0.25"))
(constraint (= (f "0.02") "0"))
(constraint (= (f "0.50") "0.5"))
(constraint (= (f "0.16") "0.15"))
(check-synth)
