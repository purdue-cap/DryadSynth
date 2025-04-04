
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":" arg0
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
(constraint (= (f "3:14:23") "03h 14m 23s"))
(constraint (= (f "11:5:25") "11h 05m 25s"))
(constraint (= (f "4:23:1") "04h 23m 01s"))
(constraint (= (f "21:7:6") "21h 07m 06s"))
(constraint (= (f "14:5:25") "14h 05m 25s"))
(constraint (= (f "12:8:29") "12h 08m 29s"))
(constraint (= (f "15:29:9") "15h 29m 09s"))
(constraint (= (f "11:23:45") "11h 23m 45s"))
(check-synth)
