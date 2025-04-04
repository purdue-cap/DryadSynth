
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":" "PM" "M-" ":30" ":30PM" ":30PM-" ":30PM-1" "AM" ":30AM" ":30AM-" arg0
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
(constraint (= (f "6:25PM") "5:30PM-6:30PM"))
(constraint (= (f "9:44PM") "9:30PM-10:30PM"))
(constraint (= (f "7:00AM") "6:30AM-7:30AM"))
(constraint (= (f "11:34PM") "11:30PM-12:30AM"))
(constraint (= (f "4:15PM") "3:30PM-4:30PM"))
(constraint (= (f "11:48PM") "11:30PM-12:30AM"))
(constraint (= (f "10:24PM") "9:30PM-10:30PM"))
(constraint (= (f "4:29AM") "3:30AM-4:30AM"))
(constraint (= (f "3:25PM") "2:30PM-3:30PM"))
(constraint (= (f "8:14AM") "7:30AM-8:30AM"))
(check-synth)
