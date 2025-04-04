
    (set-logic SLIA)
    (synth-fun f ((arg0 String) (arg1 String) (arg2 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("-" arg0 arg1 arg2
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
(constraint (= (f "521" "632" "2642") "521-632-2642"))
(constraint (= (f "91" "24" "2834") "091-024-2834"))
(constraint (= (f "14" "52" "728") "014-052-0728"))
(constraint (= (f "617" "95" "9217") "617-095-9217"))
(constraint (= (f "33" "285" "928423") "033-285-928423"))
(constraint (= (f "925" "982" "1892") "925-982-1892"))
(constraint (= (f "1" "425" "28520") "001-425-28520"))
(constraint (= (f "512" "272" "8167") "512-272-8167"))
(check-synth)
