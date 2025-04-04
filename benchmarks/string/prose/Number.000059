
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":" "/" arg0
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
(constraint (= (f "9:30 5/14") "09 30 05 14"))
(constraint (= (f "9:05 12/5") "09 05 12 05"))
(constraint (= (f "14:25 11/23") "14 25 11 23"))
(constraint (= (f "6:25 4/8") "06 25 04 08"))
(constraint (= (f "21:32 6/21") "21 32 06 21"))
(constraint (= (f "22:48 8/16") "22 48 08 16"))
(constraint (= (f "8:55 9/21") "08 55 09 21"))
(constraint (= (f "7:30 4/18") "07 30 04 18"))
(check-synth)
