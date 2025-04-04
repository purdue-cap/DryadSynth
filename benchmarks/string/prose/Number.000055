
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("1." "1.23" "1.234" "1.23456" arg0
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
(constraint (= (f "1.1") "1.10"))
(constraint (= (f "1.2345678") "1.2345678"))
(constraint (= (f "1") "1.00"))
(constraint (= (f "1.234") "1.234"))
(constraint (= (f "1.23456") "1.23456"))
(constraint (= (f "1.235") "1.235"))
(constraint (= (f "4") "4.00"))
(constraint (= (f "1.32256") "1.32256"))
(check-synth)
