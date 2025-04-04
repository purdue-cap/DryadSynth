
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
(constraint (= (f "18:25:57") "6PM"))
(constraint (= (f "21:44:40") "8PM"))
(constraint (= (f "7:00:20") "6AM"))
(constraint (= (f "23:34:17") "10PM"))
(constraint (= (f "16:15:08") "4PM"))
(constraint (= (f "23:48:20") "10PM"))
(constraint (= (f "22:24:59") "10PM"))
(constraint (= (f "4:29:27") "4AM"))
(constraint (= (f "15:25:29") "2PM"))
(constraint (= (f "8:14:07") "8AM"))
(check-synth)
