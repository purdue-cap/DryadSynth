
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." "0." "-" arg0
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
(constraint (= (f "0.74800000000000000001") "748"))
(constraint (= (f "-0.661") "-661"))
(constraint (= (f "0.326") "326"))
(constraint (= (f "2") "2000"))
(constraint (= (f "0.3377") "337.7"))
(constraint (= (f "1.00") "1000"))
(constraint (= (f "0.45") "450"))
(constraint (= (f "0.5644") "564.4"))
(constraint (= (f "0.3348") "334.8"))
(constraint (= (f "5") "5000"))
(constraint (= (f "1.0071") "1007.1"))
(constraint (= (f "-0.597") "-597"))
(check-synth)
