
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":" "-" "0AM" ":30AM-" arg0
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
(constraint (= (f "11:12:29") "10:30AM-11:50AM"))
(constraint (= (f "08:29:52") "7:30AM-8:50AM"))
(constraint (= (f "12:41:14") "12:00PM-1:20PM"))
(constraint (= (f "04:22:15") "3:00AM-4:20AM"))
(constraint (= (f "04:54:07") "4:30AM-5:50AM"))
(check-synth)
