
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("-" "6-" "76-100" arg0
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
(constraint (= (f "100") "76-100"))
(constraint (= (f "51") "51-75"))
(constraint (= (f "86") "76-100"))
(constraint (= (f "42") "26-50"))
(constraint (= (f "83") "76-100"))
(constraint (= (f "46") "26-50"))
(constraint (= (f "30") "26-50"))
(constraint (= (f "5") "1-25"))
(constraint (= (f "86") "76-100"))
(constraint (= (f "44") "26-50"))
(check-synth)
