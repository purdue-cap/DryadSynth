
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
(constraint (= (f "029.3") "29.3"))
(constraint (= (f "030.4") "30.4"))
(constraint (= (f "028.2") "28.2"))
(constraint (= (f "031.0") "31.0"))
(constraint (= (f "13.24") "13.24"))
(constraint (= (f "200.0") "200.0"))
(constraint (= (f "4.62") "4.62"))
(constraint (= (f "1.32256") "1.32256"))
(check-synth)
