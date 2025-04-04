
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":" "PM" "-" ":00PM-" "1:" "1:30PM" "11:" arg0
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
(constraint (= (f "1:24PM") "1:00PM-1:30PM"))
(constraint (= (f "11:27PM") "11:00PM-11:30PM"))
(constraint (= (f "11:12AM") "11:00AM-11:30AM"))
(constraint (= (f "11:34PM") "11:30PM-12:00AM"))
(check-synth)
