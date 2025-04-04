
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":" "PM" "M-" ":00" "0PM" ":30" "0PM-" ":30PM" ":00PM-" ":30PM-1" "AM" "0AM" ":00AM" ":00AM-" arg0
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
(constraint (= (f "6:25PM") "6:00PM-6:30PM"))
(constraint (= (f "9:44PM") "9:30PM-10:00PM"))
(constraint (= (f "7:00AM") "7:00AM-7:30AM"))
(constraint (= (f "11:34PM") "11:30PM-12:00AM"))
(constraint (= (f "4:15PM") "4:00PM-4:30PM"))
(constraint (= (f "11:48PM") "11:30PM-12:00AM"))
(constraint (= (f "10:24PM") "10:00PM-10:30PM"))
(constraint (= (f "4:29AM") "4:00AM-4:30AM"))
(constraint (= (f "3:25PM") "3:00PM-3:30PM"))
(constraint (= (f "8:14AM") "8:00AM-8:30AM"))
(check-synth)
