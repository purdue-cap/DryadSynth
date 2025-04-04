
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("PM" ":" "M-" ":00" ":59" ":59PM" ":00PM-" "AM" ":00AM-" "10:00PM-11:59PM" arg0
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
(constraint (= (f "6PM") "6:00PM-7:59PM"))
(constraint (= (f "9PM") "8:00PM-9:59PM"))
(constraint (= (f "7AM") "6:00AM-7:59AM"))
(constraint (= (f "11PM") "10:00PM-11:59PM"))
(constraint (= (f "4PM") "4:00PM-5:59PM"))
(constraint (= (f "11PM") "10:00PM-11:59PM"))
(constraint (= (f "10PM") "10:00PM-11:59PM"))
(constraint (= (f "4AM") "4:00AM-5:59AM"))
(constraint (= (f "3PM") "2:00PM-3:59PM"))
(constraint (= (f "8AM") "8:00AM-9:59AM"))
(check-synth)
