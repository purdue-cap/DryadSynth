
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":" "-" arg0
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
(constraint (= (f "5:27") "5:27 AM"))
(constraint (= (f "22:06") "10:06 PM"))
(constraint (= (f "9:42") "9:42 AM"))
(constraint (= (f "5:32") "5:32 AM"))
(constraint (= (f "22:02") "10:02 PM"))
(constraint (= (f "09:53") "9:53 AM"))
(constraint (= (f "1956-12-16 20:18") "8:18 PM"))
(constraint (= (f "1712-10-01 17:18") "5:18 PM"))
(check-synth)
