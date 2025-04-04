
    (set-logic SLIA)
    (synth-fun f ((arg0 String) (arg1 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." ".2" "2." "*" arg0 arg1
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
(constraint (= (f "1.23" "2.54") "1.2 * 2.5"))
(constraint (= (f "32.624" "5.216") "32.6 * 5.2"))
(constraint (= (f "11.26" "2") "11.3 * 2.0"))
(constraint (= (f "5.21" "2.15") "5.2 * 2.2"))
(constraint (= (f "2.62" "5.2") "2.6 * 5.2"))
(constraint (= (f "92.252" "7.15") "92.3 * 7.2"))
(constraint (= (f "1.252" "8.2") "1.3 * 8.2"))
(constraint (= (f "8.25" "2.165") "8.3 * 2.2"))
(check-synth)
