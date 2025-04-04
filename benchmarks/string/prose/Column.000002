
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("CC" "cc" "Col" "Column" "Col_" arg0
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
(constraint (= (f "CCC") "ccc"))
(constraint (= (f "CCC") "ccc"))
(constraint (= (f "CCC") "ccc"))
(constraint (= (f "Column2") "Col_2"))
(constraint (= (f "Column6") "Col_6"))
(constraint (= (f "Column9") "Col_9"))
(constraint (= (f "Column8") "Col_8"))
(constraint (= (f "AAA") "aaa"))
(constraint (= (f "CCC") "ccc"))
(constraint (= (f "Column6") "Col_6"))
(check-synth)
