
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":00" "9:00" "0:00-" "20:00-39:00" "0:00-19:00" arg0
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
(constraint (= (f "25:00") "20:00-39:00"))
(constraint (= (f "44:00") "40:00-59:00"))
(constraint (= (f "00:00") "0:00-19:00"))
(constraint (= (f "34:00") "20:00-39:00"))
(constraint (= (f "15:00") "0:00-19:00"))
(constraint (= (f "48:00") "40:00-59:00"))
(constraint (= (f "24:00") "20:00-39:00"))
(constraint (= (f "29:00") "20:00-39:00"))
(constraint (= (f "25:00") "20:00-39:00"))
(constraint (= (f "14:00") "0:00-19:00"))
(check-synth)
