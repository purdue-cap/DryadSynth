
    (set-logic SLIA)
    (synth-fun f ((arg0 String) (arg1 String) (arg2 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("@fabrikam.com" arg0 arg1 arg2
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
(constraint (= (f "ndavignon" "47345" "5306") "ndavignon@fabrikam.com"))
(constraint (= (f "gpodnieks" "62118" "63346") "gpodnieks@fabrikam.com"))
(constraint (= (f "csevilla" "32618" "14025") "csevilla@fabrikam.com"))
(check-synth)
