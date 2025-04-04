
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("\\" "ompany\\" "Company\\" "\\index.html" "company\\" arg0
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
(constraint (= (f "Company\\Et\\index.html") "Company\\Et\\"))
(constraint (= (f "company\\Totam\\index.html") "company\\Totam\\"))
(constraint (= (f "Company\\Illum\\9cRG5QHfl.lhz") "Company\\Illum\\"))
(constraint (= (f "company\\Quas\\index.html") "company\\Quas\\"))
(check-synth)
