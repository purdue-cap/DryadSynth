
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("-" "." arg0
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
(constraint (= (f "-10324.6") "-10324.60"))
(constraint (= (f "-2032.94") "-2032.90"))
(constraint (= (f "16337.651") "16337.70"))
(constraint (= (f "") ""))
(constraint (= (f "13045.76") "13045.80"))
(constraint (= (f "-16448.23111") "-16448.20"))
(constraint (= (f "-879.6818") "-879.70"))
(constraint (= (f "12373.08") "12373.10"))
(check-synth)
