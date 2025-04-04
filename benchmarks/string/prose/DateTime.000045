
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("day #" "nday #" "/" arg0
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
(constraint (= (f "Sunday #1 March 2002") "2002 3 3"))
(constraint (= (f "Saturday #2 April 1953") "1953 11 4"))
(constraint (= (f "Tuesday #3 August 2160") "2160 19 8"))
(constraint (= (f "Wednesday #2 August 1740") "1740 10 8"))
(constraint (= (f "8/7/2237") "2237 8 7"))
(constraint (= (f "3/11/1904") "1904 3 11"))
(constraint (= (f "2088 331") "2088 26 11"))
(constraint (= (f "Sunday #1 October 1920") "1920 3 10"))
(constraint (= (f "1802 268") "1802 25 9"))
(constraint (= (f "Monday #3 January 2187") "2187 15 1"))
(check-synth)
