
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ( arg0
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
(constraint (= (f "1Z 875 2X4 92 2464 908 9") "875 92"))
(constraint (= (f "1Z 388 965 96 5014 296 7") "388 96"))
(constraint (= (f "1Z 339 5D2 97 3412 476 4") "339 97"))
(constraint (= (f "1Z 887 8Q2 88 0816 829 4") "887 88"))
(constraint (= (f "1Z 781 7D9 14 5364 887 7") "781 14"))
(check-synth)
