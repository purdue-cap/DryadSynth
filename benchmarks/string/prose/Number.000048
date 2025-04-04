
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." ".0" arg0
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
(constraint (= (f "71.8") "72.0"))
(constraint (= (f "71.2") "71.0"))
(constraint (= (f "82.4") "82.0"))
(constraint (= (f "103.8") "104.0"))
(constraint (= (f "96.7") "97.0"))
(constraint (= (f "74.8") "75.0"))
(constraint (= (f "99.9") "100.0"))
(constraint (= (f "78.8") "79.0"))
(check-synth)
