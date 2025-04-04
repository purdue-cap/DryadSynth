
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":" "AM" ":5" ":00" ":59" ":59:59AM" ":00:00AM-" arg0
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
(constraint (= (f "11:12:29") "10:00:00AM-11:59:59AM"))
(constraint (= (f "08:29:52") "8:00:00AM-9:59:59AM"))
(constraint (= (f "12:41:14") "12:00:00PM-1:59:59PM"))
(constraint (= (f "04:22:15") "4:00:00AM-5:59:59AM"))
(constraint (= (f "04:54:07") "4:00:00AM-5:59:59AM"))
(check-synth)
