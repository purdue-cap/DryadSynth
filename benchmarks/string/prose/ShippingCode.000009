
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ( arg0
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
(constraint (= (f "1Z 39V 80D 24 0712 870 8") "39V"))
(constraint (= (f "1Z AI7 S7L 39 2136 908 9") "AI7"))
(constraint (= (f "1Z GU2 T24 89 4205 353 5") "GU2"))
(constraint (= (f "1Z VDB 80S 06 9545 042 0") "VDB"))
(constraint (= (f "1Z 1KN 3G8 94 1261 323 9") "1KN"))
(check-synth)
