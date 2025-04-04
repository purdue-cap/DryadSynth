
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." "-" arg0
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
(constraint (= (f "243") "240"))
(constraint (= (f "12.5") "10"))
(constraint (= (f "2345.23292") "2350"))
(constraint (= (f "10") "10"))
(constraint (= (f "1202.3433") "1200"))
(constraint (= (f "23224.1") "23220"))
(constraint (= (f "-23224.1") "-23220"))
(constraint (= (f "-0.1") "0"))
(constraint (= (f "0.1") "0"))
(check-synth)
