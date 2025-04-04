
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("/" "/2" "." "/20" "/2010" arg0
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
(constraint (= (f "6/28/2137") "6/28/2137"))
(constraint (= (f "15.3.1902") "3/15/1902"))
(constraint (= (f "659/725/2010") "659/725/2010"))
(constraint (= (f "6/24/2137") "6/24/2137"))
(constraint (= (f "578/982/2010") "578/982/2010"))
(constraint (= (f "12/24/2045") "12/24/2045"))
(constraint (= (f "4.2.1743") "2/4/1743"))
(constraint (= (f "73/193/2010") "73/193/2010"))
(check-synth)
