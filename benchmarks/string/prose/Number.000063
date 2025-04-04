
    (set-logic SLIA)
    (synth-fun f ((arg0 String) (arg1 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (":" arg0 arg1
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
(constraint (= (f "9" "45") "09:45"))
(constraint (= (f "6" "5") "06:05"))
(constraint (= (f "11" "32") "11:32"))
(constraint (= (f "21" "30") "21:30"))
(constraint (= (f "15" "15") "15:15"))
(constraint (= (f "8" "26") "08:26"))
(constraint (= (f "11" "48") "11:48"))
(constraint (= (f "23" "16") "23:16"))
(check-synth)
