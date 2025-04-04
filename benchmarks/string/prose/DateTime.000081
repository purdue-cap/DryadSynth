
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":" "PM" "5PM" "M-" ":4" ":15" ":45" ":45PM" ":15PM-" "AM" arg0
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
(constraint (= (f "6:25PM") "6:15PM-6:45PM"))
(constraint (= (f "9:44PM") "9:15PM-9:45PM"))
(constraint (= (f "7:00AM") "6:45AM-7:15AM"))
(constraint (= (f "11:34PM") "11:15PM-11:45PM"))
(constraint (= (f "4:15PM") "4:15PM-4:45PM"))
(constraint (= (f "11:48PM") "11:45PM-12:15AM"))
(constraint (= (f "10:24PM") "10:15PM-10:45PM"))
(constraint (= (f "4:29AM") "4:15AM-4:45AM"))
(constraint (= (f "3:25PM") "3:15PM-3:45PM"))
(constraint (= (f "8:14AM") "7:45AM-8:15AM"))
(check-synth)
