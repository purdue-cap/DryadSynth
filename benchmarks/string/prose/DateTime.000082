
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("-" ":" "-0" "68-06-" arg0
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
(constraint (= (f "2143-10-06 18:25:57") "2143-10-06 18:15"))
(constraint (= (f "1809-01-11 13:24:47") "1809-01-11 13:15"))
(constraint (= (f "1938-05-04 23:27:40") "1938-05-04 23:15"))
(constraint (= (f "1856-01-20 11:12:55") "1856-01-20 11:00"))
(constraint (= (f "1953-04-22 23:34:17") "1953-04-22 23:30"))
(constraint (= (f "2168-06-05 23:53:17") "2168-06-05 23:45"))
(constraint (= (f "1868-06-22 19:36:20") "1868-06-22 19:30"))
(constraint (= (f "2239-05-11 00:46:58") "2239-05-11 00:45"))
(check-synth)
