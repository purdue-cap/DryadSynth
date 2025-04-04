
    (set-logic SLIA)
    (synth-fun f ((arg0 String) (arg1 String) (arg2 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." "1." ".2" "2." "+" ".2 +" "8." arg0 arg1 arg2
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
(constraint (= (f "1.23" "2.54" "12.421") "1.2 + 2.5 + 12.4"))
(constraint (= (f "32.624" "5.216" "5") "32.6 + 5.2 + 5.0"))
(constraint (= (f "11.26" "2" "1.52") "11.3 + 2.0 + 1.5"))
(constraint (= (f "5.21" "2.15" "9.2162") "5.2 + 2.2 + 9.2"))
(constraint (= (f "1.58" "8.26" "8.28") "1.6 + 8.3 + 8.3"))
(constraint (= (f "21.282" "16.21" "18.278") "21.3 + 16.2 + 18.3"))
(constraint (= (f "1.43" "1.2" "1.82") "1.4 + 1.2 + 1.8"))
(constraint (= (f "64.262" "15.81" "38.28") "64.3 + 15.8 + 38.3"))
(check-synth)
