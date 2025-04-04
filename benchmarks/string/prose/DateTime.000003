
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":" ":2" arg0
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
(constraint (= (f "18:25:57") "18"))
(constraint (= (f "21:44:40") "21"))
(constraint (= (f "07:00:20") "7"))
(constraint (= (f "23:34:17") "23"))
(constraint (= (f "16:15:08") "16"))
(constraint (= (f "23:48:20") "23"))
(constraint (= (f "22:24:59") "22"))
(constraint (= (f "04:29:27") "4"))
(constraint (= (f "15:25:29") "15"))
(constraint (= (f "08:14:07") "8"))
(check-synth)
