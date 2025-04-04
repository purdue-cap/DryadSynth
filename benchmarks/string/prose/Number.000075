
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("$" "." "0." "0.0B" "-" arg0
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
(constraint (= (f "243") "$0.2B"))
(constraint (= (f "12.5") "$0.0B"))
(constraint (= (f "2345.23292") "$2.3B"))
(constraint (= (f "1202.3433") "$1.2B"))
(constraint (= (f "1892.3433") "$1.9B"))
(constraint (= (f "23224.1") "$23.2B"))
(constraint (= (f "29974.1") "$30.0B"))
(constraint (= (f "0.0") "$0.0B"))
(constraint (= (f "0.1") "$0.0B"))
(constraint (= (f "-0.1") "$0.0B"))
(constraint (= (f "100") "$0.1B"))
(constraint (= (f "-100") "$-0.1B"))
(check-synth)
