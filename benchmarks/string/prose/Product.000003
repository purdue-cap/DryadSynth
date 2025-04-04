
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
(constraint (= (f "INVENTORE DELENITI ENIM 9.6 OZ") "9.6 OZ"))
(constraint (= (f "QUI VOLUPTATEM VENIAM 6.2 PK") "6.2 PK"))
(constraint (= (f "EXCEPTURI DOLOR 7Z") "7Z"))
(constraint (= (f "DOLOREM SINT NUMQUAM REPELLENDUS 5Z") "5Z"))
(constraint (= (f "UT NOSTRUM 4.6Z") "4.6Z"))
(check-synth)
