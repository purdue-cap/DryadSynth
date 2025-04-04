
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." arg0
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
(constraint (= (f "100.34") "100.3400"))
(constraint (= (f "0.000347") "0.0003"))
(constraint (= (f "1.25238") "1.2524"))
(constraint (= (f "5") "5.0000"))
(constraint (= (f "200.083913") "200.0839"))
(constraint (= (f "8.238") "8.2380"))
(constraint (= (f "4.18") "4.1800"))
(constraint (= (f "19.252") "19.2520"))
(check-synth)
