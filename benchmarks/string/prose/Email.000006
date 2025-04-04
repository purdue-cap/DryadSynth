
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("@" "co" ".com" "@fabrikam.com" "@proseware.com" "@contoso.com" "a@fabrikam.com" arg0
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
(constraint (= (f "joyikutty@fabrikam.com") "joyikutty@fabrikam.com"))
(constraint (= (f "giftiantonio@fabrikam.com") "giftiantonio@fabrikam.com"))
(constraint (= (f "claudia") "claudia@proseware.com"))
(constraint (= (f "francis@contoso.com") "francis@contoso.com"))
(constraint (= (f "i\u00f1aki") "i\u00f1aki@proseware.com"))
(constraint (= (f "celtia@contoso.com") "celtia@contoso.com"))
(constraint (= (f "nina@fabrikam.com") "nina@fabrikam.com"))
(constraint (= (f "benito") "benito@proseware.com"))
(constraint (= (f "bernardosagar@fabrikam.com") "bernardosagar@fabrikam.com"))
(constraint (= (f "marthamar\u00eda@fabrikam.com") "marthamar\u00eda@fabrikam.com"))
(check-synth)
