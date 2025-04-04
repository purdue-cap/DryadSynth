
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("-" "." "xP" "/xP" "MFM-" "xPhone" "/xPhone-" "xPad" "/xPad-" "/xPhone-5." arg0
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
(constraint (= (f "MFM-5.2.59/xPhone-4.9") "xPhone"))
(constraint (= (f "MFM-6.4.90/xPad-8.9") "xPad"))
(constraint (= (f "MFM-8.9.06/xPhone-5.9") "xPhone"))
(constraint (= (f "MFM-1.4.29/xPad-6.7.0") "xPad"))
(constraint (= (f "MFM-5.7.12/xPhone-9.7") "xPhone"))
(constraint (= (f "MFM-2.4.76/xPad-4.1.8") "xPad"))
(constraint (= (f "MFM-6.6.28/xPhone-8.0") "xPhone"))
(constraint (= (f "MFM-8.2.94/xPhone-6.7.8") "xPhone"))
(constraint (= (f "MFM-1.9.14/xPhone-5.3.6") "xPhone"))
(constraint (= (f "MFM-7.7.92/xPhone-5.6") "xPhone"))
(constraint (= (f "MFM-2.1.06/xPhone-6.6") "xPhone"))
(check-synth)
