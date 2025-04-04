
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." "9." "-" "-1" arg0
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
(constraint (= (f "19039.522") "19039.50"))
(constraint (= (f "3489.67") "3489.70"))
(constraint (= (f "3533.08768") "3533.10"))
(constraint (= (f "-3469.33") "-3469.30"))
(constraint (= (f "-13577.73") "-13577.70"))
(constraint (= (f "-1961.1180") "-1961.10"))
(constraint (= (f "-1829.287") "-1829.30"))
(constraint (= (f "-28177.98") "-28178.00"))
(constraint (= (f "29720.82") "29720.80"))
(constraint (= (f "-11124.492") "-11124.50"))
(check-synth)
