
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("-" arg0
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
(constraint (= (f "(525) 949 2246") "525-949-2246"))
(constraint (= (f "896-388-9065") "896-388-9065"))
(constraint (= (f "014.2967") "425-014-2967"))
(constraint (= (f "957-1297") "425-957-1297"))
(constraint (= (f "247-641-8878") "247-641-8878"))
(constraint (= (f "880-816-8294") "880-816-8294"))
(constraint (= (f "(177) 191 4536") "177-191-4536"))
(check-synth)
