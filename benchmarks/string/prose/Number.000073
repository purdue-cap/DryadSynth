
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("-" "." "0." arg0
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
(constraint (= (f "-243") "-0.243"))
(constraint (= (f "-12.5") "-0.0125"))
(constraint (= (f "-2345.23292") "-2.34523292"))
(constraint (= (f "-1202.3433") "-1.2023433"))
(constraint (= (f "1202.3433") "1.2023433"))
(constraint (= (f "-23224.1") "-23.2241"))
(constraint (= (f "23224.1") "23.2241"))
(constraint (= (f "0.0") "0.0000"))
(constraint (= (f "0.1") "0.0001"))
(constraint (= (f "-0.1") "-0.0001"))
(constraint (= (f "100") "0.100"))
(constraint (= (f "-100") "-0.100"))
(check-synth)
