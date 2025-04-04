
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." arg0
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
(constraint (= (f "3.48") "3.50"))
(constraint (= (f "3.89") "4.00"))
(constraint (= (f "2342.35") "2342.50"))
(constraint (= (f "10.76") "11.00"))
(constraint (= (f "1284.42") "1284.50"))
(constraint (= (f "23224.98") "23225.00"))
(constraint (= (f "1024.21") "1024.00"))
(constraint (= (f "14.98") "15.00"))
(check-synth)
