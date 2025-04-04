
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
(constraint (= (f "1199") "1200"))
(constraint (= (f "913") "915"))
(constraint (= (f "914") "915"))
(constraint (= (f "786") "790"))
(constraint (= (f "601") "605"))
(constraint (= (f "814") "815"))
(constraint (= (f "816") "820"))
(constraint (= (f "333") "335"))
(constraint (= (f "1394") "1395"))
(constraint (= (f "646") "650"))
(check-synth)
