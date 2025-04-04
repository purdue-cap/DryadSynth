
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." "-" "-1" arg0
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
(constraint (= (f "19039.5") "190.395"))
(constraint (= (f "3489.7") "34.897"))
(constraint (= (f "3533.0877") "35.330877"))
(constraint (= (f "-3469") "-34.69"))
(constraint (= (f "-13578") "-135.78"))
(constraint (= (f "-1961.1180") "-19.611180"))
(constraint (= (f "-1829.29") "-18.2929"))
(constraint (= (f "-28178") "-281.78"))
(constraint (= (f "29721") "297.21"))
(constraint (= (f "-11124.5") "-111.245"))
(check-synth)
