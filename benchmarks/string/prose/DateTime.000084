
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("/" ":" ":30 PM" "0 AM" ":00 AM" arg0
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
(constraint (= (f "10/6/2143 18:25") "Sun 6:30 PM"))
(constraint (= (f "3/30/2002 21:44") "Sat 10:00 PM"))
(constraint (= (f "1/31/1846 7:00") "Sat 7:00 AM"))
(constraint (= (f "4/22/1953 23:34") "Wed 12:00 AM"))
(constraint (= (f "3/30/2241 16:15") "Tue 4:30 PM"))
(constraint (= (f "8/2/2160 23:48") "Sat 12:00 AM"))
(constraint (= (f "5/23/1984 22:24") "Wed 10:30 PM"))
(constraint (= (f "8/15/1740 4:29") "Mon 4:30 AM"))
(constraint (= (f "7/8/2237 15:25") "Sat 3:30 PM"))
(constraint (= (f "11/3/1904 8:14") "Thu 8:30 AM"))
(check-synth)
