
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":" "PM" "M-" "AM" arg0
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
(constraint (= (f "6:25PM") "5PM-7PM"))
(constraint (= (f "9:44PM") "9PM-11PM"))
(constraint (= (f "7:00AM") "7AM-9AM"))
(constraint (= (f "11:34PM") "11PM-1AM"))
(constraint (= (f "4:15PM") "3PM-5PM"))
(constraint (= (f "11:48PM") "11PM-1AM"))
(constraint (= (f "10:24PM") "9PM-11PM"))
(constraint (= (f "4:29AM") "3AM-5AM"))
(constraint (= (f "3:25PM") "3PM-5PM"))
(constraint (= (f "8:14AM") "7AM-9AM"))
(check-synth)
