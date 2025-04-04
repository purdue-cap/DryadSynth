
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
(constraint (= (f "6:25:57 PM") "18:25"))
(constraint (= (f "9:44:40 PM") "21:44"))
(constraint (= (f "0:00:20 AM") "0:00"))
(constraint (= (f "11:34:17 PM") "23:34"))
(constraint (= (f "4:15:08 PM") "16:15"))
(constraint (= (f "11:48:20 PM") "23:48"))
(constraint (= (f "10:24:59 PM") "22:24"))
(constraint (= (f "4:29:27 AM") "4:29"))
(check-synth)
